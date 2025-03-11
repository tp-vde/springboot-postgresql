# Étape 1 : Utiliser une image de base contenant Java 21
FROM openjdk:21-jdk-slim

# Étape 2 : Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le fichier JAR généré par Maven
COPY target/*.jar app.jar

# Étape 4 : Exposer le port utilisé par Spring Boot
EXPOSE 8080

# Étape 5 : Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
