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

package moonbox.core

import org.apache.spark.sql.SparkSession
import org.scalatest.FunSuite

class MoonboxSessionSuite extends FunSuite {

  test("mbSession") {

    val spark: SparkSession = SparkSession.builder().appName("test").master("local[*]").getOrCreate()

    spark.sql("create table parquet(id int, day string) using parquet options(path '/tmp/ccc') PARTITIONED BY (day)")

    spark.sql("insert into parquet partition(day='2019-08-13-12-00-00') select 1 as id")
    spark.sql("insert into parquet partition(day='2017-08-13-12-00-00') select 2 as id")
    spark.sql("insert into parquet partition(day='2018-08-13-12-00-00') select 3 as id")

    spark.sql("select id from parquet where day='2017'").show()

  }
}