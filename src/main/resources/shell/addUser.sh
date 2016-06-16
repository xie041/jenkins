#!/bin/sh

	
read username
read password
if [ -z "$( id $username &>/dev/null )" ];then
    echo "$username exist"
else
    echo "$username not exist"
fi
adduser $username
echo $password | passwd $username --stdin > /dev/null
echo "add user finished"
	

