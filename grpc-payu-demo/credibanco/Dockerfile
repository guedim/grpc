FROM adoptopenjdk/openjdk14:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} credibanco-0.0.1.jar
EXPOSE 7575
ENTRYPOINT ["java","-jar","/credibanco-0.0.1.jar"]