#!/bin/bash

DIR=/tmp/surl
rm -rf $DIR
mkdir $DIR
cd $DIR

cp -f ~/surl/server/target/scala-2.12/surl-server.jar .

unzip surl-server.jar

