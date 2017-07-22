#!/bin/bash

. ~/surl/server/config/surl.properties

exe() {
  "$@" |& tee /tmp/exe.log
}

