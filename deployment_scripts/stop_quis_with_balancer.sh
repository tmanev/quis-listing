#!/bin/sh
PORT=$1
cd ~

# stop worker 1
./apache-balancer.sh disable webfarm http://localhost:$PORT
./stop_quis_app.sh $PORT
./wait_quis_app_to_stop.sh $PORT
if [ $? -ne 0 ]
then
  echo "Too much waiting to shutdown something is not right!!!"
  exit 1
fi