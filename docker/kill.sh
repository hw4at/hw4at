#!/bin/bash
cd $(dirname $0)

if [ $# -eq 0 ]; then
  exit 1
fi

echo "Removing $1 container ..."
docker rm -f $1
echo "Remove $1 container is done"

