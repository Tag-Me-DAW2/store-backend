-- ==========================================
-- USERS
-- ==========================================
INSERT INTO tb_users (username, email, password, first_name, last_name, phone, role)
VALUES
    ('admin', 'admin@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'System', 'Admin', '600000000', 'ADMIN'),
    ('jdoe', 'john.doe@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'John', 'Doe', '611111111', 'CUSTOMER'),
    ('msmith', 'mary.smith@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'Mary', 'Smith', '622222222', 'CUSTOMER');

-- ==========================================
-- CATEGORIES
-- ==========================================
INSERT INTO tb_categories (name)
VALUES
    ('Electronics'),
    ('Books'),
    ('Clothing'),
    ('Home'),
    ('Toys');

-- ==========================================
-- PRODUCTS
-- ==========================================
-- Imagen vacía (0x) para ejemplo
INSERT INTO tb_products (name, description, base_price, discount_percentage, image, category_id)
VALUES
    ('Smartphone X1', 'Latest generation smartphone', 799.99, 10.00, 0x00, 1),
    ('4K Television', 'Ultra HD Smart TV 55 inches', 1299.99, 15.00, 0x00, 1),
    ('Novel: The Lost City', 'Adventure fiction book', 19.90, 5.00, 0x00, 2),
    ('Winter Jacket', 'Waterproof and warm jacket', 89.90, 20.00, 0x00, 3),
    ('Coffee Maker Pro', 'Automatic coffee maker with grinder', 149.90, 1.0, 0x00, 4),
    ('Toy Car Racer', 'Battery-powered children toy car', 49.90, 12.00, 0x00, 5);

-- ==========================================
-- SESSIONS (Opcional, normalmente vacío)
-- ==========================================
-- Ejemplo: sesión creada para usuario admin
INSERT INTO tb_sessions (usr_id, token)
VALUES
    (1, 'example-token-1234567890');
