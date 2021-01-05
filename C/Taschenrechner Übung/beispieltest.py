#!/usr/bin/env python

# Test definitions
suite = [

   Test (
        name = "Hilfeausgabe",
        description = "Der Hilfetext muss auf stdout ausgegeben werden.",
        command = "$DUT -h",
        stdout = ExpectFile("usage.txt"),
        stderr = "",
        returnCode = 0,
        timeout = 10
    ),

    Test (
        name = "Einfache Addition inkl. Rueckgabewert",
        description = "Einfache Adddition 5 + 4",
        command = "$DUT 5 + 4",
        stdout = "9$n",
        stderr = "",
        returnCode = "0",
        timeout = 10
    ),

    Test (
        name = "Fehlerfall inkl. Rueckgabewert",
        description = "Fehlermeldung muss mit Error: beginnen und auf stderr stehen, Returncode ist ungleich 0",
        command = "$DUT",
        stdout = "",
        stderr = "regex:^Error:",
        returnCode = lambda v: v !=0,
        timeout = 10
    ),

]
