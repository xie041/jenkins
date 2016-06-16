#!/bin/bash

#JAVA_HOME=/usr/lib/jvm/jdk1.6.0_45
#export JAVA_HOME
### not run ssh dev1 "cd /home ; ls ;/bin/echo 'hello $1' ;/sbin/ifconfig" 
#/home/d/tools/bin/dbs_tomcat.sh /home/d/jenkins/workspace/price-api 10.255.209.147 priceapi priceapi
workspace=$1
server=$2
targetFileName=$3
scpFiles=$4
shel=$5
shellock=$6
tomcatbase="/home/d/www/jobs"

#ansi color
#echo -e "\t<type>: \tpublish type, see following:"  
#echo -e "\t\t1. \033[0;35mbuild_pub: \033[0;33mpublish a build package\033[0m"  
#echo -e "\t\t2. \033[0;35mtest_fail: \033[0;33mpublish a failed test result\033[0m\n"  
#echo -e "\t\t2. \033[0;35mtest_pass: \033[0;33mpublish a passed test result"  
#echo -e "\033[0m\n"

#echo -e "\033[0;30m: 黑" 
#echo -e "\033[0;31m: 红"
#echo -e "\033[0;32m: 绿"
#echo -e "\033[0;33m: 黄"
#echo -e "\033[0;34m: 蓝"
#echo -e "\033[0;35m: 紫"
#echo -e "\033[0;36m: 深绿"
#echo -e "\033[0;37m: 白色"
#echo -e "\033[30m   --   \033[37m   设置前景色   "
#echo -e "\033[40m   --   \033[47m   设置背景色   "

#echo -e "\033[1m设置高亮度"
#echo -e "\033[0m关闭所有属性"
#echo -e "\033[43m黄色背景色\033[0m"

#echo "p1=$workspace p2=$server p3=$targetFileName p4=$scpFiles p5=$shel p6=$shellock"
#scp files to target machine temp 
#scp -r $workspace/$scpFiles $server:~/temp/
#kill process

ssh jenkins@$server "/home/d/tools/bin/shutdown.sh $targetFileName"
ssh jenkins@$server "rm -rf $tomcatbase/$targetFileName"
ssh jenkins@$server "mkdir $tomcatbase/$targetFileName"

#if .tar.gz extract first
if [ -f $workspace/$scpFiles ];then
   echo "Your input is .gz file,will extract...";
   mkdir -p $workspace/${scpFiles%%.*}
   #cd ${scpFiles%%/*}/target/
   tar xfz $workspace/$scpFiles -C $workspace/${scpFiles%%/*}/target/
   #scpFiles=${scpFiles%%.*}
   #echo "$scpFiles"
   scp -r $workspace/${scpFiles%%.*}/* $server:$tomcatbase/$targetFileName
elif [ -d $workspace/$scpFiles ];then
   echo "document"
   scp -r $workspace/${scpFiles}/* $server:$tomcatbase/$targetFileName
fi

echo "[DANGDANG] --------------------------------------------------------------------------------------------"
echo "[DANGDANG] -                                                                                          -"
echo "[DANGDANG] -     Send To DEV Machine FINISHED! (Any Question Please contact xieyong@dangdang.com)     -"
echo "[DANGDANG] -                                                                                          -"
echo "[DANGDANG] --------------------------------------------------------------------------------------------"

#login remote machine

ssh jenkins@$server   << remotetags  
echo "[DANGDANG] --------------------------------------------------------------------------------------------"
echo "[DANGDANG] login $server sucess"
#ifconfig | grep "Bcast" | awk '{print $2}'
echo "[DANGDANG] --------------------------------------------------------------------------------------------"

#cd target
cd $tomcatbase/$targetFileName

#run server
if [ $shellock == "true" ]
then
    if [ $shel ]
    then
      echo "[DANGDANG] Ready to execute $tomcatbase/$targetFileName/$shel"
      nohup /bin/sh $tomcatbase/$targetFileName/$shel start > /dev/null 2>&1 &
      sleep 1
      #nohup /bin/sh ./bin/startup.sh start > /dev/null 2>&1 &
      echo "[DANGDANG] --------------------------------------------------------------------------------------------"
    fi
else
    echo "[DANGDANG] --------------------------------------------------------------------------------------------"
    echo "[DANGDANG] not execute shell"
    echo "[DANGDANG] --------------------------------------------------------------------------------------------"
fi


#java process
ls -la
ps -ef | grep -v "grep" | grep $targetFileName

echo "[DANGDANG] Ready to exit from $server"
exit 0

remotetags



