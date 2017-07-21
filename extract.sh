#!/bin/bash

cd /tmp/surl
rm -rf * .*

cp -f ~/surl/server/target/scala-2.12/surl-server.jar .

unzip surl-server.jar

