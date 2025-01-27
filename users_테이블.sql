CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    gender ENUM('M', 'F'),
    birthdate VARCHAR(255),
    phone VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE
);
