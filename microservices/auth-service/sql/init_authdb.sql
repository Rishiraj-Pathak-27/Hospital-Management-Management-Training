-- Init script for auth-service database
CREATE DATABASE IF NOT EXISTS auth;
USE auth;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Example insert (remove or change in production)
INSERT IGNORE INTO users (username, password, role)
VALUES ('admin', '$2a$10$EXAMPLEHASHDONTUSE', 'ADMIN');
