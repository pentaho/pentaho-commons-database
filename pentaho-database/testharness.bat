@echo on


REM **************************************************
REM ** Init Classpath                               **
REM **************************************************

set CLASSPATH=.

REM ***************************
REM   Database Dialog Library
REM ***************************

set CLASSPATH=%CLASSPATH%;lib\pentaho-xul-database-1.7.0.jar

REM **********************
REM   External Libraries
REM **********************

REM Loop the libext directory and add the classpath.
REM The following command would only add the last jar: FOR %%F IN (lib-ext\*.jar) DO call set CLASSPATH=%CLASSPATH%;%%F
REM So the circumvention with a subroutine solves this ;-)

FOR %%F IN (lib-ext\*.jar) DO call :addcp %%F
FOR %%F IN (lib-ext\JDBC\*.jar) DO call :addcp %%F

goto extlibe

:addcp
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:extlibe

REM **********************
REM   Collect arguments
REM **********************

set _cmdline=
:TopArg
if %1!==! goto EndArg
set _cmdline=%_cmdline% %1
shift
goto TopArg
:EndArg


REM *****************
REM   SWT Libraries
REM *****************

set CLASSPATH=%CLASSPATH%;lib-swt\runtime.jar
set CLASSPATH=%CLASSPATH%;lib-swt\jface.jar
set CLASSPATH=%CLASSPATH%;lib-swt\common.jar
set CLASSPATH=%CLASSPATH%;lib-swt\commands.jar
set CLASSPATH=%CLASSPATH%;lib-swt\win32\swt.jar

REM ******************************************************************
REM ** Set java runtime options                                     **
REM ** Change 256m to higher values in case you run out of memory.  **
REM ******************************************************************

set OPT=-Xmx256m -classpath %CLASSPATH% -Djava.library.path=lib-swt\win32\ 


REM ***************
REM ** Run...    **
REM ***************

java %OPT% org.pentaho.test.ui.database.DatabaseDialogHarness %_cmdline%
pause