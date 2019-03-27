@ECHO OFF
rd /s /q target
@REM SET PATH=%PATH%;%MVN_HOME%\bin
call mvn clean package -Dmaven.test.skip=true -DfailIfNoTests=false
@pause
