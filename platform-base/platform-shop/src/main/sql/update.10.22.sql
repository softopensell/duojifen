DROP TABLE IF EXISTS `platform_monitor`;
CREATE TABLE `platform_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `monitor_date_number` varchar(45) NOT NULL COMMENT '中间编号，日期，序号',
  `monitor_type` int(11) DEFAULT NULL COMMENT '统计类型',
  `monitor_user_abnormal_sum` int(11) DEFAULT NULL COMMENT '异常会员数量：余额 和统计余额对应不上',
  `monitor_date` datetime DEFAULT NULL COMMENT '统计时间',
  `create_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `platform_monitor_detail`;
CREATE TABLE `platform_monitor_stat_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `monitor_date_number` varchar(45) NOT NULL COMMENT '中间编号，日期，序号',
  `monitor_member_id` int(11) DEFAULT NULL COMMENT '用户',
  `monitor_type` int(11) DEFAULT NULL COMMENT '监控类型',
  `monitor_content` varchar(45) NOT NULL COMMENT '异常说明',
  `create_time` datetime DEFAULT NULL COMMENT '添加，不允许更改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '每日监控', 'shop/platformmonitor.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'platformmonitor:list,platformmonitor:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'platformmonitor:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'platformmonitor:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'platformmonitor:delete', '2', null, '6';

    
    
    -- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '监控详情', 'shop/platformmonitorstadetail.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'platformmonitorstadetail:list,platformmonitorstadetail:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'platformmonitorstadetail:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'platformmonitorstadetail:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'platformmonitorstadetail:delete', '2', null, '6';

    
    
    
    ALTER TABLE `platform_duojifen`.`platform_monitor_stat_detail` 
CHANGE COLUMN `monitor_content` `monitor_content` VARCHAR(450) NOT NULL COMMENT '异常说明' ,
ADD COLUMN `monitor_member_name` VARCHAR(45) NULL AFTER `monitor_member_id`;




ALTER TABLE `platform_duojifen`.`sys_oss` 
CHANGE COLUMN `oss_from_no` `oss_from_no` VARCHAR(100) NULL DEFAULT NULL ;

