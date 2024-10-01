# Kitchen Sink Application Migrated to Spring Boot

## Overview
This is the migrated version of the JBoss EAP Kitchen Sink application, now built with Spring Boot (Java 21) and using MongoDB as the database.

## Installation
- Java 21
- MongoDB (local)
- Maven

## Configuration
Install MongoDB and configure like environment variable system (windows) 

1. Start the MongoDB local server:
   ```bash
      mongod
   ```

2. Using MongoDB Shell:
   ```bash
      mongosh
   ```

3. Using MongoDB Compass

   You can see the connections and create database

4. Update application property files

   In every properties files you need to change the property with local path of mongoDB like this example:
   ```bash
   spring.data.mongodb.uri=mongodb://localhost:27017/kitchensinkdb
   ```

## Compile
1. Execute this command in the root directory project to generate war packages in target/ directory:
```bash
mvn clean install
```

## Deploy
Execute this command in the root directory project:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=<dev|qa|prod>
```
   * Url develop: http://localhost:8081/api/people
   * Url qa: http://localhost:8082/api/people
   * Url production: http://localhost:8083/api/people

## Unit Testing
```bash
   mvn test
```

## Swagger
   * Load the Swagger page from the local path:
      http://localhost:<port-of-profile>/swagger-ui/index.html

      The port number of the Swagger url is according to profile dev (8081) or qa (8082) or prod (8083).
## Environment Setup
1. Clone this repository:
```bash
   git clone <repository-url>
   cd kitchensink
```

## Postman
 * Upload the Kitchen-Sink.postman_collection.json file from the root directory project

