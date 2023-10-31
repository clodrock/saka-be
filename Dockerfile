FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn clean package -DskipTests

# Step : Package image
FROM openjdk:17-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081 8000
ENTRYPOINT ["java", "-jar" , "app.jar"]