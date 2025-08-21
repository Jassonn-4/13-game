# Use Java 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (Render will override with $PORT)
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]