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

cd ~

./start_quis_app.sh $PORT
cd ~
# wait to start the app 1
./wait_quis_app_to_start.sh $PORT
if [ $? -ne 0 ]
then
  echo "Too much waiting for start something is not right!!!"
  exit 1
fi

# start worker 1
./apache-balancer.sh enable webfarm http://localhost:$PORT

exit 0