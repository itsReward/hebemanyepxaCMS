-- File: src/main/resources/db/migration/V1__init_schema.sql

-- Users for admin authentication
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Poetry section
CREATE TABLE poetry (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(200) NOT NULL,
                        slug VARCHAR(200) NOT NULL UNIQUE,
                        content TEXT NOT NULL,
                        excerpt TEXT,
                        publish_date DATE NOT NULL,
                        is_featured BOOLEAN DEFAULT false,
                        is_published BOOLEAN DEFAULT true,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Apparel section
CREATE TABLE apparel (
                         id BIGSERIAL PRIMARY KEY,
                         title VARCHAR(200) NOT NULL,
                         slug VARCHAR(200) NOT NULL UNIQUE,
                         description TEXT NOT NULL,
                         price DECIMAL(10, 2) NOT NULL,
                         is_featured BOOLEAN DEFAULT false,
                         is_available BOOLEAN DEFAULT true,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Images for apparel (since apparel might have multiple images)
CREATE TABLE images (
                        id BIGSERIAL PRIMARY KEY,
                        apparel_id INTEGER REFERENCES apparel(id) ON DELETE CASCADE,
                        file_name VARCHAR(255) NOT NULL,
                        file_path VARCHAR(255) NOT NULL,
                        is_primary BOOLEAN DEFAULT false,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Books section
CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       slug VARCHAR(200) NOT NULL UNIQUE,
                       description TEXT NOT NULL,
                       author VARCHAR(100) NOT NULL,
                       book_link VARCHAR(255),
                       cover_image VARCHAR(255),
                       is_featured BOOLEAN DEFAULT false,
                       publish_year INTEGER,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Quotes section
CREATE TABLE quotes (
                        id BIGSERIAL PRIMARY KEY,
                        quote TEXT NOT NULL,
                        author VARCHAR(100) NOT NULL,
                        category VARCHAR(100),
                        is_featured BOOLEAN DEFAULT false,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_poetry_is_featured ON poetry(is_featured);
CREATE INDEX idx_poetry_publish_date ON poetry(publish_date);
CREATE INDEX idx_apparel_is_featured ON apparel(is_featured);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_quotes_author ON quotes(author);
CREATE INDEX idx_quotes_category ON quotes(category);

-- Create an initial admin user (password: admin123)
INSERT INTO users (username, password, full_name, email)
VALUES ('admin', '$2a$10$rYKY0QaHRKHiR2VPrPLMW.w9MNRXLJkPO9D5GnHSqzdZtGJXj7b2i', 'Site Administrator', 'admin@hebemanyepxa.com');