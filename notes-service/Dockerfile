# Dockerfile pour le microservice back (notes-service)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/notes-service-0.0.1-SNAPSHOT.jar notes-service.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","notes-service.jar"]