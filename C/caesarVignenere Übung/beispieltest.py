#!/usr/bin/env python

suite = [
    Test (
        name = "Hilfeausgabe",
        description = "Der Hilfetext muss auf stdout ausgegeben werden.",
        command = "$DUT -h",
        stdout = ExpectFile("expected/usage.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Beispiel aus der Aufgabenstellung",
        description = "Ausgabe leerer Text",
        command = "$DUT -w",
        stdout = ExpectFile("expected/empty_text.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Beispiel aus der Aufgabenstellung",
        description = "Ausgabe des Histograms des leeren Texts",
        command = "$DUT -i",
        stdout = ExpectFile("expected/empty_hist.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Beispiel aus der Aufgabenstellung",
        description = "Caesar ver- und entschluesseln",
        command = "$DUT -t \"Hallo Welt\" -C 12 -w -e -w -d -w",
        stdout = ExpectFile("expected/b_caesar.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Beispiel aus der Aufgabenstellung",
        description = "Vigenere ver- und entschluesseln",
        command = "$DUT -t \"Hallo Welt\" -w -V \"  XY \" -e -w -d -w",
        stdout = ExpectFile("expected/b_vigenere.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Beispiel aus der Aufgabenstellung",
        description = "Histogrammausgabe",
        command = "$DUT -t \"HAAAAAAAAAAAAAAALLO_WELT!\" -i",
        stdout = ExpectFile("expected/b_histogram.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Beispiel aus der Aufgabenstellung",
        description = "Komplexer Aufruf",
        command = "$DUT -C 5 -t \"HALLO\" -w -i -e -w -i -t \"Welt\" -w -e -w",
        stdout = ExpectFile("expected/b_complex_call.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Fehlerfall: keine Parameter",
        description = "Fehlermeldung und Errorcode muessen zu den \
                       Vorgaben passen.",
        command = "$DUT",
        stdout = "",
        stderr = "regex:^Error: No arguments given.",
        returnCode = 1,
        timeout = 10
    ),
    Test (
        name = "Eingabe mit -t mehr als 30 Zeichen",
        description = "Ungueltiger Input 31 Zeichen",
        command = "$DUT -t \"qwertzuiopqwertzuiopqwertzuiopq\"",
        stdout = "",
        stderr = "regex:^Error: Given Text too long.",
        returnCode = 5,
        timeout = 10
    ),
    Test (
        name = "Eingabe mit -t Text vergessen",
        description = "-w als Text",
        command = "$DUT -t -w -w",
        stdout = "-w",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Eingabe mit -t danach kommt nichts mehr",
        description = "Nix da",
        command = "$DUT -t",
        stdout = "",
        stderr = "regex:^Error: Missing text.",
        returnCode = 3,
        timeout = 10
    ),
    Test (
        name = "Eingabe mit -t ungueltiges Zeichen",
        description = "Boeses Tab?",
        command = "$DUT -t \"Hallo\t\"",
        stdout = "",
        stderr = "regex:^Error: Illegal character in given text.",
        returnCode = 4,
        timeout = 10
    ),
    Test (
        name = "Eingabe mit -t Ausgabe mit -w",
        description = "Gueltiger Input",
        command = "$DUT -t \"Hello\" -w",
        stdout = "Hello",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "-C Rot vergessen",
        description = "Rot vergessen",
        command = "$DUT -t \"Hello\" -C -w",
        stdout = "",
        stderr = "regex:^Error: Missing or invalid rotation for caesar cipher.",
        returnCode = 9,
        timeout = 10
    ),
    Test (
        name = "-C Rot negativ",
        description = "Rot negativ",
        command = "$DUT -t \"Hello\" -C -10 -w",
        stdout = "",
        stderr = "regex:^Error: Missing or invalid rotation for caesar cipher.",
        returnCode = 9,
        timeout = 10
    ),
    Test (
        name = "Caesar-Verschluesselung ueber den Rand",
        description = "Rot ist 2",
        command = "$DUT -t \"}~\" -C 2 -e -w",
        stdout = " !",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
     Test (
        name = "Caesar-Verschluesselung ueber den Rand und zu grosse Rotation",
        description = "Rot ist 200",
        command = "$DUT -t \"}~\" -C 200 -e -w",
        stdout = "()",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Caesar-Verschluesselung normal",
        description = "Rot ist 12",
        command = "$DUT -t \"Hallo Welt\" -C 12 -e -w",
        stdout = "Tmxx{,cqx!",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Caesar-Verschluesselung zwei Mal",
        description = "Rot ist 1",
        command = "$DUT -t \"Hallo Welt\" -C 1 -e -e -w",
        stdout = "Jcnnq\"Ygnv",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Vigenere-Verschluesselung normal",
        description = "Key ist Key",
        command = "$DUT -t \"Helga\" -V \"Key\" -e -w",
        stdout = "sKf3G",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Vigenere-Ver- und Entschluesselung normal",
        description = "Key ist Key",
        command = "$DUT -t \"Helga\" -V \"Key\" -e -d -w",
        stdout = "Helga",
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),
    Test (
        name = "Vigenere-Verschluesselung ohne Key",
        description = "Key ist nicht vorhanden",
        command = "$DUT -t \"Helga\" -V ",
        stdout = "",
        stderr = "regex:^Error: Missing, empty or invalid key for vigenere cipher.",
        returnCode = 10,
        timeout = 10
    ),
    Test (
        name = "Vigenere-Verschluesselung ungueltiger Key",
        description = "Key enthaelt ungueltige Zeichen",
        command = "$DUT -t \"Helga\" -V \"\t\t\" ",
        stdout = "",
        stderr = "regex:^Error: Missing, empty or invalid key for vigenere cipher.",
        returnCode = 13,
        timeout = 10
    ),
    Test (
        name = "Verschluesselung ohne Verschluesselungsalgorithmus",
        description = "-e ohne ausgewaehlten Verschluesselungsalgorithmus",
        command = "$DUT -t \"Hallo Welt\" -e -w",
        stdout = "",
        stderr = "regex:^Error: No cipher algorithm selected.",
        returnCode = 6,
        timeout = 10
    ),
    Test (
        name = "Entschluesselung ohne Verschluesselungsalgorithmus",
        description = "-d ohne ausgewaehlten Verschluesselungsalgorithmus",
        command = "$DUT -t \"Hallo Welt\" -d -w",
        stdout = "",
        stderr = "regex:^Error: No cipher algorithm selected.",
        returnCode = 7,
        timeout = 10
    ),
]
