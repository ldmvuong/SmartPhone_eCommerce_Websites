# Dùng JDK 17 làm base image
FROM openjdk:17-jdk-slim

# Tạo thư mục /app trong container
WORKDIR /app

# Copy file JAR từ thư mục target (sau khi build)
COPY target/*.jar app.jar

# Mở port 8080 (khớp với server.port trong app)
EXPOSE 8080

# Lệnh chạy Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]