[![Build Status](https://travis-ci.org/manoharprabhu/CodeCompiler.svg?branch=master)](https://travis-ci.org/manoharprabhu/CodeCompiler)


[![codecov](https://codecov.io/gh/manoharprabhu/CodeCompiler/branch/master/graph/badge.svg)](https://codecov.io/gh/manoharprabhu/CodeCompiler) 
Working on improving the code coverage...

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/56505c57d693478d902980b7d6153ff9)](https://www.codacy.com/app/manoharagent13/CodeCompiler?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=manoharprabhu/CodeCompiler&amp;utm_campaign=Badge_Grade)


# CodeCompiler
A service to run code snippets and get output

#### Requirements
* Ubuntu 14.04
* RabbitMQ server
* MongoDB server
* Java 7
* GCC for compiling input C programs
* nodejs for running javascript programs
* Docker for running the containers

#### Deploy all the services automatically on local system
````bash
./start_containers.sh NO_OF_INSTANCES_OF_CONSUMER
````
Build the endpoint and consumer JAR files, create docker images out of it, build the RMQ and MongoDB images, and start the containers with appropriate input parameters automatically.
**NO_OF_INSTANCES_OF_CONSUMER** number of consumer containers will be started.

#### Deploy the Frontend service
Goto the Frontend project and edit the src/js/configuration.js file.
Change the "serverHost" and "serverPort" property to point to the Endpoint properties.

Goto the root of Frontend project and run the following
````bash
gulp minify
````
Deploy the contents of the "public" folder on a webserver.


#### Usage
For running the Endpoint service
```java
java -jar Endpoint/build/libs/endpoint.jar -mongodatabase DATABASE_NAME -mongohost DATABASE_ADDRESS -rmqhost RMQ_ADDRESS
```

For running the Compiler service 
```java
java -jar DockerConsumerCompiler/build/libs/dockerconsumercompiler.jar -mongodatabase DATABASE_NAME -mongohost DATABASE_ADDRESS -rmqhost RMQ_ADDRESS
```
#### Endpoints exposed by the endpoint service
----
 POST | :8080/codecompiler/submit |
|------|---------------------------|

| Parameter  | Description                                                        |
|------------|--------------------------------------------------------------------|
| program    | The program to be compiled and run.                                |
| input      | The input that will be fed to the program STDIN.                   |
| timeout    | Number of seconds the program can run before it is killed off.     |
| language   | The language of submitted program.                                 |
Returns Queue ID of the submitted program. This ID can be used for future reference to check the program status.
````JSON
{
    "data": {
        "queueId": 1107894191825737787218556533052298445977
    },
    "timestamp": 1465762230
}
````
----
 GET | :8080/codecompiler/status |
|------|---------------------------|

| Parameter  | Description                                                        |
|------------|--------------------------------------------------------------------|
| queueId    | The Queue ID of the submitted program to be checked.               |
Returns the status of the submitted program, result, and error if any.
````JSON
{
    "data": {
        "errorMessage": null,
        "output": "34\n5\n21",
        "programStatus": 6,
        "submittedDate": 1465760000
    },
    "timestamp": 1465762230
}
````
| Attribute       | Description                                                                                         |
|-----------------|-----------------------------------------------------------------------------------------------------|
| errorMessage    | A human readable error message encountered if any, during the compilation or execution phase.       |
| output          | Output of the submitted program if it runs successfully.                                            |
| programStatus   | One of the seven integers as defined below.                                                         |
| submittedDate   | The time at which the program was submitted.                                                        |

| programStatus value       | programStatus Description |
|---------------------------|---------------------------|
| 0 | No such program found in queue.                   |
| 1 | Program in queue for compilation.                 |
| 2 | Program is getting processed.                     |
| 3 | Program compilation error.                        |
| 4 | Program timed out while running.                  |
| 5 | Program crashed/returned non zero exit value.     |
| 6 | Program completed running successfully.           |
----

#### Running the system tests
Requirements
* NodeJS
* Mocha
* Endpoint and the Consumer services should be running on local machine

````bash
cd SystemTest
npm install
mocha app.js
````
