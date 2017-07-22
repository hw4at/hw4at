sudo $1 /var/lib/docker/containers/$(docker ps -a -f name=$2 -q --no-trunc)/$(docker ps -a -f name=$2 -q --no-trunc)-json.log
