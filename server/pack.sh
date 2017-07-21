#!/bin/bash
cd $(dirname $0)

rm -f $SERVER_JAR_PATH

sbt compile assembly package

