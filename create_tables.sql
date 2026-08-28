-- Bank Management System — Database Schema (Final)
-- Run this in phpMyAdmin (XAMPP) SQL tab, or via mysql/mariadb CLI

CREATE DATABASE IF NOT EXISTS bank_management_system;
USE bank_management_system;

-- 1. WORKER TABLE
CREATE TABLE worker (
    worker_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 2. CLIENT TABLE
CREATE TABLE client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20) UNIQUE,
    date_registered DATE NOT NULL DEFAULT (CURRENT_DATE),
    created_by INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (created_by) REFERENCES worker(worker_id)
);

-- 3. ACCOUNT TABLE
CREATE TABLE account (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    account_type VARCHAR(30) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    date_opened DATE NOT NULL DEFAULT (CURRENT_DATE),
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);

-- 4. TRANSACTION TABLE
-- Note: `transaction` is a reserved word in MariaDB/MySQL — always reference
-- it with backticks in queries, e.g. SELECT * FROM `transaction` ...
CREATE TABLE `transaction` (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    type VARCHAR(20) NOT NULL,           -- 'deposit', 'withdraw', 'transfer'
    amount DECIMAL(15,2) NOT NULL,
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    target_account INT NULL,             -- only set when type = 'transfer'
    FOREIGN KEY (account_id) REFERENCES account(account_id),
    FOREIGN KEY (target_account) REFERENCES account(account_id)
);