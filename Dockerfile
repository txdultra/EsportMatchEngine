FROM openjdk:8u151-jdk-alpine
VOLUME /tmp:/tmp
ADD api/target/api-0.0.1-SNAPSHOT.jar app.jar
RUN /bin/sh -c 'touch /app.jar'
EXPOSE 8088
ENTRYPOINT ["java","-jar","/app.jar"]