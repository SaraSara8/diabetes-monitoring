# Utilise une image de base avec OpenJDK 17 (version slim pour alléger l'image)
FROM openjdk:17-jdk-slim

# Définit un volume pour stocker des fichiers temporaires (optionnel)
VOLUME /tmp

# Déclare un argument JAR_FILE correspondant au fichier jar généré par Maven
ARG JAR_FILE=target/risk-assessment-service-0.0.1-SNAPSHOT.jar

# Copie le jar dans l'image sous le nom app.jar
COPY ${JAR_FILE} app.jar

# Expose le port sur lequel le microservice écoute (ici, on suppose le port 8084)
EXPOSE 8084

# Définit le point d'entrée pour lancer l'application Java
ENTRYPOINT ["java", "-jar", "/app.jar"]
