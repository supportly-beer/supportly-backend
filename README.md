# supportly-backend

---

### Backend service for the supportly web application. This service is responsible for handling all the business logic and data persistence for the application.

> **Note:** This service is still under development. It is ready for production use, but it's on your own risk.

---

## Technologies used

- Kotlin
- Spring Boot (Data JPA, Web, Mail, Security)
- MySQL
- Gradle
- Docker
- Azure Blob Storage
- gRPC
- Protobuf
- JWT

---

## How to run

### Prerequisites

- Docker
- Docker Compose
- Java 17
- Gradle
- MySQL Database
- Azure Blob Storage
- Google Mail Account

### Environment variables

> **Note:** The environment variables are required for the application to run. The application will fail to start if any
> of the environment variables are missing. Also ensure you have created a database in your MySQL database called
> **supportly**.

- `SUPPORTLY_MAIL_USERNAME` - The username of the google mail account.
- `SUPPORTLY_MAIL_PASSWORD` - The password of the google mail account.
- `SUPPORTLY_MYSQL_URL` - The url of the MySQL database.
- `SUPPORTLY_MYSQL_USER` - The username of the MySQL database.
- `SUPPORTLY_MYSQL_PASSWORD` - The password of the MySQL database.
- `SUPPORTLY_AZURE_CONNECTION_STRING` - The connection string of the Azure Blob Storage.
- `MEILI_MASTER_KEY` - The master key of the MeiliSearch instance.
- `SUPPORTLY_JWT_SECRET` - The secret used to sign the JWT tokens.
- `SUPPORTLY_SEARCH_API_URL` - The url of the MeiliSearch instance.
- `SUPPORTLY_FRONTEND_URL` - The url of the frontend application.
- `SUPPORTLY_CORS` - The CORS configuration for the application (frontend url).

### Running the application

- Run `docker-compose up -d` to start the envoy proxy and the MeiliSearch instance.
- Run `./gradlew bootRun` to start the application.
- The application will be available on port `8080`.
- The envoy proxy will be available on port `9091` and maps to the gRPC service.

