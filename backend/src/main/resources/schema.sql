-- PLN Payment Database Schema
-- Jalankan ini jika ingin setup database manual (JPA ddl-auto=update juga akan create otomatis)

CREATE DATABASE IF NOT EXISTS pln_payment;
USE pln_payment;

CREATE TABLE IF NOT EXISTS pln_bills (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id     VARCHAR(20)    NOT NULL,
    customer_name   VARCHAR(255),
    tariff          VARCHAR(50),
    power           VARCHAR(50),
    period          VARCHAR(20),
    amount          DECIMAL(15, 2),
    admin_fee       DECIMAL(10, 2),
    total_amount    DECIMAL(15, 2),
    reference_id    VARCHAR(100),
    iak_tr_id       VARCHAR(100),
    product_code    VARCHAR(30),
    status          VARCHAR(20)    NOT NULL DEFAULT 'UNPAID',
    inquiry_at      DATETIME,
    paid_at         DATETIME,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
