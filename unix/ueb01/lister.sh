#!/bin/sh

#Autoren: Minf103095(Daniel Pigotow) , Minf103430(Michael Smirnov) 
#Letzte Aenderung 19.04.2019 13:14
#Das Skript soll in dem Ordner in dem es sich befindet zunächst einen Unterordner mit dem Namen info anlegen.
#Sollte der Unterordner bereits existieren, so soll dies ignoriert werden und nicht zu einer Fehlermeldung führen.
#Danach soll das Skript alle Dateien im aktuellen Ordner, die die Dateiendung txt haben alphabetisch aufsteigend sortiert in die Datei mytextfiles.txt im Unterordner info schreiben.
#Sofern diese Datei noch nicht existiert, soll sie angelegt werden, falls sie bereits existiert, so soll sie überschrieben werden.
#Die Fehlermeldung, die entsteht, wenn sich keine passenden Dateien im aktuellen Ordner befinden, darf hierbei ignoriert werden.

mkdir info 2> /dev/null #"Nulldevice" dient zum verwerfen von Ausgaben
ls -X *.txt > ./info/mytextfiles.txt
