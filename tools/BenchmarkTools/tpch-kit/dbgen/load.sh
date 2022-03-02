#!/usr/bin/env bash

MYSQL=mysql
USER=$1
PASSWD=$2
HOST=$3
PORT=$4

start=$(date +%s)

$MYSQL -u$USER -p$PASSWD --host=$HOST --port=$PORT -e 'source ./tpch.sql'

end=$(date +%s)
take=$(( end - start ))
echo Time taken to load tpch data is ${take} seconds.
