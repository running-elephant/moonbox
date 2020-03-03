package org.apache.spark.sql.execution.datasources.ums

import org.apache.spark.sql.execution.datasources.mbjdbc.{JDBCPartitioningInfo, MbJDBCRelation}
import org.scalatest.FunSuite


class MbJDBCRelationTest extends FunSuite {

  test("columnPartition") {
    val partitionInfo = new JDBCPartitioningInfo(column = "id", lowerBound = 1, upperBound = 15, numPartitions = 7)

    MbJDBCRelation.columnPartition(partitionInfo).foreach(part => println(part))
  }
}
