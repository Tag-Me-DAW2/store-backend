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