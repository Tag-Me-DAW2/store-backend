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
    ('Toy Car Racer', 'Battery-powered children toy car', 49.90, 12.00, null, null, 5),
    ('Laptop Pro 15', 'High performance laptop for professionals', 1899.99, 12.00, NULL, NULL, 1),
    ('Wireless Headphones', 'Noise cancelling over-ear headphones', 299.90, 18.00, NULL, NULL, 1),
    ('Bluetooth Speaker', 'Portable waterproof speaker', 129.99, 8.00, NULL, NULL, 1),
    ('Smartwatch Active', 'Fitness and health tracking smartwatch', 249.90, 10.00, NULL, NULL, 1),
    ('Gaming Console Z', 'Next-gen gaming console', 499.99, 5.00, NULL, NULL, 1),
    ('E-reader Light', 'E-ink reader with backlight', 139.90, 7.50, NULL, NULL, 1),
    ('Wireless Mouse', 'Ergonomic wireless mouse', 39.90, 15.00, NULL, NULL, 1),
    ('Mechanical Keyboard', 'RGB mechanical gaming keyboard', 119.90, 20.00, NULL, NULL, 1),
    ('USB-C Hub', 'Multiport USB-C adapter', 59.90, 10.00, NULL, NULL, 1),
    ('External SSD 1TB', 'High speed portable SSD', 169.99, 9.00, NULL, NULL, 1),
    ('Science Fiction Novel', 'Futuristic sci-fi adventure', 24.90, 6.00, NULL, NULL, 2),
    ('Historical Novel', 'Story set in medieval times', 21.50, 4.00, NULL, NULL, 2),
    ('Cooking Recipes Book', 'Over 300 easy recipes', 29.90, 10.00, NULL, NULL, 2),
    ('Programming in Java', 'Comprehensive Java programming guide', 49.90, 15.00, NULL, NULL, 2),
    ('Children Fairy Tales', 'Illustrated fairy tales for kids', 18.90, 5.00, NULL, NULL, 2),
    ('Travel Guide Europe', 'Best destinations in Europe', 34.90, 8.00, NULL, NULL, 2),
    ('Mindfulness Book', 'Guide to meditation and mindfulness', 22.90, 7.00, NULL, NULL, 2),
    ('Business Strategy', 'Modern business management techniques', 39.90, 12.00, NULL, NULL, 2),
    ('Photography Basics', 'Learn photography from scratch', 27.90, 6.50, NULL, NULL, 2),
    ('Summer T-Shirt', 'Cotton short sleeve t-shirt', 19.90, 20.00, NULL, NULL, 3),
    ('Jeans Classic', 'Regular fit blue jeans', 49.90, 15.00, NULL, NULL, 3),
    ('Running Shoes', 'Lightweight running sneakers', 89.90, 18.00, NULL, NULL, 3),
    ('Leather Belt', 'Genuine leather belt', 29.90, 10.00, NULL, NULL, 3),
    ('Hoodie Casual', 'Comfortable casual hoodie', 59.90, 25.00, NULL, NULL, 3),
    ('Formal Shirt', 'Slim fit formal shirt', 44.90, 12.00, NULL, NULL, 3),
    ('Sports Jacket', 'Breathable sports jacket', 99.90, 17.00, NULL, NULL, 3),
    ('Wool Scarf', 'Warm winter scarf', 24.90, 8.00, NULL, NULL, 3),
    ('Sunglasses UV', 'UV protection sunglasses', 69.90, 14.00, NULL, NULL, 3),
    ('Blender Max', 'High power kitchen blender', 89.90, 11.00, NULL, NULL, 4),
    ('Air Fryer XL', 'Oil-free air fryer', 129.90, 13.00, NULL, NULL, 4),
    ('Electric Kettle', 'Fast boiling electric kettle', 39.90, 9.00, NULL, NULL, 4),
    ('Microwave Oven', 'Digital microwave oven', 179.90, 16.00, NULL, NULL, 4),
    ('Toaster Deluxe', 'Two-slice toaster', 34.90, 7.00, NULL, NULL, 4),
    ('Food Processor', 'Multi-function food processor', 199.90, 14.00, NULL, NULL, 4),
    ('Vacuum Cleaner', 'Bagless vacuum cleaner', 159.90, 18.00, NULL, NULL, 4),
    ('Building Blocks Set', 'Creative building blocks for kids', 39.90, 10.00, NULL, NULL, 5),
    ('Puzzle 1000 Pieces', 'Landscape puzzle 1000 pieces', 19.90, 5.00, NULL, NULL, 5),
    ('Remote Control Drone', 'Beginner RC drone with camera', 99.90, 12.00, NULL, NULL, 5),
    ('Doll House', 'Wooden doll house', 79.90, 15.00, NULL, NULL, 5),
    ('Board Game Family', 'Fun board game for all ages', 29.90, 8.00, NULL, NULL, 5),
    ('Plush Teddy Bear', 'Soft plush teddy bear', 24.90, 6.00, NULL, NULL, 5);

-- ==========================================
-- SESSIONS (Opcional, normalmente vacío)
-- ==========================================
-- Ejemplo: sesión creada para usuario admin
INSERT INTO tb_sessions (usr_id, token)
VALUES
    (1, 'example-token-1234567890');
