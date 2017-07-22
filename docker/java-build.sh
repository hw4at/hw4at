#!/bin/bash
cd $(dirname $0)
. ../common.sh

echo "Building $DOC_JAVA image ..."
docker build -t $DOC_JAVA -f ./java-build.txt .
echo "Build $DOC_JAVA image is done"

