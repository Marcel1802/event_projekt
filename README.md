# Running the project

Docker containers (PostgreSQL DB & Keycloak Auth server): `docker compose up -d` (root directory)

Quarkus (Java) backend: `.\mvnw clean quarkus:dev` (root directory)

Angular (TypeScript) frontend: `ng serve` (in .\src\main\event-frontend)
