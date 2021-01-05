/**
 * @file base64.c Implementierung der Base64-Bibliothek
 *
 * @author kar, mhe, Daniel Pigotow (minf103095), Richard Rustien(minf103705)
 * @date 05.12.2019
 */

#include <assert.h>

#include "base64.h"

/**
 * Tabelle mit allen Base64-Zeichen
 */
static char base64[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                        'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
                        '3', '4', '5', '6', '7', '8', '9', '+', '/'};
enum Bitmask {
  SIXBIT = 252 /*0b11111100*/,
  FOURBIT = 240 /*0b11110000*/,
  TWOBIT = 192 /*0b11000000*/
};

typedef enum Bitmask Bitmask;
/**
 * Kodiert eine Binaerdatei mit Base64 zu einer ASCII-Textdatei.
 *
 * @param char * source Dateiname der Quelldatei
 * @param char * dest Dateiname der Zieldatei
 * @pre source != NULL
 * @pre dest != NULL
 * @return ErrorSet mit moeglichen Fehlercodes:
 *  - ERR_ENCODE Ein Fehler ist aufgetreten.
 *  - ERR_SOURCE_FILE_ERROR Fehler beim Oeffnen der Quelldatei
 *  - ERR_DESTINATION_FILE_ERROR Fehler beim Oeffnen der Zieldatei
 *  - Alle moeglichen Fehlercodes, die beim Aufruf von base64_encodeStream
 *    entstehen koennen.
 */
ErrorSet base64_encodeFile(char *source, char *dest) {
  ErrorSet error = ERR_NULL;
  assert(source != NULL);
  assert(dest != NULL);
  {
    int closeStat = 0;
    FILE *sourceFile = fopen(source, "rb");
    FILE *destFile = fopen(dest, "wb");

    if (sourceFile == NULL) {
      error |= ERR_SOURCE_FILE_ERROR;
    }
    if (destFile == NULL) {
      error |= ERR_DESTINATION_FILE_ERROR;
    }
    if (!error) {
      error |= base64_encodeStream(sourceFile, destFile);
    } else {
      error |= ERR_B64_ENCODE;
    }
    if (sourceFile != NULL) {
      closeStat = fclose(sourceFile);
    }
    if (closeStat != 0) {
      error |= ERR_SOURCE_FILE_ERROR;
    }
    if (destFile != NULL) {
      closeStat = fclose(destFile);
    }
    if (closeStat != 0) {
      error |= ERR_DESTINATION_FILE_ERROR;
    }
  }
  return error;
}

/**
 * Dekodiert eine mit Base64 kodierte Quelldatei zu einer Binaerdatei.
 *
 * @param char * source Dateiname der Quelldatei
 * @param char * dest Dateiname der Zieldatei
 * @pre source != NULL
 * @pre dest != NULL
 * @return ErrorSet mit moeglichen Fehlercodes:
 *  - ERR_DECODE Ein Fehler ist aufgetreten.
 *  - ERR_SOURCE_FILE_ERROR Fehler beim Oeffnen der Quelldatei
 *  - ERR_DESTINATION_FILE_ERROR Fehler beim Oeffnen der Zieldatei
 *  - Alle moeglichen Fehlercodes, die beim Aufruf von base64_decodeStream
 *    entstehen koennen.
 */
ErrorSet base64_decodeFile(char *source, char *dest) {
  ErrorSet error = ERR_NULL;
  assert(source != NULL);
  assert(dest != NULL);
  {
    int closeStat = 0;
    FILE *sourceFile = fopen(source, "rb");
    FILE *destFile = fopen(dest, "wb");

    if (sourceFile == NULL) {
      error |= ERR_SOURCE_FILE_ERROR;
    }
    if (destFile == NULL) {
      error |= ERR_DESTINATION_FILE_ERROR;
    }
    if (!error) {
      error |= base64_decodeStream(sourceFile, destFile);
    } else {
      error |= ERR_B64_DECODE;
    }
    if (sourceFile != NULL) {
      closeStat = fclose(sourceFile);
    }
    if (closeStat != 0) {
      error |= ERR_SOURCE_FILE_ERROR;
    }
    if (destFile != NULL) {
      closeStat = fclose(destFile);
    }
    if (closeStat != 0) {
      error |= ERR_DESTINATION_FILE_ERROR;
    }
  }
  return error;
}

/**
 * Kodiert einen Stream mit Binaerdaten mit Base64 zu einem ASCII-Text.
 *
 * @param FILE * source Quelldatei
 * @param FILE * dest Zieldatei
 * @pre source != NULL
 * @pre dest != NULL
 * @return ErrorSet mit moeglichen Fehlercodes:
 *  - ERR_ENCODE Ein Fehler ist aufgetreten.
 *  - ERR_SOURCE_NOT_READABLE Quelldatei nicht lesbar
 *  - ERR_DESTINATION_NOT_WRITEABLE Zieldatei nicht beschreibbar
 */
