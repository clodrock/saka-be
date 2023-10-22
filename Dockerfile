FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/saka-0.0.2.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]