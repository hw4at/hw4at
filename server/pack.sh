#!/bin/bash
cd $(dirname $0)

rm -f $SERVER_JAR

sbt assembly

