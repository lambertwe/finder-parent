@ECHO OFF

@REM finder restart
@REM http://www.finderweb.net
@set JSSE_OPTS=
@SET JAVA_PROCESS_ID=%1

@ECHO stop tomcat
@ECHO "#{work.directory}\shutdown.bat"
call "#{work.directory}\shutdown.bat"

@ECHO kill process: %JAVA_PROCESS_ID%
if not "%JAVA_PROCESS_ID%"=="" taskkill /F /pid %JAVA_PROCESS_ID%

@echo start tomcat
call "#{work.directory}\startup.bat"
@ECHO start success
