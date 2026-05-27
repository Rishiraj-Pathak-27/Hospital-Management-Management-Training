# Step-by-Step: Convert MySQL to PostgreSQL for Neon

## 🔍 Step 1: Check Current pom.xml Files

Your microservices currently use MySQL. Here's what to change:

---

## ✏️ Step 2.1: Update Auth Service pom.xml

**File:** `microservices/auth-service/pom.xml`

### Find This Section:
```xml
<dependencies>
    <!-- ... other dependencies ... -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

### Replace With:
```xml
<dependencies>
    <!-- ... other dependencies ... -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.6.0</version>
    </dependency>
</dependencies>
```

---

## ✏️ Step 2.2: Update Patient Service pom.xml

**File:** `microservices/patient-service/pom.xml`

Same changes as auth service above.

---

## ✏️ Step 3.1: Update Auth Service application.properties

**File:** `microservices/auth-service/src/main/resources/application.properties`

### Change From:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/authdb?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=Root@1234
```

### Change To:
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/authdb}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## ✏️ Step 3.2: Update Patient Service application.properties

**File:** `microservices/patient-service/src/main/resources/application.properties`

### Change From:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=Root@1234
```

### Change To:
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/patientdb}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 🗄️ Step 4: Database Tables (PostgreSQL Version)

Neon will auto-create tables with `spring.jpa.hibernate.ddl-auto=update`, but here's the SQL if needed:

### Auth Database Schema
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id INTEGER REFERENCES users(id),
    role_id INTEGER REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);
```

### Patient Database Schema
```sql
CREATE TABLE patients (
    patientId SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER,
    disease VARCHAR(255)
);

CREATE TABLE doctors (
    doctorId SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255)
);

CREATE TABLE appointments (
    appointmentId SERIAL PRIMARY KEY,
    patientId INTEGER REFERENCES patients(patientId),
    doctorId INTEGER REFERENCES doctors(doctorId),
    appointment_date DATE NOT NULL
);

CREATE TABLE wards (
    wardId SERIAL PRIMARY KEY,
    wardName VARCHAR(255) NOT NULL,
    wardType VARCHAR(100),
    severityRank INTEGER,
    totalBeds INTEGER
);

CREATE TABLE beds (
    bedId SERIAL PRIMARY KEY,
    wardId INTEGER REFERENCES wards(wardId),
    bedNumber VARCHAR(50),
    isAvailable BOOLEAN DEFAULT true
);

CREATE TABLE admissions (
    admissionId SERIAL PRIMARY KEY,
    patientId INTEGER REFERENCES patients(patientId),
    severity VARCHAR(50),
    admissionStatus VARCHAR(50),
    wardId INTEGER REFERENCES wards(wardId),
    bedId INTEGER REFERENCES beds(bedId),
    admissionDate DATE DEFAULT CURRENT_DATE,
    notes TEXT
);
```

---

## 🎯 Quick Edit Summary

### For Each Microservice:

1. **pom.xml**: Remove MySQL → Add PostgreSQL dependency
2. **application.properties**: Update datasource config
3. **SQL Files**: Convert MySQL → PostgreSQL syntax

### Key Changes:
- `jdbc:mysql://` → `jdbc:postgresql://`
- `com.mysql.cj.jdbc.Driver` → `org.postgresql.Driver`
- `AUTO_INCREMENT` → `SERIAL`
- Add `spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect`

---

## ✅ Before You Deploy:

1. ✅ Update both pom.xml files
2. ✅ Update both application.properties
3. ✅ Commit changes to GitHub
4. ✅ Create Neon database
5. ✅ Deploy to Render with env variables

**Next:** Follow RENDER_DEPLOYMENT_GUIDE.md
