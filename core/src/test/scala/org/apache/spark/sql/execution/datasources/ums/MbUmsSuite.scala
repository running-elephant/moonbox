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
		//.set("spark.sql.hive.manageFilesourcePartitions", "false")
            .setMaster("local[*]")
            .setAppName("ums")
    val session = SparkSession.builder().config(conf).getOrCreate()

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

}
