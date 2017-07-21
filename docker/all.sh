#!/bin/bash
cd $(dirname $0)

if [ $# -eq 0 ]; then
  exit 1 
fi 
 
echo "$1 all containers ..."
docker $1 `docker ps --no-trunc -aq`
echo "$1 for all containers is done"

