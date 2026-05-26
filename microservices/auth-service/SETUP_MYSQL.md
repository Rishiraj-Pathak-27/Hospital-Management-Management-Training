# MySQL Setup for Auth Service

## Quick Start (3 Options)

### Option 1: No Password (Recommended for Development)
```bash
# MySQL is already running with root user without password
# Just start the service:
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Option 2: With Password "root"
```bash
export DB_USERNAME=root
export DB_PASSWORD=root
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Option 3: Custom Credentials
```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

---

## Fix MySQL Root Access

If MySQL is not accepting connections, try these steps:

### Check MySQL Status
```bash
sudo systemctl status mysql
```

### Start MySQL if not running
```bash
sudo systemctl start mysql
```

### Reset ROOT Password (if needed)
```bash
# Stop MySQL
sudo systemctl stop mysql

# Start without grant tables
sudo mysqld_safe --skip-grant-table &

# Connect without password
mysql -u root

# Run these SQL commands
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY '';
EXIT;

# Kill the daemon
sudo pkill mysqld_safe

# Start normally
sudo systemctl start mysql
```

### Test Connection
```bash
# Without password
mysql -u root

# List databases
SHOW DATABASES;

# Create auth if needed
CREATE DATABASE IF NOT EXISTS auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Exit
EXIT;
```

---

## Docker MySQL Setup (Alternative)

If you want to use Docker for MySQL:

```bash
docker run -d \
  --name mysql-auth \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=auth \
  -p 3306:3306 \
  mysql:8.0
```

Then run the auth-service:
```bash
export DB_USERNAME=root
export DB_PASSWORD=root
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

---

## Troubleshooting

### Error: "Access denied for user 'root'@'localhost'"
- Verify MySQL is running: `sudo systemctl status mysql`
- Check credentials: `mysql -u root -p` (try empty password with just Enter)
- Ensure database exists: `mysql -u root -e "CREATE DATABASE auth;"`

### Error: "Can't connect to MySQL server"
- MySQL is not running
- Solution: `sudo systemctl start mysql`

### Error: "Unknown database 'auth'"
- The database hasn't been created yet
- Solution: The service will auto-create it on first run (with `ddl-auto=update`)
- Or manually: `mysql -u root -e "CREATE DATABASE auth;"`

---

## Current Configuration

The service uses these environment variables with defaults:

```properties
DB_USERNAME=root     (default: root)
DB_PASSWORD=         (default: empty)
```

To customize, set before running:
```bash
export DB_USERNAME=myuser
export DB_PASSWORD=mypass
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

