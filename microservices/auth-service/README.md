# Auth Service Microservice

A production-ready authentication microservice for the Hospital Management System, built with Spring Boot 3.2, Spring Security 6, and JWT.

## Overview

The Auth Service handles:
- User registration with role-based access control
- Authentication with JWT token generation
- Token validation for downstream services
- BCrypt password hashing for security

## Technology Stack

- **Java**: 17
- **Framework**: Spring Boot 3.2.3
- **Security**: Spring Security 6
- **Database**: MySQL 8
- **JWT**: jjwt 0.12.3 (HS256)
- **Build**: Maven
- **Container**: Docker

## Prerequisites

- MySQL 8 running on `localhost:3306`
- Java 17 installed
- Maven 3.8+
- Docker (optional, for containerization)

## Database Setup

The service automatically creates the `auth` database and `users` table on startup.

If you want to create the table manually, use the SQL file at:
[src/main/resources/schema.sql](/media/rishiraj/New%20Volume/CRT_Phase_2/microservices/auth-service/src/main/resources/schema.sql)

**users table structure:**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## Configuration

Create a local `.env` file in the project root for confidential values:

```bash
DB_URL=jdbc:mysql://localhost:3306/auth?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=<your_mysql_password>
JWT_SECRET=<your_jwt_secret>
JWT_EXPIRATION=86400000
```

`src/main/resources/application.properties` reads these values from the environment.

Edit `src/main/resources/application.properties` only if you need different defaults:

```properties
# Server Port
server.port=8085

# MySQL Connection
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/auth?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}

# JWT Configuration
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

## Build & Run

### Build with Maven
```bash
cd /media/rishiraj/New\ Volume/CRT_Phase_2/microservices/auth-service
mvn clean package -DskipTests
```

### Run the JAR
```bash
set -a
. .env
set +a
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Build Docker Image
```bash
docker build -t auth-service:latest .
```

### Run Docker Container
```bash
docker run -p 8085:8085 \
  -e spring.datasource.url=jdbc:mysql://host.docker.internal:3306/auth \
  auth-service:latest
```

## API Endpoints

### 0. Root URL (Browser Check)
```bash
curl -X GET http://localhost:8085/
```

**Response:**
```json
{
  "service": "auth-service",
  "status": "UP",
  "message": "Auth service is running",
  "port": 8085
}
```

### 1. Health Check (No Auth Required)
```bash
curl -X GET http://localhost:8085/api/auth/health
```

**Response:**
```json
{
    "service": "auth-service",
    "status": "UP",
    "port": 8085
}
```

---

### 2. User Registration (No Auth Required)
```bash
curl -X POST http://localhost:8085/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123",
    "role": "PATIENT"
  }'
```

**Success Response (201 Created):**
```json
{
    "message": "User registered successfully",
    "username": "john_doe",
    "role": "PATIENT"
}
```

**Error Response (409 Conflict - Username Exists):**
```json
{
    "error": "Username already exists",
    "status": 409
}
```

**Error Response (400 Bad Request - Validation Failed):**
```json
{
    "error": "Validation failed",
    "details": {
        "password": "Password must be at least 6 characters"
    },
    "status": 400
}
```

---

### 3. User Login (No Auth Required)
```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Success Response (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "john_doe",
    "role": "PATIENT",
    "expiresIn": 86400000
}
```

**Error Response (401 Unauthorized - Invalid Credentials):**
```json
{
    "error": "Invalid username or password",
    "status": 401
}
```

---

### 4. Token Validation (Auth Required)
```bash
TOKEN="<token_from_login_response>"

curl -X GET http://localhost:8085/api/auth/validate \
  -H "Authorization: Bearer $TOKEN"
```

**Success Response (200 OK - Valid Token):**
```json
{
    "valid": true,
    "username": "john_doe",
    "role": "PATIENT"
}
```

**Error Response (401 Unauthorized - Expired/Invalid Token):**
```json
{
    "valid": false,
    "message": "Token expired"
}
```

---

## Complete Testing Workflow

