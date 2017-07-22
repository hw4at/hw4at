#!/bin/bash
cd $(dirname $0)

[[ -n "$1" ]] && ../server/pack.sh

./kill-all.sh

./create-network.sh

./db-run.sh
./db-wait.sh
./db-init.sh

rm -f ../server/log4j.log
./server-run.sh
./server-wait.sh
tail -f ../server/log4j.log

