#!/bin/sh
PORT=$1
# wait spring application to shutdown about 30 sec
echo "Sleeping a bit"
sleep 5
set +x
timeout=240
delay=3
elapsed=0
success=0
response=000

while [ $elapsed -le $timeout ]; do
   sleep $delay
   elapsed=$((elapsed+delay))

   if [[ -z `ps -ef | grep server.port=$PORT | grep -v grep | awk '{print $2}'` ]]; then
        success=1
        break
    else
        echo "($elapsed/$timeout) Waiting for shutdown to complete..."
    fi
done

if [ $elapsed -ge $timeout ]; then
    echo "Shuting down timed out!"
fi

if [ $success -eq 0 ]; then
    echo "Shuting down failed!"
    exit 1
fi

echo "Shuting down completed!"
exit 0