DROP TABLE IF EXISTS products;

CREATE TABLE products
(
    product_id     INT PRIMARY KEY,
    price          DECIMAL(10, 2),
    stock_quantity INT
);

INSERT INTO products (product_id, price, stock_quantity)
VALUES (1, 100.50, 10),
       (2, 50.55, 10),
       (3, 1499.99, 10)