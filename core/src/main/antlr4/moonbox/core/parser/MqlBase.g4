grammar MqlBase;

tokens {
    DELIMITER
}

single
    : mql EOF
    ;

mql
    : CREATE (ORG|ORGANIZATION) (IF NOT EXISTS)?
        name=identifier (COMMENT comment=STRING)? (OPTIONS propertyList)?                       # createOrganization
    | RENAME (ORG|ORGANIZATION) name=identifier TO newName=identifier                           # renameOrganization
    | ALTER (ORG|ORGANIZATION) name=identifier RENAME TO newName=identifier                     # setOrganizationName
    | ALTER (ORG|ORGANIZATION) name=identifier SET OPTIONS propertyList                         # setOrganizationOptions
    | ALTER (ORG|ORGANIZATION) name=identifier SET COMMENT comment=STRING                       # setOrganizationComment
    | DROP (ORG|ORGANIZATION) (IF EXISTS)? name=identifier (CASCADE)?                           # dropOrganization

    | CREATE SA (IF NOT EXISTS)? name=identifier IN (ORG|ORGANIZATION)?
        org=identifier IDENTIFIED BY pwd=password (OPTIONS propertyList)?                       # createSa
    | RENAME SA name=identifier IN (ORG|ORGANIZATION)? org=identifier TO newName=identifier      # renameSa
    | ALTER SA name=identifier IN (ORG|ORGANIZATION)? org=identifier
        RENAME TO newName=identifier                                                            # setSaName
    | ALTER SA name=identifier IN (ORG|ORGANIZATION)? org=identifier
        IDENTIFIED BY pwd=password                                                              # setSaPassword
    | ALTER SA name=identifier IN (ORG|ORGANIZATION)? org=identifier SET OPTIONS propertyList    # setSaOptions
    | DROP SA (IF EXISTS)? name=identifier IN (ORG|ORGANIZATION)? org=identifier                 # dropSa

    | GRANT GRANT OPTION grantPrivilegeList TO USER? user=identifier                        # grantGrantToUser
    | REVOKE GRANT OPTION grantPrivilegeList FROM USER? user=identifier                     # revokeGrantFromUser


    | GRANT grantPrivilegeList TO USER? user=identifier                                     # grantPrivilegeToUsers
    | REVOKE grantPrivilegeList FROM USER? user=identifier                                  # revokePrivilegeFromUsers


    | GRANT privileges ON tableCollections TO USER? user=identifier                        # grantResourcePrivilegeToUsers
    | REVOKE privileges ON tableCollections FROM USER? user=identifier                     # revokeResourcePrivilegeFromUsers


    | CREATE USER (IF NOT EXISTS)? name=identifier IDENTIFIED BY pwd=password
        (OPTIONS propertyList)?                                                                 # createUser
    | RENAME USER name=identifier TO newName=identifier                                         # renameUser
    | ALTER USER name=identifier RENAME TO newName=identifier                                   # setUserName
    | ALTER USER name=identifier IDENTIFIED BY pwd=password                                     # setUserPassword
    | ALTER USER name=identifier SET OPTIONS propertyList                                       # setUserOptions
    | DROP USER (IF EXISTS)? name=identifier                                                    # dropUser

    | MOUNT STREAM? TABLE (IF NOT EXISTS)? tableIdentifier ('('columns=colTypeList')')?
        OPTIONS propertyList                                                                    # mountTable
    | RENAME TABLE name=tableIdentifier TO newName=tableIdentifier                              # renameTable
    | ALTER TABLE name=tableIdentifier RENAME TO newName=tableIdentifier                        # setTableName
    | ALTER TABLE name=tableIdentifier SET OPTIONS propertyList                                 # setTableProperties
    | UNMOUNT TABLE (IF EXISTS)? name=tableIdentifier                                           # unmountTable

    | MOUNT DATABASE (IF NOT EXISTS)? name=identifier OPTIONS propertyList                      # mountDatabase
    | UNMOUNT DATABASE (IF EXISTS)? name=identifier   (CASCADE)?                                # unmountDatabase
    | ALTER DATABASE name=identifier SET OPTIONS propertyList                                   # setDatabaseProperties
    | REFRESH DATABASE name=identifier                                                          # refreshDatabase

    | CREATE DATABASE (IF NOT EXISTS)? name=identifier (COMMENT comment=STRING)?                # createDatabase
    | RENAME DATABASE name=identifier TO newName=identifier                                     # renameDatabase
    | ALTER DATABASE name=identifier RENAME TO newName=identifier                               # setDatabaseName
    | ALTER DATABASE name=identifier SET COMMENT comment=STRING                                 # setDatabaseComment
    | DROP DATABASE (IF EXISTS)? name=identifier (CASCADE)?                                     # dropDatabase
    | USE db=identifier                                                                         # useDatabase

    | CREATE (TEMP | TEMPORARY)? FUNCTION (IF NOT EXISTS)? name=funcIdentifier
        AS className=STRING (methodName=STRING)? (USING resource (',' resource)*)?              # createFunction
    | DROP (TEMP | TEMPORARY)? FUNCTION (IF EXISTS)? name=funcIdentifier                        # dropFunction

    | CREATE (OR REPLACE)? VIEW name=tableIdentifier (COMMENT comment=STRING)? AS query         # createView
   // | RENAME VIEW name=tableIdentifier TO newName=tableIdentifier                               # renameView
    //| ALTER VIEW name=tableIdentifier RENAME TO newName=tableIdentifier                         # setViewName
   // | ALTER VIEW name=tableIdentifier SET COMMENT comment=STRING                                # setViewComment
    | ALTER VIEW name=tableIdentifier AS query                                                  # setViewQuery
    | DROP VIEW (IF EXISTS)? name=tableIdentifier                                               # dropView

    | CREATE (PROC | PROCEDURE) (IF NOT EXISTS)? name=identifier USING (MQL | HQL) AS procCmds # createProcedure
    | RENAME (PROC | PROCEDURE) name=identifier TO newName=identifier                           # renameProcedure
    | ALTER  (PROC | PROCEDURE) name=identifier RENAME TO newName=identifier                    # setProcedureName
    | ALTER  (PROC | PROCEDURE) name=identifier AS procCmds                                     # setProcedureQuerys
    | DROP   (PROC | PROCEDURE) (IF EXISTS)? name=identifier                                    # dropProcedure

    | CREATE (DEFINER definer)? EVENT (IF NOT EXISTS)? name=identifier ON SCHEDULE AT
        cronExpression=STRING
        (ENABLE | DISABLE)? (COMMENT comment=STRING)? DO CALL proc=identifier                   # createEvent
    | RENAME EVENT name=identifier TO newName=identifier                                        # renameEvent
    | ALTER DEFINER definer EVENT name=identifier                                               # setDefiner
    | ALTER EVENT name=identifier RENAME TO newName=identifier                                  # setEventName
    | ALTER EVENT name=identifier ON SCHEDULE AT cronExpression=STRING                          # setEventSchedule
    | ALTER EVENT name=identifier (ENABLE | DISABLE)                                            # setEventEnable
    | DROP EVENT (IF EXISTS)? name=identifier                                                   # dropEvent

    | SHOW SYSINFO                                                                              # showSysInfo
    | SHOW JOBS                                                                                 # showJobs
    | SHOW RUNNING EVENTS                                                                       # showRunningEvents
    | SHOW EVENTS (LIKE pattern=STRING)?                                                        # showEvents

    | SHOW DATABASES (LIKE pattern=STRING)?                                                     # showDatabase
    | SHOW TABLES ((FROM | IN) db=identifier)? (LIKE pattern=STRING)?                           # showTables

    | SHOW (scope=identifier)? FUNCTIONS ((FROM | IN) db=identifier)? (LIKE pattern=STRING)?    # showFunctions

    | SHOW (ORGS | ORGANIZATIONS) (LIKE pattern=STRING)?                                        # showOrgs
    | SHOW SAS (LIKE pattern=STRING)?                                                           # showSas
    | SHOW USERS (LIKE pattern=STRING)?                                                         # showUsers

    | SHOW (PROCS | PROCEDURES) (LIKE pattern=STRING)?                                          # showProcedures
    | SHOW VARIABLES (LIKE pattern=STRING)?                                                     # showVariable
    | SHOW GRANTS FOR user=identifier                                                           # showGrants
    | SHOW CREATE TABLE name=tableIdentifier                                                    # showCreateTable
    | SHOW SCHEMA FOR query                                                                     # showSchema

    | (DESC | DESCRIBE) EVENT name=identifier                                                   # descEvent
    | (DESC | DESCRIBE) (PROC | PROCEDURE) name=identifier                                      # descProcedure
    | (DESC | DESCRIBE) DATABASE name=identifier                                                # descDatabase
    | (DESC | DESCRIBE) TABLE? EXTENDED? tableIdentifier                                        # descTable

    | (DESC | DESCRIBE) FUNCTION EXTENDED? funcIdentifier                                       # descFunction
    | (DESC | DESCRIBE) USER name=identifier                                                    # descUser
    | (DESC | DESCRIBE) ORG name=identifier                                                     # descOrg

    | EXPLAIN EXTENDED? PLAN? query                                                             # explain
    //| SET (GLOBAL | SESSION?) key=identifier EQ? value=.*?                                      # setVariable
    //| REFRESH TABLE tableIdentifier                                                             # refreshTable
    //| REFRESH path=.*?                                                                          #refreshResource

    //| INSERT (INTO | OVERWRITE | UPDATE | MERGE ) TABLE? tableIdentifier partitionSpec? coalesceSpec? AS? query                 # insertInto
    | CREATE (OR REPLACE)? CACHE? (TEMP | TEMPORARY) VIEW name=identifier AS? query             # createTemporaryView
    //| query                                                                                     # mqlQuery
    | statement=(SELECT | WITH | INSERT | SET | ANALYZE | REFRESH).*?                                                                             # statement
    ;

