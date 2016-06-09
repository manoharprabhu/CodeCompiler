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
java -jar codecompiler.jar -mongodatabase DATABASE_NAME -mongohost DATABASE_ADDRESS
```

For running the Compiler service 
```java
java -jar dockerconsumercompiler.jar -mongodatabase codecompiler -mongohost localhost -rmqhost localhost
```
