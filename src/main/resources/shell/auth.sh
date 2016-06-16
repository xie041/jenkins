#!/bin/sh

echo "Please enter target server ip"
read ip
#echo "Please enter unpass login user name"
#read username
#echo "Please enter unpass login user password"
#read -s -p "" password
username="jenkins"
password="abc123"
tool="/home/d/tools/bin"
pub=`cat /home/$username/.ssh/id_rsa.pub`

ssh root@$ip   << code
	ifconfig | grep "Bcast"
	
	
	if [ -z "$( id $username &>/dev/null )" ];then
	    echo "$username exist"
	else
	    echo "$username not exist"
	fi
	adduser $username
	echo $password | passwd $username --stdin > /dev/null
	echo "add user finished"
	
	cd /home/$username
	if [ -d .ssh ];then
            echo "/home/$username/.ssh exist"
	else
	    echo "/home/$username/.ssh not exist ,will mkdir .ssh"
	    mkdir .ssh
	fi
	
	chown -R $username:$username .ssh/
	echo "$pub" >> .ssh/authorized_keys
	chown -R $username:$username .ssh/
	chmod 700 .ssh
        chmod 600 .ssh/authorized_keys
	echo "Auth $username no pass login sucess"

	if [ -d /home/d ];then
            echo "/home/d exist"
	else
	    echo "/home/d/www/jobs not exist,will mkdir"
            mkdir -p /home/d/www/jobs
	    mkdir -p $tool
            chown -R jenkins:jenkins /home/d/
	    echo "auth finished"
	fi
code


scp $tool/shutdown.sh $ip:$tool
scp $tool/start_tomcat.sh $ip:$tool
echo "copy shell finished"

#ssh $username@$ip

echo "auth finished"

