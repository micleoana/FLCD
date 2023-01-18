%{
#include <stdio.h>
#include <string.h>
#include "language.tab.h"
int line_nr = 0;
%}

%option noyywrap
%option caseless

INTCONST		[+-]?[1-9][0-9]*|0
STRINGCONST 	\"[a-zA-Z0-9 ]*\"
IDENTIFIER		[a-zA-Z][a-zA-Z0-9]*

%%

"integer" {printf( "Reserved keyword: %s\n", yytext ); return INT;}
"string" {printf( "Reserved keyword: %s\n", yytext ); return STRING;}
"verify" {printf( "Reserved keyword: %s\n", yytext ); return VERIFY;}
"otherwise" {printf( "Reserved keyword: %s\n", yytext ); return OTHERWISE;}
"loopFor" {printf( "Reserved keyword: %s\n", yytext ); return FOR;}
"loopWhile" {printf( "Reserved keyword: %s\n", yytext ); return WHILE;}
"read" {printf( "Reserved keyword: %s\n", yytext ); return READ;}
"print" {printf( "Reserved keyword: %s\n", yytext ); return PRINT;}

"+" {printf( "Operator: %s\n", yytext ); return PLUS;}
"-" {printf( "Operator: %s\n", yytext ); return MINUS;}
"*" {printf( "Operator: %s\n", yytext ); return MUL;}
"/" {printf( "Operator: %s\n", yytext ); return DIV;}
"%" {printf( "Operator: %s\n", yytext ); return MOD;}
"<" {printf( "Operator: %s\n", yytext ); return L;}
">" {printf( "Operator: %s\n", yytext ); return G;}
"<=" {printf( "Operator: %s\n", yytext ); return LE;}
">=" {printf( "Operator: %s\n", yytext ); return GE;}
"==" {printf( "Operator: %s\n", yytext ); return EQ;}
"=" {printf( "Operator: %s\n", yytext ); return A;}
"!=" {printf( "Operator: %s\n", yytext ); return NEQ;}
"AND" {printf( "Operator: %s\n", yytext ); return AND;}
"OR" {printf( "Operator: %s\n", yytext ); return OR;}

"(" {printf( "Separator: %s\n", yytext ); return ROBRACKET;}
")" {printf( "Separator: %s\n", yytext ); return RCBRACKET;}
"[" {printf( "Separator: %s\n", yytext ); return OBRACKET;}
"]" {printf( "Separator: %s\n", yytext ); return CBRACKET;}
"{" {printf( "Separator: %s\n", yytext ); return COBRACKET;}
"}" {printf( "Separator: %s\n", yytext ); return CCBRACKET;}
"," {printf( "Separator: %s\n", yytext ); return COMMA;}
";" {printf( "Separator: %s\n", yytext ); return SEMICOLON;}
"\"" {printf( "Separator: %s\n", yytext ); return QUOTE;}


{IDENTIFIER}	{printf( "Identifier: %s\n", yytext); return IDENTIFIER;}
{INTCONST}	{printf( "Integer constant: %s\n", yytext ); return INTCONST;}
{STRINGCONST}	{printf( "String constant: %s\n", yytext ); return STRCONST;}

[ \t]+

[\n]+ {line_nr++;}

[0-9]{IDENTIFIER} {printf("Error: %s - %d\n", yytext, line_nr); exit(1);}

. printf("Error: %s - %d\n", yytext, line_nr);
%%