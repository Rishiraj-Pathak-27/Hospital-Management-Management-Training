# 🚀 Complete Microservices Deployment Guide - Render + Vercel

## 📊 Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│              VERCEL (Frontend)                      │
│  hospital-frontend.vercel.app                       │
│  (React/HTML/CSS/JS - Port 80/443)                 │
└────────────────┬────────────────────────────────────┘
                 │ HTTPS REST API calls
                 ▼
┌─────────────────────────────────────────────────────┐
│              RENDER (Backend)                       │
│                                                     │
│  ┌─────────────────────────────────────────┐       │
│  │  Eureka Server (Service Registry)       │       │
│  │  Port: 8761 (Internal)                  │       │
│  └─────────────────────────────────────────┘       │
│           ▲              ▲                          │
│           │ Register    │ Register                  │
│           │             │                          │
│  ┌────────┴──────┐  ┌───┴──────────┐              │
│  │ Auth Service  │  │Patient Service│             │
│  │ Port: 8085    │  │Port: 8086     │             │
│  │ (Eureka Client)  │(Eureka Client)│             │
│  └────────┬──────┘  └───┬──────────┘              │
│           │             │                          │
│  ┌────────┴─────────────┴──────┐                 │
│  │   PostgreSQL Database        │                 │
│  │   (Neon or similar)          │                 │
│  └──────────────────────────────┘                 │
└─────────────────────────────────────────────────────┘
```

---

## 🎯 Deployment Order (CRITICAL!)

Follow this exact order:

```
1. ✅ Set up PostgreSQL Database (Neon)
2. ✅ Deploy Eureka Server (Service Registry)
3. ✅ Deploy Auth Service (Authentication)
4. ✅ Deploy Patient Service (Main API)
5. ✅ Deploy Frontend (Vercel)
6. ✅ Connect & Test Everything
```

---

# STEP 1: Set Up PostgreSQL Database (Neon) ⭐

## Why PostgreSQL?
- Free tier allows multiple databases
- Multiple services can connect
- Better than trying separate MySQL instances

### 1.1 Create Neon PostgreSQL Database

1. Go to: **https://console.neon.tech**
2. Sign up with GitHub or Email
3. Create new project (Free tier ✓)
4. You'll get connection string like:
   ```
   postgresql://user:password@ep-xxxxx.neon.tech/neondb?sslmode=require
   ```

### 1.2 Create Two Databases

For each microservice, we need a separate database. Use Neon's SQL Editor:

**For Auth Service:**
```sql
CREATE DATABASE authdb;
```

**For Patient Service:**
```sql
CREATE DATABASE patientdb;
```

### 1.3 Save Connection Strings

You'll use these later:

```
AUTH_DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
PATIENT_DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/patientdb?sslmode=require
```

---

# STEP 2: Deploy Eureka Server 🎯

## Why First?
- Auth & Patient services register with it
- Must be running before they start

### 2.1 Check Eureka Configuration

File: `microservices/eureka-server/src/main/resources/application.properties`

Should have:
```properties
server.port=8761
spring.application.name=eureka-server
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.server.enable-self-preservation=false
```

### 2.2 Create Render Service (Eureka)

1. Go to: **https://dashboard.render.com**
2. Click **"New +"** → **"Web Service"**

**Configuration:**
- **Name:** `eureka-server`
- **Repository:** Your GitHub repo (CRT_Phase_2)
- **Build Command:** 
  ```
  cd microservices/eureka-server && mvn clean package -DskipTests
  ```
- **Start Command:**
  ```
  cd microservices/eureka-server && java -jar target/eureka-server-*.jar
  ```
- **Environment:** Java 17
- **Instance Type:** Free tier

**Environment Variables:**
```
PORT=8761
```

**Deploy & Wait:** 3-5 minutes for cold start

**Get URL:** Render will give you a URL like: `https://eureka-server-xxxx.onrender.com`

**Save it:** You'll need this for Auth & Patient services

### 2.3 Verify Eureka is Running

Once deployed, visit:
```
https://eureka-server-xxxx.onrender.com/
```

