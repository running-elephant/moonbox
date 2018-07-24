/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.catalyst.adapter.mongo.schema

import java.util

import com.mongodb.MongoClient
import com.mongodb.client.model.Aggregates
import org.bson.BsonNull
import org.apache.spark.sql.catalyst.analysis.TypeCoercion
import org.apache.spark.sql.types._
import org.bson.{BsonDocument, BsonType, BsonValue}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

class MongoSchemaInfer(sampleSize: Int = 1000) {

  val logger = LoggerFactory.getLogger(this.getClass)

  val compatibleDecimalTypes = Seq(ByteType, ShortType, IntegerType, LongType, FloatType, DoubleType)

  def inferSchema(client: MongoClient, database: String, collection: String): StructType = {
    val coll = client.getDatabase(database).getCollection(collection)
    val iterator = coll.aggregate(Seq(Aggregates.sample(sampleSize)).asJava).iterator()
    var sampleData = List[BsonDocument]()
    while(iterator.hasNext){
      sampleData ::= iterator.next().toBsonDocument(classOf[BsonDocument], MongoClient.getDefaultCodecRegistry)
    }
    inferSchema(sampleData)
  }

  def inferSchema(data: List[BsonDocument]): StructType = {
    val schemaList = data.map(getSchemaFromDocument)
    val rootType = schemaList.reduce(compatibleType)

    canonicalizeType(rootType) match {
      case Some(st: StructType) => st
      case _                    => StructType(Seq()) // canonicalizeType erases all empty structs, including the only one we want to keep
    }
  }

  /**
    * Remove StructTypes with no fields or SkipFields
    */
  private def canonicalizeType: DataType => Option[DataType] = {
    case at @ ArrayType(elementType, _) => canonicalizeType(elementType).map(dt => at.copy(elementType = dt))
    case StructType(fields) =>
      val canonicalFields = fields.flatMap(field => {
        if (field.name.isEmpty || field.dataType == SkipFieldType) {
          None
        } else {
          if (fieldContainsConflictType(field.dataType)) {
            val start = if (field.dataType.isInstanceOf[ArrayType]) "Array Field" else "Field"
            logger.warn(s"$start '${field.name}' contains conflicting types converting to StringType")
          }
          canonicalizeType(field.dataType).map(dt => field.copy(dataType = dt))
        }
      })
      if (canonicalFields.nonEmpty) {
        Some(StructType(canonicalFields))
      } else {
        // per SPARK-8093: empty structs should be deleted
        None
      }
    case other: ConflictType => Some(StringType)
    case other               => Some(other)
  }

  private def fieldContainsConflictType(dataType: DataType): Boolean = {
    dataType match {
      case ArrayType(elementType, _) if elementType == ConflictType => true
      case ConflictType => true
      case _ => false
    }
  }

  private def getSchemaFromDocument(document: BsonDocument): StructType = {
    val fields = new util.ArrayList[StructField]()
    document.entrySet.asScala.foreach(kv => fields.add(DataTypes.createStructField(kv.getKey, getDataType(kv.getValue), true)))
    DataTypes.createStructType(fields)
  }

  // scalastyle:off cyclomatic.complexity null
  private def getDataType(bsonValue: BsonValue): DataType = {
    bsonValue.getBsonType match {
      case BsonType.NULL                  => DataTypes.NullType
      case BsonType.ARRAY                 => getSchemaFromArray(bsonValue.asArray().asScala)
      case BsonType.BINARY                => BsonCompatibility.Binary.getDataType(bsonValue.asBinary())
      case BsonType.BOOLEAN               => DataTypes.BooleanType
      case BsonType.DATE_TIME             => DataTypes.TimestampType
      case BsonType.DOCUMENT              => getSchemaFromDocument(bsonValue.asDocument())
      case BsonType.DOUBLE                => DataTypes.DoubleType
      case BsonType.INT32                 => DataTypes.IntegerType
      case BsonType.INT64                 => DataTypes.LongType
      case BsonType.STRING                => DataTypes.StringType
      case BsonType.OBJECT_ID             => BsonCompatibility.ObjectId.structType
      case BsonType.TIMESTAMP             => BsonCompatibility.Timestamp.structType
      case BsonType.MIN_KEY               => BsonCompatibility.MinKey.structType
      case BsonType.MAX_KEY               => BsonCompatibility.MaxKey.structType
      case BsonType.JAVASCRIPT            => BsonCompatibility.JavaScript.structType
      case BsonType.JAVASCRIPT_WITH_SCOPE => BsonCompatibility.JavaScriptWithScope.structType
      case BsonType.REGULAR_EXPRESSION    => BsonCompatibility.RegularExpression.structType
      case BsonType.UNDEFINED             => BsonCompatibility.Undefined.structType
      case BsonType.SYMBOL                => BsonCompatibility.Symbol.structType
      case BsonType.DB_POINTER            => BsonCompatibility.DbPointer.structType
      case BsonType.DECIMAL128 =>
        val bigDecimalValue = bsonValue.asDecimal128().decimal128Value().bigDecimalValue()
        DataTypes.createDecimalType(bigDecimalValue.precision(), bigDecimalValue.scale())
      case _ => ConflictType
    }
  }

