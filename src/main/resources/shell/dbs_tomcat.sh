#!/bin/bash

JAVA_HOME=/usr/lib/jvm/jdk1.6.0_45
export JAVA_HOME
### not run ssh dev1 "cd /home ; ls ;/bin/echo 'hello $1' ;/sbin/ifconfig" 

#echo "p1=$1 p2=$2 p3=$3 p4=$4"
workspace=$1
server=$2
targetFileName=$3
scpFiles=$4
tomcatbase="/home/d/www"
#scp files to target machine temp 
scp -r $workspace/target/$scpFiles.war $server:~/temp/


#login remote machine
echo "will login $server"
ssh jenkins@$server   << remotetags  

echo "login $server sucess"
ifconfig | grep "Bcast" | awk '{print $3}'

/home/d/tools/bin/shutdown.sh $targetFileName

##move files to tomcat run home
cd ~/temp
mv $scpFiles.war $scpFiles.zip
rm -rf ROOT
mkdir ROOT
mv $scpFiles.zip ./ROOT
cd ./ROOT
unzip $scpFiles.zip
rm $scpFiles.zip

cd ..
rm -rf $tomcatbase/$targetFileName/webapps/ROOT
mv ROOT $tomcatbase/$targetFileName/webapps/

#start tomcat
/home/d/tools/bin/start_tomcat.sh $targetFileName

ps -ef | grep $targetFileName

echo "will exit from $server"
exit  
remotetags



