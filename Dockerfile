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
RUN ./gradlew build -x test

# Set the JAR file as the entrypoint
# Use the specific JAR name that includes the version
ENTRYPOINT ["java", "-jar", "/app/build/libs/hebemanyepxa-0.0.1-SNAPSHOT.jar"]