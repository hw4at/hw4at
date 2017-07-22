#!/bin/bash
cd $(dirname $0)

for file in ./*-build.sh; do
  $file
done
