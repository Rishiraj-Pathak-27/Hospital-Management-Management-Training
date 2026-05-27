#!/bin/bash

# Auth Service Startup Script

echo "╔════════════════════════════════════════════════════════════╗"
echo "║          Auth Service - Startup Script                    ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Check if JAR exists
if [ ! -f "target/auth-service-0.0.1-SNAPSHOT.jar" ]; then
    echo "❌ JAR file not found. Building..."
    mvn clean package -DskipTests
fi

# Load confidential settings from .env if present
if [ -f ".env" ]; then
    set -a
    . ".env"
    set +a
fi

# Use environment variables or defaults
DB_URL=${DB_URL:-jdbc:mysql://localhost:3306/auth?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
DB_USERNAME=${DB_USERNAME:-root}
DB_PASSWORD=${DB_PASSWORD:-}
JWT_SECRET=${JWT_SECRET:-}
JWT_EXPIRATION=${JWT_EXPIRATION:-86400000}

echo "📋 Configuration:"
echo "   • Service Port: 8085"
echo "   • Database URL: $DB_URL"
echo "   • Username: $DB_USERNAME"
echo "   • Password: ${DB_PASSWORD:-(set via .env)}"
echo ""

# Set environment and run
export DB_URL
export DB_USERNAME
export DB_PASSWORD
export JWT_SECRET
export JWT_EXPIRATION

echo "🚀 Starting Auth Service..."
java -jar target/auth-service-0.0.1-SNAPSHOT.jar

