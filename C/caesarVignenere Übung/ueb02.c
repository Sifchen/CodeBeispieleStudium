/**
 * @file ueb02.c
 *
 * Hauptprogramm.
 *
 * @author kar, mhe, Daniel Pigotow (minf103095), Richard Rustien(minf103705)
 * @date 21.08.2019
 */

#include "cipher.h"
#include "error.h"
#include <stdio.h>

int isNotPositive(char *argument, int *rot, char *dummy) {

  if (sscanf(argument, "%i%c", rot, dummy) == 2 || *rot < 0) {
    return 0;
  } else {
    return 1;
  }
}

/**
 * Gibt die Hilfe auf dem uebergebenen Stream (stdout/stderr) aus.
 *
 * @param[in] stream Ausgabestrom
 */
void printUsage(FILE *stream) {

  fprintf(stream, "Usage:\n");
  fprintf(stream, "ueb02 -h\n\n");
  fprintf(stream, "  shows this help and exits.\n\n");
  fprintf(stream, "- or -\n\n");
  fprintf(stream, "ueb02 [OPERATION [ARGUMENT], ...]\n\n");
  fprintf(stream, "  Executes a number of (simple cryptographic) operations. "
                  "The maximum text\n");
  fprintf(stream,
          "  size for plain / cipher text and key is %i. The allowed input "
          "characters\n",
          MAX_TEXT_LENGTH);
  fprintf(stream, "  range from \"%c\" to \"%c\".\n\n", SMALLEST_CHAR,
          BIGGEST_CHAR);
  fprintf(stream, "  OPERATION:\n");
  fprintf(stream, "    -t TEXT:  Sets the TEXT as new (plain/cipher) text.\n");
  fprintf(stream, "    -C ROT:   Sets the cipher algorithm to caesar.\n");
  fprintf(stream, "              ROT is the rotation (a positive integer).\n");
  fprintf(stream, "    -V KEY:   Sets the cipher algorithm to vigenere.\n");
  fprintf(stream, "              KEY is the nonempty passkey.\n");
  fprintf(stream, "    -e:       Encodes and replaces the current text using "
                  "the current\n");
  fprintf(stream, "              cipher algorithm.\n");
  fprintf(stream, "    -d:       Decodes and replaces the current text using "
                  "the current\n");
  fprintf(stream, "              cipher algorithm.\n");
  fprintf(stream, "    -i:       Writes the histogram corresponding to the "
                  "current text to\n");
  fprintf(stream, "              stdout, followed by a line break.\n");
  fprintf(stream, "    -w:       Writes the current text to stdout, followed "
                  "by a line break.\n");
}

/**
 * Gibt die Hilfe und die Fehlermeldung auf dem uebergebenen Stream
 * (stdout/stderr) aus.
 *
 * @param[in] stream Ausgabestrom
 */
void printErrorAndUsage(FILE *stream, Errorcode error) {
  printError(stream, error);
  printUsage(stream);
}

/*Cypher aufzaehlung, zum setzen des Verschluesselungsverfahrens */
enum Cypher { C_NONE = 0, C_CAESAR = 1, C_VIG };

/*Typdefinition*/
typedef enum Cypher Cypher;

int main(int argc, char *argv[]) {

  int argument = 0, /*Zaehlt durch argv*/
      rot = -1      /*Caesar Rotation*/
      ;

  Cypher c = C_NONE;

  char dash = '\0', letter = '\0', dummy = '\0';

  Text key, /*Vig. Key*/
      text  /*User Input*/
      ;

  Errorcode error = ERR_NULL;

  initText(text);
  initText(key);

  if (argc < 2) {
    error = ERR_CALL_MISSING_ARGS;
  } else {
    if (sscanf(argv[1], "%c%c%c", &dash, &letter, &dummy) == 2 && dash == '-' &&
        letter == 'h') {
      printUsage(stdout);
    } else {
      for (argument = 1; argument < argc && error == ERR_NULL; argument++) {
        if (sscanf(argv[argument], "%c%c%c", &dash, &letter, &dummy) == 2 &&
            dummy == '\0' && dash == '-') {
          switch (letter) {
          case 't':
            if (argument + 1 < argc) {
              /*Pruefen ob Text gesetzt wurde*/
              error = readText(text, argv[++argument]);
            } else {
              error = ERR_TEXT_MISSING_TEXT;
            }
            break;
          case 'C':
            c = C_CAESAR;
            if ((argument + 1 < argc)) {
              /*Pruefen ob Rotation eingegeben wurde*/
              if (isNotPositive(argv[++argument], &rot, &dummy) == 0) {
                /*Hier kann als rot auch eine Zeichenfolge eingelesen werden
                 * ohne einen Fehler zu werfen*/
                error = ERR_CAESAR_INVALID_ROTATION;
              }
            } else {
              error = ERR_CAESAR_MISSING_ROTATION;
            }
            break;
          case 'V':
            c = C_VIG;
            if (argument + 1 < argc) {
              /*Pruefen ob ein key eingegeben wurde*/
              error = readText(key, argv[++argument]);
              if (error == ERR_TEXT_ILLEGAL_CHAR) {
                error = ERR_VIGENERE_KEY_ILLEGAL_CHAR;
              } else {
                if (key[0] == '\0') {
                  error = ERR_VIGENERE_EMPTY_KEY;
                }
              }
            } else {
              error = ERR_VIGENERE_MISSING_KEY;
            }
            break;
          case 'e':
            if (c == C_NONE) {
              error = ERR_ENCODE_WITHOUT_CIPHER;
            } else {
              if (c == C_VIG) {
                vigenereEncode(text, key);
              } else {
                caesarEncode(text, rot);
              }
            }
            break;
          case 'd':
            if (c == C_NONE) {
              error = ERR_DECODE_WITHOUT_CIPHER;
            } else {
              if (c == C_VIG) {
                vigenereDecode(text, key);
              } else {
                caesarDecode(text, rot);
              }
            }
            break;
          case 'i':
            printHistogram(stdout, text);
            break;
          case 'w':
            printText(stdout, text);
            break;
          default:
            error = ERR_UNKNOWN_OPERATION;
            break;
          }
        }
      }
    }
  }
  if (error != ERR_NULL) {
    printErrorAndUsage(stderr, error);
  }
  return error;
}
