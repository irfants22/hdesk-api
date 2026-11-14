# API

Spring Boot RESTful API.

## How to run

1. Create database: `CREATE DATABASE myapi_db;`
2. Create a JWT secret: `openssl rand -base64 32`
3. Copy `.env.example` into `.env`, then fill in the values.
4. Run the project: `./mvnw spring-boot:run`

Database migrations are executed automatically by Flyway when the application starts.
