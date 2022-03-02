#!/usr/bin/env bash

PSQL=psql
USER=$1
HOST=$3
PORT=$4

export PGPASSWORD=$2
start=$(date +%s)

$PSQL -h $HOST -U $USER -p $PORT -f ./tpch_pgsql.sql

end=$(date +%s)
take=$(( end - start ))
echo Time taken to load tpch data is ${take} seconds.
unset PGPASSWORD
