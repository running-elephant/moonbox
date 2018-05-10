package moonbox.jdbc

import java.sql.{Connection, DatabaseMetaData, ResultSet, RowIdLifetime}

class MoonboxDatabaseMetaData extends DatabaseMetaData{
  override def supportsMinimumSQLGrammar(): Boolean = ???

  override def getResultSetHoldability: Int = ???

  override def getMaxColumnsInGroupBy: Int = ???

  override def supportsSubqueriesInComparisons(): Boolean = ???

  override def getMaxColumnsInSelect: Int = ???

  override def nullPlusNonNullIsNull(): Boolean = ???

  override def supportsCatalogsInDataManipulation(): Boolean = ???

  override def supportsDataDefinitionAndDataManipulationTransactions(): Boolean = ???

  override def supportsTableCorrelationNames(): Boolean = ???

  override def getDefaultTransactionIsolation: Int = ???

  override def supportsFullOuterJoins(): Boolean = ???

  override def supportsExpressionsInOrderBy(): Boolean = ???

  override def allProceduresAreCallable(): Boolean = ???

  override def supportsResultSetConcurrency(`type`: Int, concurrency: Int): Boolean = ???

  override def getDriverMinorVersion: Int = ???

  override def getMaxTablesInSelect: Int = ???

  override def nullsAreSortedAtStart(): Boolean = ???

  override def getImportedKeys(catalog: String, schema: String, table: String): ResultSet = ???

  override def supportsPositionedUpdate(): Boolean = ???

  override def ownDeletesAreVisible(`type`: Int): Boolean = ???

  override def supportsResultSetHoldability(holdability: Int): Boolean = ???

  override def getMaxStatements: Int = ???

  override def getRowIdLifetime: RowIdLifetime = ???

  override def getDriverVersion: String = ???

  override def getSchemaTerm: String = ???

  override def getMaxCatalogNameLength: Int = ???

  override def getCrossReference(parentCatalog: String, parentSchema: String, parentTable: String, foreignCatalog: String, foreignSchema: String, foreignTable: String): ResultSet = ???

  override def getCatalogTerm: String = ???

  override def getAttributes(catalog: String, schemaPattern: String, typeNamePattern: String, attributeNamePattern: String): ResultSet = ???

  override def getMaxStatementLength: Int = ???

  override def supportsOuterJoins(): Boolean = ???

  override def supportsBatchUpdates(): Boolean = ???

  override def supportsLimitedOuterJoins(): Boolean = ???

  override def getMaxColumnsInTable: Int = ???

  override def allTablesAreSelectable(): Boolean = ???

  override def getMaxCharLiteralLength: Int = ???

  override def supportsMultipleOpenResults(): Boolean = ???

  override def getMaxRowSize: Int = ???

  override def supportsUnion(): Boolean = ???

  override def supportsOpenCursorsAcrossCommit(): Boolean = ???

  override def ownUpdatesAreVisible(`type`: Int): Boolean = ???

  override def getSearchStringEscape: String = ???

  override def getMaxBinaryLiteralLength: Int = ???

  override def supportsAlterTableWithDropColumn(): Boolean = ???

  override def supportsResultSetType(`type`: Int): Boolean = ???

  override def supportsCatalogsInProcedureCalls(): Boolean = ???

  override def supportsSelectForUpdate(): Boolean = ???

  override def getUDTs(catalog: String, schemaPattern: String, typeNamePattern: String, types: Array[Int]): ResultSet = ???

  override def supportsOpenStatementsAcrossRollback(): Boolean = ???

  override def getSystemFunctions: String = ???

  override def supportsColumnAliasing(): Boolean = ???

  override def insertsAreDetected(`type`: Int): Boolean = ???

  override def supportsMixedCaseIdentifiers(): Boolean = ???

  override def getDatabaseProductVersion: String = ???

  override def getSQLKeywords: String = ???

  override def dataDefinitionIgnoredInTransactions(): Boolean = ???

  override def getJDBCMajorVersion: Int = ???