procCmds
    : '(' mql (';' mql)* ')'
    ;
definer
    : EQ? (user = identifier | CURRENT_USER)
    ;

query
    : SELECT (~';')*
    | ctes SELECT (~';')*
    ;
ctes
    : WITH namedQuery (',' namedQuery)*
    ;

partitionSpec
    : PARTITION '(' identifier (',' identifier)* ')'
    ;

coalesceSpec
    : COALESCE num=INTEGER_VALUE
    ;

dataType
    : complex=ARRAY '<' dataType '>'                            #complexDataType
    | complex=MAP '<' dataType ',' dataType '>'                 #complexDataType
    | complex=STRUCT ('<' complexColTypeList? '>' | NEQ)        #complexDataType
    | identifier ('(' INTEGER_VALUE (',' INTEGER_VALUE)* ')')?  #primitiveDataType
    ;

colTypeList
    : colType (',' colType)*
    ;

colType
    : identifier dataType
    ;

complexColTypeList
    : complexColType (',' complexColType)*
    ;

complexColType
    : identifier ':' dataType
    ;

namedQuery
    : name=identifier AS? '(' query ')'
    ;

grantPrivilegeList
    :  grantPrivilege (',' grantPrivilege)*
    ;

grantPrivilege
    : ACCOUNT
    | DDL
    | DCL
    ;

