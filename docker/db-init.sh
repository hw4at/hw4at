#!/bin/bash
cd $(dirname $0)
. ../common.sh

echo "Initializing the DB ..."

./db-exe.sh "CREATE DATABASE IF NOT EXISTS $DB_SCHEMA"
./db-pipe.sh $DB_SCHEMA < ./db-init.sql

echo "Initialize the DB is done"

