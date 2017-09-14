/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.core.split

import java.sql.DriverManager
import java.util.Properties

import edp.moonbox.calcite.jdbc.CalciteConnection
import edp.moonbox.common.{EdpLogging, Util}
import org.apache.spark.sql.catalyst.plans.logical._


abstract class Platform extends EdpLogging {

	val name: String

	val externalSupported: Seq[Class[_]]

	val baseSupport: Seq[Class[_]] = Seq(classOf[Filter], classOf[Project])

	def isSupportAll: Boolean

	def beGoodAt: Seq[Class[_]]

	def supported: Seq[Class[_]] = baseSupport ++ externalSupported

	def isSupport(plan: LogicalPlan): Boolean = isSupportAll || supported.contains(plan.getClass)

	def equals(other: Platform): Boolean

	def getConnection(): () => CalciteConnection = throw new Exception("can not call getConnection function")
}

class SparkPlatform extends Platform {

	override val name: String = "spark"

	override def isSupportAll: Boolean = true

	override def equals(other: Platform): Boolean = {
		if (other.isInstanceOf[SparkPlatform]) true
		else false
	}

	// when function isSupportAll returns true, externalSupported takes no effect
	override val externalSupported: Seq[Class[_]] = Nil

	override def beGoodAt: Seq[Class[_]] = Seq()
}

class EsV5Platform(val property: Map[String, String]) extends Platform {

	override val name: String = "es5"

	override def isSupportAll: Boolean = false

	override def equals(other: Platform): Boolean = other match {
		case p: this.type => coordinates.equals(p.coordinates) && index.equals(p.index) && cluster.equals(p.cluster)
		case _ => false
	}

	def coordinates = nodes + ":" + port

	def alias = Util.getOrException(property, "tableAlias")

	def nodes = Util.getOrException(property, "es.nodes")

	def port = property.getOrElse("es.transparent.port", {
		logWarning("calcite connect to elasticsearch via transparent client, " +
			"transparent port is not defined, use 9300 default")
		"9300"
	})

	def index = Util.getOrException(property, "es.index")

	def cluster = Util.getOrException(property, "es.cluster")

	def dbtable = Util.getOrException(property, "dbtable")

	override val externalSupported: Seq[Class[_]] = Seq(
		classOf[Aggregate],
		classOf[Sort],
		classOf[GlobalLimit],
		classOf[LocalLimit]
	)

	override def beGoodAt: Seq[Class[_]] = Seq(
		classOf[Aggregate],
		classOf[Sort],
		classOf[GlobalLimit],
		classOf[LocalLimit]
	)

	override def getConnection(): () => CalciteConnection =  {
		val schema: String = s"""
			   |{
			   |  version: '1.0',
			   |  defaultSchema: 'origin',
			   |  schemas: [
			   |   {
			   |       type: 'custom',
			   |       name: 'origin',
			   |       factory: 'edp.elasticsearch.ElasticsearchSchemaFactory',
			   |       operand: {
			   |         coordinates: '{\\'$nodes\\':$port}',
			   |         userConfig: '{\\'cluster.name\\': \\'$cluster\\'}',
			   |         index: '$index'
			   |       },
			   |       tables: [{
			   |         name: '$alias',
			   |         type: 'view',
			   |         sql: 'select * from "origin"."$dbtable"'
			   |       }]
			   |   }
			   |  ]
			   |}
				""".stripMargin
		// in order to eliminate object not Serializable problem when construct CalciteRDD
		((schema: String) => {
					logInfo(schema)
			() => {
				Class.forName("edp.moonbox.calcite.jdbc.Driver")
				val info = new Properties()
				info.put("LEX", "MYSQL")
				val conn = DriverManager.getConnection("jdbc:edp:calcite:model=inline:" + schema, info)
				val calciteConnection: CalciteConnection = conn.unwrap(classOf[CalciteConnection])
				calciteConnection
			}
		})(schema)
	}
}

class JdbcPlatform(property: Map[String, String]) extends Platform {

	override val name: String = "jdbc"

	override def isSupportAll: Boolean = false

	// TODO add other supported logical plan
	override val externalSupported: Seq[Class[_]] = Seq(
		classOf[Join],
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Sort],
		classOf[Aggregate],
		classOf[Window],
		classOf[SubqueryAlias]
	)

	override def beGoodAt: Seq[Class[_]] = Seq(
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Aggregate]
	)

	private def `type` =  Util.getOrException(property, "type")

	private def driver = `type` match {
		case "mysql" => "com.mysql.jdbc.Driver"
		//case "oracle" => "oracle.OracleDriver"
		case _ => throw new Exception("")

	}
	def alias = Util.getOrException(property, "tableAlias")

	private def url = Util.getOrException(property, "url")

	private def user = Util.getOrException(property, "user")

	private def password = Util.getOrException(property, "password")

	private def dbtable = Util.getOrException(property, "dbtable")

	// true if and only if two platforms are the same database
	override def equals(other: Platform): Boolean = {
		if (!other.isInstanceOf[JdbcPlatform]) false
		else {
			if (!name.equals(other.name)) false
			else {
				url.equals(other.asInstanceOf[JdbcPlatform].url)
			}
		}
	}

	override def getConnection(): () => CalciteConnection =  {

		val schema: String =
			s"""
			   |{
			   |  version: '1.0',
			   |  defaultSchema: 'aliased',
			   |  schemas: [
			   |   {
			   |       type: 'custom',
			   |       name: 'origin',
			   |       factory: 'edp.moonbox.calcite.adapter.jdbc.JdbcSchema$$Factory',
			   |       operand: {
			   |            jdbcDriver: '$driver',
			   |            jdbcUser: '$user',
			   |            jdbcUrl: '$url',
			   |            jdbcPassword: '$password'
			   |       }
			   |   },{
			   |       name : "aliased",
			   |       tables: [{
			   |             name: '$alias',
			   |             type: 'view',
			   |             sql: 'select * from "origin"."$dbtable"'
			   |       }]
			   |   }]
			   |}
				""".stripMargin

		((schema: String) => {
				logInfo(schema)
				() => {
					Class.forName("edp.moonbox.calcite.jdbc.Driver")
					val info = new Properties()
					info.put("LEX", "MYSQL")
					val conn = DriverManager.getConnection("jdbc:edp:calcite:model=inline:" + schema, info)
					val calciteConnection: CalciteConnection = conn.unwrap(classOf[CalciteConnection])
					calciteConnection
				}
			}
		)(schema)
	}
}

