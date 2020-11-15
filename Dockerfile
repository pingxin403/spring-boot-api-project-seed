FROM openjdk:8-jdk-alpine
ARG JAR_FILE
MAINTAINER hyp
LABEL app=${JAR_FILE} version="1.0.0" by="hyp"
ADD target/${JAR_FILE} /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Xms10m -Xmx128m","-jar","/app.jar"]