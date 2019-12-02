ALTER TABLE `platform_duojifen`.`t_goods` 
ADD COLUMN `good_size_tag` VARCHAR(450) NULL COMMENT '尺寸' AFTER `updatetime`,
ADD COLUMN `good_color_tag` VARCHAR(450) NULL COMMENT '颜色' AFTER `good_size_tag`,
ADD COLUMN `good_other_tag` VARCHAR(450) NULL COMMENT '其他\n' AFTER `good_color_tag`;


ALTER TABLE `platform_duojifen`.`t_shop` 
ADD COLUMN `system_user_id` INT NULL AFTER `update_time`;


ALTER TABLE `platform_duojifen`.`t_goods_order_detail` 
ADD COLUMN `good_tags` VARCHAR(450) NULL COMMENT '购买规格标签，以，分隔' AFTER `update_time`;
