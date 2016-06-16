#!/bin/bash
echo "Building all the required images. Please wait..."
BUILD_IMAGES=`./build_docker_images.sh`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the docker images properly"; echo $BUILD_IMAGES; exit $rc; fi

#Start the rabbitmq container
echo "Starting the rabbitmq container"
RABBITMQ_CONTAINER_ID=`docker run -d -p 5672:5672 -p 15672:15672 dockerfile/rabbitmq`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not run the rabbitmq container"; exit $rc; fi
echo "Started rabbitmq container with ID as $RABBITMQ_CONTAINER_ID"
RABBITMQ_CONTAINER_ADDRESS=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' "$RABBITMQ_CONTAINER_ID"`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not get the IP address of rabbitmq container"; exit $rc; fi
echo "IP Address of rabbitmq container is $RABBITMQ_CONTAINER_ADDRESS"

#Start the mongodb container
echo "Starting the mongodb container"
MONGODB_CONTAINER_ID=`docker run -d -p 27017:27017  dockerfile/mongodb`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not run the mongodb container"; exit $rc; fi
echo "Started mongodb container with ID as $MONGODB_CONTAINER_ID"
MONGODB_CONTAINER_ADDRESS=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' "$MONGODB_CONTAINER_ID"`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not get the IP address of mongodb container"; exit $rc; fi
echo "IP Address of mongodb container is $MONGODB_CONTAINER_ADDRESS"

#Start the endpoint container
echo "Starting the endpoint container"
ENDPOINT_CONTAINER_ID=`docker run -d -p 8080:8080 manoharprabhu/codecompiler-endpoint java -jar codecompiler.jar -mongodatabase codecompiler -mongohost "$MONGODB_CONTAINER_ADDRESS" -rmqhost "$RABBITMQ_CONTAINER_ADDRESS"`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not run the endpoint container"; exit $rc; fi
echo "Started endpoint container with ID as $ENDPOINT_CONTAINER_ID"
ENDPOINT_CONTAINER_ADDRESS=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' "$ENDPOINT_CONTAINER_ID"`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not get the IP address of endpoint container"; exit $rc; fi
echo "IP Address of endpoint container is $ENDPOINT_CONTAINER_ADDRESS"

NUMBER_OF_CONSUMER_INSTANCES=$1
#Start the consumer container
echo "Starting $NUMBER_OF_CONSUMER_INSTANCES consumer container instances"
COUNTER=0
PORT=8089
while [ $COUNTER -lt $NUMBER_OF_CONSUMER_INSTANCES ]; do
	CONSUMER_CONTAINER_ID=`docker run -d -p "$PORT":"$PORT" manoharprabhu/codecompiler-consumer java -jar dockerconsumercompiler.jar -mongodatabase codecompiler -mongohost "$MONGODB_CONTAINER_ADDRESS" -rmqhost "$RABBITMQ_CONTAINER_ADDRESS"`
	rc=$?; if [[ $rc != 0 ]]; then echo "Could not run the consumer container instance $COUNTER"; exit $rc; fi
	echo "Started consumer container with ID as $CONSUMER_CONTAINER_ID"
	CONSUMER_CONTAINER_ADDRESS=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' "$CONSUMER_CONTAINER_ID"`
	rc=$?; if [[ $rc != 0 ]]; then echo "Could not get the IP address of consumer container instance $COUNTER"; exit $rc; fi
	echo "IP Address of endpoint container $COUNTER is $CONSUMER_CONTAINER_ADDRESS"
	let COUNTER=COUNTER+1
	let PORT=PORT+1
done

exit 0

