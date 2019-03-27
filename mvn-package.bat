@ECHO OFF
@REM SET PATH=%PATH%;%MVN_HOME%\bin
call mvn clean package -Dmaven.test.skip=true -DfailIfNoTests=false
@pause
