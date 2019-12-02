ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `total_invest_income_money` DECIMAL(11,6) NULL COMMENT '资产总收益' AFTER `signup_user_level_type`,
ADD COLUMN `invest_income_money` DECIMAL(11,6) NULL COMMENT '已经收益额度 同时添加到钱包' AFTER `total_invest_income_money`,
ADD COLUMN `surplus_invest_money` DECIMAL(11,6) NULL COMMENT '剩余资产' AFTER `invest_income_money`,
ADD COLUMN `share_invest_last_time` DATETIME NULL COMMENT '结算时间' AFTER `surplus_invest_money`;


ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `user_level_type_value` DECIMAL(11,6) NULL COMMENT '对应水平 享受额度' AFTER `share_invest_last_time`,
ADD COLUMN `user_node_bonus_level` INT NULL COMMENT '会员对应享受分享层级 node' AFTER `user_level_type_value`;
