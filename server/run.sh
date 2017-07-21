#!/bin/bash
cd $(dirname $0)
. ../common.sh

scala target/scala-2.12/$SERVER_JAR_NAME

