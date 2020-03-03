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

package moonbox.grid.deploy.audit

import moonbox.common.MbConf

abstract class AuditLoggerFactory {
	def createAuditLogger(): AuditLogger
}

class MySQLAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new MySQLAuditLogger(conf)
	}
}

class ElasticSearchAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new ElasticSearchAuditLogger(conf)
	}
}

class FileAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new FileAuditLogger(conf)
	}
}

class HdfsAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new HdfsAuditLogger(conf)
	}
}

class BlackHoleAuditLoggerFactory extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new BlackHoleAuditLogger
	}
}
