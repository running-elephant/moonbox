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

package moonbox.catalyst.adapter.mongo

import java.util.Properties

import com.mongodb.MongoClient
import moonbox.catalyst.adapter.jdbc.JdbcRow
import moonbox.catalyst.adapter.mongo.client.MbMongoClient
import moonbox.catalyst.adapter.mongo.schema.MongoSchemaInfer
import moonbox.catalyst.adapter.mongo.util.MongoJDBCUtils
import moonbox.catalyst.core.plan.CatalystPlan
import moonbox.catalyst.core.{CatalystContext, CatalystPlanner, CatalystQueryExecutor, Strategy}
import moonbox.common.MbLogging
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, GetStructField}
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, LogicalPlan, Project}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, UDFRegistration}
import org.bson.{BsonDocument, Document}

import scala.collection.JavaConverters._
import scala.collection.mutable

class MongoCatalystQueryExecutor(cli: MongoClient, props: Properties) extends CatalystQueryExecutor with MongoTranslateSupport with MbLogging {

  def this(properties: Properties) = this(null, properties)
  private var closed: Boolean = _
  var client: MbMongoClient = MbMongoClient(cli, props)
  override val planner: CatalystPlanner = new CatalystPlanner(MongoRules.rules)

  def this(props: Properties, client: MongoClient) = this(props)

  private def getTableSchema(mongoJavaClient: MongoClient, dbName: String, collectionName: String) = {
    new MongoSchemaInfer().inferSchema(mongoJavaClient, dbName, collectionName)
  }

  override def getTableSchema: StructType = getTableSchema(client.client, client.database, client.collectionName)

  def isClose(): Boolean = {
    closed
  }

  def close(): Unit ={
    if (!closed){
      closed = true
      // Note: it's important that we set closed = true before calling close(), since setting it
      // afterwards would permit us to call close() multiple times if close() threw an exception.
      client.close()
    }
  }

  def toRowIterator(plan: LogicalPlan): Iterator[Row] = {
    val schema = plan.schema
    val documentIter = getBsonIterator(plan)._1
    new Iterator[Row] {
      override def hasNext: Boolean = documentIter.hasNext
      override def next(): Row = {
        MapFunctions.documentToRow(documentIter.next().toBsonDocument(classOf[BsonDocument], MongoClient.getDefaultCodecRegistry), schema)
      }
    }
  }

  def toIterator[T](plan: LogicalPlan, converter: Seq[Any] => T): Iterator[T] = {
    val (iter, _, context) = getBsonIterator(plan)
    bsonIteratorConverter(iter, context.index2FieldName, converter)
  }

  private def bsonIteratorConverter[T](iter: Iterator[Document], index2FieldName: mutable.Map[Int, String], converter: => Seq[Any] => T): Iterator[T] = {
    new Iterator[T] {
      override def hasNext = iter.hasNext
      override def next() = {
        val doc = iter.next().toBsonDocument(classOf[BsonDocument], MongoClient.getDefaultCodecRegistry)
        var res = Seq[Any]()
        for (i <- 1 to index2FieldName.size) {
          val ithFieldName = {
            if (index2FieldName(i).contains("."))
              index2FieldName(i).split("\\.").toSeq
            else
              Seq(index2FieldName(i))
          }
          if (ithFieldName.isEmpty)
            throw new Exception("Field name cannot be null")
          res :+= MongoJDBCUtils.bsonValue2Value(doc.get(ithFieldName.head), ithFieldName.tail)
        }
        converter(res)
      }
    }
  }

  override def execute4Jdbc(plan: LogicalPlan): (Iterator[JdbcRow], Map[Int, Int], Map[String, Int]) = {
    val (iter, outputSchema, context) = getBsonIterator(plan, new CatalystContext)
    val newIterator = bsonIteratorConverter(iter, context.index2FieldName, in => new JdbcRow(in: _*))
    val columnLabel2Index = context.index2FieldName.map(e => e._2 -> e._1).toMap
    val index2SqlType = MongoJDBCUtils.index2SqlType(outputSchema)
    (newIterator, index2SqlType, columnLabel2Index)
  }

  private def getBsonIterator(plan: LogicalPlan, context: CatalystContext = new CatalystContext): (Iterator[Document], StructType, CatalystContext) = {
//    val tableSchema = getTableSchema(client.client, client.database, client.collectionName)
    val (jsonPipeline, outputSchema) = translate(plan, context)
    val coll = client.client.getDatabase(client.database).getCollection(client.collectionName)
    (coll.aggregate(jsonPipeline.map(Document.parse).toList.asJava).iterator().asScala, outputSchema, context)
  }

  private def translate(plan: LogicalPlan, context: CatalystContext): (Seq[String], StructType) = {
    recordFieldNames(plan, context)
    logInfo(s"index -> columnName: ${context.index2FieldName.mkString("(", ", ", ")")}")
    val next: CatalystPlan = planner.plan(plan).next()
    logInfo(s"output schema: ${next.schema}")
    (next.translate(context), next.schema)
  }

  override def translate(plan: LogicalPlan): Seq[String] = {
    planner.plan(plan).next().translate(new CatalystContext)
  }

  private def recordFieldNames(logicalPlan: LogicalPlan, context: CatalystContext): Unit = {
    val fieldNames = getFieldNames(logicalPlan)
    (1 to fieldNames.length).zip(fieldNames).foreach {
      case (index, fieldName) => context.index2FieldName += (index -> fieldName)
    }
  }

  private def getFieldNames(logicalPlan: LogicalPlan): Seq[String] = {
    var fieldNames = Seq[String]()
    logicalPlan match {
      case p: Project => {
        for (expression <- p.projectList) {
          expression match {
            case a: AttributeReference =>
              val fieldName = expressionToBson(a)
              fieldNames :+= fieldName
            case Alias(child, name) =>
              child match {
                case g: GetStructField =>
                  val nestedName = nestedDocumentToBson(g)
                  if (nestedName.length > 2)
                    fieldNames :+= nestedName.substring(1, nestedName.length - 1)
                case _ =>
                  fieldNames :+= name
              }
            case other =>
              fieldNames :+= other.name
          }
        }
      }
      case a: Aggregate =>
        for (expression <- a.aggregateExpressions) {
          expression match {
            case Alias(child, name) =>
              child match {
                case g: GetStructField =>
                  val nestedName = nestedDocumentToBson(g)
                  if (nestedName.length > 2)
                    fieldNames :+= nestedName.substring(1, nestedName.length - 1)
                case _ =>
                  fieldNames :+= name
              }
            case other =>
              fieldNames :+= expressionToBson(other)
          }
        }
      case c: CatalogRelation =>
        fieldNames = c.output.map(_.name)
      case l: LogicalRelation =>
        fieldNames = l.output.map(_.name)
      case other =>
        fieldNames = getFieldNames(other.children.head)
    }
    fieldNames
  }

  override val provider: String = "mongo"

  override def getPlannerRule(): Seq[Strategy] = MongoRules.rules

  def adaptorFunctionRegister(udf: UDFRegistration): Unit = {
    import moonbox.catalyst.adapter.mongo.function.UDFunctions._
    udf.register("geo_near", geoNear _)
    udf.register("index_stats", indexStats _)
  } // TODO:

}
