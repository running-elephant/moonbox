package moonbox.core.datasys.mongo

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.mongodb._
import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.mongodb.client.model.{InsertOneModel, ReplaceOneModel, UpdateOneModel, UpdateOptions}
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import moonbox.catalyst.adapter.mongo.{MapFunctions, MongoCatalystQueryExecutor}
import moonbox.catalyst.core.parser.udf.{ArrayFilter, ArrayMap}
import moonbox.common.MbLogging
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import moonbox.core.datasys._
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.bson.{BsonDocument, Document}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

class MongoDataSystem(props: Map[String, String]) extends DataSystem(props)
	with Pushdownable with Insertable with Truncatable with MbLogging {

  //common
  val MONGO_SPARK_INPUT_PREFIX: String = "spark.mongodb.input."
  val MONGO_SPARK_OUTPUT_PREFIX: String = "spark.mongodb.output."
  val URI_KEY: String = ReadConfig.mongoURIProperty
  val DATABASE_KEY: String = ReadConfig.databaseNameProperty
  val COLLECTION_KEY: String = ReadConfig.collectionNameProperty
  val LOCAL_THRESHOLD_KEY: String = "localThreshold".toLowerCase()

  //input
  val READ_CONCERN_LEVEL_KEY: String = ReadConfig.readConcernLevelProperty
  val READ_PREFERENCE_NAME_KEY: String = ReadConfig.readPreferenceNameProperty
  val READ_PREFERENCE_TAG_SETS_KEY: String = ReadConfig.readPreferenceTagSetsProperty

  //output
  val REPLACE_DOCUMENT_KEY: String = WriteConfig.replaceDocumentProperty
  val MAX_BATCH_SIZE_KEY: String = WriteConfig.maxBatchSizeProperty
  /*write_concern*/
  val WRITE_CONCERN_W_KEY: String = WriteConfig.writeConcernWProperty
  val WRITE_CONCERN_JOURNAL_KEY: String = WriteConfig.writeConcernJournalProperty
  val WRITE_CONCERN_W_TIMEOUT_MS_KEY: String = WriteConfig.writeConcernWTimeoutMSProperty

  val cleanedInputMap: Map[String, String] = props.filterKeys(!_.startsWith(MONGO_SPARK_OUTPUT_PREFIX)).map { case (k, v) => k.stripPrefix(MONGO_SPARK_INPUT_PREFIX) -> v }

  val cleanedOutputMap: Map[String, String] = props.filterKeys(!_.startsWith(MONGO_SPARK_INPUT_PREFIX)).map { case (k, v) => k.stripPrefix(MONGO_SPARK_OUTPUT_PREFIX) -> v }

  var readClientURI: MongoClientURI = _
  var writeClientURI: MongoClientURI = _

  def readExecutor: MongoCatalystQueryExecutor = {
    val uri = cleanedInputMap(URI_KEY)
    val clientOptions = getMongoClientOptions(cleanedInputMap)
    readClientURI = new MongoClientURI(uri, MongoClientOptions.builder(clientOptions))
    val readClient = new MongoClient(readClientURI)
    new MongoCatalystQueryExecutor(readClient, map2Property(cleanedInputMap))
  }

  def writeExecutor: MongoCatalystQueryExecutor = {
    val uri = cleanedOutputMap(URI_KEY)
    val clientOptions = getMongoClientOptions(cleanedOutputMap)
    writeClientURI = new MongoClientURI(uri, MongoClientOptions.builder(clientOptions))
    val writeClient = new MongoClient(writeClientURI)
    new MongoCatalystQueryExecutor(writeClient, map2Property(cleanedOutputMap))
  }

  lazy val readDatabase: String = {
    if (cleanedInputMap.contains(DATABASE_KEY)) {
      cleanedInputMap(DATABASE_KEY)
    } else {
      Option(readClientURI.getDatabase).getOrElse(throw new Exception("No input database found"))
    }
  }

  lazy val readCollection = {
    if (cleanedInputMap.contains(COLLECTION_KEY)) {
      cleanedInputMap(COLLECTION_KEY)
    } else {
      Option(readClientURI.getCollection).getOrElse(throw new Exception("No input collection found"))
    }
  }

  lazy val writeDatabase: String = {
    if (cleanedOutputMap.contains(DATABASE_KEY)) {
      cleanedOutputMap(DATABASE_KEY)
    } else {
      Option(writeClientURI.getDatabase).getOrElse(throw new Exception("No output database found"))
    }
  }

  lazy val writeCollection: String = {
    if (cleanedOutputMap.contains(COLLECTION_KEY)) {
      cleanedOutputMap(COLLECTION_KEY)
    } else {
      Option(writeClientURI.getCollection).getOrElse(throw new Exception("No output collection found"))
    }
  }

  lazy val maxBatchSize: Int = {
    if (cleanedOutputMap.contains(MAX_BATCH_SIZE_KEY)) {
      cleanedOutputMap(MAX_BATCH_SIZE_KEY).toInt
    } else 512
  }

  lazy val replaceDocument: Boolean = {
    if (cleanedOutputMap.contains(REPLACE_DOCUMENT_KEY)) {
      cleanedOutputMap(REPLACE_DOCUMENT_KEY).toBoolean
    } else true
  }

  private def createReadPreference(map: Map[String, String]): Option[ReadPreference] = {
    // TODO: "maxstalenessseconds"
    var res: ReadPreference = null
    if (map.contains(READ_PREFERENCE_NAME_KEY)) {
      val name = map(READ_PREFERENCE_NAME_KEY)
      if (map.contains(READ_PREFERENCE_TAG_SETS_KEY)) {
        val tagsString = map(READ_PREFERENCE_TAG_SETS_KEY)
        if (tagsString.length > 0) {
          val tags = tagsString.split(",").map { kv =>
            val pair = kv.split(":")
            if (pair.length != 2) {
              new IllegalArgumentException(s"The connection string contains an invalid read preference tag. $kv is not a key value pair")
            }
            new Tag(pair(0), pair(1))
          }.toSeq.asJava
          val tagSetList = Seq(new TagSet(tags)).asJava
          res = ReadPreference.valueOf(name, tagSetList)
        }
      } else {
        res = ReadPreference.valueOf(name)
      }
    }
    Option(res)
  }

  private def createCompressors(map: Map[String, String]): Seq[MongoCompressor] = {
    // TODO:
    // COMPRESSOR_KEYS.add("compressors")
    // COMPRESSOR_KEYS.add("zlibcompressionlevel")
    Nil
  }

  private def createWriteConcern(map: Map[String, String]): Option[WriteConcern] = {
    // TODO: "safe"
    // TODO: "fsync"
    //the value with w maybe string "majority" or int 1, 0
    val writeConcern: WriteConcern = if (map.contains(WRITE_CONCERN_W_KEY)) {
      val w = map(WRITE_CONCERN_W_KEY)
      try {
        new WriteConcern(w.toInt)
      } catch {
        case _: NumberFormatException =>
          new WriteConcern(w)
      }
    } else {
      new WriteConcern()
    }
    if (map.contains(WRITE_CONCERN_JOURNAL_KEY)) {
      val journal = map(WRITE_CONCERN_JOURNAL_KEY).toBoolean
      writeConcern.withJournal(journal)
    }
    if (map.contains(WRITE_CONCERN_W_TIMEOUT_MS_KEY)) {
      val wTimeout = map(WRITE_CONCERN_W_TIMEOUT_MS_KEY).toInt
      writeConcern.withWTimeout(wTimeout, TimeUnit.MILLISECONDS)
    }
    Some(writeConcern)
  }

  private def getMongoClientOptions(map: Map[String, String]): MongoClientOptions = {
    val builder = MongoClientOptions.builder()
    createReadPreference(map).foreach(builder.readPreference)
    createWriteConcern(map).foreach(builder.writeConcern)
    // builder.compressorList(createCompressors(map).asJava)
    map.foreach {
      case (k, v) =>
        k match {
          case READ_CONCERN_LEVEL_KEY => builder.readConcern(new ReadConcern(ReadConcernLevel.fromString(v)))
          case LOCAL_THRESHOLD_KEY => builder.localThreshold(v.toInt)
          case _ => //do nothing
        }
    }
    builder.build()
  }

  override val supportedOperators: Seq[Class[_]] = Seq(
    classOf[Project],
    classOf[Filter],
    classOf[Aggregate],
    classOf[Sort],
    classOf[GlobalLimit],
    classOf[LocalLimit]
  )
  override val supportedJoinTypes: Seq[JoinType] = Seq()
  override val supportedExpressions: Seq[Class[_]] = Seq(
    classOf[AttributeReference], classOf[Alias], classOf[Literal],
    classOf[Abs], classOf[Not], classOf[And], classOf[Or], classOf[EqualTo], classOf[Max], classOf[Min], classOf[Average], classOf[Count], classOf[Add], classOf[Subtract], classOf[Multiply], classOf[Divide],
    classOf[Sum], classOf[GreaterThan], classOf[GreaterThanOrEqual], classOf[LessThan], classOf[LessThanOrEqual], classOf[Not],
    classOf[ArrayMap], classOf[ArrayFilter], classOf[IsNull], classOf[IsNotNull], classOf[Lower], classOf[Upper], classOf[Substring], classOf[Hour], classOf[Second], classOf[Month], classOf[Minute],
    classOf[Year], classOf[WeekOfYear], classOf[CaseWhen], classOf[DayOfYear], classOf[Concat], classOf[DayOfMonth], classOf[CaseWhenCodegen]
  )
  override val beGoodAtOperators: Seq[Class[_]] = Seq(classOf[Filter], classOf[Aggregate], classOf[Sort], classOf[GlobalLimit], classOf[LocalLimit])
  override val supportedUDF: Seq[String] = Seq()

  override def isSupportAll: Boolean = false

  override def fastEquals(other: DataSystem): Boolean = false

  override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
    val builder = MongoSpark.builder().sparkSession(sparkSession)
    val newProps = new Properties()
    props.foreach {
      case (k, v) =>
        newProps.setProperty(k, v)
        builder.option(k, v)
    }
    val pipeline = new MongoCatalystQueryExecutor(newProps).translate(plan).map(BsonDocument.parse)
    logInfo(pipeline.map(_.toJson).mkString("\n"))
    val schema = plan.schema
    builder.pipeline(pipeline).build().toDF(schema)
  }

  private def map2Property(map: Map[String, String]): Properties = {
    val prop = new Properties()
    map.foreach { case (k, v) => prop.setProperty(k, v) }
    prop
  }

  override def buildQuery(plan: LogicalPlan): DataTable = {
    val schema = plan.schema
    val iter: Iterator[Row] = readExecutor.toRowIterator(plan)
    new DataTable(iter, schema, () => readExecutor.close())
  }

  private def insertDirect(collection: MongoCollection[BsonDocument], table: DataTable, batchSize: Int): Unit = {
    if (table.iter.nonEmpty) {
      table.iter.grouped(batchSize).foreach { batch =>
        collection.insertMany(batch.map(row => MapFunctions.rowToDocument(row)).asJava)
      }
    }
  }

  private def bulkWrite(collection: MongoCollection[BsonDocument], table: DataTable, batchSize: Int): Unit = {
    if (table.iter.nonEmpty) {
      table.iter.grouped(batchSize).foreach { batch =>
        val updateOptions = new UpdateOptions().upsert(true)
        val requests = batch.map { row =>
          val doc = MapFunctions.rowToDocument(row)
          Option(doc.get("_id")) match {
            case Some(id) =>
              if (replaceDocument) {
                new ReplaceOneModel[BsonDocument](new BsonDocument("_id", id), doc, updateOptions)
              } else {
                doc.remove("_id")
                new UpdateOneModel[BsonDocument](new BsonDocument("_id", id), new BsonDocument("$set", doc), updateOptions)
              }
            case None =>
              new InsertOneModel[BsonDocument](doc)
          }
        }
        collection.bulkWrite(requests.asJava)
      }
    }

  }

  private def batchInsert(database: MongoDatabase, collectionName: String, batchSize: Int, table: DataTable, saveMode: SaveMode): Unit = {
    lazy val collectionExists: Boolean = {
      val iter = database.listCollectionNames().iterator()
      var flag = false
      while (iter.hasNext && !flag) {
        if (iter.next() == collectionName) flag = true
      }
      flag
    }
    saveMode match {
      case SaveMode.Append => /* do nothing */
      case SaveMode.Overwrite =>
        /* drop this collection */
        database.getCollection(collectionName).drop()
        database.createCollection(collectionName)
      case SaveMode.ErrorIfExists =>
        if (collectionExists) {
          throw new UnsupportedOperationException(s"SaveMode is set to ErrorIfExists and ${database.getName}.$collectionName exists. Consider changing the SaveMode")
        } else {
          database.createCollection(collectionName)
        }
      case SaveMode.Ignore =>
        if (!collectionExists) {
          database.createCollection(collectionName)
        } else return
      case _ => throw new IllegalArgumentException(s"Unknown save mode: $saveMode. " + "Accepted save modes are 'overwrite', 'append', 'ignore', 'error'.")
    }
    val collection = database.getCollection(collectionName, classOf[BsonDocument])
    if (table.schema.fields.exists(_.name == "_id")) {
      bulkWrite(collection, table, batchSize)
    } else {
      insertDirect(collection, table, batchSize)
    }
  }

  override def insert(table: DataTable, saveMode: SaveMode): Unit = {
    val executor = writeExecutor
    try {
      val client = executor.client.client
      batchInsert(client.getDatabase(writeDatabase), writeCollection, maxBatchSize, table, saveMode)
    } finally {
      executor.close()
      table.close()
    }
  }

  override def truncate(): Unit = {
    val executor = writeExecutor
    try {
      executor.client.client.getDatabase(writeDatabase).getCollection(writeCollection).deleteMany(new Document())
    } finally {
      executor.close()
    }
  }

  override def tableNames(): Seq[String] = {
    val executor = readExecutor
    try {
      val iter = executor.client.client.getDatabase(readDatabase).listCollectionNames().iterator()
      val buffer = new ArrayBuffer[String]()
      while (iter.hasNext) {
        buffer += iter.next()
      }
      buffer
    } finally {
      executor.close()
    }
  }

  // mongo spark configurations
  override def tableProperties(tableName: String): Map[String, String] = {
    props + ((MONGO_SPARK_INPUT_PREFIX + COLLECTION_KEY) -> tableName)
  }

  override def tableName(): String = {
    props(MONGO_SPARK_INPUT_PREFIX + COLLECTION_KEY)
  }

}
