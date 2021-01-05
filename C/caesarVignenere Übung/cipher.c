/**
 * @file cipher.c
 *
 * Implementation des Moduls mit Verschluesselungsfunktionen.
 *
 * @author kar, mhe,  Daniel Pigotow (minf103095), Richard Rustien(minf103705)
 * @date 21.08.2019
 */

#include "cipher.h"
#include <assert.h>
#include <stdio.h>

void initText(Text text) {
  unsigned int i = 0;
  for (i = 0; i < MAX_TEXT_LENGTH; i++) {
    text[i] = '\0';
  }
}

void caesarEncode(Text text, unsigned int rot) {
  unsigned int i = 0;

  rot %= (BIGGEST_CHAR - SMALLEST_CHAR + 1);
  for (i = 0; text[i] != '\0'; i++) {
    text[i] = text[i] - SMALLEST_CHAR;
    text[i] = text[i] + rot;
    text[i] = text[i] % (BIGGEST_CHAR - SMALLEST_CHAR + 1);
    text[i] = text[i] + SMALLEST_CHAR;
  }
}

void caesarDecode(Text plain, unsigned int rot) {
  int i = 0;
  int overflow = 0;
  char res = '\0';
  rot %= (BIGGEST_CHAR - SMALLEST_CHAR + 1);
  for (i = 0; plain[i] != '\0'; i++) {
    res = plain[i] - rot;
    if (res < SMALLEST_CHAR) {
      overflow = SMALLEST_CHAR - res - 1;
      plain[i] = BIGGEST_CHAR - overflow;
    } else {
      plain[i] = res;
    }
  }
}

void vigenereEncode(Text text, Text pass) {
  assert(pass[0] != '\0');
  {
    int i = 0;
    int j = 0;
    Text newPass;
    int charRange = 0;

    charRange = BIGGEST_CHAR - SMALLEST_CHAR + 1;
    initText(newPass);
    /*Key auf gleiche länge mit text bringen*/
    for (i = 0; text[i] != '\0'; i++) {
      if (pass[j] == '\0') {
        j = 0;
      }
      newPass[i] = pass[j];
      j++;
    }

    for (i = 0; text[i] != '\0'; i++) {
      text[i] = (((text[i] - SMALLEST_CHAR) + (newPass[i] - SMALLEST_CHAR)) %
                 charRange) +
                SMALLEST_CHAR;
    }
  }
}

void vigenereDecode(Text plain, Text pass) {
  assert(pass[0] != '\0');
  {
    int i = 0;
    int j = 0;
    int overflow = 0;
    Text newPass;
    char res = '\0';

    initText(newPass);
    /*auf funktion auslagern key stutzen*/
    for (i = 0; plain[i] != '\0'; i++) {
      if (pass[j] == '\0') {
        j = 0;
      }
      newPass[i] = pass[j];
      j++;
    }

    for (i = 0; plain[i] != '\0'; i++) {
      if (newPass[i] != SMALLEST_CHAR) {

        res = ((plain[i] - SMALLEST_CHAR) - (newPass[i] - SMALLEST_CHAR));
        if (res < SMALLEST_CHAR) {
          overflow = SMALLEST_CHAR - res;
          plain[i] = (BIGGEST_CHAR - overflow) + SMALLEST_CHAR + 1;
        } else {
          plain[i] = res + SMALLEST_CHAR;
        }
      }
    }
  }
}

Errorcode readText(Text t, char *str) {
  /** @TODO Implementation */
  unsigned int i = 0;
  Errorcode err = ERR_NULL;

  initText(t);
  while ((str[i] != '\0') && (err == ERR_NULL)) {
    if (i >= MAX_TEXT_LENGTH) {
      err = ERR_TEXT_TOO_LONG;
    }
    if (str[i] < SMALLEST_CHAR || str[i] > BIGGEST_CHAR) {
      err = ERR_TEXT_ILLEGAL_CHAR;
    } else {
      t[i] = str[i];
      i++;
    }
  }
  return err;
}

void printText(FILE *stream, Text t) {
  int i = 0;

  while (t[i] != '\0') {
    fprintf(stream, "%c", t[i]);
    i++;
  }

  fprintf(stream, "\n");
}

void printHistogram(FILE *stream, Text text) {

  int i = 0, range = BIGGEST_CHAR - SMALLEST_CHAR + 1;
  int chIndex = 0;
  int countArr[BIGGEST_CHAR - SMALLEST_CHAR + 1] = {0};
  /*Anzahl buchstaben bestimmen*/
  if (text[0] != '\0') {
    /*laufen über jeden char in der range, somit sind einträge in chArr
     * sortiert*/
    for (i = 0; text[i] != '\0'; i++) {
      chIndex = text[i] - SMALLEST_CHAR;
      countArr[chIndex]++;
    }

    /*formatierungsberechnung anzahl spaces zwischen Characters in sizeArr*/
    for (i = 0; i < range; i++) {
      if (countArr[i] > 0) {
        char ch = SMALLEST_CHAR + i;
        if (countArr[i] > 9) {
          fprintf(stream, " ");
          if (countArr[i] > 99) {
            fprintf(stream, " ");
          }
        }
        fprintf(stream, "%c", ch);
        fprintf(stream, " ");
      }
    }
    fprintf(stream, "\n");
    for (i = 0; i < range; i++) {
      if (countArr[i] > 0) {
        fprintf(stream, "%i ", countArr[i]);
      }
    }
    fprintf(stream, "\n");
  } else {
    fprintf(stream, "\n\n");
  }
}
