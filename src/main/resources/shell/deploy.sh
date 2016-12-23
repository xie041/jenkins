#!/bin/bash

#JAVA_HOME=/usr/lib/jvm/jdk1.6.0_45
#export JAVA_HOME

#$1 /home/d/jenkins_v2/workspace/price-distribute 
#$2 10.255.209.147 
#$3 /home/d/www/temp 
#$4 price-web/target/price-web 
#$5 bin/startup.sh false

workspace=$1
server=$2
targetDir=$3
scpFiles=$4
shel=$5
run=$6

scp -r $workspace/$scpFiles/* $server:$targetDir

#login remote machine
echo "will login $server"
ssh jenkins@$server   << remotetags  

echo "login $server sucess"

if [ $run == "true" ]
then
  echo "[DANGDANG] --------------------------------------------------------------------------------------------"
  echo -e "[DANGDANG] Execute Shell Successfull "
  echo "[DANGDANG] --------------------------------------------------------------------------------------------"
else
  echo "[DANGDANG] --------------------------------------------------------------------------------------------"
  echo -e "[DANGDANG] Not Execute Shell"
  echo "[DANGDANG] --------------------------------------------------------------------------------------------"
fi

echo "will exit from $server"
exit  
remotetags

