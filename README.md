[![Build Status](https://travis-ci.org/manoharprabhu/CodeCompiler.svg?branch=master)](https://travis-ci.org/manoharprabhu/CodeCompiler)
[![codecov](https://codecov.io/gh/manoharprabhu/CodeCompiler/branch/master/graph/badge.svg)](https://codecov.io/gh/manoharprabhu/CodeCompiler)
# CodeCompiler
A service to run code snippets and get output

#### Requirements
* Ubuntu 14.04
* RabbitMQ server
* MongoDB server
* Java 7
* GCC for compiling input C programs

#### Usage
For running the Endpoint service
```java
java -jar codecompiler.jar -mongodatabase DATABASE_NAME -mongohost DATABASE_ADDRESS -rmqhost RMQ_ADDRESS
```

For running the Compiler service 
```java
java -jar dockerconsumercompiler.jar -mongodatabase DATABASE_NAME -mongohost DATABASE_ADDRESS -rmqhost RMQ_ADDRESS
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
