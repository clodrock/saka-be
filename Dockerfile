# İlk aşama (build aşaması)
FROM openjdk:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean install

# İkinci aşama (runtime aşaması)
FROM openjdk:17-jdk-alpine
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /app/target/saka-0.0.2.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]