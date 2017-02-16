/*
 * Copyright © 2017 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

grammar Directives;

directives
  : directive (SEMI_COLON directive )* EOF
  ;

directive
  : dropColumn
  | renameColumn
  | flattenColumn
  | parseAsCSV
  ;

// flatten(column, ...)
flattenColumn
  : 'flatten' LPAREN COLUMN_NAME (',' COLUMN_NAME)* RPAREN
  ;

// parse-as-csv(column, ",", true)
parseAsCSV
  : 'parse-as-csv' LPAREN COLUMN_NAME ',' QUOTED_STRING ',' BOOLEAN RPAREN
  ;

// parse-as-hl7(column, depth)


// drop(column1, column2, ...)
dropColumn
  : 'drop' LPAREN COLUMN_NAME (',' COLUMN_NAME)* RPAREN
  ;

// rename(column, column, ...)
renameColumn
  : 'rename' LPAREN COLUMN_NAME (',' COLUMN_NAME)* RPAREN
  ;

LPAREN : '(';
RPAREN : ')';
COMMA : ',';
DOT : '.';
SEMI_COLON : ';';

BOOLEAN
  : 'true'
  | 'false'
  ;

COLUMN_NAME
  : [a-zA-Z_] [a-zA-Z_0-9]*
  ;

IDENTIFIER
  : [a-zA-Z_] [a-zA-Z_0-9]*
  ;

QUOTED_STRING
  : ["] (~["\r\n] | '\\\\' | '\\"')* ["]
  | ['] (~['\r\n] | '\\\\' | '\\\'')* [']
  ;

SPACE
  : [ \t\r\n\u000C] -> skip
  ;

NUMBER
  : INT ('.' DIGIT*)?
  ;

fragment INT
  : [1-9] DIGIT*
  | '0'
  ;

fragment DIGIT
  : [0-9]
  ;

SINGLE_LINE_COMMENT
  : ('--') (.)*? [\n] -> channel(HIDDEN)
  ;

MULTI_LINE_COMMENT
  : ('/*') (.)*? ('*/' | EOF) -> channel(HIDDEN)
  ;