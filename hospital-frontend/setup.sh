#!/bin/bash

# Hospital Management System - Frontend Setup Script

echo "🏥 Hospital Management System - Frontend Setup"
echo "=============================================="
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js from https://nodejs.org"
    exit 1
fi

echo "✅ Node.js version: $(node --version)"
echo ""

# Install dependencies
echo "📦 Installing dependencies..."
npm install

echo ""
echo "✅ Setup complete!"
echo ""
echo "🚀 Next steps:"
echo "   1. For local development: npm run dev"
echo "   2. To deploy to Vercel: vercel"
echo "   3. Visit: http://localhost:8000"
echo ""
echo "📖 For more information, see README.md"
