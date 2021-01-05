# ---------------------------------------------	#
# Fachhochschule Wedel                          #
# Makefile SP-Uebung 02, WS19/20                #
# kar, mhe                                      #
# ---------------------------------------------	#

# Variablen fuer die Praeprozessorkonstanten
MAX_TEXT_LENGTH	= 30
SMALLEST_CHAR 	= ' '
BIGGEST_CHAR 	= '~'

# Flag that states that warnings will not lead to compilation errors
FORCE = false

# Compiler
CC		= gcc
# Doc generator
DOC		= doxygen

# Include directories
INCLUDES	=
# Compiler flags
CFLAGS_FORCE    = -c -g -ansi -pedantic -Wall -Wextra -DMAX_TEXT_LENGTH=$(MAX_TEXT_LENGTH) -DSMALLEST_CHAR="$(SMALLEST_CHAR)" -DBIGGEST_CHAR="$(BIGGEST_CHAR)"  
CFLAGS		= $(CFLAGS_FORCE) -Werror
# Linker flags
LDFLAGS		=

# Source codes
SOURCES		= cipher.c error.c ueb02.c
OBJECTS		= $(SOURCES:.c=.o)

# Target name
BINARY		= ueb02

.PHONY: all help clean doc force default

default: all

force: 
	@make FORCE=true

all: $(BINARY)

# Compile a single file
%.o : %.c
	@echo "  - Building $@"
ifeq ($(FORCE),true)
	$(CC) $(CFLAGS_FORCE) $(INCLUDES) -o $@ $<
else
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ $<
endif
	@echo "  ... done"

# Link files to an executable
$(BINARY): $(OBJECTS)
	@echo "  - linking $@"
	$(CC) $(LDFLAGS) $(OBJECTS) -o $(BINARY)
	@echo "  ... done"

# Clean the project
clean:
	@echo "  - deleting all generated files"
	rm -f $(OBJECTS) $(BINARY) *~ doxygen.log
	rm -rf doc/
	@echo "  ... done"

# Create html-documentation
doc:
	@echo "  - creating documentation"
	$(DOC)
	@echo "  ... done"

# Show help
help:
	@echo "Options:"
	@echo "make all      - create program"
	@echo "make clean    - clean up"
	@echo "make doc      - create documentation"
	@echo "make force    - compile without -Werror (just for 'comfier' development)"
	@echo "Variables:"
	@echo "MAX_TEXT_LENGTH - sets the maximum text length (default = 30)"
	@echo "SMALLEST_CHAR   - sets the smallest possible ASCII character (default = \" \")"
	@echo "BIGGEST_CHAR    - sets the biggest possible ASCII character (default = \"~\")"

