#!/bin/sh

/home/d/tools/bin/shutdown.sh jenkins-web

sleep 2

/home/d/tools/bin/start_tomcat.sh jenkins-web
