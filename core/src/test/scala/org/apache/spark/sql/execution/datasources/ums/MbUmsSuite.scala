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

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.scalatest.FunSuite

class MbUmsSuite extends FunSuite {
  val conf = new SparkConf(true)
    //.set("spark.sql.hive.manageFilesourcePartitions", "false")
    .setMaster("local[*]")
    .setAppName("ums")

  var sparkContext = SparkContext.getOrCreate(conf)

//  val session = SparkSession.builder().config(conf).getOrCreate()
  var session = SparkSession.builder().sparkContext(sparkContext).getOrCreate()

  SparkSession.clearDefaultSession()
  SparkSession.clearActiveSession()

  sparkContext.stop()

  sparkContext = SparkContext.getOrCreate(conf)

  session = SparkSession.builder().sparkContext(sparkContext).getOrCreate()


  /*test("ums1 read local json") {
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
test("ums") {
  session.sql(
    """
      |create table aaa(
      |`header.tag` string,
      |ums_ts_ timestamp,
      |ums_op_ string,
      |`header.ip` string,
      |tmax long,
      |tavg long,
      |tmin long,
      |`header.ext.appgroup` string,
      |tmin_st long
      |) using ums options(path 'hdfs://master:8020/tmp/uav/[0-9]*')
    """.stripMargin)
  session.sql("select * from aaa").show(10000, false)
}*/

  test("aa") {
    session.sql(
      """
        			  |create table bbb(
        			  |ums_id_  		string,
        			  |ums_ts_  		timestamp,
        			  |ums_op_  		string,
        			  |ums_uid_  		long,
        			  |actor_id  		string,
        			  |first_name  		string,
        			  |last_name   		string,
        			  |mobile      		long,
        			  |last_update 		date,
        			  |col_1      		string,
        			  |mark      		string,
        			  |col_2      		string,
        			  |aaa				string
        			  |) using ums options(path '/tmp/actor/10/0/0/data_increment_data/right/[0-9]*')
      			""".stripMargin)

    // val df = session.read.format("ums").load(s"hdfs://master:8020/tmp/actor")
    session.sql("select * from bbb").show(1000, false)
  }

  test("string") {
    session.sql(
      // ums_id_ string,ums_ts_ timestamp,ums_op_ string,ums_uid_ long,advance_rate string,advance_rate_desc string,advance_type string,bank_code string,bank_name string,create_time timestamp,enterprise_or_personal int,expire_period int,expire_period_unit int,id long,incr_amount decimal,interest_rule string,interest_time_desc string,left_quota decimal,min_amount decimal,name string,open_time timestamp,product_id long,product_type int,publish_quota decimal,publish_time timestamp,purchase_agreement string,quota decimal,remark string,return_rate decimal,sort_weight int,status int,update_time timestamp,valid int,withdrawal_agreement string
      """create table a(
        			  |interest_rule string
        			  |) using ums options(path '/Users/wanghao/moonbox_test_data/product_ums.txt')
      			""".stripMargin
    )
    session.sql("select * from a").show()
  }

  test("ums") {
    session.sql(
      """create table a(
        			  |ums_id_ long,
        			  |ums_ts_ timestamp,
        			  |ums_op_ string,
        			  |ums_uid_ string,
        			  |ID long,
        			  |ACC_TYPE_ID string,
        			  |USER_ID long,
        			  |ACC_ID string
        			  |) using ums options(path '/tmp/user_account_new.txt', allowNumericLeadingZeros 'true')
      			""".stripMargin
    )
    session.sql("select count(*) from a").show(1000000)
  }

  test("ums1") {
    session.sql(
      """
        |create table a(
        |ums_id_ long,ums_ts_ timestamp,ums_op_ string,ums_uid_ long,calendar_name string,description string,end_time long,job_data string,job_group string,job_name string,misfire_instr int,next_fire_time long,prev_fire_time long,priority int,sched_name string,start_time long,trigger_group string,trigger_name string,trigger_state string,trigger_type string
        |) using ums options(path 'file:///Users/swallow/Desktop/data.txt', allowNumericLeadingZeros 'true', mode 'FAILFAST')
""".stripMargin
    )
    session.sql("select job_data from\na").show()
  }

