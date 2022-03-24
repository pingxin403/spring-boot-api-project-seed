FROM openjdk:11-jre-slim
ARG JAR_FILE
MAINTAINER hyp
LABEL app=${JAR_FILE} version="1.0.0" by="hyp"
VOLUME /tmp
WORKDIR /data
COPY docker-entrypoint.sh /data/entrypoint.sh
COPY target/*.jar /data/app.jar
RUN chmod a+x /data/entrypoint.sh
ENV JAVA_OPTS="-Dspring.profiles.active=test"
EXPOSE 8080
CMD [ "sh", "/data/entrypoint.sh"]