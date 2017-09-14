grammar CMD;

tokens {
    DELIMITER
}

singleCmd
    : cmd EOF
    ;
cmd
    : MOUNT DATASOURCE identifier OPTIONS tablePropertyList                                          #mountDataSource
    | UNMOUNT DATASOURCE identifier									                                 #unmountDataSource
    | MOUNT DATABASE identifier OPTIONS tablePropertyList                                            #mountDatabase
    | UNMOUNT DATABASE identifier									                                 #unmountDatabase
    | MOUNT TABLE tableIdentifier OPTIONS tablePropertyList                                          #mountTable
    | UNMOUNT TABLE tableIdentifier									                                 #unmountTable
    | CREATE TABLE (IF NOT EXISTS)? tableIdentifier (OPTIONS tablePropertyList)?                     #createTableOnly
    | CREATE TABLE (IF NOT EXISTS)? tableIdentifier (OPTIONS tablePropertyList)?  AS query           #createTableAsSelect
    | CREATE VIEW (IF NOT EXISTS)? tableIdentifier AS query                                          #createViewAsSelect
    | INSERT INTO tableIdentifier (OPTIONS tablePropertyList)? AS query                              #insertAsSelect
    | DROP TABLE tableIdentifier																	 #dropTable
    | TRUNCATE TABLE tableIdentifier                                                                 #truncateTable
    | DESCRIBE DATABASE identifier																	 #describeDatabase
    | DESCRIBE TABLE tableIdentifier                                                                 #describeTable
    | query                                                                                          #cmdDefault
    ;

query
    : SELECT (.)*?
    | WITH (.)*?
    ;

tableIdentifier
    : (db=identifier '.')? table=identifier
    ;

identifier
    : IDENTIFIER
    ;

tablePropertyList
    : '(' tableProperty (',' tableProperty)* ')'
    ;

tableProperty
    : key=tablePropertyKey (EQ? value=STRING)?
    ;

tablePropertyKey
    : identifier ('.' identifier)*
    | STRING
    ;

SELECT: 'SELECT';
WITH: 'WITH';
CREATE: 'CREATE';
TABLE: 'TABLE';
VIEW: 'VIEW';
IF: 'IF';
NOT: 'NOT';
EXISTS: 'EXISTS';
AS: 'AS';
MOUNT: 'MOUNT';
UNMOUNT: 'UNMOUNT';
DATASOURCE: 'DATASOURCE';
DATABASE: 'DATABASE';
OPTIONS: 'OPTIONS';
INSERT: 'INSERT';
INTO: 'INTO';
DROP: 'DROP';
TRUNCATE: 'TRUNCATE';
DESCRIBE: 'DESCRIBE';
EQ: '='| '==';
STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '\"' ( ~('\"'|'\\') | ('\\' .) )* '\"'
    ;

IDENTIFIER
    : (LETTER | DIGIT | '_' )+
    | '\'' (LETTER | DIGIT | '_' )+ '\''
    | '"' (LETTER | DIGIT | '_' )+ '"'
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
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;

UNRECOGNIZED
    : .
    ;
