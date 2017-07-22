#!/bin/bash
cd $(dirname $0)

. ./server/config/surl.properties

exe() {
  "$@" |& tee /tmp/exe.log
}