  private def getSchemaFromArray(bsonArray: Seq[BsonValue]): DataType = {
    val arrayTypes: Seq[BsonType] = bsonArray.map(_.getBsonType).distinct
    arrayTypes.length match {
      case 0 => SkipFieldType
      case 1 if Seq(BsonType.ARRAY, BsonType.DOCUMENT).contains(arrayTypes.head) => getCompatibleArraySchema(bsonArray)
      case 1 => DataTypes.createArrayType(getDataType(bsonArray.head), true)
      case _ => getCompatibleArraySchema(bsonArray)
    }
  }

  def getCompatibleArraySchema(bsonArray: Seq[BsonValue]): DataType = {
    var arrayType: Option[DataType] = Some(SkipFieldType)
    bsonArray.takeWhile({
      case (bsonValue: BsonNull) => true
      case (bsonValue: BsonValue) =>
        val previous: Option[DataType] = arrayType
        arrayType = Some(getDataType(bsonValue))
        if (previous.nonEmpty && arrayType != previous) arrayType = Some(compatibleType(arrayType.get, previous.get))
        !arrayType.contains(ConflictType) // Option.contains was added in Scala 2.11
    })
    arrayType.get match {
      case SkipFieldType => SkipFieldType
      case dataType      => DataTypes.createArrayType(dataType, true)
    }
  }
  // scalastyle:on cyclomatic.complexity null

  // scalastyle:off cyclomatic.complexity method.length
  /**
    * Gets the matching DataType for the input DataTypes.
    *
    * For simple types, returns a ConflictType if the DataTypes do not match.
    *
    * For complex types:
    * - ArrayTypes: if the DataTypes of the elements cannot be matched, then
    * an ArrayType(ConflictType, true) is returned.
    * - StructTypes: for any field on which the DataTypes conflict, the field
    * value is replaced with a ConflictType.
    *
    * @param t1 the DataType of the first element
    * @param t2 the DataType of the second element
    * @return the DataType that matches on the input DataTypes
    */
  private def compatibleType(t1: DataType, t2: DataType): DataType = {
    TypeCoercion.findTightestCommonType(t1, t2).getOrElse {
      // t1 or t2 is a StructType, ArrayType, or an unexpected type.
      (t1, t2) match {
        // Double support larger range than fixed decimal, DecimalType.Maximum should be enough
        // in most case, also have better precision.
        case (DoubleType, _: DecimalType) | (_: DecimalType, DoubleType) =>
          DoubleType

        case (t1: DecimalType, t2: DecimalType) =>
          val scale = math.max(t1.scale, t2.scale)
          val range = math.max(t1.precision - t1.scale, t2.precision - t2.scale)
          if (range + scale > 38) {
            // DecimalType can't support precision > 38
            DoubleType
          } else {
            DecimalType(range + scale, scale)
          }

        case (StructType(fields1), StructType(fields2)) =>
          val newFields = (fields1 ++ fields2).groupBy(field => field.name).map {
            case (name, fieldTypes) =>
              val dataType = fieldTypes.view.map(_.dataType).reduce(compatibleType)
              StructField(name, dataType, nullable = true)
          }
          StructType(newFields.toSeq.sortBy(_.name))

        case (ArrayType(elementType1, containsNull1), ArrayType(elementType2, containsNull2)) =>
          ArrayType(compatibleType(elementType1, elementType2), containsNull1 || containsNull2)

        // The case that given `DecimalType` is capable of given `IntegralType` is handled in
        // `findTightestCommonTypeOfTwo`. Both cases below will be executed only when
        // the given `DecimalType` is not capable of the given `IntegralType`.
        case (t1: DataType, t2: DecimalType) if compatibleDecimalTypes.contains(t1) => compatibleType(forType(t1), t2)
        case (t1: DecimalType, t2: DataType) if compatibleDecimalTypes.contains(t2) => compatibleType(t1, forType(t2))

        // SkipFieldType Types
        case (s: SkipFieldType, dataType: DataType) => dataType
        case (dataType: DataType, s: SkipFieldType) => dataType

        // Conflicting types
        case (_, _) => ConflictType
      }
    }
  }
  // scalastyle:on cyclomatic.complexity method.length

  // scalastyle:off magic.number
  // The decimal types compatible with other numeric types
  private val ByteDecimal = DecimalType(3, 0)
  private val ShortDecimal = DecimalType(5, 0)
  private val IntDecimal = DecimalType(10, 0)
  private val LongDecimal = DecimalType(20, 0)
  private val FloatDecimal = DecimalType(14, 7)
  private val DoubleDecimal = DecimalType(30, 15)
  private val BigIntDecimal = DecimalType(38, 0)
  // scalastyle:on magic.number

  private def forType(dataType: DataType): DecimalType = dataType match {
    case ByteType    => ByteDecimal
    case ShortType   => ShortDecimal
    case IntegerType => IntDecimal
    case LongType    => LongDecimal
    case FloatType   => FloatDecimal
    case DoubleType  => DoubleDecimal
  }

}
