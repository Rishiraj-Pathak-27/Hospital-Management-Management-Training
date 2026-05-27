#!/bin/bash

# 🚀 Microservices Deployment Checklist
# Use this to track your progress through the deployment

echo "╔════════════════════════════════════════════════════════════╗"
echo "║  🏥 Hospital Management System - Deployment Checklist     ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Checklist arrays
declare -a steps=(
    "📊 STEP 1: Set up Neon PostgreSQL"
    "🎯 STEP 2: Deploy Eureka Server"
    "🔐 STEP 3: Deploy Auth Service"
    "👥 STEP 4: Deploy Patient Service"
    "🎨 STEP 5: Deploy Frontend (Vercel)"
    "✅ STEP 6: Test & Verify"
)

declare -a completed=(false false false false false false)

show_menu() {
    echo ""
    echo "════════════════════════════════════════════════════════"
    echo "📋 Deployment Progress:"
    echo "════════════════════════════════════════════════════════"
    
    for i in "${!steps[@]}"; do
        if [ "${completed[$i]}" = true ]; then
            echo -e "${GREEN}✅${NC} ${steps[$i]}"
        else
            echo -e "${RED}⬜${NC} ${steps[$i]}"
        fi
    done
    
    echo ""
    echo "════════════════════════════════════════════════════════"
    echo "Commands:"
    echo "  1-6  : Mark step as completed"
    echo "  info : Show detailed info about next step"
    echo "  urls : Show service URLs"
    echo "  test : Run backend tests"
    echo "  q    : Quit"
    echo ""
}

show_step_info() {
    case $1 in
        1)
            echo ""
            echo "📊 STEP 1: Set up Neon PostgreSQL"
            echo "───────────────────────────────────"
            echo "1. Visit: https://console.neon.tech"
            echo "2. Sign up with GitHub"
            echo "3. Create new project (Free tier)"
            echo "4. Create two databases:"
            echo "   - authdb"
            echo "   - patientdb"
            echo "5. Get connection string: postgresql://user:pass@..."
            echo ""
            echo "⏱️  Time: ~5 minutes"
            echo ""
            ;;
        2)
            echo ""
            echo "🎯 STEP 2: Deploy Eureka Server"
            echo "────────────────────────────────"
            echo "1. Go to: https://dashboard.render.com"
            echo "2. Click 'New +' → 'Web Service'"
            echo "3. Select your GitHub repository"
            echo ""
            echo "Configuration:"
            echo "  Name: eureka-server"
            echo "  Build: cd microservices/eureka-server && mvn clean package -DskipTests"
            echo "  Start: cd microservices/eureka-server && java -jar target/eureka-server-*.jar"
            echo "  Port: 8761"
            echo ""
            echo "⏱️  Time: ~5 minutes (cold start)"
            echo "📝 See: render-deployment/eureka-server-setup.md"
            echo ""
            ;;
        3)
            echo ""
            echo "🔐 STEP 3: Deploy Auth Service"
            echo "───────────────────────────────"
            echo "Prerequisites:"
            echo "  ✓ Eureka Server running"
            echo "  ✓ Neon PostgreSQL with authdb"
            echo ""
            echo "Configuration:"
            echo "  Name: auth-service"
            echo "  Build: cd microservices/auth-service && mvn clean package -DskipTests"
            echo "  Start: cd microservices/auth-service && java -jar target/auth-service-*.jar"
            echo "  Port: 8085"
            echo ""
            echo "Environment Variables:"
            echo "  DB_URL=postgresql://user:pass@...authdb"
            echo "  DB_USERNAME=user"
            echo "  DB_PASSWORD=password"
            echo "  JWT_SECRET=your-32-character-secret-key"
            echo "  EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/"
            echo ""
            echo "⏱️  Time: ~5 minutes"
            echo "📝 See: render-deployment/auth-service-setup.md"
            echo ""
            ;;
        4)
            echo ""
            echo "👥 STEP 4: Deploy Patient Service"
            echo "──────────────────────────────────"
            echo "Prerequisites:"
            echo "  ✓ Eureka Server running"
            echo "  ✓ Auth Service running"
            echo "  ✓ Neon PostgreSQL with patientdb"
            echo ""
            echo "Configuration:"
            echo "  Name: patient-service"
            echo "  Build: cd microservices/patient-service && mvn clean package -DskipTests"
            echo "  Start: cd microservices/patient-service && java -jar target/patient-service-*.jar"
            echo "  Port: 8086"
            echo ""
            echo "Environment Variables:"
            echo "  DB_URL=postgresql://user:pass@...patientdb"
            echo "  DB_USERNAME=user"
            echo "  DB_PASSWORD=password"
            echo "  AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com"
            echo "  EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/"
            echo ""
            echo "⏱️  Time: ~5 minutes"
            echo "📝 See: render-deployment/patient-service-setup.md"
            echo ""
            ;;
        5)
            echo ""
            echo "🎨 STEP 5: Deploy Frontend (Vercel)"
            echo "────────────────────────────────────"
            echo "Prerequisites:"
            echo "  ✓ All backend services running"
            echo "  ✓ Code pushed to GitHub"
            echo ""
            echo "Update API URL:"
            echo "  File: hospital-frontend/app.js (line 3)"
            echo "  Change: const API_BASE_URL = 'https://patient-service-xxxx.onrender.com/api/patients';"
            echo ""
            echo "Deploy:"
            echo "  1. Go to: https://vercel.com/dashboard"
            echo "  2. Click 'Add New' → 'Project'"
            echo "  3. Import your GitHub repository"
            echo "  4. Select Root Directory: 'hospital-frontend'"
            echo "  5. Click Deploy"
            echo ""
            echo "⏱️  Time: ~2 minutes"
            echo ""
            ;;
        6)
            echo ""
            echo "✅ STEP 6: Test & Verify"
            echo "────────────────────────"
            echo "Health Checks:"
            echo ""
            echo "1. Eureka Server:"
            echo "   curl https://eureka-server-xxxx.onrender.com/"
            echo ""
            echo "2. Auth Service:"
            echo "   curl https://auth-service-xxxx.onrender.com/api/auth/health"
            echo ""
            echo "3. Patient Service:"
            echo "   curl https://patient-service-xxxx.onrender.com/api/patients/health"
            echo ""
            echo "Frontend Tests:"
            echo "  1. Open frontend URL in browser"
            echo "  2. Try adding a patient"
            echo "  3. Verify it appears in list"
            echo "  4. Check other tabs (Doctors, Appointments, etc.)"
            echo ""
            echo "Service Registration:"
            echo "  Visit: https://eureka-server-xxxx.onrender.com/"
            echo "  Should show:"
            echo "    - AUTH-SERVICE (8085)"
            echo "    - PATIENT-SERVICE (8086)"
            echo ""
            ;;
    esac
}

