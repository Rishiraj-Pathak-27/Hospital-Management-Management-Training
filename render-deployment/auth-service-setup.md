# Auth Service - Render Configuration

## Service Details
- **Name:** auth-service
- **Port:** 8085
- **Type:** Spring Boot Microservice with Database
- **Dependency:** Eureka Server (must be running first)

## Prerequisites

1. ✅ Eureka Server deployed on Render
2. ✅ Neon PostgreSQL account created
3. ✅ `authdb` database created in Neon
4. ✅ Auth Service code ready in `microservices/auth-service`

## Build & Start Commands

**Build Command:**
```bash
cd microservices/auth-service && mvn clean package -DskipTests
```

**Start Command:**
```bash
cd microservices/auth-service && java -jar target/auth-service-*.jar
```

## Environment Variables

Set these in Render dashboard:

```
# Port
PORT=8085

# Database (Neon PostgreSQL)
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password

# JWT Configuration
JWT_SECRET=your-secret-key-minimum-32-characters-long-is-required!!!
JWT_EXPIRATION=86400000

# Eureka Server
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/

# Spring Boot Profile
SPRING_PROFILES_ACTIVE=prod
```

## Update application.properties

File: `microservices/auth-service/src/main/resources/application.properties`

Make sure it has:

```properties
server.port=${PORT:8085}
spring.application.name=auth-service

# Database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}

# Eureka
eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL}
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
```

## Render Dashboard Setup

1. Click "New +" → "Web Service"
2. Connect your GitHub repository
3. Fill in:
   - **Name:** auth-service
   - **Build Command:** `cd microservices/auth-service && mvn clean package -DskipTests`
   - **Start Command:** `cd microservices/auth-service && java -jar target/auth-service-*.jar`
   - **Instance Type:** Free tier

4. Add all Environment Variables (see above)

5. Click "Create Web Service"

## Verification (After ~5 minutes)

```bash
# Check health
curl https://auth-service-xxxx.onrender.com/api/auth/health

# Should return:
# {"service":"auth-service","status":"UP"}

# Check Eureka registration
# Visit https://eureka-server-xxxx.onrender.com/
# Should show AUTH-SERVICE as registered
```

## Database Setup

Neon will auto-create tables via JPA when service starts:

- users (user accounts)
- roles (user roles)
- etc.

## Important Notes

- **Wait for Eureka first** before deploying this
- Service auto-registers with Eureka on startup
- PostgreSQL required (Neon recommended)
- JWT secret should be strong (32+ characters)

## Troubleshooting

**Database connection failed:**
- Check PostgreSQL connection string
- Verify authdb exists in Neon
- Check username and password

**Eureka registration failed:**
- Verify EUREKA_SERVER_URL is correct
- Check if Eureka server is running
- Look at service logs for connection errors

**Cold start timeout:**
- First deployment takes 3-5 minutes
- Neon database needs time to initialize
- Be patient, it will start

## Next Step

Once this runs successfully and registers with Eureka, deploy Patient Service.
