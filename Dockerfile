# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Copy .env file into the correct directory inside the container
COPY etc/secrets/.env /etc/secrets/.env 

# Install Maven and build the project
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests \
    && mv target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]