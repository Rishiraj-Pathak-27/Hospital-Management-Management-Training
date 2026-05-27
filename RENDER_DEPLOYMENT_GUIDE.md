# Complete Render Deployment Guide - Microservices with Neon Database

## 📋 Deployment Overview

You'll deploy:
1. **Eureka Server** (Service Registry - runs on port 8761)
2. **Auth Service** (Port 8085)
3. **Patient Service** (Port 8086)
4. **Frontend** (Vercel)

All backed by **Neon PostgreSQL** database.

---

## 🎯 Step 1: Create Neon Database (FIRST STEP!)

### 1.1 Create Neon Account
- Go to: https://console.neon.tech
- Sign up with GitHub/Google
- Create a new project

### 1.2 Create Database Connection String
1. Click "Databases" in left panel
2. You'll see a connection string like:
```
postgresql://user:password@host/database?sslmode=require
```

**Save this!** You'll need it for all microservices.

### 1.3 Get Your Connection Details
```
Host: xxxxxx.neon.tech
Database: neondb
User: neondb_owner
Password: your-password-here
Port: 5432
```

---

## 📝 Step 2: Update Each Microservice for PostgreSQL

### 2.1 Check Current MySQL Dependencies

Each microservice has `pom.xml`. You need to:
1. Remove MySQL driver
2. Add PostgreSQL driver
3. Update connection strings

---

## 🔧 Step 3: Update Auth Service (microservices/auth-service)

### 3.1 Update pom.xml

Replace MySQL dependency with PostgreSQL:

**Find this:**
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.x.x</version>
</dependency>
```

**Replace with:**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.6.0</version>
</dependency>
```

### 3.2 Update application.properties

**File:** `src/main/resources/application.properties`

Replace:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/authdb?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
```

With:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 3.3 Update schema.sql (if exists)

Convert MySQL to PostgreSQL syntax:
- `AUTO_INCREMENT` → `SERIAL`
- `BIGINT UNSIGNED` → `BIGINT`
- `VARCHAR(255)` → `VARCHAR(255)` (same)
- etc.

---

## 🔧 Step 4: Update Patient Service (microservices/patient-service)

Same as Auth Service steps above.

---

## 🔧 Step 5: Update Eureka Server

**File:** `src/main/resources/application.properties`

Add/Update:
```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

