DROP TABLE IF EXISTS `platform_stat`;
CREATE TABLE `platform_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stat_date_number` varchar(45) NOT NULL COMMENT '中间编号，日期，序号',
  `stat_date_type` int(11) DEFAULT NULL COMMENT '统计类型',
  `stat_member_id` int(11) DEFAULT NULL COMMENT '用户',
  
  `stat_user_total_sum` int(11) DEFAULT NULL COMMENT '会员总数',
  `stat_user_v0_sum` int(11) DEFAULT NULL COMMENT '未激活总数',
  `stat_user_v1_sum` int(11) DEFAULT NULL COMMENT 'V1总数',
  `stat_user_v2_sum` int(11) DEFAULT NULL COMMENT 'V2总数',
  `stat_user_v3_sum` int(11) DEFAULT NULL COMMENT 'V3总数',
  `stat_user_v4_sum` int(11) DEFAULT NULL COMMENT 'V4总数',
  
  `stat_user_day_add_sum` int(11) DEFAULT NULL COMMENT '新增总数',
  
  `stat_user_day_add_v0_sum` int(11) DEFAULT NULL COMMENT '新增未激活总数',
  `stat_user_day_add_v1_sum` int(11) DEFAULT NULL COMMENT '新增V1总数',
  `stat_user_day_add_v2_sum` int(11) DEFAULT NULL COMMENT '新增V2总数',
  `stat_user_day_add_v3_sum` int(11) DEFAULT NULL COMMENT '新增V3总数',
  `stat_user_day_add_v4_sum` int(11) DEFAULT NULL COMMENT '新增V4总数',
  
  `stat_user_total_zc` decimal(21,6) DEFAULT NULL COMMENT '用户总资产',
  `stat_user_total_jf` decimal(21,6) DEFAULT NULL COMMENT '用户积分',
  `stat_user_total_sy_zc` decimal(21,6) DEFAULT NULL COMMENT '用户总剩余资产',
  `stat_user_total_fund` decimal(21,6) DEFAULT NULL COMMENT '用户基金',
  
  
  `stat_day_rate` decimal(21,6) DEFAULT NULL COMMENT '今日分成比例',
  `stat_day_pay_balance_sum` int(11) DEFAULT NULL COMMENT '今日余额支付数量',
  `stat_day_pay_balance` decimal(21,6) DEFAULT NULL COMMENT '今日余额支付总额',
  
  `stat_day_pay_jf_sum` int(11) DEFAULT NULL COMMENT '今日积分支付数量',
  `stat_day_pay_jf` decimal(21,6) DEFAULT NULL COMMENT '今日积分支付总额',
  
  
  `stat_day_money_recharge_sum` int(11) DEFAULT NULL COMMENT '今日充值数量',
  `stat_day_money_recharge` decimal(21,6) DEFAULT NULL COMMENT '今日充值金额',
  
  `stat_day_balance_zz_in_sum` int(11) DEFAULT NULL COMMENT '今日余额转入数量',
  `stat_day_balance_zz_in` decimal(21,6) DEFAULT NULL COMMENT '今日余额转入总额',
  
  `stat_day_balance_zz_out_sum` int(11) DEFAULT NULL COMMENT '今日余额转出数量',
  `stat_day_balance_zz_out` decimal(21,6) DEFAULT NULL COMMENT '今日余额转出总额',
  
  `stat_day_money_tx_sum` int(11) DEFAULT NULL COMMENT '今日提现数量',
  `stat_day_money_tx` decimal(21,6) DEFAULT NULL COMMENT '今日提现金额',
  
  `stat_day_jf_zz_sum` int(11) DEFAULT NULL COMMENT '今日转账数量',
  `stat_day_jf_zz` decimal(21,6) DEFAULT NULL COMMENT '今日转账金额',
  
  `stat_day_jf_dh_sum` int(11) DEFAULT NULL COMMENT '积分兑换数量',
  `stat_day_jf_dh` decimal(21,6) DEFAULT NULL COMMENT '积分兑换金额',
  
  `stat_day_money_qy_sum` int(11) DEFAULT NULL COMMENT '今日权益数量',
  `stat_day_money_qy` decimal(21,6) DEFAULT NULL COMMENT '今日权益金额',
  
  
  `stat_day_money_fw_sum` int(11) DEFAULT NULL COMMENT '今日服务数量',
  `stat_day_money_fw` decimal(21,6) DEFAULT NULL COMMENT '今日服务金额',
  
  
  `stat_day_money_xx_sum` int(11) DEFAULT NULL COMMENT '今日星星数量',
  `stat_day_money_xx` decimal(21,6) DEFAULT NULL COMMENT '今日星星金额',
  
  `stat_day_money_sq_sum` int(11) DEFAULT NULL COMMENT '今日社区数量',
  `stat_day_money_sq` decimal(21,6) DEFAULT NULL COMMENT '今日社区金额',
  
  `stat_day_money_fund_sum` int(11) DEFAULT NULL COMMENT '今日基金数量',
  `stat_day_money_fund` decimal(21,6) DEFAULT NULL COMMENT '今日基金金额',
  
  
  
  `stat_date` datetime DEFAULT NULL COMMENT '统计时间',
  `create_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `platform_duojifen`.`platform_stat` 
ADD COLUMN `stat_date` DATETIME NULL COMMENT '统计时间' AFTER `create_time`;





-- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '', 'shop/platformstat.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'platformstat:list,platformstat:info', '2', null, '6';
    
    
-- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '个人财务统计', 'shop/userstat.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'userstat:list,userstat:info', '2', null, '6';
    
    
#统计sql
会员统计

SELECT date_format(register_time,'%Y-%m-%d') as day,
count(if(user_level_type=1,user_level_type,null)) as v1,
count(if(user_level_type=2,user_level_type,null)) as v2,
count(if(user_level_type=3,user_level_type,null)) as v2,
count(if(user_level_type=4,user_level_type,null)) as v2,
count(*) 
FROM platform_duojifen.t_user group by day;




UPDATE `platform_duojifen`.`t_payment_info` SET `payment_date`=create_time WHERE payment_date is null and id>0;

UPDATE `platform_duojifen`.`t_payment_info` SET `update_time`=create_time WHERE update_time is null and id>0;


SELECT date_format(register_time,'%Y-%m-%d') as day,
count(if(user_level_type=1,user_level_type,null)) as v1,
FROM platform_duojifen.t_payment_info group by day;



SELECT date_format(payment_date,'%Y-%m-%d') as day,
sum(if(money_type_wallet=2,amount,0)) as walletType2,
sum(if(money_type_wallet=3,amount,0)) as walletType3,
sum(if(money_type_wallet=62,amount,0)) as walletType62,
sum(if(money_type_wallet=92,amount,0)) as walletType92,
sum(if(money_type_wallet=1,amount,0)) as walletType1,
sum(if(money_type_wallet=43,amount,0)) as walletType43,
sum(if(money_type_wallet=411,amount,0)) as walletType411,
sum(if(money_type_wallet=421,amount,0)) as walletType421,
sum(if(money_type_wallet=431,amount,0)) as walletType431,
sum(if(money_type_wallet=400,amount,0)) as walletType400,
sum(if(money_type_wallet=6,amount,0)) as walletType6,
sum(if(money_type_wallet=61,amount,0)) as walletType61,
count(*) 
FROM platform_duojifen.t_payment_info group by day;

<i-option :value="6">钱包充值</i-option> 
	                  <i-option :value="61">积分充值</i-option> 

 <i-select v-model="q.moneyTypeWallet" @on-change="query" filterable  placeholder="分类">
	                  <i-option :value="2">余额转出记录</i-option> 
	                  <i-option :value="3">余额转入记录</i-option> 
	                  <i-option :value="62">积分兑换记录</i-option> 
	                  <i-option :value="92">积分支付记录</i-option> 
	                  <i-option :value="1">余额支付数量</i-option> 
	                  <i-option :value="43">资产权益收益</i-option> 
	                  <i-option :value="411">服务分享收益</i-option> 
	                  <i-option :value="421">幸运星星收益</i-option> 
	                  <i-option :value="431">分享社区收益</i-option> 
	                  <i-option :value="4000">社区活动基金</i-option> 
	                </i-select>


