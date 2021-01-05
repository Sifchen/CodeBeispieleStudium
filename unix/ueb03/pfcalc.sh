#!/bin/bash
## Authoren: Michael Smirnov, Daniel Pigotow
#Es soll ein Shell-Skript geschrieben werden, welches einfache mathematische
# Berechnungen mit den folgenden Operationen durchführen kann:
#Addition (ADD)
#Subtraktion (SUB)
#Multiplikation (MUL)
#Ganzzahlige Division (DIV)
#Rest der ganzzahligen Division (MOD)
# EXITCODES:
#1:WRONG USEAGE OF SCRIPT
#2:Division with 0


#Prints the Helptext on stdout
#No Parameters necessary
function printHelp(){
	echo "Usage:
pfcalc.sh -h | pfcalc.sh --help

  prints this help and exits

pfcalc.sh [-i | --visualize] NUM1 NUM2 OP [NUM OP] ...

  provides a simple calculator using a postfix notation. A call consists of
  an optional parameter, which states whether a visualization is wanted,
  two numbers and an operation optionally followed by an arbitrary number
  of number-operation pairs.

  NUM1, NUM2 and NUM are treated as integer numbers.

  NUM is treated in the same way as NUM2 whereas NUM1 in this case is the
  result of the previous operation.

  OP is one of:

    ADD - adds NUM1 and NUM2

    SUB - subtracts NUM2 from NUM1

    MUL - multiplies NUM1 and NUM2

    DIV - divides NUM1 by NUM2 and returns the integer result

    MOD - divides NUM1 by NUM2 and returns the integer remainder

  When the visualization is active the program additionally prints the
  corresponding mathematical expression before printing the result."
}

#Echos the Command with its corresponding sign
#Param: [$1] - the Command to replace
#Errorcodes: 1 - Wrong use of script or no Parameter
function replace() {
    case $1 in
        ADD)
            echo "+"
            ;;
        SUB)
            echo "-"
            ;;
        MUL)
            echo "*"
            ;;
        DIV)
            echo "/"
            ;;
        MOD)
            echo "%"
            ;;
        *)
            return 1
    esac
}

#Prints "Error:" followed by an Message to stdError and the usage of the SCRIPT
#Param: $1 - The Message which describes the Error
error (){
    echo "Error: $1" 1>&2
    printHelp 1>&2
}

#------MAIN------#
VISUALIZE=false
#Deals with the optional flags to use the script
case $1 in
    -h | --help)
        shift
        #If there are additional parameters the script is used wrong
        [ $# -ne 0 ] && error "No further arguments after help allowed" && exit 1
        #If there is only the help option the script terminates with exitcode 0
        printHelp && exit 0
        ;;
    -i | --visualize)
        VISUALIZE=true
        shift
        ;;
esac
#If less than 3 parameters the script is used wrong
[ $# -lt 3 ] && error "Arguments needed" && exit 1
#For the first time
RESULT=$1
OUT=$1
#If the first number is negative for visualization
[ $OUT -lt 0 ] && OUT="($1)"
#so that the the rest matches the pattern [Number][Operator]
shift

#For all the ramaining [Number][Operator]
while [ $# -gt 0 ]; do
    if [ "$VISUALIZE" = "true" ]; then
        #Replaces the Operand with the visual sign
        REPLACED=$(replace $2)
        #Checks if sign to replace was valid
        [ $? -eq 1 ] && error "Unknown Operand used" && exit 1
        FIRST=$1
        #If the first number is negative for visualization
        [ $FIRST -lt 0 ] && FIRST="($1)"
        #Builds the visual output
        OUT="($OUT$REPLACED$FIRST)"
    fi
    #Checks the operand and does the mathematical calculation
    case $2 in
        ADD)
            RESULT=$(($RESULT + $1))
            ;;
        SUB)
            RESULT=$(($RESULT - $1))
            ;;
        MUL)
        RESULT=$(($RESULT * $1))
            ;;
        DIV)
            #Checks if the number is 0
            [ $1 -eq 0 ] && error "Division with zero" && exit 2
            RESULT=$(($RESULT / $1))
            ;;
        MOD)
            #Checks if the number is 0
            [ $1 -eq 0 ] && error "Division with zero" && exit 2
            RESULT=$(($RESULT % $1))
            ;;
        *)
            #Invalid operands
            error "Unknown Operand used" && exit 1
            ;;
    esac
    #For the next pair of [Number][Operator]
    shift 2
done
#Prints the visualization if VISUALIZE was set with flag
if [ "$VISUALIZE" = "true" ]; then
    echo "$OUT"
fi
#Always prints the result of the mathematical calculation
echo "$RESULT"
