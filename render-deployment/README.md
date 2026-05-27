# 🎯 Render Deployment Configuration Files

This folder contains templates for deploying microservices to Render.

## Quick Start

1. **Create accounts:**
   - Render: https://dashboard.render.com (connect GitHub)
   - Neon: https://console.neon.tech

2. **Create database (Neon):**
   - Get connection string
   - Create authdb and patientdb

3. **Follow deployment order:**
   - Eureka Server
   - Auth Service
   - Patient Service
   - Frontend (Vercel)

## Files Included

- `eureka-server-render.yml` - Eureka Server config
- `auth-service-render.yml` - Auth Service config
- `patient-service-render.yml` - Patient Service config
- `environment-variables.example` - Template for env vars

## For Detailed Steps

See: `MICROSERVICES_DEPLOYMENT_GUIDE.md` in parent directory
