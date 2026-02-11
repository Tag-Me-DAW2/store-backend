-- Create table for storing payment information temporarily for retry
CREATE TABLE `tb_payment_info` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `order_id` int(11) NOT NULL,
    `card_number` varchar(255) NOT NULL,
    `card_holder_name` varchar(255) NOT NULL,
    `cvv` varchar(10) NOT NULL,
    `expiration_date` varchar(10) NOT NULL,
    `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`order_id`),
    CONSTRAINT `tb_payment_info_order_FK` FOREIGN KEY (`order_id`) REFERENCES `tb_orders` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
