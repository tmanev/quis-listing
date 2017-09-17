#!/bin/sh
PORT=$1
cd ~/quis-app
echo "--- Starting QuisListing on port: $PORT BEGIN..."
nohup java -Djava.security.egd=file:/dev/./urandom -Xmx384m -jar -Dspring.profiles.active=prod ~/artifacts/quis-listing.war --server.port=$PORT --spring.mail.password=your_pass --spring.datasource.password=your_pass --logging.file=./logs/quis$PORT.log > /dev/null 2>&1 &
echo "--- Finish Starting QuisListing on port: $PORT END"