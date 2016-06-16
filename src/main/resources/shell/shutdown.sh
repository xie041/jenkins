#!/bin/bash
source /etc/profile


#port=$1

#tomcat_pid=`/usr/sbin/lsof -n -P -t -i :$port`
#[ -n "$tomcat_pid" ] && kill -9 $tomcat_pid

pro=`ps aux|grep $1|grep -v grep | grep java |awk '{print $2}'`


for i in $pro
do
    kill $i

done


echo "stop $1 sucess"

