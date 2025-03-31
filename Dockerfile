

# Étape 1: Utiliser une image Java optimisée
FROM openjdk:17-jdk-slim

# Étape 2: Définir le répertoire de travail
WORKDIR /app

# Étape 3: Copier l'application
COPY target/*.jar app.jar

# Étape 4: Copier le fichier Logback (assurez-vous qu'il existe)
COPY src/main/resources/logback-spring.xml /app/logback-spring.xml

# Étape 5: Exposer le port 8080
EXPOSE 8080

# Étape 6: Définir la commande d'exécution
ENTRYPOINT ["java", "-Dlogging.config=/app/logback-spring.xml", "-jar", "app.jar"]

RUN mkdir -p /logs && chmod 777 /logs





