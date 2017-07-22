#!/bin/bash
cd $(dirname $0)

rm -f $SERVER_JAR

mvn clean package

