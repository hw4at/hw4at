#!/bin/bash

. ~/surl/server/src/main/resources/surl.properties

exe() {
  "$@" |& tee /tmp/exe.log
}

