FROM amazoncorretto:17-alpine

WORKDIR /app

# Copy Gradle wrapper files
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Make gradlew executable
RUN chmod +x ./gradlew

# Download dependencies
RUN ./gradlew dependencies

# Copy source code
COPY src src

# Create upload directory and make it writable
RUN mkdir -p /opt/render/project/uploads && chmod -R 777 /opt/render/project/uploads

# Build the application
RUN ./gradlew bootJar

# Set the JAR file as the entrypoint
ENTRYPOINT ["java", "-jar", "/app/build/libs/hebemanyepxa-0.0.1-SNAPSHOT.jar"]