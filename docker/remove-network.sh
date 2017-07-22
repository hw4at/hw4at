#!/bin/bash
cd $(dirname $0)
. ../common.sh

if `docker network ls | grep $DOC_NET >& /dev/null` ; then
  echo "Removing $DOC_NET network on docker ..."
  docker network rm $DOC_NET
  echo "Remove $DOC_NET network on docker is done"
fi

