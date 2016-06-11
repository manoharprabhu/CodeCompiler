FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-8-jdk
RUN apt-get install -y gcc
RUN mkdir -p "/codecompiler"
WORKDIR "/codecompiler"
COPY build/libs/dockerconsumercompiler-0.1.0.jar .

