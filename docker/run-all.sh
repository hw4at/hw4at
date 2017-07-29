#!/bin/bash
cd $(dirname $0)

./kill-all.sh

[[ -n "$1" ]] && ../server/pack.sh

./create-network.sh

./db-run.sh
./db-wait.sh
./db-init.sh

rm -f ../server/log4j.log
touch ../server/log4j.log
./server-run.sh

docker ps

[[ -n "$1" ]] && tail -f ../server/log4j.log

