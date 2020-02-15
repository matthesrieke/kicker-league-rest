# ---- Base maven ----
FROM gradle:6.1.1-jdk8 AS base

# prepare the source files for build
RUN mkdir /tmp/app
COPY . /tmp/app

# global install bower and grunt
RUN cd /tmp/app && gradle build -x test

# find the JAR file and move it
RUN bash -c 'find /tmp/app/build/libs -maxdepth 1 -size +1048576c | grep jar | xargs -I{} mv {} /app.jar'

# now the runnable image
FROM adoptopenjdk/openjdk8:alpine

# copy over the dist from the base build image
COPY --from=base /app.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

EXPOSE 8080
