[[ -z "$1" ]] && IMAGE="ubuntu:trusty" || IMAGE="$1"
[[ -z "$2" ]] && CMD="sleep 200" || CMD="${@:2}"
echo Running $CMD on $IMAGE image ...
docker run -d $IMAGE bash -c "$CMD"
