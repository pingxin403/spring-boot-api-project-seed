#!/bin/sh
WORKDIR="/data"
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.basedir=${WORKDIR} -Dserver.tomcat.accesslog.enabled=true -Xss256k -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8" && cmd="${JAVA_HOME}/bin/java -jar ${JAVA_OPTS} ${WORKDIR}/app.jar"
$cmd
