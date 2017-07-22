#!/bin/bash
cd $(dirname $0)
. ../common.sh

echo "Building $DOC_MYSQL image ..."
docker build -t $DOC_MYSQL -f ./mysql-build.txt .
echo "Build $DOC_MYSQL image is done"
