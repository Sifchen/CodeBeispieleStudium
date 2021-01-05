/**
 * @file error.c
 *
 * Ausgabe der entsprechenden Fehlermeldungen.
 *
 * @author kar, mhe, Daniel Pigotow (minf103095), Richard Rustien(minf103705)
 * @date 05.12.2019
 */

#include <assert.h>

#include "error.h"

/**
 * Gibt die uebergebene Zeichenkette aus, wenn das Bit des
 * uebergebenen Errorcodes im uebergebenen ErrorSet gesetzt ist.
 *
 * @param stream Zu nutzender Ausgabestrom
 * @param error BitSet aus Fehlercodes (Fehlermenge)
 * @param flag Errorcode, dessen Bit in error gesetzt sein muss
 * @param msg Auszugebene Zeichenkette
 * @pre stream != NULL
 * @pre msg != NULL
 */
void printIfSet(FILE *stream, ErrorSet error, Errorcode flag, char *msg) {
  assert(stream != NULL);
  assert(msg != NULL);
  {
    int errchecker = flag & error;

    if (errchecker != 0) {
      fprintf(stream, "%s", msg);
    }
  }
}

void printError(FILE *stream, ErrorSet error) {
  assert(stream != NULL);

  fprintf(stream, "ERROR[0x%04X]:\n", error);
  if (error == ERR_UNKNOWN_ERROR) {
    fprintf(stream, "%s", "  An unknown error has occurred.\n");
  } else if (error) {
    printIfSet(stream, error, ERR_B64_ENCODE,
               "  An error occurred while encoding.\n");
    printIfSet(stream, error, ERR_B64_DECODE,
               "  An error occurred while decoding.\n");
    printIfSet(stream, error, ERR_INVALID_CALL, "  Invalid program call.\n");
    printIfSet(stream, error, ERR_ARG_INPUT_OUTPUT,
               "  INPUTFILE must be different from OUTPUTFILE.\n");
    printIfSet(stream, error, ERR_SOURCE_FILE_ERROR,
               "  Could not open source file.\n");
    printIfSet(stream, error, ERR_DESTINATION_FILE_ERROR,
               "  Could not open destination file.\n");
    printIfSet(stream, error, ERR_SOURCE_NOT_READABLE,
               "  Source is not readable.\n");
    printIfSet(stream, error, ERR_DESTINATION_NOT_WRITEABLE,
               "  Destination is not writable.\n");
    printIfSet(stream, error, ERR_B64_INVALID_CHARACTER,
               "  The Base64 data contains an invalid character.\n");
    printIfSet(stream, error, ERR_B64_INCOMPLETE_DATA,
               "  The Base64 data is incomplete.\n");
  }
}
