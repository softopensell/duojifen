use platform_duojifen;
DROP TABLE IF EXISTS `platform_fw_manager`;
CREATE TABLE `platform_fw_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fw_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `fw_user_id` int(11) DEFAULT NULL COMMENT '服务中心',
  `fw_cur_yj` decimal(21,6) DEFAULT NULL COMMENT '服务中心当前业绩',
  `fw_cur_date` datetime DEFAULT NULL COMMENT '开始统计时间',
  `fw_total_reset_time` int(11) DEFAULT NULL COMMENT '重置次数',
  `fw_total_yj` decimal(21,6) DEFAULT NULL COMMENT '累计业绩',
  `fw_total_pay_money` decimal(21,6) DEFAULT NULL COMMENT '累计奖励',
  `state` int(11) DEFAULT NULL COMMENT '状态，0 OK 1 删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  `update_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `platform_withdraw_share`;
CREATE TABLE `platform_withdraw_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '会员',
  `withdraw_type` int(11) NOT NULL COMMENT '类别:1 是 共识机制 0 共识股东',
  `withdraw_type_star` int(11) NOT NULL COMMENT '星级类型',
  `state` int(11) DEFAULT NULL COMMENT '状态，0 OK 1 删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  `update_time` datetime DEFAULT NULL COMMENT '添加，更改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `platform_withdraw_share_order`;
CREATE TABLE `platform_withdraw_share_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_date_no` varchar(45) NOT NULL COMMENT '序号',
  `total_pay_money` decimal(21,6) DEFAULT NULL COMMENT '本次奖励总额',
  `share_start_date` datetime DEFAULT NULL COMMENT '开始统计时间',
  `share_end_date` datetime DEFAULT NULL COMMENT '开始统计时间',
  
  `share_user_day_sys` decimal(21,6) DEFAULT NULL COMMENT '级别分成 系统',

  `share_user_day_v1` decimal(21,6) DEFAULT NULL COMMENT '级别分成一星',
  `share_user_day_v2` decimal(21,6) DEFAULT NULL COMMENT '级别分成二星',
  `share_user_day_v3` decimal(21,6) DEFAULT NULL COMMENT '级别分成三星',
  `share_user_day_v4` decimal(21,6) DEFAULT NULL COMMENT '级别分成四星',
  `share_user_day_v5` decimal(21,6) DEFAULT NULL COMMENT '级别分成五星',
  
  `share_user_day_v6` decimal(21,6) DEFAULT NULL COMMENT '级别分成一钻',
  `share_user_day_v7` decimal(21,6) DEFAULT NULL COMMENT '级别分成二钻',
  `share_user_day_v8` decimal(21,6) DEFAULT NULL COMMENT '级别分成三钻',
  `share_user_day_v9` decimal(21,6) DEFAULT NULL COMMENT '级别分成四钻',
  `share_user_day_v10` decimal(21,6) DEFAULT NULL COMMENT '级别分成五钻',
  `state` int(11) DEFAULT NULL COMMENT '状态，0 OK 1 删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





