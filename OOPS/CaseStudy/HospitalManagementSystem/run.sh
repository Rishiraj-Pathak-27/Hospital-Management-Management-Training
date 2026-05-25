#!/bin/bash
# JavaFX Application Runner Script

JAVAFX_PATH="/home/rishiraj/Downloads/javafx-26_linux-x64_bin-sdk/javafx-sdk-26.0.1/lib"
JAVA_PATH="/home/rishiraj/.jdk/jdk-25/bin/java"
MYSQL_DRIVER="lib/mysql-connector-j-9.7.0.jar"
PG_DRIVER="lib/postgresql-42.7.5.jar"

# Load environment variables from .env
if [ -f .env ]; then
    echo "Loading .env file..."
    export $(grep -v '^#' .env | xargs)
else
    echo "Warning: .env file not found. Using default env vars (MySQL local)."
fi

# Compile
echo "Compiling Main.java..."
"${JAVA_PATH%/bin/java}"/bin/javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls -cp "$MYSQL_DRIVER:$PG_DRIVER" Main.java HMS.java JDBConn.java

# Run
echo "Running JavaFX application..."
"$JAVA_PATH" --module-path "$JAVAFX_PATH" --add-modules javafx.controls -cp ".:$MYSQL_DRIVER:$PG_DRIVER" Main
