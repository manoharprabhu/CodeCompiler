#!/bin/bash

#Generate the JAR files from endpoint service

echo "Building Endpoint JAR..."
GENERATE_ENDPOINT=`./gradlew clean build`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the endpoint JAR"; exit $rc; fi
echo "Endpoint JAR built"

#Build codecompiler endpoint service here
echo "Building Endpoint docker image..."
ENDPOINT_BUILD=`docker build -t manoharprabhu/codecompiler-endpoint .`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the endpoint image properly"; exit $rc; fi
echo "Built Endpoint docker image successfully"

#Generate the JAR files from consumer service
echo "Building Consumer JAR..."
GENERATE_CONSUMER=`./gradlew -q -p DockerConsumerCompiler/ clean build`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the consumer JAR"; exit $rc; fi
echo "Consumer JAR built"

#Build codecompiler consumer service here
echo "Building Consumer docker image..."
CONSUMER_BUILD=`docker build -t manoharprabhu/codecompiler-consumer ./DockerConsumerCompiler/`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the consumer image properly"; exit $rc; fi
echo "Built Consumer docker image successfully"

#Build the mongoDB image
echo "Building mongoDB image..."
MONGODB_BUILD=`docker build -t dockerfile/mongodb ./mongodb/`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the mongodb image properly"; exit $rc; fi
echo "Built Mongodb docker image successfully"

#Build the rabbitmq image
echo "Building rabbitmq image..."
RABBITMQ_BUILD=`docker build -t dockerfile/rabbitmq ./rabbitmq/`
rc=$?; if [[ $rc != 0 ]]; then echo "Could not build the rabbitmq image properly"; exit $rc; fi
echo "Built Rabbitmq docker image successfully"

exit 0;
