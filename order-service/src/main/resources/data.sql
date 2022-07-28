DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    original_order_id VARCHAR(13) PRIMARY KEY,
    order_price       DECIMAL(10, 2) NOT NULL
);

CREATE TABLE order_product
(
    original_order_id VARCHAR(13),
    product_id        INT NOT NULL,
    ordered_qty       INT NOT NULL,

    CONSTRAINT FK_order_product
        FOREIGN KEY (original_order_id)
            REFERENCES orders (original_order_id)
);

INSERT INTO orders (original_order_id, order_price)
VALUES ('111-12345-111', 100.50),
       ('112-12345-112', 50.55),
       ('123-12345-123', 1499.99);

INSERT INTO order_product (original_order_id, product_id, ordered_qty)
VALUES ('111-12345-111', 1, 1),
       ('112-12345-112', 2, 1),
       ('123-12345-123', 3, 1)


