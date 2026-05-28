#!/bin/bash

echo "╔════════════════════════════════════════════════════════════╗"
echo "║    Hospital Microservices - Complete Startup Script        ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Get the project root directory
PROJECT_ROOT="/media/rishiraj/New Volume/CRT_Phase_2"

# Set environment variables
export JWT_SECRET="hospital-secret-key-2024"
export JWT_EXPIRATION="86400000"
export DB_PASSWORD=""
export DB_USERNAME="root"
export DB_URL="jdbc:mysql://localhost:3306/auth?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"

echo "Environment Variables Set:"
echo "  JWT_SECRET: $JWT_SECRET"
echo "  DB_USERNAME: $DB_USERNAME"
echo "  DB_URL: $DB_URL"
echo ""

# Determine which Maven command to use
if command -v mvn &> /dev/null; then
    MAVEN_CMD="mvn"
elif [ -f "$PROJECT_ROOT/microservices/eureka-server/mvnw" ]; then
    MAVEN_CMD="./mvnw"
else
    echo "Error: Maven is not installed and mvnw wrapper is not found."
    exit 1
fi

echo "Using Maven command: $MAVEN_CMD"
echo ""

echo "Step 1: Building Eureka Server..."
cd "$PROJECT_ROOT/microservices/eureka-server"
$MAVEN_CMD -DskipTests package
if [ $? -ne 0 ]; then
    echo "Error building Eureka Server. Exiting."
    exit 1
fi

echo ""
echo "Step 2: Building Auth Service..."
cd "$PROJECT_ROOT/microservices/auth-service"
$MAVEN_CMD -DskipTests package
if [ $? -ne 0 ]; then
    echo "Error building Auth Service. Exiting."
    exit 1
fi

echo ""
echo "Step 3: Building Patient Service..."
cd "$PROJECT_ROOT/microservices/patient-service"
$MAVEN_CMD -DskipTests package
if [ $? -ne 0 ]; then
    echo "Error building Patient Service. Exiting."
    exit 1
fi

echo ""
echo "Verifying JAR files..."
if [ ! -f "$PROJECT_ROOT/microservices/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar" ]; then
    echo "Error: Eureka Server JAR not found!"
    exit 1
fi
if [ ! -f "$PROJECT_ROOT/microservices/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar" ]; then
    echo "Error: Auth Service JAR not found!"
    exit 1
fi
if [ ! -f "$PROJECT_ROOT/microservices/patient-service/target/patient-service-0.0.1-SNAPSHOT.jar" ]; then
    echo "Error: Patient Service JAR not found!"
    exit 1
fi
echo "✓ All JAR files found"

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║        All Services Built Successfully!                    ║"
echo "║  Starting services (output shown below)...                 ║"
echo "║  - Eureka Server:   http://localhost:8761                  ║"
echo "║  - Auth Service:    http://localhost:8085                  ║"
echo "║  - Patient Service: http://localhost:8086                  ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Create logs directory
mkdir -p "$PROJECT_ROOT/logs"

# Clear previous logs
rm -f "$PROJECT_ROOT/logs/"*.log

# Start Eureka Server
echo "═══════════════════════════════════════════════════════════"
echo "Starting Eureka Server..."
echo "═══════════════════════════════════════════════════════════"
cd "$PROJECT_ROOT/microservices/eureka-server"
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar 2>&1 | tee "$PROJECT_ROOT/logs/eureka-server.log" &
EUREKA_PID=$!
echo "✓ Eureka Server started with PID: $EUREKA_PID"
echo ""

# Wait for Eureka to be ready
echo "[*] Waiting for Eureka Server to be ready (12 seconds)..."
sleep 12

# Start Auth Service with JWT_SECRET
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "Starting Auth Service..."
echo "═══════════════════════════════════════════════════════════"
cd "$PROJECT_ROOT/microservices/auth-service"
JWT_SECRET="hospital-secret-key-2024" \
JWT_EXPIRATION="86400000" \
DB_USERNAME="root" \
DB_PASSWORD="" \
DB_URL="jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.mysql.cj.jdbc.Driver" \
SPRING_JPA_DIALECT="org.hibernate.dialect.MySQL8Dialect" \
java -jar target/auth-service-0.0.1-SNAPSHOT.jar 2>&1 | tee "$PROJECT_ROOT/logs/auth-service.log" &
AUTH_PID=$!
echo "✓ Auth Service started with PID: $AUTH_PID"
echo ""

# Wait for Auth Service to register
echo "[*] Waiting for Auth Service to register with Eureka (7 seconds)..."
sleep 7

# Start Patient Service  
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "Starting Patient Service..."
echo "═══════════════════════════════════════════════════════════"
cd "$PROJECT_ROOT/microservices/patient-service"
DB_USERNAME="root" \
DB_PASSWORD="" \
DB_URL="jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.mysql.cj.jdbc.Driver" \
SPRING_JPA_DIALECT="org.hibernate.dialect.MySQL8Dialect" \
java -jar target/patient-service-0.0.1-SNAPSHOT.jar 2>&1 | tee "$PROJECT_ROOT/logs/patient-service.log" &
PATIENT_PID=$!
echo "✓ Patient Service started with PID: $PATIENT_PID"
echo ""

# Wait for Patient Service to register
echo "[*] Waiting for Patient Service to register with Eureka (7 seconds)..."
sleep 7

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║              All Services Launched Successfully!           ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""
echo "Service PIDs:"
echo "  Eureka Server:   $EUREKA_PID"
echo "  Auth Service:    $AUTH_PID"
echo "  Patient Service: $PATIENT_PID"
echo ""
echo "Service URLs:"
echo "  Eureka Dashboard:  http://localhost:8761"
echo "  Auth Service:      http://localhost:8085"
echo "  Patient Service:   http://localhost:8086"
echo ""
echo "View logs (in another terminal):"
echo "  tail -f '$PROJECT_ROOT/logs/eureka-server.log'"
echo "  tail -f '$PROJECT_ROOT/logs/auth-service.log'"
echo "  tail -f '$PROJECT_ROOT/logs/patient-service.log'"
echo ""
echo "Stop services:"
echo "  kill $EUREKA_PID $AUTH_PID $PATIENT_PID"
echo ""
echo "[✓] Navigate to http://localhost:8761 to see registered services!"
echo ""

# Keep the script running
echo "Press Ctrl+C to stop all services..."
trap "echo ''; echo 'Stopping all services...'; kill $EUREKA_PID $AUTH_PID $PATIENT_PID 2>/dev/null; echo 'All services stopped.'; exit 0" SIGINT

# Wait for all background processes
wait
