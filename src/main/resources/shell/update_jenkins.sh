#!/bin/sh

p=jenkins-web
url=$1

rm -rf jenkins.war*

if [ ! -n "$1" ] ;then  
    echo "you have not input jenkins download url"
    exit
else  
    echo "ready to download $1"  
fi  

wget $url

ls -l jenkins.war && echo "down jenkins.war success" || echo "download jenkins.war fail!"

echo "ready to update jenkins"
echo "stop jenkins's tomcat"

/home/d/tools/bin/shutdown.sh $p

sleep 3

ps -ef | grep $p

rm -rf /home/d/www/$p/webapps/ROOT/*

mv ./jenkins.war /home/d/www/$p/webapps/ROOT/

cd /home/d/www/$p/webapps/ROOT

mv ./jenkins.war ./jenkins.zip
unzip ./jenkins.zip
rm -rf ./jenkins.zip

sleep 2

/home/d/tools/bin/start_tomcat.sh $p

echo "finished upgrade jenkins, enjoy it!"

