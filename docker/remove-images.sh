#!/bin/bash
cd $(dirname $0)
. ../common.sh

docker images -a | grep $PROJECT_KEY | awk '{print $1}' | xargs docker rmi -f

