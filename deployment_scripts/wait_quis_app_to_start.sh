#!/bin/sh
PORT=$1
currentTime=$(date '+%Y-%m-%d %H:%M')
# wait spring application to start about 30 sec
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

   if [[ ! -z `awk "/^$currentTime/,/^2999-01-01 00:00:00/" ~/quis-app/logs/quis$PORT.log | grep "Started QuisListingApp in"` ]]; then
        success=1
        break
    else
        echo "($elapsed/$timeout) Waiting for deployment to complete..."
    fi
done

if [ $elapsed -ge $timeout ]; then
    echo "Deployment timed out!"
fi

if [ $success -eq 0 ]; then
    echo "Deployment failed!"
    exit 1
fi

echo "Deployment completed!"
exit 0