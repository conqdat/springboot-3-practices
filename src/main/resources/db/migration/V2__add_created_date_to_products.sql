-- Add the created_date column to the products table
ALTER TABLE products
    ADD COLUMN created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;