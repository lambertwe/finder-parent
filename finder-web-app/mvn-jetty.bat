@echo off
del "..\logs\*.log"
call mvn jetty:run
@pause