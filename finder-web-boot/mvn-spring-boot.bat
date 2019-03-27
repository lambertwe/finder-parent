@ECHO OFF

del "logs\*.log"
@ECHO JAVA_HOME: %JAVA_HOME%
call mvn spring-boot:run
pause
