#!/bin/dash
#Authoren: Michael Smirnov, Daniel Pigotow
#Shell-Skript, welches LaTeX-Dateien analysieren kann.

#EXITCODES:
#1:WRONG USEAGE OF SCRIPT


#Prints the Helptext on stdout
#No Parameters necessary
printHelp(){
    cat << EOT
Usage:
latexanalyse.sh -h | latexanalyse.sh --help

  prints this help and exits

latexanalyse.sh INPUT OPTION

  INPUT is a valid latex-File (.tex)

  and OPTION is one of

  -g, --graphics      prints a list of all included graphics

  -s, --structure     prints the structure of the input file

  -u, --usedpackages  prints a list of the used packages and their options
EOT
}

#Prints "Error:" followed by an Message to stdError and the usage of the SCRIPT
#Param: $1 - The Message which describes the Error
error(){
    echo "Error: $1" 1>&2 #Redirects stdout to stderr
    printHelp 1>&2
    exit 1
}
#Removes comments from $1
#Param: $1 - Latex file containing comments
removeComments(){
    local comment=%.*\$
    sed "s/$comment//g" $1
}

#Prints graphics in Latex document
#Param: $1 - Latex file to get the graphics from
printGraphics (){
    local OPTIONS='\[[^]]*\]'
    local ExPATH='\{[^}]*\}' #variable name "Path" is not allowed!
    removeComments $1 |\
    tr --delete "\n" |\
    grep --extended-regexp --only-matching "\\\includegraphics($OPTIONS)?$ExPATH"|\
    #"\\\" necessary couse dash gives grep only "\\" which grep interprets as "\"
    grep --extended-regexp --only-matching "\{.+\}" |\
    sed 's/[{}]//g'

}

#Prints the documents Structure
#Param: $1 - Latex file to get the structure from
structure() {
    local ExPATH='\{[^}]*\}'
    removeComments $1 |\
    tr --delete "\n" |\
    grep --extended-regexp --only-matching "\\\(chapter|section|subsection)\*?$ExPATH" |\
    sed --regexp-extended --expression='s,\\chapter\*?, ,g' --expression='s,\\section\*?, |-- ,g' --expression='s,\\subsection\*?,     |-- ,g' |\
    sed 's/[{}]//g'

}

#Prints the documents packages and their options
#Param: $1 - Latex file to get package names from
usedpackages() {
    local OPTIONS='\[[^]]*\]'
    local NAME='\{[^}]*\}'
    removeComments $1 |\
    tr --delete "\n" |\
    grep --extended-regexp --only-matching "\\\usepackage($OPTIONS)?$NAME" |\
    grep --extended-regexp --only-matching "($OPTIONS)?$NAME" |\
    sed --regexp-extended --expression="s/($OPTIONS)?($NAME)/\2:\1/" --expression='s/[][{}]//g' --expression='s/ //g'|\
    sort
}

if [ "$1" = "-h" ] || [ "$1" = "--help" ]
#Checks if script is called with help option (other order)
then
    shift
    #If there are additional parameters the script is used wrong
    [ $# -eq 0 ] || error "No further arguments after help allowed"
    #If there is only the help option the script terminates with exitcode 0
    printHelp && exit 0
else
#If no help option is given
    [ $# -eq 2 ] || error "Wrong Use of Script (ARGUMENTS)"
    #There have to be 2 Argumens present for correct call
    INFILE="$1"
    OPTION="$2"
    [ -r "$INFILE" ] || error "File '$INFILE' does not exist"
    #Test if file is present and has read permission
    case "$OPTION" in
        -g | --graphics )
            printGraphics $INFILE
        ;;
        -s | --structure )
            structure $INFILE
        ;;
        -u | --usedpackages )
            usedpackages $INFILE
        ;;
        * )
            error "Wrong use of Script (OPTION)"
        ;;
    esac
fi