  override def getMaxColumnNameLength: Int = ???

  override def isReadOnly: Boolean = ???

  override def getProcedureColumns(catalog: String, schemaPattern: String, procedureNamePattern: String, columnNamePattern: String): ResultSet = ???

  override def getCatalogs: ResultSet = ???

  override def locatorsUpdateCopy(): Boolean = ???

  override def supportsANSI92FullSQL(): Boolean = ???

  override def supportsMultipleResultSets(): Boolean = ???

  override def storesUpperCaseIdentifiers(): Boolean = ???

  override def supportsSchemasInPrivilegeDefinitions(): Boolean = ???

  override def getDriverName: String = "Moonbox jdbc Driver"

  override def getMaxConnections: Int = ???

  override def othersUpdatesAreVisible(`type`: Int): Boolean = ???

  override def getVersionColumns(catalog: String, schema: String, table: String): ResultSet = ???

  override def supportsNamedParameters(): Boolean = ???

  override def doesMaxRowSizeIncludeBlobs(): Boolean = ???

  override def getBestRowIdentifier(catalog: String, schema: String, table: String, scope: Int, nullable: Boolean): ResultSet = ???

  override def getProcedures(catalog: String, schemaPattern: String, procedureNamePattern: String): ResultSet = ???

  override def getDriverMajorVersion: Int = ???

  override def supportsSubqueriesInQuantifieds(): Boolean = ???

  override def getMaxSchemaNameLength: Int = ???

  override def supportsIntegrityEnhancementFacility(): Boolean = ???