(Eureka doesn't use database, no changes needed for DB)

---

## 🚀 Step 6: Deploy Eureka Server to Render (DEPLOY FIRST!)

### 6.1 Create New Web Service on Render

Go to: https://dashboard.render.com
- Click **New +** → **Web Service**
- Select **Build from Git**
- Connect your GitHub repo

### 6.2 Configuration

```
Name: hospital-eureka-server
Build Command: mvn clean package -DskipTests
Start Command: java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```

### 6.3 Environment Variables

```
PORT=8761
```

### 6.4 Deploy
- Click **Create Web Service**
- Wait for deployment (5-10 minutes)
- Note your Eureka URL: `https://hospital-eureka-server.onrender.com`

---

## 🚀 Step 7: Deploy Auth Service to Render

### 7.1 Create New Web Service

- Go to Render Dashboard
- Click **New +** → **Web Service**
- Connect GitHub repo

### 7.2 Configuration

```
Name: hospital-auth-service
Build Command: cd microservices/auth-service && mvn clean package -DskipTests
Start Command: java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### 7.3 Environment Variables

```
PORT=8085
DB_URL=postgresql://user:password@host/database?sslmode=require
DB_USERNAME=neondb_owner
DB_PASSWORD=your-password
SPRING_DATASOURCE_URL=postgresql://user:password@host/database?sslmode=require
SPRING_DATASOURCE_USERNAME=neondb_owner
SPRING_DATASOURCE_PASSWORD=your-password
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://hospital-eureka-server.onrender.com/eureka
```

**Get exact values from your Neon project dashboard**

### 7.4 Deploy
- Click **Create Web Service**
- Wait 5-10 minutes
- Note Auth URL: `https://hospital-auth-service.onrender.com`

---

## 🚀 Step 8: Deploy Patient Service to Render

### 8.1 Create New Web Service

- Go to Render Dashboard
- Click **New +** → **Web Service**
- Connect GitHub repo

### 8.2 Configuration

```
Name: hospital-patient-service
Build Command: cd microservices/patient-service && mvn clean package -DskipTests
Start Command: java -jar target/patient-service-0.0.1-SNAPSHOT.jar
```

### 8.3 Environment Variables

```
PORT=8086
DB_URL=postgresql://user:password@host/database?sslmode=require
DB_USERNAME=neondb_owner
DB_PASSWORD=your-password
SPRING_DATASOURCE_URL=postgresql://user:password@host/database?sslmode=require
SPRING_DATASOURCE_USERNAME=neondb_owner
SPRING_DATASOURCE_PASSWORD=your-password
AUTH_SERVICE_URL=https://hospital-auth-service.onrender.com
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://hospital-eureka-server.onrender.com/eureka
```

### 8.4 Deploy
- Click **Create Web Service**
- Wait 5-10 minutes
- Note Patient Service URL: `https://hospital-patient-service.onrender.com`

---

## 🌐 Step 9: Deploy Frontend to Vercel

### 9.1 Update API Base URL

Edit: `hospital-frontend/app.js`

Change line 3 from:
```javascript
const API_BASE_URL = 'https://hospital-management-management-training.onrender.com/api/patients';
```

To:
```javascript
const API_BASE_URL = 'https://hospital-patient-service.onrender.com/api/patients';
```

### 9.2 Deploy to Vercel

```bash
cd hospital-frontend
npm install -g vercel
vercel
```

Follow prompts to deploy.

### 9.3 Get Frontend URL
```
https://hospital-frontend-xxx.vercel.app
```

---

## 🔗 Single Backend Link Architecture

Your deployment looks like:

```
┌──────────────────────────────────────────────────────────┐
│           Frontend (Vercel)                              │
│  https://hospital-frontend-xxx.vercel.app               │
└──────────────┬───────────────────────────────────────────┘
               │ API Calls
               ↓
┌──────────────────────────────────────────────────────────┐
│           Patient Service (Render Port 8086)            │
│  https://hospital-patient-service.onrender.com/api/... │
├──────────────────────────────────────────────────────────┤
│ ↓ Auth Validation                                        │
│  https://hospital-auth-service.onrender.com            │
├──────────────────────────────────────────────────────────┤
│ ↓ Service Discovery                                      │
│  https://hospital-eureka-server.onrender.com           │
└──────────────┬───────────────────────────────────────────┘
               │ SQL Queries
               ↓
┌──────────────────────────────────────────────────────────┐
│           PostgreSQL on Neon                             │
│  host.neon.tech database                               │
└──────────────────────────────────────────────────────────┘
```

---

## ✅ Deployment Checklist

- [ ] Neon Database created
- [ ] Auth Service updated for PostgreSQL
- [ ] Patient Service updated for PostgreSQL
- [ ] Eureka Server deployed on Render
- [ ] Auth Service deployed on Render
- [ ] Patient Service deployed on Render
- [ ] Frontend API URL updated
- [ ] Frontend deployed on Vercel
- [ ] Test all endpoints
- [ ] Services can communicate

---

## 🧪 Test After Deployment

### Test Eureka Server
```bash
curl https://hospital-eureka-server.onrender.com
```

### Test Auth Service
```bash
curl https://hospital-auth-service.onrender.com/api/auth/health
```

### Test Patient Service
```bash
curl https://hospital-patient-service.onrender.com/api/patients/health
```

### Test Frontend
Visit: `https://hospital-frontend-xxx.vercel.app`

---

## 🆘 Troubleshooting

### "502 Bad Gateway" on Render
- Wait 2 minutes (cold start)
- Check logs on Render dashboard
- Verify database connection in env vars

### "Cannot connect to database"
- Check Neon connection string
- Verify username/password in env vars
- Make sure sslmode=require is in URL

### "Services can't communicate"
- Check service URLs in env vars
- Make sure EUREKA_CLIENT_SERVICEURL_DEFAULTZONE is set
- Check AUTH_SERVICE_URL is correct

---

## 📋 Quick Reference URLs

| Service | URL |
|---------|-----|
| Eureka Server | https://hospital-eureka-server.onrender.com |
| Auth Service | https://hospital-auth-service.onrender.com |
| Patient Service | https://hospital-patient-service.onrender.com |
| Frontend | https://hospital-frontend-xxx.vercel.app |

---

## 🚀 Deployment Order (IMPORTANT!)

1. ✅ **Neon Database** (Create first)
2. ✅ **Eureka Server** (Deploy first)
3. ✅ **Auth Service** (Deploy second, needs Eureka)
4. ✅ **Patient Service** (Deploy third, needs Auth & Eureka)
5. ✅ **Frontend** (Deploy last, needs Patient Service)

---

**Next Step:** Start with Neon database creation!
