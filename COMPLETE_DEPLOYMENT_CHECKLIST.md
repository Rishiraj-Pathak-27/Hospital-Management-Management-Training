# 🚀 DEPLOYMENT CHECKLIST - Complete Microservices to Render

## PHASE 1: Preparation (Before Deployment)

### ☐ Step 1: Update Microservices to PostgreSQL
```bash
# Run the automated script
bash update-to-postgresql.sh

# OR manual: Follow EXACT_DEPLOYMENT_STEPS.md
```

**What this does:**
- Converts MySQL → PostgreSQL driver
- Updates application.properties
- Adds environment variable support

**Time:** 5 minutes

---

### ☐ Step 2: Commit Changes to GitHub
```bash
cd /media/rishiraj/New\ Volume/CRT_Phase_2

git add .
git commit -m "Convert microservices to PostgreSQL for Render deployment"
git push origin main
```

**Time:** 2 minutes

---

### ☐ Step 3: Create Neon Database
1. Go to: https://console.neon.tech
2. Sign up with Google/GitHub
3. Create new project
4. **SAVE Connection String:**
   ```
   postgresql://neondb_owner:PASSWORD@host.neon.tech/neondb?sslmode=require
   ```

**Copy these values:**
- HOST: `ep-xxxx.neon.tech`
- DATABASE: `neondb`
- USER: `neondb_owner`
- PASSWORD: `(from neon)`

**Time:** 5 minutes

---

## PHASE 2: Deploy to Render (In Order!)

### ☐ FIRST: Deploy Eureka Server (Port 8761)

1. Go to: https://dashboard.render.com
2. Click **New +** → **Web Service**
3. Select GitHub repo
4. Fill in:
   ```
   Name: hospital-eureka-server
   Build Command: mvn clean package -DskipTests
   Start Command: java -jar microservices/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
   ```
5. Add env vars:
   ```
   PORT=8761
   ```
6. Click **Create Web Service**
7. **SAVE URL:** `https://hospital-eureka-server.onrender.com`
8. ⏳ Wait 5-10 minutes for deployment

**Time:** 10 minutes

---

### ☐ SECOND: Deploy Auth Service (Port 8085)

1. Go to: https://dashboard.render.com
2. Click **New +** → **Web Service**
3. Select GitHub repo
4. Fill in:
   ```
   Name: hospital-auth-service
   Build Command: mvn clean package -DskipTests
   Start Command: java -jar microservices/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar
   ```
5. Add env vars (**Replace with YOUR values**):
   ```
   PORT=8085
   
   SPRING_DATASOURCE_URL=postgresql://neondb_owner:PASSWORD@host.neon.tech/neondb?sslmode=require
   SPRING_DATASOURCE_USERNAME=neondb_owner
   SPRING_DATASOURCE_PASSWORD=PASSWORD
   
   JWT_SECRET=your-secret-key-here-make-it-long-and-random
   JWT_EXPIRATION=86400000
   
   EUREKA_SERVER_URL=https://hospital-eureka-server.onrender.com/eureka/
   ```
6. Click **Create Web Service**
7. **SAVE URL:** `https://hospital-auth-service.onrender.com`
8. ⏳ Wait 5-10 minutes for deployment

**Time:** 10 minutes

---

### ☐ THIRD: Deploy Patient Service (Port 8086)

1. Go to: https://dashboard.render.com
2. Click **New +** → **Web Service**
3. Select GitHub repo
4. Fill in:
   ```
   Name: hospital-patient-service
   Build Command: mvn clean package -DskipTests
   Start Command: java -jar microservices/patient-service/target/patient-service-0.0.1-SNAPSHOT.jar
   ```
5. Add env vars (**Replace with YOUR values**):
   ```
   PORT=8086
   
   SPRING_DATASOURCE_URL=postgresql://neondb_owner:PASSWORD@host.neon.tech/neondb?sslmode=require
   SPRING_DATASOURCE_USERNAME=neondb_owner
   SPRING_DATASOURCE_PASSWORD=PASSWORD
   
   AUTH_SERVICE_URL=https://hospital-auth-service.onrender.com
   
   EUREKA_SERVER_URL=https://hospital-eureka-server.onrender.com/eureka/
   ```
6. Click **Create Web Service**
7. **SAVE URL:** `https://hospital-patient-service.onrender.com`
8. ⏳ Wait 5-10 minutes for deployment

**Time:** 10 minutes

---

## PHASE 3: Deploy Frontend

### ☐ Step 1: Update Frontend API URL

**File:** `hospital-frontend/app.js` (Line 3)

**Change from:**
```javascript
const API_BASE_URL = 'https://hospital-management-management-training.onrender.com/api/patients';
```

**Change to:**
```javascript
const API_BASE_URL = 'https://hospital-patient-service.onrender.com/api/patients';
```

**Time:** 1 minute

---

