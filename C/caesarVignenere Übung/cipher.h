/**
 * @file cipher.h
 * 
 * Schnittstellendefinition des Moduls mit
 * Verschluesselungsfunktionen.
 *
 * Diese Datei darf nicht veraendert werden. 
 * 
 * @author kar, mhe 
 *
 */

#include <stdio.h>
#include "error.h"

#ifndef __CIPHER_H__
#define __CIPHER_H__

/** Sofern nicht vom Compiler festgelegt, definiere eine maximale Laenge. */
#ifndef MAX_TEXT_LENGTH
#define MAX_TEXT_LENGTH 30
#endif

/** Sofern nicht vom Compiler festgelegt, definiere das kleinste Zeichen. */
#ifndef SMALLEST_CHAR
#define SMALLEST_CHAR ' '
#endif

/** Sofern nicht vom Compiler festgelegt, definiere das groesste Zeichen. */
#ifndef BIGGEST_CHAR
#define BIGGEST_CHAR '~'
#endif



/** Typendefinition fuer einen Text */
typedef char Text[MAX_TEXT_LENGTH+1]; /*inkl. Nulltermination*/


/**
 * Erzeugt in text einen leeren String.
 *  
 * @param[out] text
 */
void initText(Text text);

/** 
 * Verschluesselt einen Text mit Caesar-Chiffre, dabei wird der uebergebene
 * Text veraendert.
 * 
 * @param[in]  plain Klartext
 * @param[out] plain Kryptotext
 * @param      rot   Rotation
 *
 */ 
void caesarEncode(Text plain, unsigned int rot);

/** 
 * Verschluesselt einen Text mit Vigenere-Chiffre, dabei wird der uebergebene
 * Text veraendert.
 * 
 * @param[in]  plain Klartext
 * @param[out] plain Kryptotext
 * @param[in]  pass  Passwort
 *
 * @pre key ist nicht leer 
 *
 */
void vigenereEncode(Text plain, Text key);

/** 
 * Entschluesselt einen Text mit Caesar-Chiffre, dabei wird der uebergebene
 * Text veraendert.
 * 
 * @param[in]  plain Kryptotext
 * @param[out] plain Klartext
 * @param      rot Rotation
 *
 */ 
void caesarDecode(Text plain, unsigned int rot);

/** 
 * Entschluesselt einen Text mit Vigenere-Chiffre, dabei wird der uebergebene
 * Text veraendert.
 * 
 * @param[in]  plain Kryptotext
 * @param[out] plain Klartext
 * @param      pass  Passwort
 *
 * @pre key ist nicht leer
 *
 */
void vigenereDecode(Text plain, Text key);

/** 
 * Liest einen Text ein.
 * 
 * @param[out] text eingelesener Text
 * @param[in]  str  Eingabestring
 *
 * @return Fehlercode: 
 *         ERR_TEXT_ILLEGAL_CHAR wenn der einzulesende Text ungueltige Zeichen enthaelt
 *         ERR_TEXT_TOO_LONG wenn der Text laenger ist als der verfuegbare Platz
 */
Errorcode readText(Text text, char * str); 


/**
 * Schreibt den uebergebenen Text auf den uebergebenen Ausgabestrom.
 *
 * @param[in] stream Ausgabestrom
 * @param[in] text   Text
 * 
 */
void printText(FILE * stream, Text text);

/**
 * Gibt das Histogram des uebergebenen Texts auf dem uebergebenen Ausgabestrom aus.
 *
 * Die Ausgabe erfolgt spaltenweise in zwei Zeilen. Eine Spalte beinhaltet 
 * stets das im Text vorhandene Zeichen (Auftrittshaeufigkeit > 0) und die 
 * Anzahl seiner Vorkommen. Die Inhalte der Spalten sind voneinander durch 
 * Leerzeichen getrennt und rechtsbuendig ausgerichtet. Die Breite der Anzahl 
 * der Vorkommen bestimmt die Breite der Spalte. Die Zeichen werden gemaess
 * ihrer Reihenfolge im Zeichenvorrat ausgegeben.
 *
 * @param[in] stream Ausgabestrom
 * @param[in] text   Text
 * 
 */
void printHistogram (FILE * stream, Text text);

#endif
