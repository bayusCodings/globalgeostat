FROM openjdk:17-jdk-alpine

MAINTAINER ogunbayo.abayo@gmail.com

#create a working directory inside the image
ENV PWD=/usr/app
RUN mkdir -p $PWD

#set current directory
WORKDIR $PWD

#copy all project files into image
COPY . ./

RUN ./gradlew clean build

#This will be the executable to start when the container is booting
ENTRYPOINT [ "java", "-jar", "./build/libs/globalgeostat-0.0.1-SNAPSHOT.jar" ]

#Because our container will be running in an isolated environment,
#with no direct network access, we have to define a mountpoint-placeholder for our configuration repository
VOLUME /var/lib/dev

EXPOSE 8080