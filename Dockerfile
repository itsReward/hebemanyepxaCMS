FROM amazoncorretto:17-alpine

WORKDIR /app

# Install dependencies
RUN apk add --no-cache bash

# Copy Gradle files first for better layer caching
COPY gradle gradle
COPY gradlew gradlew
COPY build.gradle.kts settings.gradle.kts ./

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Run Gradle wrapper to verify it works and download dependencies
RUN ./gradlew --version

# Copy source code
COPY src src

# Create upload directory and make it writable
RUN mkdir -p /opt/render/project/uploads && chmod -R 777 /opt/render/project/uploads

# Build the application with more detailed output
RUN ./gradlew bootJar --info

# Verify the JAR file exists
RUN ls -la build/libs/

# Set the JAR file as the entrypoint
ENTRYPOINT ["java", "-jar", "/app/build/libs/hebemanyepxa-0.0.1-SNAPSHOT.jar"]