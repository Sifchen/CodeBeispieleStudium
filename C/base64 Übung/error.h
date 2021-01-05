#ifndef __ERROR_H__
#define __ERROR_H__
/**
 * @file error.h
 *
 * Definition der Fehlercodes / Fehlerfaelle.
 *
 * Diese Datei darf nicht veraendert werden. 
 * 
 * @author kar, mhe
 */

#include <stdio.h>

/**
 * Aufzaehlung aller moeglichen Fehlerfaelle.
 */
typedef enum {
	/** Alles gut - kein Fehler */
	ERR_NULL                      = 0x0000,
  /** Ungueltiger Programmaufruf */
  ERR_INVALID_CALL              = 0x0001,
  /** INPUTFILE ist gleich OUTPUTFILE */
  ERR_ARG_INPUT_OUTPUT          = 0x0002,
  /** Fehler beim Oeffnen der Quelldatei */
	ERR_SOURCE_FILE_ERROR         = 0x0004,
  /** Fehler beim Oeffnen der Zieldatei */
	ERR_DESTINATION_FILE_ERROR    = 0x0008,
	/** Quelldatei nicht lesbar */
	ERR_SOURCE_NOT_READABLE       = 0x0010,
	/** Zieldatei nicht beschreibbar */
	ERR_DESTINATION_NOT_WRITEABLE = 0x0020,
	/** Ungueltiges Zeichen waehrend decode */
	ERR_B64_INVALID_CHARACTER     = 0x0040,
	/** Fehlendes Zeichen waehrend decode */
	ERR_B64_INCOMPLETE_DATA       = 0x0080,
  /** Fehler waehrend encode */
	ERR_B64_ENCODE                = 0x0100,
	/** Fehler waehrend decode */
	ERR_B64_DECODE                = 0x0200,
  /** Unbekannter Fehler */
	ERR_UNKNOWN_ERROR             = 0xFFFF
} Errorcode;

/**
 * Datentyp fuer Fehlermengen, ein BitSet aus Errorcodes.
 */
typedef int ErrorSet;

/**
 * Schreibt die entsprechende Fehlermeldungen in den uebergebenen Ausgabestrom.
 * 
 * @param stream Zu nutzender Ausgabestrom
 * @param error Fehlermenge mit Fehlercodes
 * @pre stream != NULL
 */
void printError(FILE * stream, ErrorSet error);

#endif
 