ErrorSet base64_encodeStream(FILE *source, FILE *dest) {
  ErrorSet error = ERR_NULL;
  assert(source != NULL);
  assert(dest != NULL);
  {
    unsigned char buffer[3] = {0};
    unsigned char indexArr[4] = {0};
    Bitmask mask = SIXBIT;
    int i = 0;
    int readtest = 0;
    int writetest = 0;
    while (!error && feof(source) == 0) {
      if (ferror(source) != 0) {
        error |= ERR_SOURCE_NOT_READABLE;
      } else {
        readtest = fscanf(source, "%c%c%c", &buffer[0], &buffer[1], &buffer[2]);
        switch (readtest) {
        case 1: {
          mask = SIXBIT;
          indexArr[0] = buffer[0] & mask;
          indexArr[1] = buffer[0] << 6;
          for (i = 0; !error && i < 2; i++) {
            if (writetest == EOF || ferror(dest) != 0) {
              error |= ERR_DESTINATION_NOT_WRITEABLE;
            }
            indexArr[i] >>= 2;
            writetest = fputc(base64[indexArr[i]], dest);
          }
          if (writetest != EOF)
            writetest = fprintf(dest, "==");
        } break;
        case 2: {
          mask = SIXBIT;
          indexArr[0] = buffer[0] & mask;
          mask = FOURBIT;
          indexArr[1] = buffer[1] & mask;
          indexArr[1] >>= 2;
          indexArr[1] |= buffer[0] << 6;
          indexArr[2] = (buffer[1] << 4);
          for (i = 0; !error && i < 3; i++) {
            if (writetest == EOF || ferror(dest) != 0) {
              error |= ERR_DESTINATION_NOT_WRITEABLE;
            }
            indexArr[i] >>= 2;
            writetest = fputc(base64[indexArr[i]], dest);
          }
          if (writetest != EOF) {
            writetest = fprintf(dest, "=");
          }
        } break;
        case 3: {
          mask = SIXBIT;
          indexArr[0] = buffer[0] & mask;
          mask = FOURBIT;
          indexArr[1] = buffer[1] & mask;
          indexArr[1] >>= 2;
          indexArr[1] |= buffer[0] << 6;
          indexArr[2] = (buffer[1] << 4);
          mask = TWOBIT;
          indexArr[2] |= (buffer[2] & mask) >> 4;
          mask = SIXBIT;
          indexArr[3] = (buffer[2] << 2) & mask;
          for (i = 0; !error && i < 4; i++) {
            if (writetest == EOF || ferror(dest) != 0) {
              error |= ERR_DESTINATION_NOT_WRITEABLE;
            }
            indexArr[i] >>= 2;
            writetest = fputc(base64[indexArr[i]], dest);
          }
        } break;
        }
      }
    }
  }

  if (error) {
    error |= ERR_B64_ENCODE;
  }
  return error;
}

/**
 * Dekodiert einen Stream mit mit Base64 kodierten Daten zu einem
 * Stream mit Binaerdaten.
 *
 * @param FILE * source Quelldatei
 * @param FILE * dest Zieldatei
 * @pre source != NULL
 * @pre dest != NULL
 * @return ErrorSet mit moeglichen Fehlercodes:
 *  - ERR_DECODE Ein Fehler ist aufgetreten.
 *  - ERR_SOURCE_NOT_READABLE Quelldatei nicht lesbar
 *  - ERR_DESTINATION_NOT_WRITEABLE Zieldatei nicht beschreibbar
 *  - ERR_B64_INVALID_CHARACTER Ungueltiges Zeichen in der Quelldatei
 *  - ERR_B64_INCOMPLETE_DATA Fehlendes Zeichen in der Quelldatei
 *                            (Anzahl der Zeichen % 4 != 0)
 */
ErrorSet base64_decodeStream(FILE *source, FILE *dest) {
  ErrorSet error = ERR_NULL;
  assert(source != NULL);
  assert(dest != NULL);
  {
    unsigned char buffer[4] = {0};
    unsigned char bufferIdx[4] = {0};
    unsigned char toWrite[3] = {0};
    Bitmask mask = TWOBIT;
    unsigned char i = 0;
    unsigned char j = 0;
    long int size = 0;
    int count = 0;
    int writetest = 0;

    if (fseek(source, 0, SEEK_END) == 0) {
      size = ftell(source);
    } else {
      error |= ERR_SOURCE_NOT_READABLE;
    }
    if (fseek(source, 0, SEEK_SET) != 0) {
      error |= ERR_SOURCE_NOT_READABLE;
    }
    if (size % 4 != 0) {
      error |= ERR_B64_INCOMPLETE_DATA;
    }
    while (!error && feof(source) == 0) {
      if (ferror(source) != 0) {
        error |= ERR_SOURCE_NOT_READABLE;
      } else {
        if (fscanf(source, "%c%c%c%c", &buffer[0], &buffer[1], &buffer[2],
                   &buffer[3]) == 4) {
          for (i = 0; !error && i < 4; i++) {
            if (buffer[i] == '=') {
              count++;
            }
            for (j = 0; !error && j < 64; j++) {
              if (buffer[i] == base64[j]) {
                bufferIdx[i] = j;
                count++;
              }
            }
          }
          if (count == 4) {
            count = 0;
            mask = TWOBIT;
            toWrite[0] = bufferIdx[0] << 2;
            toWrite[0] |= ((bufferIdx[1] << 2) & mask) >> 6;
            mask = FOURBIT;
            if (buffer[2] != '=') {
              toWrite[1] = bufferIdx[1] << 4;
              toWrite[1] |= ((bufferIdx[2] << 2) & mask) >> 4;
            } else {
              toWrite[1] = 0;
            }
            if (buffer[3] != '=') {
              toWrite[2] = bufferIdx[3] << 2;
              toWrite[2] >>= 2;
              toWrite[2] |= bufferIdx[2] << 6;
            } else {
              toWrite[2] = 0;
            }
            for (i = 0; !error && i < 3; i++) {
              if (writetest == EOF || ferror(dest) != 0) {
                error |= ERR_DESTINATION_NOT_WRITEABLE;
              }
              if (toWrite[i] != 0) {
                writetest = fputc(toWrite[i], dest);
              }
            }
          } else {
            error |= ERR_B64_INVALID_CHARACTER;
          }
        }
      }
    }
  }
  if (error) {
    error |= ERR_B64_DECODE;
  }
  return error;
}
