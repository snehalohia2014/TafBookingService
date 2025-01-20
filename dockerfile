# Use a base image with JDK 17 (or another version if needed)
FROM openjdk:17-jdk-slim AS build

# Set working directory inside the container
WORKDIR /app

# Copy the jar file to the container
COPY build/libs/bookingService.jar app.jar

# Expose the port your Spring Boot application will run on
EXPOSE 8084

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
