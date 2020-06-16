FROM adoptopenjdk/openjdk11:alpine-jre

# https://spring.io/guides/gs/spring-boot-docker/

LABEL author czetsuya@gmail.com

# Refer to Maven build -> finalName
ARG JAR_FILE=./keycloak-rest-admin-api.jar

RUN addgroup -S tomcat && adduser -S tomcat -G tomcat
USER tomcat

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
# Use sh to support JAVA_OPTS
# Use ${@} to pass all command arguments
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar ${0} ${@}"]