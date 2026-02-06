CREATE TABLE `tb_users` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `username` varchar(50) NOT NULL,
                          `email` varchar(255) NOT NULL,
                          `password` varchar(255) NOT NULL,
                          `first_name` varchar(50) NOT NULL,
                          `last_name` varchar(50) DEFAULT NULL,
                          `phone` varchar(20) DEFAULT NULL,
                          `profile_picture` LONGBLOB DEFAULT NULL,
                          `profile_picture_name` varchar(255) DEFAULT NULL,
                          `role` ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
                          `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

CREATE TABLE `tb_sessions` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `usr_id` int(11) NOT NULL,
                             `token` varchar(255) NOT NULL,
                             `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`),
                             KEY `tb_sessions_FK` (`usr_id`),
                             CONSTRAINT `tb_sessions_FK` FOREIGN KEY (`usr_id`) REFERENCES `tb_users` (`id`)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

CREATE TABLE `tb_categories` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `name` varchar(100) NOT NULL,
                          `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

CREATE TABLE `tb_products` (
                               id int(11) AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               description VARCHAR(255) NOT NULL,
                               base_price DECIMAL(19,2) NOT NULL,
                               discount_percentage DECIMAL(5,2) NOT NULL,
                               image LONGBLOB DEFAULT NULL,
                               image_name VARCHAR(255) DEFAULT NULL,
                               category_id INT(11),
                               `material` ENUM('STEEL', 'PVC', 'WOOD', 'GOLDEN') NOT NULL DEFAULT 'PVC',
                               `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT `tb_products_FK` FOREIGN KEY (category_id) REFERENCES tb_categories(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

CREATE TABLE `tb_orders` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `usr_id` int(11) NOT NULL,
                            `order_status` ENUM('PENDING', 'PROCESSING','PAYED') NOT NULL DEFAULT 'PENDING',
                            `shipping_cost` DECIMAL(19,2) NOT NULL,
                            `paid-date` timestamp NULL DEFAULT NULL,
                            `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `tb_orders_FK` (`usr_id`),
                            CONSTRAINT `tb_orders_FK` FOREIGN KEY (`usr_id`) REFERENCES `tb_users` (`id`)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
                            
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

CREATE TABLE `tb_shipping_info` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `order_id` int(11) NOT NULL,
                                  `first_name` varchar(255) NOT NULL,
                                  `last_name` varchar(255) NOT NULL,
                                  `email` varchar(255) NOT NULL,
                                  `address` varchar(255) NOT NULL,
                                  `city` varchar(100) NOT NULL,
                                  `postal_code` varchar(20) NOT NULL,
                                  `country` varchar(100) NOT NULL,
                                  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY (`order_id`),
                                  CONSTRAINT `tb_orders_shipping_info_FK` FOREIGN KEY (`order_id`) REFERENCES `tb_orders` (`id`)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

CREATE TABLE `tb_order_items` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `order_id` int(11) NOT NULL,
                                  `product_id` int(11) NOT NULL,
                                  `product_name` varchar(255) NULL,
                                  `product_image` LONGBLOB DEFAULT NULL,
                                  `product_image_name` varchar(255) DEFAULT NULL,
                                  `discount_percentage` DECIMAL(5,2) NULL,
                                  `base_price` DECIMAL(19,2) NULL,
                                  `quantity` int(11) NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `tb_order_items_order_FK` (`order_id`),
                                  KEY `tb_order_items_product_FK` (`product_id`),
                                  CONSTRAINT `tb_order_items_order_FK` FOREIGN KEY (`order_id`) REFERENCES `tb_orders` (`id`)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                                  CONSTRAINT `tb_order_items_product_FK` FOREIGN KEY (`product_id`) REFERENCES `tb_products` (`id`)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;