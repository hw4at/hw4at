#!/bin/bash
cd $(dirname $0)
. ../common.sh

echo "Building $DOC_SCALA image ..."
docker build -t $DOC_SCALA -f ./scala-build.txt .
echo "Build $DOC_SCALA image is done"

