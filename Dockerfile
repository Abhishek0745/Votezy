# Use Java 21
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 9091

# Start the application
ENTRYPOINT ["java", "-jar", "target/Votezy-0.0.1-SNAPSHOT.jar"]