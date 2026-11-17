# Microservices Task

A simple microservices application consisting of two Spring Boot services and a PostgreSQL database.

## Architecture

The application consists of two independent services:

- **AuthApi (Service A)** — handles user registration and authentication, validates JWT tokens, processes requests, communicates with DataApi, and stores processing logs in PostgreSQL.
- **DataApi (Service B)** — performs text transformations and accepts requests only when a valid internal token is provided.
- **PostgreSQL** — stores users and processing logs.

### Communication flow

1. A client registers or logs in through AuthApi.
2. AuthApi returns a JWT access token.
3. The client sends a processing request with the JWT token.
4. AuthApi validates the JWT and forwards the request to DataApi.
5. AuthApi includes the `X-Internal-Token` header in the internal request.
6. DataApi validates the internal token and transforms the text.
7. AuthApi saves the input, output, user ID, and timestamp in PostgreSQL.
8. AuthApi returns the result to the client.

## Project Structure

```text
microservices-task/
├── AuthApi/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── DataApi/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
├── .env
├── .env.example
└── README.md
```

## Technologies

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- JWT
- BCrypt
- Maven
- Docker and Docker Compose

## Prerequisites

Make sure the following tools are installed:

- Java 21
- Maven
- Docker
- Docker Compose

## Configuration

Create a `.env` file in the project root directory.

Example:

```dotenv
POSTGRES_DB=auth_db
POSTGRES_USER=ilyaleonov
POSTGRES_PASSWORD=change_me_to_a_strong_password

POSTGRES_URL=jdbc:postgresql://postgres:5432/auth_db

JWT_SECRET=replace_with_a_long_random_secret
JWT_EXPIRATION=86400000

INTERNAL_TOKEN=replace_with_a_different_random_secret
```

Replace the placeholder values with your own configuration.

**Important:** Do not commit the actual `.env` file or expose passwords, JWT secrets, or internal tokens. Use `.env.example` with placeholder values when sharing the project.

## Running the Application

Run all commands from the project root directory.

### 1. Build the services

Build AuthApi:

```bash
mvn -f AuthApi/pom.xml clean package -DskipTests
```

Build DataApi:

```bash
mvn -f DataApi/pom.xml clean package -DskipTests
```

### 2. Start the application

```bash
docker compose up -d --build
```

### 3. Check running containers

```bash
docker compose ps
```

You should see three running services:

- `postgres`
- `auth-api`
- `data-api`

### 4. View logs

AuthApi:

```bash
docker compose logs -f auth-api
```

DataApi:

```bash
docker compose logs -f data-api
```

PostgreSQL:

```bash
docker compose logs -f postgres
```

## API Endpoints

AuthApi is available at:

```text
http://localhost:8080
```

DataApi is available at:

```text
http://localhost:8081
```

### 1. Register a user

**Endpoint:**

```http
POST /api/auth/register
```

**Request:**

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Example:**

```bash
curl -i -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

**Expected result:** `201 Created`

The user's password is stored as a BCrypt hash.

### 2. Login

**Endpoint:**

```http
POST /api/auth/login
```

**Request:**

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Example:**

```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

**Expected result:** `200 OK`

**Example response:**

```json
{
  "token": "your-jwt-token"
}
```

### 3. Process text

**Endpoint:**

```http
POST /api/process
```

**Authorization:** Bearer JWT token required.

**Request:**

```json
{
  "text": "Hello Two Microservices"
}
```

First, save the JWT token returned by the login endpoint:

```bash
TOKEN="your-jwt-token"
```

Then send a processing request:

```bash
curl -i -X POST http://localhost:8080/api/process \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"text":"Hello Two Microservices"}'
```

**Expected result:** `200 OK`

**Example response:**

```json
{
  "result": "secivresorciM owT olleH"
}
```

The text is reversed by DataApi. AuthApi then saves the processing result in PostgreSQL.

### 4. Test DataApi directly

DataApi requires the `X-Internal-Token` header.

**Endpoint:**

```http
POST /api/transform
```

#### Request without an internal token

```bash
curl -i -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -d '{"text":"hello"}'
```

**Expected result:** `403 Forbidden`

#### Request with an invalid internal token

```bash
curl -i -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -H "X-Internal-Token: wrong-token" \
  -d '{"text":"hello"}'
```

**Expected result:** `403 Forbidden`

#### Request with a valid internal token

Use the value of `INTERNAL_TOKEN` from your local `.env` file:

```bash
curl -i -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -H "X-Internal-Token: YOUR_INTERNAL_TOKEN" \
  -d '{"text":"hello"}'
```

**Expected result:** `200 OK`

The response should contain the transformed text.

## Database Verification

Connect to the PostgreSQL container:

```bash
docker exec -it postgres psql -U ilyaleonov -d auth_db
```

If you changed the database name or username in `.env`, use those values instead.

List database tables:

```sql
\dt
```

Check registered users:

```sql
SELECT id, email FROM users;
```

Check processing logs:

```sql
SELECT
    id,
    user_id,
    input_text,
    output_text,
    created_at
FROM processing_logs
ORDER BY created_at DESC;
```

Count processing records:

```sql
SELECT COUNT(*) FROM processing_logs;
```

Exit PostgreSQL:

```sql
\q
```

## Security

- User passwords are hashed using BCrypt.
- Protected endpoints require a valid JWT.
- DataApi validates the internal token on every transformation request.
- Sensitive configuration values are provided through environment variables.
- Secrets and credentials should not be committed to version control.

## Acceptance Checklist

- [ ] Both Spring Boot services build successfully.
- [ ] PostgreSQL starts successfully.
- [ ] All services run through Docker Compose.
- [ ] A user can register successfully.
- [ ] A registered user can log in and receive a JWT.
- [ ] Protected endpoints reject requests without a valid JWT.
- [ ] AuthApi successfully communicates with DataApi.
- [ ] DataApi rejects requests without an internal token.
- [ ] DataApi rejects requests with an invalid internal token.
- [ ] DataApi processes requests with a valid internal token.
- [ ] AuthApi saves processing logs in PostgreSQL.
- [ ] Flyway migrations are applied successfully.
