grammar UDF;

@members {
  /**
   * Verify whether current token is a valid decimal token (which contains dot).
   * Returns true if the character that follows the token is not a digit or letter or underscore.
   *
   * For example:
   * For char stream "2.3", "2." is not a valid decimal token, because it is followed by digit '3'.
   * For char stream "2.3_", "2.3" is not a valid decimal token, because it is followed by '_'.
   * For char stream "2.3W", "2.3" is not a valid decimal token, because it is followed by 'W'.
   * For char stream "12.0D 34.E2+0.12 "  12.0D is a valid decimal token because it is folllowed
   * by a space. 34.E2 is a valid decimal token because it is followed by symbol '+'
   * which is not a digit or letter or underscore.
   */
  public boolean isValidDecimal() {
    int nextChar = _input.LA(1);
    if (nextChar >= 'A' && nextChar <= 'Z' || nextChar >= '0' && nextChar <= '9' ||
      nextChar == '_') {
      return false;
    } else {
      return true;
    }
  }
}

tokens {
    DELIMITER
}

udf
    : sql=statement EOF
    ;

statement
    :   prefix FROM table=identifier (whereStatement)?
    ;

prefix
    :   SELECT (identifier | udfunction) (',' (identifier|udfunction))*
    |   SELECT (.)*?
    ;

whereStatement
    :   WHERE booleanExpression
    ;

udfunction
    :   arrayMap
    |   arrayFilter
    |   arrayExists
    ;

arrayMap
    :   ('array_map(' | 'ARRAY_MAP(') ((identifier|udfunction) ',' aAnde=arrowAndExpression) ')'
    ;

arrayFilter
    :   ('array_filter(' | 'ARRAY_FILTER(') ((identifier|udfunction) ',' aAnde=arrowAndExpression) ')'
    ;

arrayExists
    :   ('array_exists(' | 'ARRAY_EXISTS(') ((identifier|udfunction) ',' aAnde=arrowAndExpression) ')'
    ;

arrowAndExpression
    :   (arrowPrefix=variableAndArrow)? expression
    ;

variableAndArrow
    :   variable=IDENTIFIER ARROW
    ;

expression
    :   literal                    #litExpr
    |   booleanExpression          #boolExpr
    |   mapExpression              #mapExpr
    ;

literal
    :   NULL | DELIMITER
    |   number
    |   booleanValue
    |   STRING
    ;
mapExpression
   : term (('+' | '-') term)*
   ;

term
    : term1 (('%' term1))*
    ;

term1
   : atom (('*' | '/') atom)*
   ;

atom
   : IDENTIFIER
   | numberAndString
   | LPAREN mapExpression RPAREN
   ;

numberAndString
    :   number | STRING | NULL
    ;

ideantifierOrLiteral
    :   IDENTIFIER
    |   literal
    ;

booleanExpression
    :   termf ((AND | OR) termf)*
    ;
termf
    :   TRUE | FALSE
    |   IDENTIFIER BinaryComparator numberAndString
    |   IDENTIFIER BinaryComparator IDENTIFIER
    |   arrayExists
    |   LPAREN booleanExpression RPAREN
    ;
//booleanExpression
//    :   simpleBooleanExpression (AND simpleBooleanExpression)* (OR simpleBooleanExpression)*
//    ;
//
//simpleBooleanExpression
//    :   booleanValue
//    |   IDENTIFIER BinaryComparator ideantifierOrLiteral
//    ;
//
booleanValue
    :   TRUE | FALSE
    ;

identifier
    :   IDENTIFIER (. identifier)*
    |   quotedIdentifier
    ;

quotedIdentifier
    :   BACKQUOTED_IDENTIFIER
    ;

number
    :   MINUS? DECIMAL_VALUE
    |   MINUS? INTEGER_VALUE
    |   MINUS? BIGINT_LITERAL
    |   MINUS? SMALLINT_LITERAL
    |   MINUS? TINYINT_LITERAL
    |   MINUS? DOUBLE_LITERAL
    |   MINUS? BIGDECIMAL_LITERAL
    ;

AND: 'AND' | 'and' | '&&' | '&';
OR: 'OR'| 'or' | '||' | '|';
MINUS: '-';
TRUE: 'TRUE' | 'true';
FALSE: 'FALSE' | 'false';
NULL: 'NULL';
ARROW: '=>';
LPAREN: '(';
RPAREN: ')';
SELECT: 'SELECT'|'select';
FROM: 'from'|'FROM';
WHERE: 'where' | 'WHERE';

BinaryArithmeticOperator
    :   '+'
    |   '-'
    |   '*'
    |   '/'
    |   '%'
    ;

BinaryComparator
    :   '>'
    |   '>='
    |   '<'
    |   '<='
    |   '=='
    |   '!='
    |   '='
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

STRING
    :   '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    |   '\"' ( ~('\"'|'\\') | ('\\' .) )* '\"'
    ;

BIGINT_LITERAL
    :   DIGIT+ 'L'
    ;

SMALLINT_LITERAL
    :   DIGIT+ 'S'
    ;

TINYINT_LITERAL
    :   DIGIT+ 'Y'
    ;

BYTELENGTH_LITERAL
    :   DIGIT+ ('B' | 'K' | 'M' | 'G')
    ;

DOUBLE_LITERAL
    :   DIGIT+ EXPONENT? 'D'
    |   DECIMAL_DIGITS EXPONENT? 'D' {isValidDecimal()}?
    ;

BIGDECIMAL_LITERAL
    :   DIGIT+ EXPONENT? 'BD'
    |   DECIMAL_DIGITS EXPONENT? 'BD' {isValidDecimal()}?
    ;

INTEGER_VALUE
    : DIGIT+
    ;

DECIMAL_VALUE
    : DIGIT+ EXPONENT
    | DECIMAL_DIGITS EXPONENT? {isValidDecimal()}?
    ;

IDENTIFIER
    : (LETTER | DIGIT | '_')+
    ;

// ==================================================================
fragment DECIMAL_DIGITS
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

fragment EXPONENT
    : 'E' [+-]? DIGIT+
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

BRACKETED_EMPTY_COMMENT
    : '/**/' -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*' ~[+] .*? '*/' -> channel(HIDDEN)
    ;

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;

// Catch-all for anything we can't recognize.
// We use this to be able to ignore and recover all the text
// when splitting statements with DelimiterLexer
UNRECOGNIZED
    : .
    ;