```bash
# 1. Check health
curl -X GET http://localhost:8085/api/auth/health

# 2. Register a new user
curl -X POST http://localhost:8085/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "doctor_smith",
    "password": "secure_password_123",
    "role": "DOCTOR"
  }'

# 3. Login to get token
TOKEN=$(curl -s -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "doctor_smith",
    "password": "secure_password_123"
  }' | jq -r '.token')

echo "Token: $TOKEN"

# 4. Validate the token
curl -X GET http://localhost:8085/api/auth/validate \
  -H "Authorization: Bearer $TOKEN"

# 5. Try with expired/invalid token
curl -X GET http://localhost:8085/api/auth/validate \
  -H "Authorization: Bearer invalid_token_here"
```

## Supported Roles

- **ADMIN**: Full system access, can manage users and hospitals
- **DOCTOR**: Can view and manage patient data, appointments
- **PATIENT**: Can access own medical records and appointments

## Security Features

- **Password Security**: BCrypt hashing (no plain text storage)
- **JWT Tokens**: 24-hour expiration, HS256 algorithm
- **CORS**: Enabled for all origins (can be restricted in production)
- **CSRF**: Disabled (stateless REST API)
- **Session**: Stateless (SessionCreationPolicy.STATELESS)

## Error Handling

All errors return consistent JSON responses with error details and HTTP status codes:

- `400 Bad Request`: Validation failed
- `401 Unauthorized`: Invalid credentials or expired token
- `409 Conflict`: Username already exists
- `500 Internal Server Error`: Unexpected errors

## Logging

Logs are configured in `application.properties`:
- Root level: INFO
- Auth service level: DEBUG

## Project Structure

```
auth-service/
├── src/main/java/com/hospital/auth/
│   ├── AuthServiceApplication.java          # Spring Boot entry point
│   ├── controller/
│   │   └── AuthController.java              # REST endpoints
│   ├── service/
│   │   └── AuthService.java                 # Business logic
│   ├── repository/
│   │   └── UserRepository.java              # Data access
│   ├── model/
│   │   └── User.java                        # User entity
│   ├── dto/
│   │   ├── RegisterRequest.java             # Registration payload
│   │   ├── LoginRequest.java                # Login payload
│   │   └── AuthResponse.java                # API response
│   ├── security/
│   │   ├── JwtUtil.java                     # JWT utilities
│   │   ├── JwtAuthFilter.java               # JWT validation filter
│   │   └── SecurityConfig.java              # Spring Security config
│   └── exception/
│       ├── UserAlreadyExistsException.java  # Custom exception
│       └── GlobalExceptionHandler.java      # Global error handling
├── src/main/resources/
│   └── application.properties                # Configuration
├── pom.xml                                   # Maven dependencies
└── Dockerfile                                # Container image
```

## Database Schema

### Users Table
```
+------------+-----------+-----------+---------------------+
| id         | username  | password  | role      | created_at  |
+------------+-----------+-----------+---------------------+
| BIGINT PK  | VARCHAR   | VARCHAR   | VARCHAR   | TIMESTAMP   |
| AUTO_INC   | UNIQUE    | HASHED    | ENUM-like | AUTO        |
+------------+-----------+-----------+---------------------+
```

## Integration with Other Microservices

Other microservices should:

1. Call `/api/auth/validate` with the JWT token from the Authorization header
2. Extract user details (username, role) from the response
3. Implement role-based access control using the role claim

Example integration in another service:
```java
@GetMapping("/api/data")
public ResponseEntity<Data> getData(
    @RequestHeader("Authorization") String authHeader) {
    
    // Call auth-service to validate token
    // Use username and role for business logic
}
```

## Troubleshooting

### Issue: "Access denied (java.net.ConnectException)"
- Ensure MySQL is running on localhost:3306
- Check database credentials in application.properties

### Issue: "Invalid or expired token"
- Tokens expire after 24 hours
- Generate a new token using the login endpoint

### Issue: "Cannot find symbol" during build
- Run `mvn clean install` to resolve dependencies
- Ensure Java 17 is being used (`mvn -version`)

## License

Part of the Hospital Management System - Microservices Architecture
