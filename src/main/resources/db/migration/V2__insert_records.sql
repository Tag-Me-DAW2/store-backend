-- ==========================================
-- USERS
-- ==========================================
INSERT INTO tb_users (username, email, password, first_name, last_name, phone, profile_picture, profile_picture_name, role)
VALUES
    ('admin', 'admin@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'System', 'Admin', '+600000000',null, null, 'ADMIN'),
    ('jdoe', 'john.doe@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'John', 'Doe', '+611111111',null, null, 'CUSTOMER'),
    ('msmith', 'mary.smith@example.com', '$2a$10$2yH7qG19E2Hq8dH1kzPrQeJmGCWl7ZTWWc7hD5VbZ3X2t8iE1iL6y', 'Mary', 'Smith', '+622222222',null, null, 'CUSTOMER'),
    ('jlopez', 'javier@gmail.com','$argon2id$v=19$m=15360,t=2,p=1$IWkQZkJkfAWYmn4hvwAQUe61Z3C6+9PzHr5Uu9Qf2VqPHFu480qXQE9fyUhxGXEWrI8qNbchVbBi8D9XErCt8A$XjJrwUROra2ZyuYg5RLgVmTs1FpnvdtKcn6iD+5HyFw','Javier', 'Lopez', '+633333333', null, null, 'ADMIN'),
    ('javier', 'javier2@gmail.com','$argon2id$v=19$m=15360,t=2,p=1$IWkQZkJkfAWYmn4hvwAQUe61Z3C6+9PzHr5Uu9Qf2VqPHFu480qXQE9fyUhxGXEWrI8qNbchVbBi8D9XErCt8A$XjJrwUROra2ZyuYg5RLgVmTs1FpnvdtKcn6iD+5HyFw','Javier', 'Lopez', '+633333334', null, null, 'CUSTOMER');

INSERT INTO tb_categories (name)
VALUES
    ('Cards'),
    ('Wearables'),
    ('Tags & Stickers'),
    ('Desk & Office'),
    ('Accessories');


INSERT INTO tb_products (name, description, base_price, discount_percentage, image, image_name, category_id, material)
VALUES
-- =========================
-- NFC CARDS (1)
-- =========================
('Smart CV Card', 'NFC smart card to instantly share your digital CV', 19.99, 10.00, NULL, NULL, 1, 'PVC'),
('Business Card Pro', 'Professional NFC business card with CV and portfolio', 29.99, 15.00, NULL, NULL, 1, 'PVC'),
('Metal CV Card', 'Premium metal NFC card with personalized digital resume', 49.99, 20.00, NULL, NULL, 1, 'STEEL'),
('Wallet CV Card', 'Slim NFC card designed to carry your CV in your wallet', 24.99, 10.00, NULL, NULL, 1, 'PVC'),

-- =========================
-- NFC WEARABLES (2)
-- =========================
('CV Bracelet', 'Wearable NFC bracelet to share your professional profile', 22.99, 12.00, NULL, NULL, 2, 'PVC'),
('Smart Wristband', 'Smart NFC wristband with instant CV access', 27.99, 15.00, NULL, NULL, 2, 'STEEL'),
('CV Keychain', 'Electronic NFC keychain linked to your digital curriculum', 14.99, 8.00, NULL, NULL, 2, 'WOOD'),

-- =========================
-- NFC TAGS & STICKERS (3)
-- =========================
('CV Sticker', 'Adhesive NFC sticker to share your CV by scanning', 9.99, 5.00, NULL, NULL, 3, 'PVC'),
('Resume Tag', 'Compact NFC tag programmable with your digital resume', 11.99, 7.00, NULL, NULL, 3, 'PVC'),
('Phone CV Tag', 'NFC tag for smartphones with CV redirection', 12.99, 10.00, NULL, NULL, 3, 'PVC'),
('CV Stickers Pack', 'Pack of programmable NFC stickers for digital CVs', 19.99, 12.00, NULL, NULL, 3, 'PVC'),

-- =========================
-- NFC DESK & OFFICE (4)
-- =========================
('Desk CV Stand', 'Desktop NFC stand for scanning your CV in meetings', 29.99, 15.00, NULL, NULL, 4, 'WOOD'),
('Office CV Plaque', 'Office plaque with embedded NFC linking to your CV', 39.99, 18.00, NULL, NULL, 4, 'GOLDEN'),
('Interview Card Stand', 'NFC stand designed for interviews and job fairs', 24.99, 10.00, NULL, NULL, 4, 'STEEL'),

-- =========================
-- NFC ACCESSORIES (5)
-- =========================
('Lanyard CV', 'Electronic lanyard with NFC chip for CV sharing', 16.99, 10.00, NULL, NULL, 5, 'PVC'),
('Badge CV Holder', 'Badge holder with integrated NFC CV access', 18.99, 12.00, NULL, NULL, 5, 'STEEL'),
('Backpack CV Tag', 'Backpack accessory with NFC linking to your resume', 15.99, 8.00, NULL, NULL, 5, 'WOOD');

-- ==========================================
-- SESSIONS (Opcional, normalmente vacío)
-- ==========================================
-- Ejemplo: sesión creada para usuario admin
INSERT INTO tb_sessions (usr_id, token)
VALUES
    (1, 'example-token-1234567890');
