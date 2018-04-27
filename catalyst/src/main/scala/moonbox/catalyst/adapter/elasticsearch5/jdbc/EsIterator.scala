package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.client.EsRestClient
import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.parser.udf.FunctionUtil
import org.apache.spark.sql.types.StructType
import moonbox.catalyst.adapter.util.SparkUtil._

class EsIterator[T](client: EsRestClient,
                    properties: Properties,
                    json: String,
                    mapping: Seq[(String, String)],
                    schema: StructType,
                    context: CatalystContext,
                    mapRow: (Option[StructType], Seq[Any]) => T) extends scala.collection.Iterator[T] {


  var gotNext: Boolean = false
  var nextValue: T = null.asInstanceOf[T]
  var closed: Boolean = false
  var finished: Boolean = false

  var scrollIter: Iterator[T] = null

  var totalSize: Long = 0L
  var fetchSize: Long = -1L
  var passedSize: Long = -1L
  var scrollId: String = ""

  def getInitQuery(): Unit = {

//    if (passedSize != -1 && passedSize >= totalSize) {  //already iter all data
//      finished = true
//      return
//    }

    val index = properties.getProperty("index")
    val typ = properties.getProperty("type")
    val (response, sid, tsize, fsize, isfinish) = client.performScrollFirst(index, typ, json, context.hasLimited)
    totalSize = tsize
    fetchSize += fsize
    scrollId = sid

    if (fsize == 0) {  //get nothing, return
      finished = true
    } else{
      val data :Seq[Seq[Any]] = response.getResult(schema, colId2colNameMap(mapping))
      val pipeLine :Seq[Seq[Any]] = FunctionUtil.doProjectFunction(data, schema, context.projectFunctionSeq) //doProjectFunction
      val pipeLine2 :Seq[Seq[Any]] = FunctionUtil.doFilterFunction(pipeLine, colName2colIdMap(mapping), context.filterFunctionSeq)

      scrollIter = pipeLine2.map(elem => mapRow(Some(schema), elem)).iterator
    }
  }

  def getSeqQuery(): Unit = {
    println(s"sgetSeqQuery $passedSize, $totalSize ")
//    if (passedSize != -1 && passedSize >= totalSize) {  //already iter all data
//      finished = true
//      return
//    }

    val (response, sid, fsize) = client.performScrollLast(scrollId)
    fetchSize += fsize
    scrollId = sid

    if (fsize == 0) {  //get nothing, return
      println(s"getSeqQuery ${fsize}")
      finished = true
    }else {
      val data :Seq[Seq[Any]] = response.getResult(schema, colId2colNameMap(mapping))
      val pipeLine :Seq[Seq[Any]] = FunctionUtil.doProjectFunction(data, schema, context.projectFunctionSeq) //doProjectFunction
      val pipeLine2 :Seq[Seq[Any]] = FunctionUtil.doFilterFunction(pipeLine, colName2colIdMap(mapping), context.filterFunctionSeq)

      scrollIter = pipeLine2.map(elem => mapRow(Some(schema), elem)).iterator
    }
  }

  // private def getNext(): T = {
  //     if (scrollIter == null) {  //run first time
  //         //println("in first getNext")
  //         val index = properties.getProperty("index")
  //         val typ = properties.getProperty("type")
  //         val (response, sid, tsize, fsize, isfinish) = client.performScrollFirst(index, typ, json, context.hasLimited)
  //         totalSize = tsize
  //         fetchSize += fsize
  //         scrollId = sid

  //         val data :Seq[Seq[Any]] = response.getResult(schema, context.colId2colNameMap)
  //         val pipeLine :Seq[Seq[Any]] = FunctionUtil.doProjectFunction(data, schema, context.projectFunctionSeq) //doProjectFunction
  //         val pipeLine2 :Seq[Seq[Any]] = FunctionUtil.doFilterFunction(pipeLine, context.colName2colIdMap, context.filterFunctionSeq)

  //         scrollIter = pipeLine2.map(elem => mapRow(Some(schema), elem)).iterator

  //         if (fsize == 0) {  //get nothing, return
  //             finished = true
  //             return null.asInstanceOf[T]
  //         }
  //     }

  //     if (passedSize != -1 && passedSize >= totalSize) {  //already iter all data
  //         finished = true
  //         return null.asInstanceOf[T]
  //     }

  //     //have more data, but this current iter no more
  //     if (scrollIter.hasNext) {
  //         passedSize += 1
  //         scrollIter.next()
  //     }
  //     else {
  //         //println("in later getNext")

  //         val (response, sid, fsize) = client.performScrollLast(scrollId)
  //         fetchSize += fsize
  //         scrollId = sid

  //         if (fsize == 0) {  //get nothing, return
  //             finished = true
  //             null.asInstanceOf[T]
  //         }else {
  //             val data :Seq[Seq[Any]] = response.getResult(schema, context.colId2colNameMap)
  //             val pipeLine :Seq[Seq[Any]] = FunctionUtil.doProjectFunction(data, schema, context.projectFunctionSeq) //doProjectFunction
  //             val pipeLine2 :Seq[Seq[Any]] = FunctionUtil.doFilterFunction(pipeLine, context.colName2colIdMap, context.filterFunctionSeq)

  //             scrollIter = pipeLine2.map(elem => mapRow(Some(schema), elem)).iterator
  //             if(scrollIter.hasNext) {
  //                 passedSize += 1
  //                 scrollIter.next()
  //             }
  //             else{
  //                 null.asInstanceOf[T]
  //             }
  //         }
  //     }
  // }


  protected def close(): Unit = {
    finished = true
    client.close()
  }


  def closeIfNeeded() {
    if (!closed) {
      closed = true
      close()
    }
  }

  override def hasNext: Boolean = {
    if(scrollIter == null) { //get the first bulk and init the iter
      getInitQuery()
    }

    if(scrollIter.hasNext) {
      None //nothing to do
    }else{  //get second bulk, and set the iter
      getSeqQuery()
    }

    if (passedSize != -1 && passedSize+1 >= totalSize) {  //already iter all data
      finished = true
    }


    if(finished){
      closeIfNeeded()
    }
    !finished

    // if (!finished) {
    //     if (!gotNext) {
    //         nextValue = getNext()
    //         if (finished) {
    //             closeIfNeeded()
    //         }
    //         gotNext = true
    //     }
    // }
    // !finished
  }

  override def next(): T = {
    // if (!hasNext) {
    //     throw new NoSuchElementException("no next")
    // }
    // gotNext = false
    // nextValue
    passedSize += 1
    scrollIter.next()
  }
}

