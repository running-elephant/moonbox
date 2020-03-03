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

package moonbox.jdbc

import java.sql.{Connection, DatabaseMetaData, ResultSet, RowIdLifetime}

import moonbox.client.entity.{MoonboxRow, MoonboxRowSet}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

class MoonboxDatabaseMetaData(connection: MoonboxConnection) extends DatabaseMetaData {

	override def supportsMultipleOpenResults(): Boolean = ???
	override def supportsSubqueriesInIns(): Boolean = ???
	override def getSuperTypes(catalog: String, schemaPattern: String, typeNamePattern: String): ResultSet = ???
	override def getTablePrivileges(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet = ???
	override def supportsFullOuterJoins(): Boolean = ???
	override def insertsAreDetected(`type`: Int): Boolean = ???
	override def getDriverMajorVersion: Int = ???
	override def getDatabaseProductVersion: String = ???
	override def getIndexInfo(catalog: String, schema: String, table: String, unique: Boolean, approximate: Boolean): ResultSet = ???
	override def getFunctionColumns(catalog: String, schemaPattern: String, functionNamePattern: String, columnNamePattern: String): ResultSet = ???
	override def supportsCatalogsInTableDefinitions(): Boolean = ???
	override def isCatalogAtStart: Boolean = ???
	override def getJDBCMinorVersion: Int = ???
	override def supportsMixedCaseQuotedIdentifiers(): Boolean = ???
	override def storesUpperCaseQuotedIdentifiers(): Boolean = ???
	override def getUDTs(catalog: String, schemaPattern: String, typeNamePattern: String, types: Array[Int]): ResultSet = ???
	override def getAttributes(catalog: String, schemaPattern: String, typeNamePattern: String, attributeNamePattern: String): ResultSet = ???
	override def supportsStoredFunctionsUsingCallSyntax(): Boolean = ???
	override def nullsAreSortedAtStart(): Boolean = ???
	override def getMaxIndexLength: Int = ???
	override def getMaxTablesInSelect: Int = ???
	override def getClientInfoProperties: ResultSet = ???
	override def supportsSchemasInDataManipulation(): Boolean = ???
	override def getDatabaseMinorVersion: Int = ???
	override def supportsSchemasInProcedureCalls(): Boolean = ???
	override def supportsOuterJoins(): Boolean = ???
	override def supportsGroupBy(): Boolean = ???
	override def doesMaxRowSizeIncludeBlobs(): Boolean = ???
	override def supportsCatalogsInDataManipulation(): Boolean = ???
	override def getDatabaseProductName: String = "moonbox"
	override def supportsOpenCursorsAcrossCommit(): Boolean = ???
	override def supportsTableCorrelationNames(): Boolean = ???
	override def supportsExtendedSQLGrammar(): Boolean = ???
	override def getJDBCMajorVersion: Int = ???
	override def getUserName: String = {
		connection.getUserName()
	}
	override def getMaxProcedureNameLength: Int = ???
	override def getDriverName: String = "moonbox.jdbc.MbDriver"
	override def getMaxRowSize: Int = ???
	override def dataDefinitionCausesTransactionCommit(): Boolean = ???
	override def getMaxColumnNameLength: Int = ???
	override def getMaxSchemaNameLength: Int = ???
	override def getVersionColumns(catalog: String, schema: String, table: String): ResultSet = ???
	override def getNumericFunctions: String = ???
	override def supportsIntegrityEnhancementFacility(): Boolean = ???
	override def getIdentifierQuoteString: String = ???
	override def supportsNonNullableColumns(): Boolean = ???
	override def getMaxConnections: Int = ???
	override def supportsResultSetHoldability(holdability: Int): Boolean = ???
	override def supportsGroupByBeyondSelect(): Boolean = ???
	override def getFunctions(catalog: String, schemaPattern: String, functionNamePattern: String): ResultSet = ???
	override def supportsSchemasInPrivilegeDefinitions(): Boolean = ???
	override def supportsResultSetConcurrency(`type`: Int, concurrency: Int): Boolean = ???
	override def getURL: String = {
		connection.getUrl()
	}
	override def supportsSubqueriesInQuantifieds(): Boolean = ???
	override def supportsBatchUpdates(): Boolean = ???
	override def supportsLikeEscapeClause(): Boolean = ???
	override def supportsExpressionsInOrderBy(): Boolean = ???
	override def allTablesAreSelectable(): Boolean = ???
	override def getCrossReference(parentCatalog: String, parentSchema: String, parentTable: String, foreignCatalog: String, foreignSchema: String, foreignTable: String): ResultSet = ???
	override def getDatabaseMajorVersion: Int = ???
	override def supportsColumnAliasing(): Boolean = ???
	override def getMaxCursorNameLength: Int = ???
	override def getRowIdLifetime: RowIdLifetime = ???
	override def ownDeletesAreVisible(`type`: Int): Boolean = ???
	override def supportsDifferentTableCorrelationNames(): Boolean = ???
	override def getDefaultTransactionIsolation: Int = ???
	override def getSearchStringEscape: String = ???
	override def getMaxUserNameLength: Int = ???
	override def supportsANSI92EntryLevelSQL(): Boolean = ???
	override def getProcedureColumns(catalog: String, schemaPattern: String, procedureNamePattern: String, columnNamePattern: String): ResultSet = ???
	override def storesMixedCaseQuotedIdentifiers(): Boolean = ???
	override def supportsANSI92FullSQL(): Boolean = ???
	override def getMaxStatementLength: Int = ???
	override def othersDeletesAreVisible(`type`: Int): Boolean = ???
	override def supportsTransactions(): Boolean = ???
	override def deletesAreDetected(`type`: Int): Boolean = ???
	override def locatorsUpdateCopy(): Boolean = ???
	override def allProceduresAreCallable(): Boolean = ???
	override def getImportedKeys(catalog: String, schema: String, table: String): ResultSet = ???
	override def usesLocalFiles(): Boolean = ???
	override def supportsLimitedOuterJoins(): Boolean = ???
	override def storesMixedCaseIdentifiers(): Boolean = ???
	override def getCatalogTerm: String = ???
	override def getMaxColumnsInGroupBy: Int = ???
	override def supportsSubqueriesInExists(): Boolean = ???
	override def supportsPositionedUpdate(): Boolean = ???
	override def supportsGetGeneratedKeys(): Boolean = ???
	override def supportsUnion(): Boolean = ???
	override def nullsAreSortedLow(): Boolean = ???
	override def getSQLKeywords: String = ???
	override def supportsCorrelatedSubqueries(): Boolean = ???
	override def isReadOnly: Boolean = ???
	override def getProcedures(catalog: String, schemaPattern: String, procedureNamePattern: String): ResultSet = ???
	override def supportsUnionAll(): Boolean = ???
	override def supportsCoreSQLGrammar(): Boolean = ???
	override def getPseudoColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet = ???
	override def getCatalogs: ResultSet = ???
	override def getSuperTables(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet = ???
	override def getMaxColumnsInOrderBy: Int = ???
	override def supportsAlterTableWithAddColumn(): Boolean = ???
	override def getProcedureTerm: String = ???
	override def getMaxCharLiteralLength: Int = ???
	override def supportsMixedCaseIdentifiers(): Boolean = ???
	override def supportsDataDefinitionAndDataManipulationTransactions(): Boolean = ???
	override def supportsCatalogsInProcedureCalls(): Boolean = ???
	override def supportsGroupByUnrelated(): Boolean = ???
	override def getResultSetHoldability: Int = ???
	override def ownUpdatesAreVisible(`type`: Int): Boolean = ???
	override def nullsAreSortedHigh(): Boolean = ???
	override def getTables(catalog: String, schemaPattern: String, tableNamePattern: String, types: Array[String]): ResultSet = {
		val statement = connection.createStatement()
		val sqlBuilder = new StringBuilder
		sqlBuilder.append("show tables")
		if (catalog != null) {
			sqlBuilder.append(s" in $catalog")
		}
		if (tableNamePattern != null) {
			sqlBuilder.append(s" like '$tableNamePattern'")
		}
		statement.executeQuery(sqlBuilder.toString)
	}
	override def supportsMultipleTransactions(): Boolean = ???
	override def supportsNamedParameters(): Boolean = ???
	override def getTypeInfo: ResultSet = ???
	override def supportsAlterTableWithDropColumn(): Boolean = ???
	override def getSchemaTerm: String = ???
	override def nullPlusNonNullIsNull(): Boolean = ???
	override def getPrimaryKeys(catalog: String, schema: String, table: String): ResultSet = {
		val statement = connection.createStatement().asInstanceOf[MoonboxStatement]
		new MoonboxResultSet(connection, statement, new MoonboxRowSet())
	}
	override def supportsOpenCursorsAcrossRollback(): Boolean = ???
	override def getMaxBinaryLiteralLength: Int = ???
	override def getExtraNameCharacters: String = ???
	override def getSchemas: ResultSet = ???
	override def getSchemas(catalog: String, schemaPattern: String): ResultSet = ???
	override def supportsMultipleResultSets(): Boolean = ???
	override def ownInsertsAreVisible(`type`: Int): Boolean = ???
	override def nullsAreSortedAtEnd(): Boolean = ???
	override def supportsSavepoints(): Boolean = ???
	override def getMaxStatements: Int = ???
	override def getBestRowIdentifier(catalog: String, schema: String, table: String, scope: Int, nullable: Boolean): ResultSet = ???
	override def getDriverVersion: String = ???
	override def storesUpperCaseIdentifiers(): Boolean = ???
	override def storesLowerCaseIdentifiers(): Boolean = ???
	override def getMaxCatalogNameLength: Int = ???
	override def supportsDataManipulationTransactionsOnly(): Boolean = ???
	override def getSystemFunctions: String = ???
	override def getColumnPrivileges(catalog: String, schema: String, table: String, columnNamePattern: String): ResultSet = ???
	override def getDriverMinorVersion: Int = ???
	override def getMaxTableNameLength: Int = ???
	override def dataDefinitionIgnoredInTransactions(): Boolean = ???
	override def getStringFunctions: String = ???
	override def getMaxColumnsInSelect: Int = ???
	override def usesLocalFilePerTable(): Boolean = ???
	override def autoCommitFailureClosesAllResultSets(): Boolean = ???
	override def supportsCatalogsInIndexDefinitions(): Boolean = ???
	override def storesLowerCaseQuotedIdentifiers(): Boolean = ???
	override def othersUpdatesAreVisible(`type`: Int): Boolean = ???
	override def supportsStatementPooling(): Boolean = ???
	override def supportsCatalogsInPrivilegeDefinitions(): Boolean = ???
	override def supportsStoredProcedures(): Boolean = ???
	override def supportsSelectForUpdate(): Boolean = ???
	override def supportsOpenStatementsAcrossCommit(): Boolean = ???
	override def supportsSubqueriesInComparisons(): Boolean = ???
	override def supportsTransactionIsolationLevel(level: Int): Boolean = ???
	override def getTableTypes: ResultSet = ???
	override def getMaxColumnsInTable: Int = ???
	override def getConnection: Connection = ???
	override def updatesAreDetected(`type`: Int): Boolean = ???
	override def supportsPositionedDelete(): Boolean = ???
	override def getColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet = {
		val resultSet = connection.createStatement().executeQuery(s"desc $tableNamePattern")
		val schema =
			s"""{
			|type: struct,
			|fields: [
			|{name: TABLE_CAT, type: string, nullable: true},
			|{name: TABLE_SCHEM, type: string, nullable: true},
			|{name: TABLE_NAME, type: string, nullable: true},
			|{name: COLUMN_NAME, type: string, nullable: true},
			|{name: DATA_TYPE, type: string, nullable: true},
			|{name: TYPE_NAME, type: string, nullable: true}
			|]
			|}""".stripMargin

		val moonboxRowSet = {
			val rows = new ArrayBuffer[MoonboxRow]()
			while (resultSet.next()) {
				val row = Array[Any](connection.getCatalog, connection.getSchema, tableNamePattern, resultSet.getString(1), resultSet.getString(2), resultSet.getString(2))
				rows.append(new MoonboxRow(row))
			}
			new MoonboxRowSet(rows.toIterator.asJava, schema)
		}
		new MoonboxResultSet(connection, resultSet.getStatement.asInstanceOf[MoonboxStatement], moonboxRowSet)
	}
	override def supportsResultSetType(`type`: Int): Boolean = ???
	override def supportsMinimumSQLGrammar(): Boolean = ???
	override def generatedKeyAlwaysReturned(): Boolean = ???
	override def supportsConvert(): Boolean = ???
	override def supportsConvert(fromType: Int, toType: Int): Boolean = ???
	override def getExportedKeys(catalog: String, schema: String, table: String): ResultSet = ???
	override def supportsOrderByUnrelated(): Boolean = ???
	override def getSQLStateType: Int = ???
	override def supportsOpenStatementsAcrossRollback(): Boolean = ???
	override def getMaxColumnsInIndex: Int = ???
	override def getTimeDateFunctions: String = ???
	override def supportsSchemasInIndexDefinitions(): Boolean = ???
	override def supportsANSI92IntermediateSQL(): Boolean = ???
	override def getCatalogSeparator: String = ???
	override def othersInsertsAreVisible(`type`: Int): Boolean = ???
	override def supportsSchemasInTableDefinitions(): Boolean = ???
	override def unwrap[T](iface: Class[T]): T = ???
	override def isWrapperFor(iface: Class[_]): Boolean = ???
}
