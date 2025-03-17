# Use an official OpenJDK 21 runtime as a parent image
FROM eclipse-temurin:21-jdk

LABEL maintainer="Chandra Prakash"
LABEL version="1.0"
LABEL description="Dockerfile to run a Spring Boot application"
LABEL usage="docker build -t <image-name> ."
LABEL usage="docker run -p 8080:8080 <image-name>"

# Set the working directory inside the container
WORKDIR /app

# Copy the application's JAR file
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]