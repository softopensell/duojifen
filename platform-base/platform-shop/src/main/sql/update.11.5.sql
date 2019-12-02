use platform_duojifen;

DROP TABLE IF EXISTS `activity_sponsor`;
CREATE TABLE `activity_sponsor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_no` varchar(50) NOT NULL COMMENT '活动itemNo',
  `sponsor_name` varchar(50) DEFAULT NULL COMMENT '赞助商名称',
  `sponsor_logo` varchar(500) DEFAULT NULL COMMENT '图标',
  `sponsor_desc` varchar(500) DEFAULT NULL COMMENT '描述',
  `sponsor_money` int(11) DEFAULT '0' COMMENT '赞助金额',
  `statu` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `activity_ticket`;
CREATE TABLE `activity_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `ticket_kind` int(11) NOT NULL COMMENT '分类',
  `item_no` varchar(100) NOT NULL COMMENT '大活动NO',
  `ticket_name` varchar(100) DEFAULT NULL COMMENT 'ticket_name',
  `ticket_logo` varchar(100) DEFAULT NULL COMMENT 'ticket_logo',
  `description` varchar(100) DEFAULT NULL COMMENT 'description',
  `ticket_price` int(11) DEFAULT NULL COMMENT '价格',
  `ticket_org_price` int(11) DEFAULT NULL COMMENT '原价格',
  `max_sum` int(11) DEFAULT NULL COMMENT 'max_sum',
  `sell_sum` int(11) DEFAULT NULL COMMENT 'sell_sum',
  `statu` int(11) NOT NULL COMMENT '状态 0 有效 1 删除',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1015 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `activity_item`;
CREATE TABLE `activity_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_no` varchar(100) NOT NULL COMMENT '活动号',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `item_list` text COMMENT '演出节目',
  `description` text COMMENT '活动描述',
  `statu` int(11) NOT NULL COMMENT '状态 0 有效 1 删除',
  `province_id` int(11) DEFAULT NULL COMMENT '省份',
  `city_id` int(11) DEFAULT NULL COMMENT '城市',
  `region_id` int(11) DEFAULT NULL,
  `address` varchar(145) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `organizer_name` varchar(100) DEFAULT NULL COMMENT '主办方',
  `organizer_link_name` varchar(45) DEFAULT NULL,
  `organizer_link_phone` varchar(45) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `click_sum` int(11) DEFAULT NULL COMMENT '点击次数',
  `item_kind` int(11) DEFAULT NULL COMMENT '分类',
  `item_logo` varchar(145) DEFAULT NULL COMMENT '轮番图片',
  `jobs_sum` int(11) DEFAULT '0',
  `ticket_sum` int(11) DEFAULT '0',
  `item_type` int(11) DEFAULT '0' COMMENT '类型',
  `apply_people_sum` int(11) DEFAULT NULL,
  `product_sum` int(11) DEFAULT NULL,
  `virtual_product_sum` int(11) DEFAULT '0',
  `virtual_apply_people_sum` int(11) DEFAULT '0',
  `apply_end_time` datetime DEFAULT NULL,
  `apply_statu` int(11) DEFAULT NULL,
  `apply_intro` text,
  `product_price` int(11) DEFAULT NULL,
  `item_sequence` int(11) DEFAULT NULL,
  `item_audit_statu` int(11) DEFAULT NULL,
  `item_audit_refuse` varchar(45) DEFAULT NULL,
  `praise_sum` int(11) DEFAULT '0',
  `item_max_sum` int(11) DEFAULT NULL,
  `company_sn` varchar(45) DEFAULT NULL,
  `item_pay_type` int(11) DEFAULT NULL COMMENT '支付类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_no_UNIQUE` (`item_no`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `activity_join`;
CREATE TABLE `activity_join` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_no` varchar(50) NOT NULL COMMENT '活动itemNo',
  `statu` int(11) DEFAULT '0' COMMENT '状态',
  `join_type` int(11) DEFAULT NULL,
  `join_invite_number` varchar(145) DEFAULT NULL,
  `join_invite_name` varchar(45) DEFAULT NULL,
  `join_invite_logo` varchar(545) DEFAULT NULL,
  `join_invite_title` varchar(145) DEFAULT NULL,
  `join_invite_contentType` int(11) DEFAULT NULL,
  `join_invite_desc` varchar(545) DEFAULT NULL,
  `join_invite_image` varchar(545) DEFAULT NULL,
  `join_invite_write_name` varchar(145) DEFAULT NULL,
  `join_invite_write_date` datetime DEFAULT NULL,
  `join_invite_read_statu` int(11) DEFAULT NULL,
  `join_member_id` int(11) DEFAULT NULL,
  `join_audit_statu` int(11) DEFAULT NULL,
  `join_audit_refuse` varchar(545) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `activity_ticket_order`;
CREATE TABLE `activity_ticket_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `member_id` int(11) DEFAULT NULL COMMENT '用户id',
  `member_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `member_phone` varchar(100) DEFAULT NULL COMMENT '电话',
  `member_address` varchar(100) DEFAULT NULL COMMENT '邮寄地址',
  `member_remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `ticket_id` int(11) DEFAULT NULL COMMENT '票',
  `ticket_name` varchar(100) DEFAULT NULL COMMENT 'ticket_name',
  `ticket_price` int(11) DEFAULT NULL COMMENT '价格',
  `ticket_sum` int(11) DEFAULT NULL,
  `total_price` int(11) DEFAULT NULL COMMENT '总价 ',
  `pay_statu` int(11) DEFAULT NULL COMMENT '状态 0 有效 1 删除',
  `member_statu` int(11) DEFAULT NULL COMMENT '状态 0 有效 1 删除',
  `order_statu` int(11) DEFAULT NULL COMMENT '状态 0 有效 1 咨询过，2 发货 ',
  `statu` int(11) NOT NULL COMMENT '状态 0 有效 1 删除',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `item_no` varchar(100) DEFAULT NULL,
  `order_type` int(11) DEFAULT NULL,
  `item_title` varchar(100) DEFAULT NULL,
  `company_sn` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1183 DEFAULT CHARSET=utf8;









-- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '活动管理', 'shop/activityitem.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'activityitem:list,activityitem:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'activityitem:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'activityitem:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'activityitem:delete', '2', null, '6';

    -- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '参与人员', 'shop/activityjoin.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'activityjoin:list,activityjoin:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'activityjoin:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'activityjoin:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'activityjoin:delete', '2', null, '6';

    
    -- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '赞助商', 'shop/activitysponsor.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'activitysponsor:list,activitysponsor:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'activitysponsor:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'activitysponsor:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'activitysponsor:delete', '2', null, '6';

    
    -- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '活动报名', 'shop/activitytickeorder.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'activitytickeorder:list,activitytickeorder:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'activitytickeorder:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'activitytickeorder:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'activitytickeorder:delete', '2', null, '6';

    -- 菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('1', '活动门票', 'shop/activityticket.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '查看', null, 'activityticket:list,activityticket:info', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '新增', null, 'activityticket:save', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '修改', null, 'activityticket:update', '2', null, '6';
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT @parentId, '删除', null, 'activityticket:delete', '2', null, '6';
    
    
    
    
    ALTER TABLE `platform_duojifen`.`activity_item` 
ADD COLUMN `item_pay_type` INT NULL COMMENT '支付类型' AFTER `company_sn`;

