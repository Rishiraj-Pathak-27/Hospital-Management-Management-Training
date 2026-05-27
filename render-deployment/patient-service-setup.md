# Patient Service - Render Configuration

## Service Details
- **Name:** patient-service
- **Port:** 8086
- **Type:** Spring Boot Microservice with Database
- **Dependencies:** Eureka Server + Auth Service

## Prerequisites

1. ✅ Eureka Server deployed and running
2. ✅ Auth Service deployed and registered
3. ✅ Neon PostgreSQL account created
4. ✅ `patientdb` database created in Neon
5. ✅ Patient Service code ready in `microservices/patient-service`

## Build & Start Commands

**Build Command:**
```bash
cd microservices/patient-service && mvn clean package -DskipTests
```

**Start Command:**
```bash
cd microservices/patient-service && java -jar target/patient-service-*.jar
```

## Environment Variables

Set these in Render dashboard:

```
# Port
PORT=8086

# Database (Neon PostgreSQL)
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/patientdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password

# Auth Service URL (for validation)
AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com

# Eureka Server
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/

# Spring Boot Profile
SPRING_PROFILES_ACTIVE=prod
```

## Update application.properties

File: `microservices/patient-service/src/main/resources/application.properties`

Make sure it has:

```properties
server.port=${PORT:8086}
spring.application.name=patient-service

# Database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=never

# Auth Service
auth.service.url=${AUTH_SERVICE_URL}

# Eureka
eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL}
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
eureka.instance.lease-renewal-interval-in-seconds=10
eureka.client.registry-fetch-interval-seconds=5
```

## Render Dashboard Setup

1. Click "New +" → "Web Service"
2. Connect your GitHub repository
3. Fill in:
   - **Name:** patient-service
   - **Build Command:** `cd microservices/patient-service && mvn clean package -DskipTests`
   - **Start Command:** `cd microservices/patient-service && java -jar target/patient-service-*.jar`
   - **Instance Type:** Free tier

4. Add all Environment Variables (see above)

5. Click "Create Web Service"

## Verification (After ~5 minutes)

```bash
# Check health
curl https://patient-service-xxxx.onrender.com/api/patients/health

# Should return:
# {"service":"patient-service","status":"UP","port":8086}

# Check Eureka registration
# Visit https://eureka-server-xxxx.onrender.com/
# Should show PATIENT-SERVICE as registered

# Test fronthaul API
curl https://patient-service-xxxx.onrender.com/api/patients/patients
# Should return: [] (empty patient list initially)
```

## Database Setup

Neon will auto-create tables via JPA when service starts:

- patients
- doctors
- appointments
- admissions
- wards
- beds

Run SQL init scripts if needed:

```sql
-- See: microservices/patient-service/sql/init_patientdb.sql
```

## Important Notes

- **Deploy AFTER Eureka and Auth** - it depends on both
- Service auto-registers with Eureka
- All CRUD operations validated through Auth Service
- PostgreSQL required (Neon recommended)

## API Endpoints

Once running:

```
GET  /api/patients/health          → Health check
GET  /api/patients/patients        → List all patients
POST /api/patients/patients        → Add patient
GET  /api/patients/doctors         → List doctors
POST /api/patients/doctors         → Add doctor
GET  /api/patients/appointments    → List appointments
POST /api/patients/appointments    → Book appointment
GET  /api/patients/admissions      → List admissions
POST /api/patients/admissions      → Process admission
GET  /api/patients/wards           → List wards
GET  /api/patients/beds            → List beds
```

## Troubleshooting

**Database connection failed:**
- Check PostgreSQL connection string
- Verify patientdb exists in Neon
- Verify username and password

**Auth Service connection failed:**
- Verify AUTH_SERVICE_URL is correct
- Check if Auth Service is running
- Check logs for connection errors

**Can't access API endpoints:**
- Wait for Eureka registration (1-2 minutes)
- Check if Auth Service is accessible
- Verify JWT token handling if authentication required

**CORS errors:**
- Frontend should have CORS enabled
- Patient Service should allow cross-origin requests
- Check browser console for details

## Next Step

Once Patient Service is running and registered:
1. Update frontend API URL
2. Deploy frontend to Vercel
3. Test end-to-end functionality
