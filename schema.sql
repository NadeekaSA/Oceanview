CREATE DATABASE IF NOT EXISTS oceanview_db;
USE oceanview_db;

-- Table for Users (Admin and Receptionist)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'RECEPTIONIST') NOT NULL,
    full_name VARCHAR(100)
);

-- Table for Guests
CREATE TABLE IF NOT EXISTS guests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT,
    contact VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    id_card_number VARCHAR(50) UNIQUE
);

-- Table for Rooms
CREATE TABLE IF NOT EXISTS rooms (
    room_number VARCHAR(10) PRIMARY KEY,
    type ENUM('SINGLE', 'DOUBLE', 'SUITE') NOT NULL,
    rate DECIMAL(10, 2) NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE') DEFAULT 'AVAILABLE'
);

-- Table for Reservations
CREATE TABLE IF NOT EXISTS reservations (
    reservation_number VARCHAR(20) PRIMARY KEY,
    guest_id INT,
    room_number VARCHAR(10),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_cost DECIMAL(10, 2),
    status ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') DEFAULT 'PENDING',
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    FOREIGN KEY (room_number) REFERENCES rooms(room_number)
);

-- Insert Default Admin
INSERT INTO users (username, password, role, full_name) 
VALUES ('admin', 'admin123', 'ADMIN', 'System Administrator');

-- Insert Initial Room Types
INSERT INTO rooms (room_number, type, rate) VALUES ('101', 'SINGLE', 50.00);
INSERT INTO rooms (room_number, type, rate) VALUES ('102', 'DOUBLE', 80.00);
INSERT INTO rooms (room_number, type, rate) VALUES ('201', 'SUITE', 150.00);
