%{
#include <stdio.h>
#include <string.h>
int line_nr = 0;

struct Pair {
    char token[100];
    int ST_pos;
} entry;

char ST[200][200];
int idx_ST = 0;
int idx_PIF = 0;
struct Pair PIF[200] ;

int add_st(char* text) {
	int idx = -1;
    for (int i = 0; i < idx_ST; i++) {
        if (strcmp(ST[i], text) == 0) {
            idx = i;
        }
    }
	if(idx == -1){
    		strcpy(ST[idx_ST], text);
    		idx = idx_ST;
    		idx_ST++;
	}
	return idx;
}

void add_pif(char* text) {
    int idx = -1;
        for (int i = 0; i < idx_ST; i++) {
            if (strcmp(ST[i], text) == 0) {
                idx = i;
            }
        }
    strcpy(entry.token, text);
    entry.ST_pos = idx;
    PIF[idx_PIF++] = entry;
}

%}

%option noyywrap
%option caseless

INTCONST		[+-]?[1-9][0-9]*|0
STRINGCONST 	\"[a-zA-Z0-9 ]*\"
IDENTIFIER		[a-zA-Z][a-zA-Z0-9]*

%%

"integer"|"string"|"verify"|"otherwise"|"loopFor"|"loopWhile"|"read"|"print" {printf( "Reserved keyword: %s\n", yytext ); add_pif(yytext);}
"+"|"-"|"*"|"/"|"%"|"<"|">"|"<="|">="|"=="|"="|"!="|"AND"|"OR" {printf( "Operator: %s\n", yytext ); add_pif(yytext);}
"("|")"|"["|"]"|"{"|"}"|","|";"|"\"" {printf( "Separator: %s\n", yytext ); add_pif(yytext);}


{IDENTIFIER}	{printf( "Identifier: %s\n", yytext); add_st(yytext); add_pif(yytext);}
{INTCONST}	{printf( "Integer constant: %s\n", yytext ); add_pif(yytext);}
{STRINGCONST}	{printf( "String constant: %s\n", yytext ); add_pif(yytext);}

[ \t]+

[\n]+ {line_nr++;}

[0-9]{IDENTIFIER} {printf("Error: %s - %d\n", yytext, line_nr); exit(1);}

. printf("Error: %s - %d\n", yytext, line_nr);
%%

int main( argc, argv ) 
int argc; 
char **argv; 
{ 
    ++argv, --argc; /* skip over program name */ 
    if ( argc > 0 ) 
    yyin = fopen( argv[0], "r" ); 
    else 
     yyin = stdin; 
    yylex();

     printf("\nPIF: \n");
    for (int i = 0; i < idx_PIF; i++) {
        printf("\t%s\t%d\n", PIF[i].token, PIF[i].ST_pos);
    }

	 printf("\nST: \n");
    for (int i = 0; i < idx_ST; ++i) {
        printf("\t%d\t%s\n", i, ST[i]);
    }
} 