You should see Eureka Dashboard with 0 registered instances (that's OK).

---

# STEP 3: Deploy Auth Service 🔐

### 3.1 Update Auth Service Configuration

Edit: `microservices/auth-service/src/main/resources/application.properties`

Change Eureka URL to point to deployed Eureka:

```properties
# OLD (local)
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# NEW (deployed)
eureka.client.service-url.defaultZone=https://eureka-server-xxxx.onrender.com/eureka/
```

### 3.2 Create Render Service (Auth)

1. Go to: **https://dashboard.render.com**
2. Click **"New +"** → **"Web Service"**

**Configuration:**
- **Name:** `auth-service`
- **Repository:** Your GitHub repo
- **Build Command:**
  ```
  cd microservices/auth-service && mvn clean package -DskipTests
  ```
- **Start Command:**
  ```
  cd microservices/auth-service && java -Dspring.profiles.active=prod -jar target/auth-service-*.jar
  ```
- **Instance Type:** Free tier

**Environment Variables:**
```
PORT=8085
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
JWT_SECRET=your-secret-key-min-32-characters-long!!!
JWT_EXPIRATION=86400000
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/
```

**Deploy & Wait:** 3-5 minutes

**Get URL:** `https://auth-service-xxxx.onrender.com`

### 3.3 Verify Auth Service

Once running:
```bash
curl https://auth-service-xxxx.onrender.com/api/auth/health
```

Should return:
```json
{
  "service": "auth-service",
  "status": "UP"
}
```

---

# STEP 4: Deploy Patient Service 👥

### 4.1 Update Patient Service Configuration

Edit: `microservices/patient-service/src/main/resources/application.properties`

Change Eureka URL:

```properties
# OLD
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# NEW
eureka.client.service-url.defaultZone=https://eureka-server-xxxx.onrender.com/eureka/
```

Change Auth Service URL:

```properties
# OLD
auth.service.url=http://localhost:8085

# NEW
auth.service.url=https://auth-service-xxxx.onrender.com
```

### 4.2 Create Render Service (Patient)

1. Go to: **https://dashboard.render.com**
2. Click **"New +"** → **"Web Service"**

**Configuration:**
- **Name:** `patient-service`
- **Repository:** Your GitHub repo
- **Build Command:**
  ```
  cd microservices/patient-service && mvn clean package -DskipTests
  ```
- **Start Command:**
  ```
  cd microservices/patient-service && java -Dspring.profiles.active=prod -jar target/patient-service-*.jar
  ```
- **Instance Type:** Free tier

**Environment Variables:**
```
PORT=8086
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/patientdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/
```

**Deploy & Wait:** 3-5 minutes

**Get URL:** `https://patient-service-xxxx.onrender.com`

### 4.3 Verify Patient Service

Once running:
```bash
curl https://patient-service-xxxx.onrender.com/api/patients/health
```

Should return:
```json
{
  "service": "patient-service",
  "status": "UP",
  "port": 8086
}
```

---

# STEP 5: Deploy Frontend on Vercel 🎨

### 5.1 Update Frontend API URL

Edit: `hospital-frontend/app.js` (line 3)

```javascript
// OLD
const API_BASE_URL = 'http://localhost:8086/api/patients';

// NEW
const API_BASE_URL = 'https://patient-service-xxxx.onrender.com/api/patients';
```

### 5.2 Push Code to GitHub

```bash
cd /media/rishiraj/New\ Volume/CRT_Phase_2
git add .
git commit -m "Update microservices URLs for production deployment"
git push origin main
```

### 5.3 Deploy to Vercel

1. Go to: **https://vercel.com/dashboard**
2. Click **"Add New"** → **"Project"**
3. Import your GitHub repository
4. **Select Root Directory:** `hospital-frontend`
5. Click **"Deploy"**

**Wait:** 1-2 minutes

**Get URL:** `https://hospital-frontend-xxxx.vercel.app`

---

# STEP 6: Connect & Test Everything ✅

## 6.1 Test Health Endpoints

```bash
# Test Eureka
curl https://eureka-server-xxxx.onrender.com/

# Test Auth Service
curl https://auth-service-xxxx.onrender.com/api/auth/health

# Test Patient Service
curl https://patient-service-xxxx.onrender.com/api/patients/health
```

All should return status UP.

## 6.2 Test Frontend

1. Open: `https://hospital-frontend-xxxx.vercel.app`
2. Navigate to Patients tab
3. Try adding a patient
4. Verify it appears in the list

If successful → ✅ Everything working!

## 6.3 Check Eureka Registration

Visit: `https://eureka-server-xxxx.onrender.com/`

You should see:
- ✅ AUTH-SERVICE (8085)
- ✅ PATIENT-SERVICE (8086)

Both should be registered and UP.

---

# 🔧 Troubleshooting

## Problem: Services fail to start

**Check logs on Render:**
1. Go to service dashboard
2. Click "Logs" tab
3. Look for error messages

**Common issues:**
- Database connection failed → Check DB_URL and credentials
- Port already in use → Render assigns PORT automatically
- JAR not built → Check build command output

## Problem: Database errors

**Solution:**
1. Verify Neon connection string
2. Make sure databases exist (authdb, patientdb)
3. Check username/password spelling

## Problem: Services won't register with Eureka

**Check:**
1. Eureka server URL is correct
2. Services have Eureka dependency (they do)
3. Services can reach Eureka URL

## Problem: Frontend can't reach backend

**Solution:**
1. Verify AUTH_SERVICE_URL in patient-service is correct
2. Check CORS is enabled in backend
3. Look for network errors in browser console (F12)

---

# 📋 Quick Reference

## Service URLs After Deployment

```
Eureka Server:   https://eureka-server-xxxx.onrender.com
Auth Service:    https://auth-service-xxxx.onrender.com
Patient Service: https://patient-service-xxxx.onrender.com
Frontend:        https://hospital-frontend-xxxx.vercel.app
```

## Database

```
Provider: Neon PostgreSQL
Databases: authdb, patientdb
Connection: Your Neon connection string
```

## Ports

```
Eureka:  8761 (internal, not exposed)
Auth:    8085 (exposed as web service)
Patient: 8086 (exposed as web service)
Frontend: 80/443 (Vercel)
```

---

# 🚀 Complete Command Summary

## Before Deployment

```bash
# Update application.properties
# Update app.js frontend URL
# Commit and push to GitHub
git add .
git commit -m "Production deployment configuration"
git push origin main
```

## Deployment Steps

1. **Create Neon PostgreSQL** (5 minutes)
   - Two databases: authdb, patientdb

2. **Deploy Eureka Server** (5 minutes)
   - Wait for cold start

3. **Deploy Auth Service** (5 minutes)
   - Set environment variables
   - Point to Eureka and Neon

4. **Deploy Patient Service** (5 minutes)
   - Set environment variables
   - Point to Eureka, Auth, and Neon

5. **Deploy Frontend** (2 minutes)
   - Connect GitHub to Vercel
   - Auto-deploys on push

## Total Time: ~22 minutes ⚡

---

# 📞 Useful Links

| Resource | Link |
|----------|------|
| Render Dashboard | https://dashboard.render.com |
| Vercel Dashboard | https://vercel.com/dashboard |
| Neon Console | https://console.neon.tech |
| GitHub | https://github.com |

---

# ✅ Success Checklist

- [ ] Neon PostgreSQL created with 2 databases
- [ ] Eureka Server deployed and running
- [ ] Auth Service deployed and registered
- [ ] Patient Service deployed and registered
- [ ] Frontend deployed on Vercel
- [ ] All services show UP in health checks
- [ ] Eureka dashboard shows both services registered
- [ ] Frontend can add and view patients
- [ ] Frontend can add and view doctors
- [ ] All CRUD operations working

---

## 🎉 Next Actions

1. **Start with Neon PostgreSQL** (takes 5 minutes)
2. **Deploy Eureka first** (takes 5 minutes)
3. **Then auth Service** (after Eureka is UP)
4. **Then Patient Service** (after Auth is UP)
5. **Finally Frontend** (everything else ready)

**Ready to start?** Begin with Neon PostgreSQL setup! 🚀
