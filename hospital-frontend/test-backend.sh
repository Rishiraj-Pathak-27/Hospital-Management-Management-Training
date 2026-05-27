#!/bin/bash

# Hospital Management System - Frontend Test Script
# This script tests the frontend connectivity and functionality

API_BASE="https://hospital-management-management-training.onrender.com/api/patients"
BLUE='\033[0;34m'
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}🏥 Hospital Management System - API Test Suite${NC}"
echo "=================================================="
echo ""

# Test 1: Health Check
echo -e "${YELLOW}Test 1: Checking API Health Status...${NC}"
HEALTH_RESPONSE=$(curl -s -w "\n%{http_code}" "${API_BASE}/health")
HEALTH_CODE=$(echo "$HEALTH_RESPONSE" | tail -n1)
HEALTH_BODY=$(echo "$HEALTH_RESPONSE" | head -n-1)

if [ "$HEALTH_CODE" = "200" ]; then
    echo -e "${GREEN}✅ API Health Check Passed${NC}"
    echo "Response: $HEALTH_BODY"
else
    echo -e "${RED}❌ API Health Check Failed (Status: $HEALTH_CODE)${NC}"
fi
echo ""

# Test 2: Fetch Patients
echo -e "${YELLOW}Test 2: Fetching Patients...${NC}"
PATIENTS_RESPONSE=$(curl -s -w "\n%{http_code}" "${API_BASE}/patients")
PATIENTS_CODE=$(echo "$PATIENTS_RESPONSE" | tail -n1)
PATIENTS_BODY=$(echo "$PATIENTS_RESPONSE" | head -n-1)

if [ "$PATIENTS_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Patients Fetch Passed${NC}"
    COUNT=$(echo "$PATIENTS_BODY" | wc -l)
    echo "Response: $PATIENTS_BODY"
else
    echo -e "${RED}❌ Patients Fetch Failed (Status: $PATIENTS_CODE)${NC}"
fi
echo ""

# Test 3: Fetch Doctors
echo -e "${YELLOW}Test 3: Fetching Doctors...${NC}"
DOCTORS_RESPONSE=$(curl -s -w "\n%{http_code}" "${API_BASE}/doctors")
DOCTORS_CODE=$(echo "$DOCTORS_RESPONSE" | tail -n1)

if [ "$DOCTORS_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Doctors Fetch Passed${NC}"
else
    echo -e "${RED}❌ Doctors Fetch Failed (Status: $DOCTORS_CODE)${NC}"
fi
echo ""

# Test 4: Fetch Appointments
echo -e "${YELLOW}Test 4: Fetching Appointments...${NC}"
APPOINTMENTS_RESPONSE=$(curl -s -w "\n%{http_code}" "${API_BASE}/appointments")
APPOINTMENTS_CODE=$(echo "$APPOINTMENTS_RESPONSE" | tail -n1)

if [ "$APPOINTMENTS_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Appointments Fetch Passed${NC}"
else
    echo -e "${RED}❌ Appointments Fetch Failed (Status: $APPOINTMENTS_CODE)${NC}"
fi
echo ""

# Test 5: Fetch Wards
echo -e "${YELLOW}Test 5: Fetching Wards...${NC}"
WARDS_RESPONSE=$(curl -s -w "\n%{http_code}" "${API_BASE}/wards")
WARDS_CODE=$(echo "$WARDS_RESPONSE" | tail -n1)

if [ "$WARDS_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Wards Fetch Passed${NC}"
else
    echo -e "${RED}❌ Wards Fetch Failed (Status: $WARDS_CODE)${NC}"
fi
echo ""

# Test 6: Fetch Beds
echo -e "${YELLOW}Test 6: Fetching Beds...${NC}"
BEDS_RESPONSE=$(curl -s -w "\n%{http_code}" "${API_BASE}/beds")
BEDS_CODE=$(echo "$BEDS_RESPONSE" | tail -n1)

if [ "$BEDS_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Beds Fetch Passed${NC}"
else
    echo -e "${RED}❌ Beds Fetch Failed (Status: $BEDS_CODE)${NC}"
fi
echo ""

# Summary
echo "=================================================="
echo -e "${BLUE}Test Summary${NC}"
echo "=================================================="
PASSED=0
FAILED=0

[[ "$HEALTH_CODE" = "200" ]] && ((PASSED++)) || ((FAILED++))
[[ "$PATIENTS_CODE" = "200" ]] && ((PASSED++)) || ((FAILED++))
[[ "$DOCTORS_CODE" = "200" ]] && ((PASSED++)) || ((FAILED++))
[[ "$APPOINTMENTS_CODE" = "200" ]] && ((PASSED++)) || ((FAILED++))
[[ "$WARDS_CODE" = "200" ]] && ((PASSED++)) || ((FAILED++))
[[ "$BEDS_CODE" = "200" ]] && ((PASSED++)) || ((FAILED++))

echo -e "${GREEN}✅ Tests Passed: $PASSED${NC}"
echo -e "${RED}❌ Tests Failed: $FAILED${NC}"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}🎉 All tests passed! Your backend is ready.${NC}"
    echo "The frontend should work correctly with your backend."
else
    echo -e "${RED}⚠️  Some tests failed. Please check your backend configuration.${NC}"
fi
