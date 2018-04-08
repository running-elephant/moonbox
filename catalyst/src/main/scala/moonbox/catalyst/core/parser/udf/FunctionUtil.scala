package moonbox.catalyst.core.parser.udf

import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.common.MbLogging
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.types._
import org.apache.spark.sql.udf.JaninoCodeGen

object FunctionUtil extends MbLogging{

    private def getDataType(expression: Expression) = {
        expression match {
            case a: ArrayType => a.elementType
            case _ => expression.dataType
        }
    }

    def doFilterFunction(rows: Seq[Seq[Any]], colName2colIdMap: Map[String, Int], functions: Seq[Expression]): Seq[Seq[Any]] ={
        //TODO: filter function here
        if(functions.isEmpty) {
            return rows
        }
        val iterator = rows.iterator
        var ret = scala.collection.Seq[scala.collection.Seq[Any]]()
        while(iterator.hasNext) {
            val current = iterator.next()
            functions.foreach {
                case ArrayExists(left, right) =>
                    //val elementType = left.dataType.asInstanceOf[ArrayType].elementType
                    //val elementType = left.dataType
                    val elementType = getDataType(left)

                    val data = current.toArray.asInstanceOf[Array[AnyRef]]
                    val leftName = SparkUtil.parseLeafExpression(left)
                    //TODO: find the proper index, if it not in project list, add filter array name to the project list before send to ES
                    logWarning("name2colId " + colName2colIdMap)
                    var contains = true
                    if(colName2colIdMap.contains(leftName.name)) {
                        val fIndex = colName2colIdMap(leftName.name) // exist occur on col id
                        if (elementType != NullType) {
                            val convertedLambda = ExprUtil.genArrayExistsCode(right, elementType, false)
                            data.zipWithIndex.foreach{ case (elem, dIndex) =>
                                if(fIndex == dIndex) {
                                    val array = elem.asInstanceOf[Array[AnyRef]]
                                    contains = JaninoCodeGen.exists(array, elementType, convertedLambda)
                                }
                            }
                        }
                    }
                    else {  //prompt add to project
                        println(s"please add array_exist col ${leftName.name} to select list, $current")
                    }

                    if(contains) {
                        ret = ret :+ current
                    }
            }
        }
        ret
    }


    def doProjectFunction(rows: Seq[Seq[Any]], structType: StructType, functions: Seq[(Expression, Int)]): Seq[Seq[Any]] ={
        if(functions.isEmpty) {
            return rows
        }

        val iterator = rows.iterator
        var ret = scala.collection.Seq[scala.collection.Seq[Any]]()
        while(iterator.hasNext) {
            val current = iterator.next()
            functions.foreach {
                    case (ArrayMap(left, right), fIndex) =>
                        val elementType = left.dataType.asInstanceOf[ArrayType].elementType
                        val data = current.toArray.asInstanceOf[Array[AnyRef]]

                        val nData = if (elementType != NullType) {
                            val convertedLambda = ExprUtil.genArrayMapCode(right, elementType, false)  //not inner spark
                            //TODO: add complex gen code for array[array[AnyRef]], 效率
                            val newData: Array[AnyRef] = data.zipWithIndex.map{ case (elem, dIndex) =>
                                if(fIndex == dIndex) {
                                    val array = elem.asInstanceOf[Array[AnyRef]]
                                    JaninoCodeGen.map(array, elementType, convertedLambda)
                                }else{
                                    elem
                                }
                            }
                            newData.asInstanceOf[Array[Any]].toSeq
                        }
                        else {
                            current.asInstanceOf[Array[Any]].toSeq
                        }
                        ret = ret :+ nData

                    case (ArrayFilter(left, right), fIndex) =>
                        val elementType = left.dataType.asInstanceOf[ArrayType].elementType
                        val data = current.toArray.asInstanceOf[Array[AnyRef]]

                        val nData = if (elementType != NullType) {
                            val convertedLambda = ExprUtil.genArrayFilterCode(right, elementType, false) //not inner spark
                            //TODO: add complex gen code for array[array[AnyRef]], 效率
                            val newData: Array[AnyRef] = data.zipWithIndex.map{ case (elem, dIndex) =>
                                if(fIndex == dIndex) {
                                    val array = elem.asInstanceOf[Array[AnyRef]]
                                    JaninoCodeGen.filter(array, elementType, convertedLambda)
                                }else{
                                    elem
                                }
                            }
                            newData.asInstanceOf[Array[Any]].toSeq
                        }
                        else {
                            current.asInstanceOf[Array[Any]].toSeq
                        }
                        ret = ret :+ nData
            }
        }
        ret
    }


}
