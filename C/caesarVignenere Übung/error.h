#ifndef __ERROR_H__
#define __ERROR_H__
/**
 * @file error.h
 *
 * Defintion der Fehlercodes / Fehlerfaelle.
 *
 * Diese Datei darf nicht veraendert werden. 
 * 
 * @author kar, mhe
 */

#include <stdio.h>

/**
 * Aufzaehlung aller moeglichen Fehlerfaelle.
 */
enum Errorcode {

  /** Alles gut - kein Fehler */
  ERR_NULL = 0,

  /** Programmaufruf ohne Argumente */
  ERR_CALL_MISSING_ARGS,

  /** Unbekanntes Kommandozeilen-Argument */
  ERR_UNKNOWN_OPERATION,

  /** Kein Text nach -t angegeben */
  ERR_TEXT_MISSING_TEXT,
  /** Unerlaubtes Zeichen bei der Eingabe des Textes */
  ERR_TEXT_ILLEGAL_CHAR,
  /** Eingegebener Text ist zu lang */
  ERR_TEXT_TOO_LONG,

   /** Verschluesselung ohne vorherige Cipherauswahl */
  ERR_ENCODE_WITHOUT_CIPHER,
   /** Entschluesselung ohne vorherige Cipherauswahl */
  ERR_DECODE_WITHOUT_CIPHER,

  /** Fehlende Rotation bei der Caesar-Verschluesselung */
  ERR_CAESAR_MISSING_ROTATION,
  /** Angabe einer ungueltigen Rotation bei der Caesar-Verschluesselung */
  ERR_CAESAR_INVALID_ROTATION,
   
  /** Fehlender Schluessel bei der Vigenere-Verschluesselung */
  ERR_VIGENERE_MISSING_KEY,
  /** Schluessel bei der Vigenere-Verschluesselung ist leer */
  ERR_VIGENERE_EMPTY_KEY,
  /** Schluessel bei der Vigenere-Verschluesselung zu lang */
  ERR_VIGENERE_KEY_TOO_LONG,
  /** Schluessel bei der Vigenere-Verschluesselung enthaelt ungueltige Zeichen */
  ERR_VIGENERE_KEY_ILLEGAL_CHAR,
 
  /** Unbekannter Fehler */
  ERR_UNKNOWN
};
/** Typdefinition. */
typedef enum Errorcode Errorcode;


/**
 * Schreibt die entsprechende Fehlermeldung in der uebergebenen Ausgabestrom.
 *
 * @param stream Ausgabestrom.
 * @param err Fehlercode
 */
void printError(FILE * stream, Errorcode err);

#endif
