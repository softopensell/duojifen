ALTER TABLE `platform_duojifen`.`t_bonus_points` 
CHANGE COLUMN `invited_user_id` `invited_user_id` INT(11) NULL DEFAULT NULL COMMENT '邀请人ID 默认 left 节点' ,
ADD COLUMN `blood_type` INT NULL COMMENT '关系树类型：0 多元 1 二叉树 ' AFTER `can_generate_qr`,
ADD COLUMN `blood_up_type` INT NULL COMMENT '升级类型：0 无 1 消费总额 2 ' AFTER `blood_type`,
ADD COLUMN `invited_right_user_id` INT NULL COMMENT '父类的右节点' AFTER `blood_up_type`;





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
  `total_incom_money` decimal(11,2) DEFAULT '0.00' COMMENT '总收益',
  `income_money` decimal(11,2) DEFAULT '0.00' COMMENT '已经收益',
  `surplus_money` decimal(11,2) DEFAULT '0.00' COMMENT '剩余收益',
  `share_last_time` datetime DEFAULT NULL COMMENT '最后收益时间 ---凌晨',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;






DROP TABLE IF EXISTS `t_user_invest_level`;
CREATE TABLE `t_user_invest_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_level_type` int(11) NOT NULL COMMENT '会员级别Type',
  `user_level_name` varchar(100) NOT NULL COMMENT '名称',
  `user_level_money_value` decimal(11,2) DEFAULT '0.00' COMMENT '消费额度',
  `user_level_node_level` int(11) NOT NULL COMMENT '10,11,12,13,14',
  `statu` int(11) NOT NULL COMMENT '状态 0 有效 1 删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;





DROP TABLE IF EXISTS `t_withdraw_type`;
CREATE TABLE `t_withdraw_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `withdraw_type` int(11) NOT NULL COMMENT '会员级别Type',
  `withdraw_name` varchar(100) NOT NULL COMMENT '名称',
  `withdraw_day` int(11) NOT NULL COMMENT '延期天数',
  `withdraw_rate` float(6,4) NOT NULL COMMENT '费率',
  `statu` int(11) NOT NULL COMMENT '状态 0 有效 1 删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;



