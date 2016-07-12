FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-8-jdk
RUN mkdir -p "/codecompiler"
WORKDIR "/codecompiler"
COPY build/libs/endpoint.jar .
EXPOSE 8080
