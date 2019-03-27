@echo off
del "catalina.home_IS_UNDEFINED\logs\wanmei_cms\*.log"
del "catalina.home_IS_UNDEFINED\logs\cms4\*.log"
call mvnDebug jetty:run
@pause