# Multi-stage build for Spring Boot application
FROM maven:3.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 80
EXPOSE 80

# Set Java options for container environment
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application on port 80
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=80 -jar app.jar"]
