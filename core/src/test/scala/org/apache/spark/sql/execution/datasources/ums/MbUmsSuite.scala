/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package org.apache.spark.sql.execution.datasources.ums

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.FunSuite

class MbUmsSuite extends FunSuite {
    val conf = new SparkConf(true)
            .setMaster("local[2]")
            .setAppName("ums")
    val session = SparkSession.builder().config(conf).getOrCreate()

    test("ums1 read local json") {
        val rootDir = System.getProperty("user.dir")
        val df = session.read.format("ums").load(s"$rootDir/core/src/test/resources/ums.json")
        println(df.schema)
        df.show(false)
    }

    test("ums2 read hdfs json") {
        val df = session.read.format("ums").load(s"hdfs://master:8020/tmp/ums4.json")
        val df1 = df.select("key", "value1", "value2")
        println(df1.schema)
        df1.show(false)
    }

    test("ums3 read hdfs directory") {
        val df = session.read.format("ums").load("hdfs://master:8020/tmp/uav/[0-9]*")
        val df1 = df.select("`header.tag`","ums_id_", "ums_ts_", "ums_op_", "`header.ip`" , "tmax", "tavg", "tmin", "`header.ext.appgroup`", "tmin_st")
        println(df1.schema)
        df1.show(500, false)
    }
}
