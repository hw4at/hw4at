#!/bin/bash
cd $(dirname $0)

./kill-all.sh

./create-network.sh

./db-run.sh &

[[ -n "$1" ]] && ../server/pack.sh

rm -f ../server/log4j.log

./server-run.sh &

./db-wait.sh

./db-init.sh

./server-wait.sh

tail -f ../server/log4j.log

