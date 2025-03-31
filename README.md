# springboot-postgresql

#  Spring Boot PostgreSQL Backend

##  Description du projet
Ce projet est une **API REST backend** développée avec **Spring Boot**, utilisant **PostgreSQL** 
comme base de données.  

L'application est conteneurisée avec **Docker** et inclut **pgAdmin** pour l'administration de la base.

---

## **Structure du projet**
springboot-postgresql/
│── src/
│   ├── main/
│   │   ├── java/com/springbootTP/springbootPostgreSQL/
│   │   │   ├── controller/      #  Gère les requêtes HTTP
│   │   │   ├── model/           #  Définit les entités JPA
│   │   │   ├── repository/      #  Accès aux données avec JPA/Hibernate
│   │   │   ├── service/         #  Contient la logique métier
│   │   │   ├── security/          #  Fichiers de configuration (sécurité JWT)
│   │   │   ├── SpringbootPostgreSQLApplication.java  #  Classe principale
│   │   ├── resources/
│   │   │   ├── application.properties  #  Configuration Spring Boot
│   ├── test/                      #  Tests unitaires et d'intégration
│── target/                         #  Dossier contenant le JAR généré après compilation
│── Dockerfile                      #  Image Docker pour exécuter le backend
│── docker-compose.yml               #  Docker Compose pour PostgreSQL et Spring Boot
│── pom.xml                          #  Fichier de configuration Maven
│── README.md                        #  Documentation du projet
│── mvnw / mvnw.cmd                   #  Wrapper Maven (permet d'exécuter Maven sans l'installer)
│── .gitignore                        #  Exclut les fichiers inutiles dans Git



##  **Technologies utilisées**
- **Java 21**
- **Spring Boot 3**
- **PostgreSQL**
- **Liquibase** (migrations de base de données)
- **Docker & Docker Compose**
- **pgAdmin** (gestion de la base de données)
- **Maven** (gestion des dépendances et du build)

---

##  **Démarrage de l'application**

- Démarrer avec Docker : **docker-compose up --build -d**


## **Accéder à l'application**

- API REST (Spring Boot) : http://localhost:8080
- pgAdmin : http://localhost:8082
       - Email : admin@example.com
       - Mot de passe : admin
  - Base de données PostgreSQL :
       - Host : postgres
       - Port : 5432 
       - User : postgres 
       - Password : password 
       - Database : vde_database

## **Arrêter l'application**

- Arréter avec docker : **docker-compose down**

- Ou pour arrêter uniquement l'application sans stopper PostgreSQL : **docker stop vde_springboot**

- Pour voir les loggs : **docker logs vde_springboot**