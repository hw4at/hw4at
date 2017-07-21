#!/bin/bash
cd $(dirname $0)

./kill-all.sh

./create-network.sh

./db-run.sh &

../server/pack.sh

./server-run.sh &

./db-wait.sh

./db-init.sh

./server-wait.sh