  // allowNumericLeadingZeros 'true',

  test("json") {
    val str = "{\"payload\":[{\"tuple\":[\"349910095380811\",\"2019-10-23 23:33:45.156616\",\"u\",\"60730989463\",\"53801231\",\"2\",\"1\",\"Y20161215000030888\",\"4800.00349921\",\"4800.00349921\",\"4800.00349921\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"创建账户\",\"2\",\"0\",\"2016-12-15 13:48:29\",\"2019-10-23 23:34:38\"]},{\"tuple\":[\"349910095383891\",\"2019-10-23 23:33:45.156616\",\"u\",\"60730989464\",\"202436124\",\"4\",\"1\",\"Y20161215000030888\",\"5000.0\",\"5000.0\",\"5000.0\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"创建账户\",\"4\",\"0\",\"2019-10-23 23:34:38\",\"2019-10-23 23:34:39\"]},{\"tuple\":[\"349910095385503\",\"2019-10-23 23:33:45.156616\",\"u\",\"60730989465\",\"53801231\",\"2\",\"1\",\"Y20161215000030888\",\"0.00349921\",\"0.00349921\",\"0.00349921\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"创建账户\",\"2\",\"0\",\"2016-12-15 13:48:29\",\"2019-10-23 23:34:39\"]},{\"tuple\":[\"349910095386610\",\"2019-10-23 23:33:45.156616\",\"u\",\"60730989466\",\"5254136\",\"1\",\"1\",\"YRD1503000155827\",\"-3.4981265498E8\",\"-3.4981265498E8\",\"-3.4981265498E8\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"创建账户\",\"1\",\"1\",\"2015-03-19 10:49:14\",\"2019-10-23 23:34:39\"]},{\"tuple\":[\"349910095387804\",\"2019-10-23 23:33:47.162839\",\"i\",\"60730989467\",\"202436125\",\"8\",\"1\",\"YRD1507000463486\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"0.0\",\"创建账户\",\"8\",\"0\",\"2019-10-23 23:34:41\",\"2019-10-23 23:34:41\"]}],\"protocol\":{\"type\":\"data_increment_data\",\"version\":\"1.3\"},\"schema\":{\"batchId\":0,\"fields\":[{\"encoded\":false,\"name\":\"ums_id_\",\"nullable\":false,\"type\":\"long\"},{\"encoded\":false,\"name\":\"ums_ts_\",\"nullable\":false,\"type\":\"datetime\"},{\"encoded\":false,\"name\":\"ums_op_\",\"nullable\":false,\"type\":\"string\"},{\"encoded\":false,\"name\":\"ums_uid_\",\"nullable\":false,\"type\":\"string\"},{\"encoded\":false,\"name\":\"ACC_ID\",\"nullable\":false,\"type\":\"long\"},{\"encoded\":false,\"name\":\"TYPE_ID\",\"nullable\":true,\"type\":\"long\"},{\"encoded\":false,\"name\":\"SYS_ID\",\"nullable\":true,\"type\":\"long\"},{\"encoded\":false,\"name\":\"USER_PASSPORTID\",\"nullable\":true,\"type\":\"string\"},{\"encoded\":false,\"name\":\"TOTAL_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"MONEY_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"UNLOCK_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"LOCK_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"FREEZE_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"TRANSIT_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"TRANSIT_LOCK_VALUE\",\"nullable\":true,\"type\":\"decimal\"},{\"encoded\":false,\"name\":\"ACC_DESC\",\"nullable\":true,\"type\":\"string\"},{\"encoded\":false,\"name\":\"TYPE_CODE\",\"nullable\":true,\"type\":\"string\"},{\"encoded\":false,\"name\":\"ACC_STATUS\",\"nullable\":true,\"type\":\"string\"},{\"encoded\":false,\"name\":\"CREATION_TIME\",\"nullable\":true,\"type\":\"datetime\"},{\"encoded\":false,\"name\":\"UPDATED_TIME\",\"nullable\":true,\"type\":\"datetime\"}],\"namespace\":\"oracle.acc3_new.ACC.USER_ACCOUNT.0.0.0\"}"
    val mapper = new ObjectMapper()

    val ums = mapper.readTree(str)

  }

}
