#!/bin/bash

PROJECT_ROOT="/media/rishiraj/New Volume/CRT_Phase_2"

echo "╔════════════════════════════════════════════════════════════╗"
echo "║         Hospital Microservices - Simple Startup            ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Build all services once
echo "Building all services..."
cd "$PROJECT_ROOT/microservices/eureka-server" && mvn -DskipTests clean package -q
cd "$PROJECT_ROOT/microservices/auth-service" && mvn -DskipTests clean package -q  
cd "$PROJECT_ROOT/microservices/patient-service" && mvn -DskipTests clean package -q
echo "✓ Build complete"
echo ""

# Start Eureka Server
echo "Starting Eureka Server (port 8761)..."
cd "$PROJECT_ROOT/microservices/eureka-server"
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar > /tmp/eureka.log 2>&1 &
EUREKA_PID=$!
echo "✓ Eureka PID: $EUREKA_PID"
sleep 8

# Start Auth Service
echo "Starting Auth Service (port 8085)..."
cd "$PROJECT_ROOT/microservices/auth-service"
export JWT_SECRET="hospital-secret-key-2024"
export JWT_EXPIRATION="86400000"
export DB_USERNAME="root"
export DB_PASSWORD="Root@1234"
export DB_URL="jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=UTC"
java -jar target/auth-service-0.0.1-SNAPSHOT.jar > /tmp/auth.log 2>&1 &
AUTH_PID=$!
echo "✓ Auth Service PID: $AUTH_PID"
sleep 5

# Start Patient Service
echo "Starting Patient Service (port 8086)..."
cd "$PROJECT_ROOT/microservices/patient-service"
export DB_USERNAME="root"
export DB_PASSWORD="Root@1234"
export DB_URL="jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export AUTH_SERVICE_URL="http://localhost:8085"
java -jar target/patient-service-0.0.1-SNAPSHOT.jar > /tmp/patient.log 2>&1 &
PATIENT_PID=$!
echo "✓ Patient Service PID: $PATIENT_PID"
sleep 5

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║              All Services Started!                         ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""
echo "Service URLs:"
echo "  Eureka Dashboard:  http://localhost:8761"
echo "  Auth Service:      http://localhost:8085"  
echo "  Patient Service:   http://localhost:8086"
echo ""
echo "View logs:"
echo "  tail -f /tmp/eureka.log"
echo "  tail -f /tmp/auth.log"
echo "  tail -f /tmp/patient.log"
echo ""
echo "Stop services:"
echo "  kill $EUREKA_PID $AUTH_PID $PATIENT_PID"
echo ""
echo "Press Ctrl+C to stop all services."
trap "echo ''; echo 'Stopping all services...'; kill $EUREKA_PID $AUTH_PID $PATIENT_PID 2>/dev/null; exit 0" SIGINT
wait