### ☐ Step 2: Commit & Push Frontend Changes
```bash
cd hospital-frontend

git add app.js
git commit -m "Update API URL to point to new patient-service"
git push origin main
```

**Time:** 2 minutes

---

### ☐ Step 3: Deploy Frontend to Vercel

**Option A: Using Vercel CLI**
```bash
cd hospital-frontend
npm install -g vercel
vercel
```

**Option B: Using Vercel Dashboard**
1. Go to: https://vercel.com/dashboard
2. Click **Add New** → **Project**
3. Select GitHub repo
4. Select `hospital-frontend` folder
5. Click **Deploy**

8. ⏳ Wait 2-3 minutes for deployment
9. **SAVE URL:** `https://hospital-frontend-xxx.vercel.app`

**Time:** 5 minutes

---

## PHASE 4: Verification

### ☐ Test Eureka Server
```bash
curl https://hospital-eureka-server.onrender.com
```
Expected: Eureka Server page HTML

### ☐ Test Auth Service
```bash
curl https://hospital-auth-service.onrender.com/api/auth/health
```
Expected: `200 OK` with health info

### ☐ Test Patient Service
```bash
curl https://hospital-patient-service.onrender.com/api/patients/health
```
Expected: `200 OK` with service info

### ☐ Test Frontend
1. Visit: `https://hospital-frontend-xxx.vercel.app`
2. Click on **Patients** tab
3. Should show "Loading patients..."
4. After 2-3 seconds, should show patients list (or "No patients found" if empty)
5. Try adding a patient

### ☐ Full Integration Test
1. Add a new patient via frontend
2. Add a new doctor
3. Book an appointment
4. Process an admission
5. View wards and beds

---

## 📊 Your Deployment Summary

| Service | URL | Port | Database |
|---------|-----|------|----------|
| **Eureka** | https://hospital-eureka-server.onrender.com | 8761 | N/A |
| **Auth** | https://hospital-auth-service.onrender.com | 8085 | Neon |
| **Patient** | https://hospital-patient-service.onrender.com | 8086 | Neon |
| **Frontend** | https://hospital-frontend-xxx.vercel.app | - | N/A |

---

## 🆘 If Something Goes Wrong

### Problem: "502 Bad Gateway"
- Wait 2-3 minutes (cold start on Render)
- Check Render dashboard logs
- Verify environment variables are set correctly
- Try restarting service

### Problem: "Cannot connect to database"
- Verify Neon connection string
- Check username/password
- Make sure sslmode=require is in URL
- Test connection from Neon dashboard

### Problem: "Services can't communicate"
- Verify EUREKA_SERVER_URL env var
- Check AUTH_SERVICE_URL in patient service
- Restart all services
- Check Render logs for errors

### Problem: "Frontend shows errors in console"
- Open DevTools (F12)
- Check Network tab
- Verify patient-service URL is correct
- Check browser console for specific errors

---

## 📝 Environment Variables Reference

### Eureka Server
```
PORT=8761
```

### Auth Service
```
PORT=8085
SPRING_DATASOURCE_URL=postgresql://...
SPRING_DATASOURCE_USERNAME=neondb_owner
SPRING_DATASOURCE_PASSWORD=...
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
EUREKA_SERVER_URL=https://hospital-eureka-server.onrender.com/eureka/
```

### Patient Service
```
PORT=8086
SPRING_DATASOURCE_URL=postgresql://...
SPRING_DATASOURCE_USERNAME=neondb_owner
SPRING_DATASOURCE_PASSWORD=...
AUTH_SERVICE_URL=https://hospital-auth-service.onrender.com
EUREKA_SERVER_URL=https://hospital-eureka-server.onrender.com/eureka/
```

---

## ✅ Final Checklist

- [ ] All 3 microservices updated to PostgreSQL
- [ ] Changes committed to GitHub
- [ ] Neon database created
- [ ] Eureka Server deployed on Render
- [ ] Auth Service deployed on Render
- [ ] Patient Service deployed on Render
- [ ] Frontend API URL updated
- [ ] Frontend deployed on Vercel
- [ ] Eureka health check passes
- [ ] Auth service health check passes
- [ ] Patient service health check passes
- [ ] Frontend loads without errors
- [ ] Can add/view patients in frontend
- [ ] Can add/view doctors in frontend
- [ ] Can book appointments
- [ ] Can process admissions

---

## 🎉 Success!

When all checks pass, you have:
✅ Microservices running on Render
✅ Database on Neon PostgreSQL
✅ Frontend on Vercel
✅ All services communicating
✅ Production-ready deployment

---

## 📞 Quick Reference Links

- **Render Dashboard:** https://dashboard.render.com
- **Neon Console:** https://console.neon.tech
- **Vercel Dashboard:** https://vercel.com/dashboard
- **Frontend:** See deployment URLs above

---

**Total Deployment Time: 45 minutes - 1 hour**

Good luck! 🚀
