#!/bin/sh
PORT=$1
QPID=`ps -ef | grep "server.port=$PORT" | grep -v grep | awk '{print $2}'`
if [[ ! -z $QPID ]]; then
    QPID=`ps -ef | grep "server.port=$PORT" | grep -v grep | awk '{print $2}'`
    echo "About to kill gracefully process with pid: $QPID"
    kill -15 $QPID
else
    echo "Applicaion on port: $PORT is not active"
fi
exit 0