privileges
    : privilege (',' privilege)*
    ;
privilege
    : SELECT columnIdentifiers?
    | UPDATE columnIdentifiers?
    | INSERT
    | DELETE
    | TRUNCATE
    | ALL
    ;

columnIdentifiers
    : '(' identifier (',' identifier)* ')'
    ;

tableCollections
    : (db=identifier '.')? table = identifierOrStar
    ;

identifierOrStar
    : identifier
    | STAR
    ;

addUser
    : ADD USER identifierList
    ;

removeUser
    : REMOVE USER identifierList
    ;

identifierList
    : identifier (',' identifier)*
    ;

funcIdentifier
    : (db=identifier '.')? func=identifier
    ;

tableIdentifier
    : (db=identifier '.')? table=identifier
    ;

propertyList
    : '(' property (',' property)* ')'
    ;

property
    : key=propertyKey EQ? value=STRING
    ;

propertyKey
    : identifier ('.' identifier)*
    | STRING
    ;

password
    : (.)*?
    ;

identifier
    : IDENTIFIER
    | BACKQUOTED_IDENTIFIER
    | nonReserved
    ;


resource
    : identifier STRING
    ;

nonReserved
    : ACCOUNT | ADD | ALL | ALTER | ARRAY | AT | MAP | STRUCT | AS
    | BY | CACHE | CALL | CASCADE | COLUMN | COLUMNS | COMMENT
    | CHANGE | CREATE | CURRENT_USER | DATABASE | DATABASES
    | DATASOURCE | DATASOURCES | DDL | DEFINER | DELETE | DESC | DESCRIBE | DISABLE | DO | DCL | DROP
    | ENABLE | EQ | NEQ | EVENT | EVENTS | EXISTS | EXPLAIN | EXTENDED
    | FOR | FROM | FUNCTION | FUNCTIONS | GLOBAL | GRANT | GRANTS | GROUP | GROUPS | HQL
    | IDENTIFIED | IF | IN | INSERT | INTO | LIKE | JOBS | MOUNT | MQL | NOT | ON | OPTION
    | OPTIONS | OR | ORG | ORGANIZATION | OVERWRITE | PLAN | PARTITION
    | PROC | PROCS | PROCEDURE | PROCEDURES | REMOVE | RENAME | REFRESH | REPLACE | REVOKE
    | RUNNING | SA | SCHEMA | SCHEDULE | SELECT | SESSION | SET | SHOW | STAR | STREAM
    | SYSINFO | TABLE | TABLES | TEMP | TEMPORARY | TO | TYPE | TRUNCATE | UNMOUNT
    | UPDATE | USE | USING | USER | USERS | VARIABLES | VIEW | VIEWS | WITH
    ;

