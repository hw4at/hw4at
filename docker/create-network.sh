#!/bin/bash
cd $(dirname $0)
. ../common.sh

if ! `docker network ls | grep $DOC_NET >& /dev/null` ; then
  echo "Creating $DOC_NET network on docker ..."
  docker network create $DOC_NET
  echo "Create $DOC_NET network on docker is done"
fi

