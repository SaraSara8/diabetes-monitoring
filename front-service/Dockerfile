FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/front-service-0.0.1-SNAPSHOT.jar front-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","front-service.jar"]