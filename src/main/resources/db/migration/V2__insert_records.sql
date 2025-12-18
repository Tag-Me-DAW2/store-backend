-- ==========================================
-- USERS
-- ==========================================
INSERT INTO tb_users (username, email, password, first_name, last_name, phone, profile_picture, profile_picture_name, role)
VALUES
    ('admin', 'admin@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'System', 'Admin', '600000000',0x00, null, 'ADMIN'),
    ('jdoe', 'john.doe@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'John', 'Doe', '611111111',0x00, null, 'CUSTOMER'),
    ('msmith', 'mary.smith@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'Mary', 'Smith', '622222222',0x00, null, 'CUSTOMER'),
    ('jlopez', 'javier@gmail.com','$argon2id$v=19$m=15360,t=2,p=1$IWkQZkJkfAWYmn4hvwAQUe61Z3C6+9PzHr5Uu9Qf2VqPHFu480qXQE9fyUhxGXEWrI8qNbchVbBi8D9XErCt8A$XjJrwUROra2ZyuYg5RLgVmTs1FpnvdtKcn6iD+5HyFw','Javier', 'Lopez', '633333333', 0x00, null, 'ADMIN'),
    ('javier', 'javier2@gmail.com','$argon2id$v=19$m=15360,t=2,p=1$IWkQZkJkfAWYmn4hvwAQUe61Z3C6+9PzHr5Uu9Qf2VqPHFu480qXQE9fyUhxGXEWrI8qNbchVbBi8D9XErCt8A$XjJrwUROra2ZyuYg5RLgVmTs1FpnvdtKcn6iD+5HyFw','Javier', 'Lopez', '633333334', 0x00, null, 'CUSTOMER');

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
INSERT INTO tb_products (name, description, base_price, discount_percentage, image, image_name, category_id)
VALUES
    ('Smartphone X1', 'Latest generation smartphone', 799.99, 10.00, null, null,1),
    ('4K Television', 'Ultra HD Smart TV 55 inches', 1299.99, 15.00, null, null, 1),
    ('Novel: The Lost City', 'Adventure fiction book', 19.90, 5.00, null, null, 2),
    ('Winter Jacket', 'Waterproof and warm jacket', 89.90, 20.00, null, null, 3),
    ('Coffee Maker Pro', 'Automatic coffee maker with grinder', 149.90, 1.0, null, null, 4),
    ('Toy Car Racer', 'Battery-powered children toy car', 49.90, 12.00, null, null, 5);

-- ==========================================
-- SESSIONS (Opcional, normalmente vacío)
-- ==========================================
-- Ejemplo: sesión creada para usuario admin
INSERT INTO tb_sessions (usr_id, token)
VALUES
    (1, 'example-token-1234567890');
