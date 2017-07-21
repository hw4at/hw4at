#!/bin/bash
cd $(dirname $0)

setup_git() {
  echo "Start GIT setup ..."
  echo "GIT setup is done"
}

setup_scala() {
  echo "Start SCALA setup ..."
  sudo apt-get install -y scala
  echo "SCALA setup is done"
}

setup_sbt() {
  echo "Start SBT setup ..."
  echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
  sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
  sudo apt-get update
  sudo apt-get install -y sbt
  echo "SBT setup is done"
}

setup() {
  setup_git
  setup_sbt
  ./docker/build-all.sh
}

