FROM amazoncorretto:17-alpine

# Install necessary tools
RUN apk add --no-cache wget

WORKDIR /app

# Copy all necessary files
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src

# Make gradlew executable
RUN chmod +x ./gradlew

# Create upload directory and make it writable
RUN mkdir -p /opt/render/project/uploads && chmod -R 777 /opt/render/project/uploads

# Set gradle user home to avoid permission issues
ENV GRADLE_USER_HOME=/gradle

# Ensure Gradle wrapper is executable and download dependencies with retry
RUN mkdir -p $GRADLE_USER_HOME && \
    chmod +x ./gradlew && \
    ./gradlew --version && \
    ./gradlew dependencies --refresh-dependencies || \
    (sleep 5 && ./gradlew dependencies --refresh-dependencies) || \
    (sleep 10 && ./gradlew dependencies --refresh-dependencies)

# Build the application
RUN ./gradlew clean build -x test --no-daemon

# Set the JAR file as the entrypoint
ENTRYPOINT ["java", "-jar", "/app/build/libs/hebemanyepxa-0.0.1-SNAPSHOT.jar"]