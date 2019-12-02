ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `pay_password` VARCHAR(145) NULL COMMENT '支付密码' AFTER `updatetime`;


ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `paibag` DECIMAL(11,2) NULL COMMENT 'π包' AFTER `pay_password`,
ADD COLUMN `fund` DECIMAL(11,2) NULL COMMENT '奖励' AFTER `paibag`;



ALTER TABLE `platform_duojifen`.`sys_oss` 
ADD COLUMN `oss_from_type` INT NULL AFTER `create_date`,
ADD COLUMN `oss_from_no` VARCHAR(45) NULL AFTER `oss_from_type`;


ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `google_secret` VARCHAR(145) NULL COMMENT 'google密钥' AFTER `total_invest_order_sum`;



