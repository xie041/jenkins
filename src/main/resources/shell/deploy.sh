#!/bin/bash

#JAVA_HOME=/usr/lib/jvm/jdk1.6.0_45
#export JAVA_HOME

#$1 /home/d/jenkins_v2/workspace/price-distribute 
#$2 10.255.209.147 
#$3 /home/d/www/temp 
#$4 price-web/target/price-web 
#$5 shell

workspace=$1
server=$2
targetDir=$3
scpFiles=$4
shellSource=$5

#scp -r $workspace/$scpFiles/* $server:$targetDir
rsync -avzr $workspace/$scpFiles/ $server:$targetDir

#login remote machine
echo "will login $server"
ssh jenkins@$server   << remotetags  

echo "Login $server sucess"
#create exe file
echo "#!/bin/sh" > ~/temp.sh
echo "$shellSource" >> ~/temp.sh
chmod +x ~/temp.sh
~/temp.sh
rm -rf ~/temp.sh

echo "will exit from $server"
exit  
remotetags

