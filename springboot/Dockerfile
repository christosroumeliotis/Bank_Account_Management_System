# ===== Stage 1: Build the application =====
FROM maven:3.9.9-amazoncorretto-17 AS build
WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY src ./src

# Build the project (skip tests for faster build)
RUN mvn clean package -DskipTests

# ===== Stage 2: Run the application =====
FROM amazoncorretto:17
WORKDIR /app

# Copy only the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]