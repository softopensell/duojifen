ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `user_pre_balance` DECIMAL(19,4) NULL COMMENT '历史数据 12.2' AFTER `blockchain_secret`;

use platform_duojifen;
UPDATE `platform_duojifen`.`t_user` SET `user_pre_balance`=balance WHERE `user_id`>0;