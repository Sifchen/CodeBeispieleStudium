#!/bin/sh
# Authoren: Michael Smirnov, Daniel Pigotow
#Ein fiktives Umfragetool hat als ausgabeformat Zeilen bestehend aus 3 Feldern, jeweils durch ein ";" getrennt.
#Die erste Spalte enthält eine Kennzahl
#die zweite eine Bezeichnung
#und die dritte eine Bewertung.
#Folgendes Skript soll die weiterverarbeitung dieser Ausgaben folgendermaßen ermöglichen.
#Es entsteht eine Datei deren Name frei wählbar ist (erster Parameter), welche nach der Bewertung absteigend sortiert.
#Einträge mit gleicher Bewertung werden außerdem nach der Kennzahl absteigend sortiert.
#Ungültige Zeilen enthalten den Ausdruck "<Failed>" in beliebiger Groß- und Kleinschreibung (inklusive der spitzen Klammern, ohne die Anführungszeichen).
#Diese Zeilen werden demnach aus der entstehenden Datei herausgeworfen.
#Letztendlich soll das Skript auf STDOUT die Bezeichnung des am schlechtesten bewerteten Eintrages ausgeben.
#Das Skript soll ein einzeiler werden.
grep -Eiv '<Failed>|^$' | sort -r -n -u -t ";" -k3 -k1 -k2 | tee "$1"| tail -n1 | cut -d ";" -f2

#grep -Eiv : "extended-regex"(Interpretiert Pattern als regulaerer Ausdruck) , "ignore case" und "inverted matching"
#sort -r -n -u -t ";" -k3 -k1 -k2 : -u -> remove uniqe; -t ";" -> trennzeichen ";" -rnk -> (r)umgedrehte (n)numerische sortiereung von (k)spalte 3, 1 und 2 da, falls -k2 nicht vorhanden ist die Option -u Zeilen die nach meinung des sort doppelt sind gelöscht werden, obwol diese sich diese doch in reihenfolge der Zeichen unterscheiden 
#tee "$1" : Schreiben in Datei mit dem Namen der in dem Parameter 1 steht und weiterleitung auf STDOUT
#tail -n1 : die letzte spalte ausgeben (n)number
#cut -d ";" -f2 : Schneidet zweite Spalte (- f2) mit trennzeichen ";" (-d ";") aus
