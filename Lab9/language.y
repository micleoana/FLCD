%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define YYDEBUG 1
%}

%token INT
%token STRING
%token VERIFY
%token OTHERWISE
%token FOR
%token WHILE
%token READ
%token PRINT

%token PLUS
%token MINUS
%token MUL
%token DIV
%token MOD
%token L
%token G
%token LE
%token GE
%token EQ
%token NEQ
%token A
%token AND
%token OR



%token ROBRACKET
%token RCBRACKET
%token OBRACKET
%token CBRACKET
%token COBRACKET
%token CCBRACKET
%token COMMA
%token SEMICOLON
%token QUOTE

%token IDENTIFIER
%token INTCONST
%token STRCONST

%start program

%%
program : stmt_list
stmt_list : stmt | stmt stmt_list
stmt : var_decl_stmt|assignment_stmt|io_stmt|if_stmt|while_stmt|for_stmt
var_decl_stmt : type identifier_list SEMICOLON
identifier_list : IDENTIFIER|IDENTIFIER COMMA identifier_list
type : simple_type | array
simple_type : INT | STRING
array : simple_type OBRACKET INTCONST CBRACKET
assignment_stmt : IDENTIFIER A expression
expression: expression PLUS term | expression MINUS term | term
term : term MUL factor | term DIV factor | term MOD factor | factor
factor : ROBRACKET expression RCBRACKET | const | IDENTIFIER
const: INTCONST | STRCONST
io_stmt : READ ROBRACKET IDENTIFIER RCBRACKET SEMICOLON | PRINT ROBRACKET expression RCBRACKET SEMICOLON
if_stmt : VERIFY ROBRACKET condition_list RCBRACKET COBRACKET stmt_list CCBRACKET | VERIFY ROBRACKET condition_list RCBRACKET COBRACKET stmt_list CCBRACKET OTHERWISE COBRACKET stmt_list CCBRACKET
while_stmt : WHILE ROBRACKET condition_list RCBRACKET COBRACKET stmt_list CCBRACKET
for_stmt : FOR ROBRACKET assignment_stmt SEMICOLON condition_list SEMICOLON assignment_stmt RCBRACKET COBRACKET stmt_list CCBRACKET
condition_list : condition | condition logical_op condition_list
condition : expression relation expression
relation : L | G | LE | GE | EQ | NEQ
logical_op : AND | OR
%%
int yyerror(char *s)
{
	printf("%s\n",s);
}

extern FILE *yyin;

int main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	if(argc>2 && !strcmp(argv[2],"-d")) yydebug: 1;
	if(!yyparse()) fprintf(stderr, "\tSyntactically correct.\n");
}
