-- Add CANCELLED status to tb_orders order_status enum
ALTER TABLE `tb_orders` 
MODIFY COLUMN `order_status` ENUM('PENDING', 'PROCESSING', 'PAYED', 'CANCELLED') NOT NULL DEFAULT 'PENDING';
