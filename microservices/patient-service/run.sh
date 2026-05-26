#!/bin/bash

# Patient Service Startup Script

echo "╔════════════════════════════════════════════════════════════╗"
echo "║        Patient Service - Startup Script                   ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

if [ ! -f "target/patient-service-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR file not found. Building..."
    mvn clean package -DskipTests
fi

AUTH_ENV_FILE="../auth-service/.env"

if [ -f "$AUTH_ENV_FILE" ]; then
    set -a
    . "$AUTH_ENV_FILE"
    set +a
fi

AUTH_SERVICE_DB_URL="${DB_URL:-}"

if [ -f ".env" ]; then
    set -a
    . ".env"
    set +a
fi

PATIENT_DB_URL_DEFAULT="jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"

if [ -n "${DATABASE_URL:-}" ]; then
    DB_URL="$DATABASE_URL"
elif [ -n "${PATIENT_DB_URL:-}" ]; then
    DB_URL="$PATIENT_DB_URL"
elif [ -n "${DB_URL:-}" ] && [ "${DB_URL}" != "${AUTH_SERVICE_DB_URL}" ]; then
    DB_URL="$DB_URL"
else
    DB_URL="$PATIENT_DB_URL_DEFAULT"
fi

DB_USERNAME=${DATABASE_USER:-${DB_USERNAME:-root}}
DB_PASSWORD=${DATABASE_PASSWORD:-${DB_PASSWORD:-}}
AUTH_SERVICE_URL=${AUTH_SERVICE_URL:-http://localhost:8085}

export DB_URL
export DB_USERNAME
export DB_PASSWORD
export AUTH_SERVICE_URL
export JWT_SECRET
export JWT_EXPIRATION

echo "Starting Patient Service on port 8086..."
java -jar target/patient-service-0.0.1-SNAPSHOT.jar