show_urls() {
    echo ""
    echo "════════════════════════════════════════════════════════"
    echo "🔗 Service URLs (After Deployment)"
    echo "════════════════════════════════════════════════════════"
    echo ""
    echo "📊 Eureka Server:"
    echo "   https://eureka-server-xxxx.onrender.com"
    echo ""
    echo "🔐 Auth Service:"
    echo "   https://auth-service-xxxx.onrender.com"
    echo "   Health: /api/auth/health"
    echo ""
    echo "👥 Patient Service:"
    echo "   https://patient-service-xxxx.onrender.com"
    echo "   Health: /api/patients/health"
    echo "   API: /api/patients/*"
    echo ""
    echo "🎨 Frontend:"
    echo "   https://hospital-frontend-xxxx.vercel.app"
    echo ""
    echo "💾 Database:"
    echo "   Neon PostgreSQL: ep-xxxxx.neon.tech"
    echo "   Databases: authdb, patientdb"
    echo ""
}

run_tests() {
    echo ""
    echo "════════════════════════════════════════════════════════"
    echo "🧪 Testing Backend Services"
    echo "════════════════════════════════════════════════════════"
    echo ""
    
    read -p "Enter Eureka Server URL (e.g., https://eureka-server-xxxx.onrender.com): " eureka_url
    read -p "Enter Auth Service URL (e.g., https://auth-service-xxxx.onrender.com): " auth_url
    read -p "Enter Patient Service URL (e.g., https://patient-service-xxxx.onrender.com): " patient_url
    
    echo ""
    echo "Testing..."
    echo ""
    
    echo -n "Testing Eureka: "
    if curl -s "$eureka_url/" > /dev/null; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAILED${NC}"
    fi
    
    echo -n "Testing Auth Service: "
    RESPONSE=$(curl -s "$auth_url/api/auth/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAILED${NC}"
    fi
    
    echo -n "Testing Patient Service: "
    RESPONSE=$(curl -s "$patient_url/api/patients/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FAILED${NC}"
    fi
    
    echo ""
}

# Main loop
while true; do
    show_menu
    read -p "Enter command: " input
    
    case $input in
        1|2|3|4|5|6)
            completed[$((input-1))]=true
            show_step_info "$input"
            ;;
        info)
            read -p "Show info for step (1-6): " step
            show_step_info "$step"
            ;;
        urls)
            show_urls
            ;;
        test)
            run_tests
            ;;
        q)
            echo ""
            echo "🎉 Good luck with your deployment!"
            echo ""
            exit 0
            ;;
        *)
            echo -e "${RED}Invalid command${NC}"
            ;;
    esac
done
