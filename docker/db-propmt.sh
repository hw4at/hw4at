#!/bin/bash
cd $(dirname $0)
. ../common.sh

docker exec -it $DOC_DB mysql -u$DB_USER -p$DB_PWD $@

