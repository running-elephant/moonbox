---
layout: global
title: MQL Indexing
---

#### Explanation of Symbols
- [  ]

    There are two or more elements in "[]" which represent the same meaning. You can choose anyone of them.  
- ?

    Content before "?" can be either kept or deleted.
- * 

    Content before "*" can be one or more separated by comma.  
- identifier

    Identifiers start with a Latin letter or underscore, and continue with a Latin letter, underscore or number.   
- STRING

    String enclosed by single quotes or double quotes  
- password

    Character sequences without space, return or line break  
- privilegeList

    One or more of ACCOUNT, DDL and DCL  
- privileges

   `SELECT (‘(‘ identifier* ‘)’)?` or `UPDATE (‘(‘ identifier* ‘)’)?`  
- tableIdentifier

   `(identifier ‘.’)?identifier`, namely databaseName.tableName
- funcIdentifier
 
    `(identifier ‘.’)?identifier`, namely databaseName.functionName  
- query

    SELECT statement   
- cmd

    All the statements  

#### Index of Instructions  
```
CREATE [ORG | ORGANIZATION] (IF NOT EXISTS)? identifier (COMMENT STRING)
RENAME [ORG | ORGANIZATION] identifier TO identifier
ALTER [ORG | ORGANIZATION] identifier RENAME TO identifier
ALTER [ORG | ORGANIZATION] identifier SET COMMENT STRING
DROP [ORG | ORGANIZATION] (IF EXISTS)? identifier (CASCADE)?

CREATE SA (IF NOT EXISTS)? identifier IN [ORG | ORGANIZATION] identifier IDENTIFIED BY password
RENAME SA identifier IN [ORG | ORGANIZATION] identifier newName=identifier
ALTER SA identifier IN [ORG | ORGANIZATION] identifier RENAME TO identifier
ALTER SA identifier IN [ORG | ORGANIZATION] identifier IDENTIFIED BY pwd=password
DROP SA (IF EXISTS)? identifier IN [ORG | ORGANIZATION] identifier

GRANT GRANT OPTION privilegeList TO USER identifier*
REVOKE GRANT OPTION privilegeList FROM USER identifier*

GRANT privilegeList TO USER identifier*
REVOKE privilegeList FROM USER identifier*

GRANT privileges ON tableCollections TO USER identifier*
REVOKE privileges ON tableCollections FROM USER identifier*

CREATE USER (IF NOT EXISTS)? identifier IDENTIFIED BY password
RENAME USER identifier TO identifier
ALTER USER identifier RENAME TO identifier
ALTER USER identifier IDENTIFIED BY password
DROP USER (IF EXISTS)? identifier

MOUNT TABLE (IF NOT EXISTS)? tableIdentifier OPTIONS(key 'value', key 'value')
RENAME TABLE tableIdentifier TO tableIdentifier
ALTER TABLE tableIdentifier RENAME TO tableIdentifier
ALTER TABLE tableIdentifier SET OPTIONS(key 'value', key 'value')
UNMOUNT TABLE (IF EXISTS)? tableIdentifier

MOUNT DATABASE (IF NOT EXISTS)? identifier OPTIONS(key 'value', key 'value')
UNMOUNT DATABASE (IF EXISTS)? identifier
ALTER DATABASE identifier SET OPTIONS(key 'value', key 'value')

CREATE DATABASE (IF NOT EXISTS)? identifier (COMMENT STRING)?
RENAME DATABASE identifier TO identifier
ALTER DATABASE identifier RENAME TO identifier
ALTER DATABASE identifier SET COMMENT STRING
DROP DATABASE (IF EXISTS)? identifier (CASCADE)?

CREATE FUNCTION (IF NOT EXISTS)? funcIdentifier AS
    STRING -- 类名
    (STRING)?  -- 函数名
    (USING resource (',' resource)*) -- 资源类型和资源

DROP FUNCTION (IF EXISTS)? funcIdentifier

CREATE [PROC | PROCEDURE] (IF NOT EXISTS)? identifier USING MQL AS cmd*
RENAME [PROC | PROCEDURE] identifier TO identifier
ALTER [PROC | PROCEDURE] identifier RENAME TO identifier
ALTER [PROC | PROCEDURE] identifier AS cmd*
DROP [PROC | PROCEDURE] (IF EXISTS)? identifier

CREATE (DEFINER identifier)? EVENT (IF NOT EXISTS)? identifier ON SCHEDULE AT
    STRING -- crontab表达式
    (ENABLE | DISABLE)?
    (COMMENT STRING)?
    DO CALL identifier -- procedure名字

RENAME EVENT identifier TO identifier
ALTER DEFINER identifier EVENT identifier
ALTER EVENT identifier RENAME TO identifier
ALTER EVENT identifier ON SCHEDULE AT STRING
ALTER EVENT identifier (ENABLE | DISABLE)
DROP EVENT (IF EXISTS)? identifier


USE identifier -- USE DATABASE
SHOW EVENTS (LIKE STRING)?
SHOW DATABASES (LIKE STRING)?
SHOW TABLES ([FROM | IN] identifier)? (LIKE STRING)?
SHOW VIEWS ([FROM | IN] identifier)? (LIKE STRING)?
SHOW (SYSTEM | USER)? FUNCTIONS ([FROM | IN] identifier)? (LIKE STRING)?
SHOW USERS (LIKE STRING)?
SHOW PROCEDURES (LIKE STRING)?
SHOW VARIABLES (LIKE STRING)?
SHOW GRANTS FOR identifier
SHOW CREATE TABLE FOR identifier
SHOW SCHEMA FORM SELECT ...

[DESC | DESCRIBE] DATABASE identifier
[DESC | DESCRIBE] TABLE? EXTENDED? tableIdentifier
[DESC | DESCRIBE] FUNCTION EXTENDED? funcIdentifier
[DESC | DESCRIBE] EVENT identifier
[DESC | DESCRIBE] USER identifier

EXPLAIN EXTENDED? PLAN? query
SET identifier [= | ==]? .*?

INSERT [INTO | OVERWRITE] TABLE? tableIdentifier AS? query
CREATE (OR REPLACE)? CACHE? [TEMP | TEMPORARY] VIEW identifier AS query
```