  override def getTablePrivileges(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet = ???

  override def supportsExtendedSQLGrammar(): Boolean = ???

  override def supportsConvert(): Boolean = ???

  override def supportsConvert(fromType: Int, toType: Int): Boolean = ???

  override def getFunctionColumns(catalog: String, schemaPattern: String, functionNamePattern: String, columnNamePattern: String): ResultSet = ???

  override def supportsPositionedDelete(): Boolean = ???

  override def autoCommitFailureClosesAllResultSets(): Boolean = ???

  override def getMaxColumnsInOrderBy: Int = ???

  override def getDatabaseMajorVersion: Int = ???

  override def supportsANSI92IntermediateSQL(): Boolean = ???

  override def getSuperTypes(catalog: String, schemaPattern: String, typeNamePattern: String): ResultSet = ???

  override def supportsMultipleTransactions(): Boolean = ???

  override def supportsCatalogsInTableDefinitions(): Boolean = ???

  override def supportsOpenCursorsAcrossRollback(): Boolean = ???

  override def supportsStatementPooling(): Boolean = ???

  override def usesLocalFiles(): Boolean = ???

  override def storesMixedCaseQuotedIdentifiers(): Boolean = ???

  override def othersInsertsAreVisible(`type`: Int): Boolean = ???

  override def supportsSchemasInProcedureCalls(): Boolean = ???

  override def getMaxCursorNameLength: Int = ???

  override def getUserName: String = ???

  override def supportsTransactionIsolationLevel(level: Int): Boolean = ???

  override def deletesAreDetected(`type`: Int): Boolean = ???

  override def supportsDataManipulationTransactionsOnly(): Boolean = ???

  override def supportsLikeEscapeClause(): Boolean = ???

  override def supportsSchemasInDataManipulation(): Boolean = ???

  override def supportsGetGeneratedKeys(): Boolean = ???

  override def supportsGroupBy(): Boolean = ???

  override def getIndexInfo(catalog: String, schema: String, table: String, unique: Boolean, approximate: Boolean): ResultSet = ???

  override def getSchemas: ResultSet = ???

  override def getSchemas(catalog: String, schemaPattern: String): ResultSet = ???

  override def supportsSchemasInIndexDefinitions(): Boolean = ???

  override def getCatalogSeparator: String = ???

  override def getExtraNameCharacters: String = ???

  override def getURL: String = ???

  override def supportsDifferentTableCorrelationNames(): Boolean = ???

  override def supportsTransactions(): Boolean = ???

  override def getDatabaseMinorVersion: Int = ???

  override def storesLowerCaseQuotedIdentifiers(): Boolean = ???

  override def supportsANSI92EntryLevelSQL(): Boolean = ???

  override def supportsStoredProcedures(): Boolean = ???

  override def supportsCatalogsInPrivilegeDefinitions(): Boolean = ???

  override def getIdentifierQuoteString: String = ???

  override def getMaxTableNameLength: Int = ???

  override def getSQLStateType: Int = ???

  override def getColumnPrivileges(catalog: String, schema: String, table: String, columnNamePattern: String): ResultSet = ???

  override def getMaxColumnsInIndex: Int = ???

  override def getTimeDateFunctions: String = ???

  override def getTableTypes: ResultSet = ???

  override def getJDBCMinorVersion: Int = ???

  override def nullsAreSortedHigh(): Boolean = ???

  override def supportsNonNullableColumns(): Boolean = ???

  override def getMaxUserNameLength: Int = ???

  override def supportsSubqueriesInExists(): Boolean = ???

  override def ownInsertsAreVisible(`type`: Int): Boolean = ???

  override def supportsOpenStatementsAcrossCommit(): Boolean = ???

  override def supportsSavepoints(): Boolean = ???

  override def getSuperTables(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet = ???

  override def dataDefinitionCausesTransactionCommit(): Boolean = ???

  override def nullsAreSortedAtEnd(): Boolean = ???

  override def getNumericFunctions: String = ???

  override def generatedKeyAlwaysReturned(): Boolean = ???

  override def supportsUnionAll(): Boolean = ???

  override def supportsAlterTableWithAddColumn(): Boolean = ???

  override def isCatalogAtStart: Boolean = ???

  override def othersDeletesAreVisible(`type`: Int): Boolean = ???

  override def supportsCoreSQLGrammar(): Boolean = ???

  override def getMaxProcedureNameLength: Int = ???

  override def getColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet = ???

  override def getConnection: Connection = ???

  override def getDatabaseProductName: String = ???

  override def supportsGroupByUnrelated(): Boolean = ???

  override def nullsAreSortedLow(): Boolean = ???

  override def getTables(catalog: String, schemaPattern: String, tableNamePattern: String, types: Array[String]): ResultSet = ???

  override def supportsCorrelatedSubqueries(): Boolean = ???

  override def supportsMixedCaseQuotedIdentifiers(): Boolean = ???

  override def supportsGroupByBeyondSelect(): Boolean = ???

  override def supportsCatalogsInIndexDefinitions(): Boolean = ???

  override def getStringFunctions: String = ???

  override def supportsOrderByUnrelated(): Boolean = ???

  override def getMaxIndexLength: Int = ???

  override def getProcedureTerm: String = ???

  override def getFunctions(catalog: String, schemaPattern: String, functionNamePattern: String): ResultSet = ???

  override def getClientInfoProperties: ResultSet = ???

  override def supportsStoredFunctionsUsingCallSyntax(): Boolean = ???

  override def getPseudoColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet = ???

  override def usesLocalFilePerTable(): Boolean = ???

  override def storesLowerCaseIdentifiers(): Boolean = ???

  override def supportsSubqueriesInIns(): Boolean = ???

  override def updatesAreDetected(`type`: Int): Boolean = ???

  override def getTypeInfo: ResultSet = ???

  override def getExportedKeys(catalog: String, schema: String, table: String): ResultSet = ???

  override def getPrimaryKeys(catalog: String, schema: String, table: String): ResultSet = ???

  override def supportsSchemasInTableDefinitions(): Boolean = ???

  override def storesUpperCaseQuotedIdentifiers(): Boolean = ???

  override def storesMixedCaseIdentifiers(): Boolean = ???

  override def unwrap[T](iface: Class[T]): T = ???

  override def isWrapperFor(iface: Class[_]): Boolean = ???
}
