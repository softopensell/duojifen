use platform_duojifen;
truncate bonus_pool_join_member;
truncate t_bonus_invest_order;
truncate t_payment_info;
truncate t_payment_out;
truncate t_payment_log;
truncate sys_log;


use platform_duojifen;
delete from t_user where user_id>1000;
delete from t_bonus_points where id>2;

UPDATE `platform_duojifen`.`t_bonus_points` SET `bonus_team_invited_points`='0' WHERE `id`='1';
UPDATE `platform_duojifen`.`t_bonus_points` SET `bonus_team_invited_points`='0' WHERE `id`='2';
