#!/bin/sh

# finder restart
# http://www.finderweb.net
WORK_DIRECTORY=#{work.directory}

echo stop tomcat
${WORK_DIRECTORY}/shutdown.sh

kill -9 $1

echo start tomcat
${WORK_DIRECTORY}/startup.sh
echo start success
