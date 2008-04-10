#!/bin/sh

#############################
# Database Dialog Library
#############################

XULLIB=lib\pentaho-xul-database-1.7.0.jar

#############################
# External Libraries
#############################

LIBEXT=$(find lib-ext -name '*.jar' | tr '\n' :)

#############################
# SWT Libraries
#############################

LIBSWT=$LIBSWT:lib-swt/runtime.jar
LIBSWT=$LIBSWT:lib-swt/jface.jar
LIBSWT=$LIBSWT:lib-swt/common.jar
LIBSWT=$LIBSWT:lib-swt/commands.jar
LIBSWT=$LIBSWT:lib-swt/linux/x86/swt.jar

CLASSPATH=.:bin:$XULLIB:$LIBEXT:$LIBSWT

echo $CLASSPATH

echo "******************************************************************"
echo "** Set java runtime options                                     **"
echo "** Change 256m to higher values in case you run out of memory.  **"
echo "******************************************************************"

VMARGS="-Xmx256m -classpath $CLASSPATH -Dswt.library.path=lib-swt/linux/x86/"

java $VMARGS org.pentaho.test.ui.database.DatabaseDialogHarness $@

