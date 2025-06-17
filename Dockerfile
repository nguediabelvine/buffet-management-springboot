# Utiliser l'image officielle Eclipse Temurin 17
FROM eclipse-temurin:17-jre

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR de l'application
COPY target/buffet-management-1.0.0.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Commande pour démarrer l'application
CMD ["java", "-jar", "app.jar"] 