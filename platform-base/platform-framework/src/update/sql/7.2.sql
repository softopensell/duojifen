ALTER TABLE `platform_duojifen`.`t_user` 
CHANGE COLUMN `balance` `balance` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '余额钱包' ,
CHANGE COLUMN `amount` `amount` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '消费金额' ,
CHANGE COLUMN `total_point` `total_point` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '累积购物钱包' ,
CHANGE COLUMN `point` `point` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '购物余额钱包' ,
CHANGE COLUMN `total_income` `total_income` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '累积收益' ,
CHANGE COLUMN `waiting_income` `waiting_income` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '等待收益' ;



ALTER TABLE `platform_duojifen`.`t_goods_order` 
CHANGE COLUMN `express_price` `express_price` DECIMAL(19,4) NULL DEFAULT NULL COMMENT '快递费用' ,
CHANGE COLUMN `total_integral_num` `total_integral_num` DECIMAL(19,4) NULL DEFAULT NULL COMMENT '使用积分数' ,
CHANGE COLUMN `total_integral_price` `total_integral_price` DECIMAL(19,4) NULL DEFAULT NULL COMMENT '使用积分抵扣价' ,
CHANGE COLUMN `total_pay_price` `total_pay_price` DECIMAL(19,4) NULL DEFAULT NULL COMMENT '在线支付总费用' ,
CHANGE COLUMN `total_price` `total_price` DECIMAL(19,4) NOT NULL COMMENT '总价' ;

ALTER TABLE `platform_duojifen`.`t_bonus_invest_order` 
CHANGE COLUMN `consumed_money` `consumed_money` DECIMAL(19,4) NULL DEFAULT '0.0000' COMMENT '消费额度' ,
CHANGE COLUMN `max_money` `max_money` DECIMAL(19,4) NULL DEFAULT '0.0000' COMMENT '最大额度' ,
CHANGE COLUMN `buy_money` `buy_money` DECIMAL(19,4) NULL DEFAULT '0.0000' COMMENT '购买额度' ,
CHANGE COLUMN `total_incom_money` `total_incom_money` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '总收益' ,
CHANGE COLUMN `income_money` `income_money` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '已经收益' ,
CHANGE COLUMN `surplus_money` `surplus_money` DECIMAL(19,4) NULL DEFAULT '0.00' COMMENT '剩余收益' ;


