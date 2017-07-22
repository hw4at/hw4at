#!/bin/bash
cd $(dirname $0)

rm -f log4j.log

java -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4jLogDelegateFactory -jar target/surl-server-fat.jar
