FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-8-jdk
RUN apt-get install -y gcc
RUN apt-get install -y nodejs
RUN ln -s /usr/bin/nodejs /usr/bin/node
RUN mkdir -p "/codecompiler"
WORKDIR "/codecompiler"
COPY build/libs/dockerconsumercompiler.jar .

