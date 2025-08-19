# Stage 1: Build the JAR
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Run the app
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/rca-mis-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
