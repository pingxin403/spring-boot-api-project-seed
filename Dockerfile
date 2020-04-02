FROM openjdk:8-jdk-alpine
MAINTAINER hyp
LABEL app="spring-boot-api-project-seed" version="1.0.0" by="hyp"
COPY ./target/spring-boot-api-project-seed-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Xms10m -Xmx128m","-jar","/app.jar"]