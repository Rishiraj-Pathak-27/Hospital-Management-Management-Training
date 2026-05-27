#!/bin/bash

# Hospital Management System - Automated Update Script
# This script updates microservices from MySQL to PostgreSQL

set -e

echo "🏥 Hospital Management System - MySQL to PostgreSQL Converter"
echo "=============================================================="
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Base path
BASE_PATH="/media/rishiraj/New Volume/CRT_Phase_2/microservices"

echo -e "${BLUE}Step 1: Updating Auth Service${NC}"
echo "================================"

# Update Auth Service pom.xml
echo "Updating auth-service/pom.xml..."
cd "$BASE_PATH/auth-service"

# Replace MySQL with PostgreSQL in pom.xml
sed -i 's/<groupId>com\.mysql<\/groupId>/<groupId>org.postgresql<\/groupId>/g' pom.xml
sed -i 's/<artifactId>mysql-connector-j<\/artifactId>/<artifactId>postgresql<\/artifactId>/g' pom.xml
sed -i '/<artifactId>postgresql<\/artifactId>/a\            <version>42.6.0<\/version>' pom.xml

echo -e "${GREEN}✅ Auth Service pom.xml updated${NC}"

# Update application.properties
echo "Updating auth-service/src/main/resources/application.properties..."
cat > src/main/resources/application.properties << 'EOF'
server.port=8085
spring.application.name=auth-service

# Database Configuration
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

# JWT Configuration
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}

# Logging
logging.level.root=WARN
logging.level.com.hospital.auth=INFO
logging.level.org.springframework.security=DEBUG

# Eureka configuration
eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
EOF

echo -e "${GREEN}✅ Auth Service application.properties updated${NC}"

echo ""
echo -e "${BLUE}Step 2: Updating Patient Service${NC}"
echo "=================================="

# Update Patient Service pom.xml
echo "Updating patient-service/pom.xml..."
cd "$BASE_PATH/patient-service"

# Replace MySQL with PostgreSQL in pom.xml
sed -i 's/<groupId>com\.mysql<\/groupId>/<groupId>org.postgresql<\/groupId>/g' pom.xml
sed -i 's/<artifactId>mysql-connector-j<\/artifactId>/<artifactId>postgresql<\/artifactId>/g' pom.xml
sed -i '/<artifactId>postgresql<\/artifactId>/a\            <version>42.6.0<\/version>' pom.xml

echo -e "${GREEN}✅ Patient Service pom.xml updated${NC}"

# Update application.properties
echo "Updating patient-service/src/main/resources/application.properties..."
cat > src/main/resources/application.properties << 'EOF'
server.port=8086
spring.application.name=patient-service

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
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=never

auth.service.url=${AUTH_SERVICE_URL:http://localhost:8085}

logging.level.root=WARN
logging.level.com.hospital.patient=INFO
logging.level.org.springframework.security=INFO

# Eureka configuration
eureka.client.service-url.defaultZone=${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
eureka.instance.lease-renewal-interval-in-seconds=10
eureka.client.registry-fetch-interval-seconds=5
EOF

echo -e "${GREEN}✅ Patient Service application.properties updated${NC}"

echo ""
echo -e "${YELLOW}Step 3: Configuration Summary${NC}"
echo "=============================="
echo ""
echo -e "${BLUE}Changes Made:${NC}"
echo "✅ Replaced MySQL driver with PostgreSQL"
echo "✅ Updated Hibernate dialect to PostgreSQL"
echo "✅ Updated connection string to use environment variables"
echo "✅ Added EUREKA_SERVER_URL configuration"
echo ""
echo -e "${BLUE}Environment Variables Used:${NC}"
echo "  SPRING_DATASOURCE_URL"
echo "  SPRING_DATASOURCE_USERNAME"
echo "  SPRING_DATASOURCE_PASSWORD"
echo "  EUREKA_SERVER_URL"
echo "  AUTH_SERVICE_URL (Patient Service only)"
echo "  JWT_SECRET (Auth Service only)"
echo "  JWT_EXPIRATION (Auth Service only)"
echo ""
echo -e "${YELLOW}Next Steps:${NC}"
echo "1. Commit changes to GitHub"
echo "2. Create Neon database account"
echo "3. Deploy Eureka Server to Render"
echo "4. Deploy Auth Service to Render"
echo "5. Deploy Patient Service to Render"
echo "6. Update frontend API URL"
echo "7. Deploy frontend to Vercel"
echo ""
echo -e "${GREEN}✅ All microservices updated successfully!${NC}"
