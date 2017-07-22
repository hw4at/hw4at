#!/bin/bash
cd $(dirname $0)

until echo show databases | ./db-pipe.sh &> /dev/null
do
  echo "Waiting for the DB to be ready ..."
  sleep 3
done
