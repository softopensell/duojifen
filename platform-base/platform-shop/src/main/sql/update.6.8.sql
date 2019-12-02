ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `create_user_id` INT NULL COMMENT '代注册人id' AFTER `fund`;

ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `addr_phone` VARCHAR(45) NULL COMMENT '联系电话' AFTER `create_user_id`,
ADD COLUMN `addr_link_name` VARCHAR(45) NULL COMMENT '联系人' AFTER `addr_phone`;

ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `signup_invited_phone` VARCHAR(45) NULL COMMENT '注册时推荐人 仅仅记录数据' AFTER `addr_link_name`,
ADD COLUMN `signup_node_phone` VARCHAR(45) NULL COMMENT '仅仅记录节点phone' AFTER `signup_invited_phone`;


ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `signup_user_level_type` INT NULL COMMENT '注册时的vip 级别' AFTER `signup_node_phone`;


DROP TABLE IF EXISTS `t_bonus_invest_order`;
CREATE TABLE `t_bonus_invest_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '会员',
  `invest_order_no` varchar(100) NOT NULL COMMENT '流水号',
  `consumed_order_no` varchar(100) NOT NULL COMMENT '消费订单：根据消费订单，产生订单号',
  `consumed_money` decimal(11,2) DEFAULT '0.00' COMMENT '消费额度',
  `max_money` decimal(11,2) DEFAULT '0.00' COMMENT '最大额度',
  `buy_money` decimal(11,2) DEFAULT '0.00' COMMENT '购买额度',
  `pay_type` int(11) NOT NULL COMMENT '支付类型:0 货到付款 1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易',
  `pay_status` int(11) DEFAULT NULL COMMENT '支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败',
  `statu` int(11) NOT NULL COMMENT '状态 0 有效 1 删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;




ALTER TABLE `platform_duojifen`.`t_user` 
ADD COLUMN `total_invest_order_sum` INT NULL DEFAULT 0 COMMENT '投资次数' AFTER `total_invest_money`;




ALTER TABLE `platform_duojifen`.`t_user_invest_level` 
ADD COLUMN `user_level_consumed_min` DECIMAL(11,2) NULL COMMENT '消费最小额度' AFTER `goods_sn`,
ADD COLUMN `user_level_consumed_max` VARCHAR(45) NULL COMMENT '消费的最大额度' AFTER `user_level_consumed_min`;
ALTER TABLE `platform_duojifen`.`t_user_invest_level` 
ADD COLUMN `user_level_time` INT NULL COMMENT '分配倍数' AFTER `user_level_consumed_max`;




