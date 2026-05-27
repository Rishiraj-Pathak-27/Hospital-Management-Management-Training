# Quick Deployment Start Guide

## Overview

Deploy your Hospital Management System microservices on Render with Neon PostgreSQL.

**Total time: ~25 minutes**

## Architecture

```
Frontend (Vercel: 80/443)
    ↓
Patient Service (Render: 8086) ← Auth Service (Render: 8085)
    ↓
PostgreSQL (Neon authdb + patientdb)
    
+ Eureka Server (Render: 8761) for service discovery
```

## ⚡ Quick Deployment Order

### 1️⃣ Create Neon PostgreSQL (5 min)

```bash
# Go to: https://console.neon.tech
# Click "Create Project" (Free tier)
# Create two databases in SQL editor:
CREATE DATABASE authdb;
CREATE DATABASE patientdb;

# Copy connection string:
postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
postgresql://user:password@ep-xxxxx.neon.tech/patientdb?sslmode=require
```

**Save both connection strings!**

### 2️⃣ Deploy Eureka Server (5 min)

```bash
# Go to: https://dashboard.render.com
# Click "New +" → "Web Service"

Name: eureka-server
Build: cd microservices/eureka-server && mvn clean package -DskipTests
Start: cd microservices/eureka-server && java -jar target/eureka-server-*.jar

# No environment variables needed

# Wait 3-5 minutes for deployment
# Get URL: https://eureka-server-xxxx.onrender.com
```

**Save Eureka URL!**

### 3️⃣ Deploy Auth Service (5 min)

```bash
# Go to: https://dashboard.render.com
# Click "New +" → "Web Service"

Name: auth-service
Build: cd microservices/auth-service && mvn clean package -DskipTests
Start: cd microservices/auth-service && java -jar target/auth-service-*.jar

# Add Environment Variables:
PORT=8085
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
JWT_SECRET=your-secret-key-minimum-32-character-length-required!!!
JWT_EXPIRATION=86400000
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/

# Wait 3-5 minutes
# Get URL: https://auth-service-xxxx.onrender.com
```

**Save Auth Service URL!**

### 4️⃣ Deploy Patient Service (5 min)

```bash
# Go to: https://dashboard.render.com
# Click "New +" → "Web Service"

Name: patient-service
Build: cd microservices/patient-service && mvn clean package -DskipTests
Start: cd microservices/patient-service && java -jar target/patient-service-*.jar

# Add Environment Variables:
PORT=8086
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/patientdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/

# Wait 3-5 minutes
# Get URL: https://patient-service-xxxx.onrender.com
```

**Save Patient Service URL!**

### 5️⃣ Update Frontend (2 min)

```bash
# Edit: hospital-frontend/app.js (line 3)

# OLD:
const API_BASE_URL = 'http://localhost:8086/api/patients';

# NEW:
const API_BASE_URL = 'https://patient-service-xxxx.onrender.com/api/patients';

# Commit and push:
git add .
git commit -m "Update API URL for production"
git push origin main
```

### 6️⃣ Deploy Frontend on Vercel (2 min)

```bash
# Go to: https://vercel.com/dashboard
# Click "Add New" → "Project"
# Import your GitHub repository
# Root Directory: hospital-frontend
# Click "Deploy"

# Wait 1-2 minutes
# Get URL: https://hospital-frontend-xxxx.vercel.app
```

## ✅ Verify Everything Works

### Test Health Endpoints

```bash
# Test each service
curl https://eureka-server-xxxx.onrender.com/
curl https://auth-service-xxxx.onrender.com/api/auth/health
curl https://patient-service-xxxx.onrender.com/api/patients/health

# All should return 200 OK
```

### Check Service Registration

Visit: `https://eureka-server-xxxx.onrender.com/`

Should show:
- ✅ AUTH-SERVICE (8085)
- ✅ PATIENT-SERVICE (8086)

### Test Frontend

1. Open: `https://hospital-frontend-xxxx.vercel.app`
2. Go to Patients tab
3. Add a test patient
4. Should appear in the list
5. Try other tabs

## 🆘 Troubleshooting

| Problem | Solution |
|---------|----------|
| Service won't start | Check build logs for errors |
| Database connection failed | Verify Neon connection string and databases exist |
| Can't access Eureka | Wait 3-5 minutes for cold start |
| Frontend can't reach backend | Verify API_BASE_URL in app.js |
| Services won't register | Check EUREKA_SERVER_URL is correct |

## 📚 Detailed Guides

For more info, see:

- `MICROSERVICES_DEPLOYMENT_GUIDE.md` - Complete guide
- `eureka-server-setup.md` - Eureka details
- `auth-service-setup.md` - Auth service details
- `patient-service-setup.md` - Patient service details
- `environment-variables.md` - All variables reference

## 🎯 Final URLs

After complete deployment:

```
Eureka:   https://eureka-server-xxxx.onrender.com
Auth:     https://auth-service-xxxx.onrender.com
Patient:  https://patient-service-xxxx.onrender.com
Frontend: https://hospital-frontend-xxxx.vercel.app
```

## 📋 Checklist

- [ ] Neon PostgreSQL created (2 databases)
- [ ] Eureka Server deployed
- [ ] Auth Service deployed
- [ ] Patient Service deployed
- [ ] Frontend URL updated
- [ ] Frontend deployed to Vercel
- [ ] All health checks passing
- [ ] Services registered in Eureka
- [ ] Frontend can access backend
- [ ] CRUD operations working

## 🚀 Ready?

Start with Step 1: Create Neon PostgreSQL!

---

**Questions?** See the detailed guides in this folder or the main MICROSERVICES_DEPLOYMENT_GUIDE.md
