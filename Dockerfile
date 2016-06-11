FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-8-jdk
RUN mkdir -p "/codecompiler"
WORKDIR "/codecompiler"
COPY build/libs/codecompiler-0.1.0.jar .
EXPOSE 8080
