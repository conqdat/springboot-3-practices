CREATE TABLE products
(
    id            SERIAL PRIMARY KEY,
    product_name  VARCHAR(255)     NOT NULL,
    product_price DOUBLE PRECISION NOT NULL,
    product_code  VARCHAR(255)     NULL
);
