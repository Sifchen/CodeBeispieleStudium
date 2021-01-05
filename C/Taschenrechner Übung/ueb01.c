/**
 * @file ueb01.c
 * Uebung Systemnahe Programmierung im WS 19/20.
 *
 * Ein einfacher Taschenrechner fuer positive ganze Zahlen.
 *
 * @author Daniel Pigotow (minf103095), Richard Rustien (minf103705)
 *
 */

#include <stdio.h>
/**
 * Gibt die Hilfe auf dem uebergebenen Stream (stdout/stderr) aus.
 *
 * @param[in] stream Ausgabestrom
 */
void
printUsage (FILE * stream) {
	fprintf(stream, "Usage:\n");
	fprintf(stream, "\n");
	fprintf(stream, "  ueb01 -h\n");
	fprintf(stream, "\n");
	fprintf(stream, "    shows this help and exits.\n");
	fprintf(stream, "\n");
	fprintf(stream, "  - or -\n");
	fprintf(stream, "\n");
	fprintf(stream, "  ueb01 NUM1 OP NUM2\n");
	fprintf(stream, "\n");
	fprintf(stream, "    computes a binary integer operation and prints the result. A call consists\n");
	fprintf(stream, "    of a number followed by an arithmetic operation and another number.\n");
	fprintf(stream, "\n");
	fprintf(stream, "    NUM1 and NUM2 have to be positive integer numbers (including 0).\n");
	fprintf(stream, "\n");
	fprintf(stream, "    OP is one of:\n");
	fprintf(stream, "      + adds NUM1 and NUM2\n");
	fprintf(stream, "      - subtracts NUM2 from NUM1\n");
	fprintf(stream, "      * multiplies NUM1 and NUM2\n");
	fprintf(stream, "      / divides NUM1 by NUM2 (integer result)\n");
	fprintf(stream, "      %% divides NUM1 by NUM2 (integer remainder)\n");
}

/*
 * Typedefinition der Fehlercodes
 */
typedef enum Error {
	ERR_NONE         = 0, 
	ERR_NUM_PARAM    = 1, 
	ERR_NEG_NUM      = 2, 
	ERR_OP_UNKNOWN   = 3, 
	ERR_DIV_BY_ZERO  = 4
} Error;

/**
 * Funktion fuer Fehlernachrichten 
 * 
 * @param[in] err Type of error
 */
void error_handling (Error err) {
	switch (err) {
		case ERR_NONE:
			break;
		case ERR_NUM_PARAM:
			fprintf(stderr, "%s", "Error: Wrong number of parameters\n");
			break;
		case ERR_NEG_NUM:
			fprintf(stderr, "%s", "Error: Numbers must be positive integers\n");
			break;
		case ERR_OP_UNKNOWN:
			fprintf(stderr, "%s", "Error: Operator unknown\n");
			break;
		case ERR_DIV_BY_ZERO:
			fprintf(stderr, "%s", "Error: Division by Zero\n");
			break;
	}
}

/**
 * Berechnet einen resultierenden Wert mit Werten n1, n2 und Operator op
 *
 * 
 * @param[in] n1 Erster Wert
 * @param[in] n2 Zweiter Wert
 * @param[in] op Operator
 * 
 * @return Berechneter Wert
 */
int calculate (int n1, int n2, char op){
	int RES = 0;
	switch (op) {
		case '+':
			RES = (n1 + n2);
			break;
		case '-':
			RES = (n1 - n2);
			break;
		case '*':
			RES = (n1 * n2);
			break;
		case '/':
			RES = (n1 / n2);
			break;
		case '%':
			RES = (n1 % n2);
			break;
	}
	return RES;
}


/**
 * Hauptprogramm.
 * Berechnet nach einer Fehlerpruefung den angegebenen Ausdruck und
 * gibt entweder das Ergebnis der Berechnung oder eine entsprechende
 * Fehlermeldung und die Hilfe aus.
 *
 * @param[in] argc Anzahl der Programmargumente.
 * @param[in] argv Array mit den Programmargumenten.
 *
 * @return Fehlercode.
 */
int	
main (int argc, char * argv[]) {
	int 
		NUM1 = 0, 	/*Erster Operand*/
		NUM2 = 0, 	/*Zweiter Operand*/
		RESULT = 0,	/*Berechneter Wert*/
		usagePrinted = 0
		;
	char
		OP = 0,		/* Operation */
		h1 = 0,		/* -h */
		h2 = 0,		/* -h */
		dummy = 0	/* -h */
		;
	Error
		ERRCODE = ERR_NONE		/*Fehlercode*/
		;

	if (argc != 4){		/*argc != 4: Nicht genug/zu viele Parameter oder Ausgabe Hilfe*/
		ERRCODE = ERR_NUM_PARAM;
		if (argc == 2){		/*Pruefung ob Hilfebefehl*/
			if ((sscanf(argv[1], "%c%c%c", &h1, &h2, &dummy) == 2)  && (h1 == '-' && h2 == 'h')){
				printUsage(stdout);	
				usagePrinted = 1;
				ERRCODE = ERR_NONE;
			}
		}
	}
	else{
		if (((sscanf(argv[1], "%i%c", &NUM1, &dummy) == 1) && (sscanf(argv[2], "%c%c", &OP, &dummy) == 1) && (sscanf(argv[3], "%i%c", &NUM2, &dummy) == 1))){
			if (NUM1 < 0 || NUM2 < 0){
				ERRCODE = ERR_NEG_NUM;
			}
			switch (OP){		/*OP Pruefung*/
				case '+':
				case '-':
				case '*':
					break;
				case '/':
				case '%':
					if(NUM2 == 0){
						ERRCODE = ERR_DIV_BY_ZERO;
					}	
					break;
				default:
					ERRCODE = ERR_OP_UNKNOWN;
			}
		} else {
			ERRCODE = ERR_NUM_PARAM;
		}
	}
	if (usagePrinted == 0){
		if (ERRCODE == 0){
			/*Liest von argv, schreibt auf Variable*/	
			RESULT = calculate(NUM1,NUM2,OP);		
			fprintf(stdout, "%i\n", RESULT);
		}
		else{
			error_handling(ERRCODE);
			printUsage(stderr);
		}
	}
	return ERRCODE;
}
