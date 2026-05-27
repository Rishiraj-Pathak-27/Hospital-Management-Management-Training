# ✅ COMPLETE STEP-BY-STEP: Deploy to Render with Neon DB

## 📌 FIRST: Create Neon Database

### Step 1: Create Neon Account
- Go to: https://console.neon.tech
- Sign up with Google/GitHub
- Create new project

### Step 2: Get Your Connection String
After creating project, you'll see:
```
Connection string:
postgresql://neondb_owner:XXXXXXXXXXXX@ep-xxx-xxx.neon.tech/neondb?sslmode=require
```

**SAVE THIS! You'll need it for all 3 microservices.**

Parse it as:
- **Host:** ep-xxx-xxx.neon.tech
- **Database:** neondb
- **User:** neondb_owner
- **Password:** XXXXXXXXXXXX
- **Port:** 5432

---

## 🔧 CHANGE 1: Update Auth Service pom.xml

**File:** `microservices/auth-service/pom.xml`

**FIND (around line 85):**
```xml
        <!-- MySQL Connector -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
```

**REPLACE WITH:**
```xml
        <!-- PostgreSQL Connector -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.6.0</version>
            <scope>runtime</scope>
        </dependency>
```

---

## 🔧 CHANGE 2: Update Auth Service application.properties

**File:** `microservices/auth-service/src/main/resources/application.properties`

**FIND:**
```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

**REPLACE WITH:**
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/neondb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**ALSO UPDATE:**
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

TO:
```properties
eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
```

---

## 🔧 CHANGE 3: Update Patient Service pom.xml

**File:** `microservices/patient-service/pom.xml`

**FIND (around line 70):**
```xml
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
```

**REPLACE WITH:**
```xml
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.6.0</version>
            <scope>runtime</scope>
        </dependency>
```

---

## 🔧 CHANGE 4: Update Patient Service application.properties

**File:** `microservices/patient-service/src/main/resources/application.properties`

**FIND:**
```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

**REPLACE WITH:**
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/neondb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**ALSO UPDATE:**
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

TO:
```properties
eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
```

---

## 🚀 DEPLOY STEP 1: Eureka Server to Render

### 1. Go to Render Dashboard
- https://dashboard.render.com
- Click **New +** → **Web Service**

### 2. Connect GitHub
- Select your repo
- Click **Connect**

### 3. Configure Service
Fill in:
```
Name: hospital-eureka-server
Build Command: cd microservices/eureka-server && mvn clean package -DskipTests
Start Command: java -cp target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout) org.springframework.boot.loader.JarLauncher
```

**OR simpler:**
```
Build Command: mvn clean package -DskipTests
Start Command: java -jar microservices/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
```

### 4. Add Environment Variables
```
PORT=8761
```

### 5. Deploy
- Click **Create Web Service**
- Wait 5-10 minutes
- **SAVE URL:** `https://hospital-eureka-server.onrender.com`

---

## 🚀 DEPLOY STEP 2: Auth Service to Render

### 1. New Web Service
- Similar to above

### 2. Configure
```
Name: hospital-auth-service
Build Command: mvn clean package -DskipTests
Start Command: java -jar microservices/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar
```

### 3. Environment Variables
Replace with **YOUR NEON values**:
```
PORT=8085

SPRING_DATASOURCE_URL=postgresql://neondb_owner:YOUR_PASSWORD@ep-xxx-xxx.neon.tech/neondb?sslmode=require
SPRING_DATASOURCE_USERNAME=neondb_owner
SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD

EUREKA_SERVER_URL=https://hospital-eureka-server.onrender.com/eureka/

JWT_SECRET=your-super-secret-key-here-make-it-long
JWT_EXPIRATION=86400000
```

### 4. Deploy
- Click **Create Web Service**
- Wait 5-10 minutes
- **SAVE URL:** `https://hospital-auth-service.onrender.com`

---

## 🚀 DEPLOY STEP 3: Patient Service to Render

### 1. New Web Service
- Similar to above

### 2. Configure
```
Name: hospital-patient-service
Build Command: mvn clean package -DskipTests
Start Command: java -jar microservices/patient-service/target/patient-service-0.0.1-SNAPSHOT.jar
```

### 3. Environment Variables
Replace with **YOUR NEON values**:
```
PORT=8086

SPRING_DATASOURCE_URL=postgresql://neondb_owner:YOUR_PASSWORD@ep-xxx-xxx.neon.tech/neondb?sslmode=require
SPRING_DATASOURCE_USERNAME=neondb_owner
SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD

AUTH_SERVICE_URL=https://hospital-auth-service.onrender.com

EUREKA_SERVER_URL=https://hospital-eureka-server.onrender.com/eureka/
```

### 4. Deploy
- Click **Create Web Service**
- Wait 5-10 minutes
- **SAVE URL:** `https://hospital-patient-service.onrender.com`

---

## 🚀 DEPLOY STEP 4: Update Frontend & Deploy to Vercel

### 1. Update Frontend API URL

**File:** `hospital-frontend/app.js`

**FIND (line 3):**
```javascript
const API_BASE_URL = 'https://hospital-management-management-training.onrender.com/api/patients';
```

**REPLACE WITH:**
```javascript
const API_BASE_URL = 'https://hospital-patient-service.onrender.com/api/patients';
```

### 2. Deploy to Vercel
```bash
cd hospital-frontend
npm install -g vercel
vercel
```

Follow prompts → Get URL

---

## ✅ Your Final Architecture

```
Frontend (Vercel):
https://hospital-frontend-xxx.vercel.app

    ↓ API Calls to

Patient Service (Render Port 8086):
https://hospital-patient-service.onrender.com/api/patients

    ↓ Validates JWT with

Auth Service (Render Port 8085):
https://hospital-auth-service.onrender.com

    ↓ Registers with

Eureka Server (Render Port 8761):
https://hospital-eureka-server.onrender.com

    ↓ All access

Neon PostgreSQL Database:
postgresql://neondb_owner:****@ep-xxx-xxx.neon.tech/neondb
```

---

## 🧪 Test After Deployment

```bash
# Test Eureka
curl https://hospital-eureka-server.onrender.com

# Test Auth
curl https://hospital-auth-service.onrender.com/api/auth/health

# Test Patient
curl https://hospital-patient-service.onrender.com/api/patients/health

# Test Frontend
visit https://hospital-frontend-xxx.vercel.app
```

---

## ⚠️ IMPORTANT NOTES

1. **Use same Neon connection string** for all microservices
2. **Same EUREKA_SERVER_URL** for both services
3. **Update frontend to use new patient-service URL**
4. **Commit changes to GitHub** before deploying
5. **Wait 2-3 minutes** for cold start on first access

---

## 🚨 If Services Won't Start

1. Check Render logs for errors
2. Verify Neon connection string is correct
3. Make sure environment variables are set
4. Try restarting service in Render dashboard
5. Check that Maven build completed successfully

**Done!** 🎉 Now your entire system is on Render with Neon database!
