#!/usr/bin/env bash
set -euo pipefail

echo "Scanning for artifacts to remove..."

echo "\n-- target directories found --"
find . -type d -name target -print || true

echo "\n-- *.jar.original files found --"
find . -type f -name '*.jar.original' -print || true

echo "\n-- logs directory status --"
if [ -d logs ]; then
  echo "logs/ exists"
else
  echo "logs/ not found"
fi

read -r -p "Proceed with deletion of these artifacts? [y/N]: " confirm
if [[ "$confirm" != "y" && "$confirm" != "Y" ]]; then
  echo "Aborting cleanup. No files were removed."
  exit 0
fi

echo "\nRemoving target directories..."
find . -type d -name target -prune -exec rm -rf '{}' + || true

echo "Removing *.jar.original files..."
find . -type f -name '*.jar.original' -delete || true

echo "Removing logs/ directory..."
rm -rf logs || true

echo "Removing .class files..."
find . -type f -name '*.class' -delete || true

echo "Removing node_modules directories if present..."
find . -type d -name node_modules -prune -exec rm -rf '{}' + || true

echo "Cleanup complete."
