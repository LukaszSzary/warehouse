CREATE TABLE IF NOT EXISTS stock (
    product_id VARCHAR(64) PRIMARY KEY,
    quantity    INT NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE IF NOT EXISTS processed_orders(
    order_id VARCHAR(64) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL
);

MERGE INTO stock (product_id, quantity) KEY (product_id) VALUES ('p1', 10);
MERGE INTO stock (product_id, quantity) KEY (product_id) VALUES ('p2', 3);