ACCOUNT: 'ACCOUNT';
ADD: 'ADD';
ALL: 'ALL';
ALTER: 'ALTER';
ANALYZE: 'ANALYZE';
ARRAY: 'ARRAY';
AT: 'AT';
MAP: 'MAP';
STRUCT: 'STRUCT';
AS: 'AS';
BY: 'BY';
CACHE: 'CACHE';
CALL: 'CALL';
CASCADE: 'CASCADE';
COLUMN: 'COLUMN';
COLUMNS: 'COLUMNS';
COMMENT: 'COMMENT';
CHANGE: 'CHANGE';
CREATE: 'CREATE';
COALESCE: 'COALESCE';
CURRENT_USER: 'CURRENT_USER';
DATABASE: 'DATABASE';
DATABASES: 'DATABASES';
DATASOURCE: 'DATASOURCE';
DATASOURCES: 'DATASOURCES';
DDL: 'DDL';
DEFINER: 'DEFINER';
DELETE: 'DELETE';
DESC: 'DESC';
DESCRIBE: 'DESCRIBE';
DISABLE: 'DISABLE';
DO: 'DO';
DCL: 'DCL';
DROP: 'DROP';
ENABLE: 'ENABLE';
EQ: '=' | '==';
NEQ: '<>';
EVENT: 'EVENT';
EVENTS: 'EVENTS';
EXISTS: 'EXISTS';
EXPLAIN: 'EXPLAIN';
EXTENDED: 'EXTENDED';
FOR: 'FOR';
FROM: 'FROM';
FUNCTION: 'FUNCTION';
FUNCTIONS: 'FUNCTIONS';
GLOBAL: 'GLOBAL';
GRANT: 'GRANT';
GRANTS: 'GRANTS';
GROUP: 'GROUP';
GROUPS: 'GROUPS';
HQL: 'HQL';
IDENTIFIED : 'IDENTIFIED ';
IF: 'IF';
IN: 'IN';
INSERT: 'INSERT';
INTO: 'INTO';
LIKE: 'LIKE';
MERGE: 'MERGE';
JOBS: 'JOBS';
MOUNT: 'MOUNT';
MQL: 'MQL';
NOT: 'NOT';
ON: 'ON';
OPTION: 'OPTION';
OPTIONS: 'OPTIONS';
OR: 'OR';
ORG: 'ORG';
ORGS: 'ORGS';
ORGANIZATION: 'ORGANIZATION';
ORGANIZATIONS: 'ORGANIZATIONS';
OVERWRITE: 'OVERWRITE';
PLAN: 'PLAN';
PARTITION: 'PARTITION';
PROC: 'PROC';
PROCS: 'PROCS';
PROCEDURE: 'PROCEDURE';
PROCEDURES: 'PROCEDURES';
REMOVE: 'REMOVE';
RENAME: 'RENAME';
REFRESH: 'REFRESH';
REPLACE: 'REPLACE';
REVOKE: 'REVOKE';
RUNNING: 'RUNNING';
SA: 'SA';
SAS: 'SAS';
SCHEMA: 'SCHEMA';
SCHEDULE: 'SCHEDULE';
SELECT: 'SELECT';
SESSION: 'SESSION';
SET: 'SET';
SHOW: 'SHOW';
STAR: '*';
STREAM: 'STREAM';
SYSINFO: 'SYSINFO';
TABLE: 'TABLE';
TABLES: 'TABLES';
TEMP: 'TEMP';
TEMPORARY: 'TEMPORARY';
TO: 'TO';
TYPE: 'TYPE';
TRUNCATE: 'TRUNCATE';
UNMOUNT: 'UNMOUNT';
UPDATE: 'UPDATE';
USE: 'USE';
USING: 'USING';
USER: 'USER';
USERS: 'USERS';
VARIABLES: 'VARIABLES';
VIEW: 'VIEW';
VIEWS: 'VIEWS';
WITH: 'WITH';


STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    ;

INTEGER_VALUE
        : DIGIT+
        ;

IDENTIFIER
    : (LETTER | DIGIT | '_' )+
    | '\'' (LETTER | DIGIT | '_' )+ '\''
    | '"' (LETTER | DIGIT | '_' )+ '"'
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [A-Z]
    | [a-z]
    ;

SIMPLE_COMMENT
    : '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

WS
    : [ \b\r\n\t]+ -> channel(HIDDEN)
    ;

UNRECOGNIZED
    : .
    ;