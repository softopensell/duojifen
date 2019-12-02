package com.platform.constants;

public class BonusConstant {
	public static final String  BONUS_POOL_NUMBER_COMPANY="BONUS_POOL_NUMBER_COMPANY_SYS_JF";
	public static final int  BONUS_POOL_TYPE_PLOT=1;
	
	public static final int  BONUS_INVEST_ORDER_STATU_OK=0;
	public static final int  BONUS_INVEST_ORDER_STATU_OFFLINE=1;
	public static final int  BONUS_INVEST_ORDER_STATU_DELETE=2;
	/**
	 * 支付状态
	 */
	public static final Integer PAY_STATUS_NOPAY=0;//待付款
	public static final Integer PAY_STATUS_PAYING=1;//付款中
	public static final Integer PAY_STATUS_PAYOK=2;//付款成功
	public static final Integer PAY_STATUS_PAYERROR=3;//付款失败
	public static final Integer PAY_STATUS_REFUNDING=4;//退款中
	public static final Integer PAY_STATUS_REFUNDOK=5;//退款成功
	public static final Integer PAY_STATUS_REFUNDERROR=6;//退款失败
	
	
	public static final int BONUS_POINT_BLOODTYPE_RECOMMD=0;
	public static final int BONUS_POINT_BLOODTYPE_BINARYTREE=1;
	
	
	public static final int BONUS_POOL_JOIN_MEMBER_ID=1;//系统用户Id
	
	public static final int BONUS_POOL_JOIN_TYPE_MEMBER=0;//普通用户
	public static final int BONUS_POOL_JOIN_TYPE_RATE=1;//日均百分比
	
	
	
	public static final String BONUS_EVERY_DAY_RANDOM_MIN ="BONUS_EVERY_DAY_RANDOM_MIN";//随机最小值
	public static final String BONUS_EVERY_DAY_RANDOM_MAX ="BONUS_EVERY_DAY_RANDOM_MAX";//随机最大值
	public static final String BONUS_EVERY_DAY_RANDOM_TYPE ="BONUS_EVERY_DAY_RANDOM_TYPE";//随机类别
	public static final String BONUS_EVERY_DAY_RANDOM_DENOM ="BONUS_EVERY_DAY_RANDOM_DENOM";//分母
	
	
	public static final String BONUS_RECOMMED_LEVEL_MAX ="BONUS_RECOMMED_LEVEL_MAX";//奖励层级
	public static final String BONUS_RECOMMED_LEVEL_DEFAULT_RATE ="BONUS_RECOMMED_LEVEL_DEFAULT_RATE";//默认奖励比例
	public static final String BONUS_RECOMMED_LEVEL_SP_RATE ="BONUS_RECOMMED_LEVEL_SP_RATE";//奖励比例_1
	
	public static final String BONUS_NODE_LEVEL_MAX ="BONUS_NODE_LEVEL_MAX";//幸运奖层级
	public static final String BONUS_NODE_LEVEL_MAX_DEFAULT_RATE ="BONUS_NODE_LEVEL_MAX_DEFAULT_RATE";//默认奖励比例
	public static final String BONUS_NODE_LEVEL_MAXL_SP_RATE ="BONUS_NODE_LEVEL_MAXL_SP_RATE";//幸运奖比例_1
	
	public static final String BONUS_NODE_POLT_LEVEL_MAX ="BONUS_NODE_POLT_LEVEL_MAX";//幸运奖层级
	public static final String BONUS_NODE_POLT_LEVEL_MAX_DEFAULT_RATE ="BONUS_NODE_POLT_LEVEL_MAX_DEFAULT_RATE";//默认奖励比例
	public static final String BONUS_NODE_POLT_LEVEL_MAXL_SP_RATE ="BONUS_NODE_POLT_LEVEL_MAXL_SP_RATE";//幸运奖比例_1
	
	
	
	
	
	public static final String BONUS_FUND_SP_RATE ="BONUS_FUND_SP_RATE";//基金
	
	public static final int  BONUS_PARENT_TYPE_MORE_TREE=0;
	//	binary tree 二叉树
	public static final int  BONUS_PARENT_TYPE_BINARY_TREE=1;//二叉树
	//Trinary tree
	public static final int  BONUS_PARENT_TYPE_THREE_TREE=2;//三叉树
	
	public static final String RECOMMEND_PERFORMANCE_BASE_MONEY ="RECOMMEND_PERFORMANCE_BASE_MONEY";//提成定额 800 元
	public static final String RECOMMEND_PERFORMANCE_MANAGE_PERCENT ="RECOMMEND_PERFORMANCE_MANAGE_PERCENT";//管理奖励比例： 团队业绩*20%
	public static final String RECOMMEND_PERFORMANCE_PROJECT_MONEY ="RECOMMEND_PERFORMANCE_PROJECT_MONEY";//项目利润=600元
	public static final String RECOMMEND_PERFORMANCE_PROJECT_PERCENT ="RECOMMEND_PERFORMANCE_PROJECT_PERCENT";//项目利润10%的平均分红
	
	
	public static final int  BONUS_STATUS_OK =0;
	public static final int  BONUS_STATUS_DELETE =1;
	
	 //职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
    
	public static final int  BONUS_MEMBER_ROLE_TYPE_member =0;
	public static final int  BONUS_MEMBER_ROLE_TYPE_manager =1;
	public static final int  BONUS_MEMBER_ROLE_TYPE_shopper_one=2;
	public static final int  BONUS_MEMBER_ROLE_TYPE_shopper_two =3;
	public static final int  BONUS_MEMBER_ROLE_TYPE_shopper_three =4;
	public static final int  BONUS_MEMBER_ROLE_TYPE_shopper_four =5;
	
	
	public static final String  BONUS_MEMBER_ROLE_TYPE_member_LEAST_MONEY_KEY = "BONUS_MEMBER_ROLE_TYPE_member_LEAST_MONEY_KEY";
	public static final String  BONUS_MEMBER_ROLE_TYPE_manager_LEAST_MONEY_KEY= "BONUS_MEMBER_ROLE_TYPE_manager_LEAST_MONEY_KEY";
	public static final String  BONUS_MEMBER_ROLE_TYPE_shopper_one_LEAST_MONEY_KEY= "BONUS_MEMBER_ROLE_TYPE_shopper_one_LEAST_MONEY_KEY";
	public static final String  BONUS_MEMBER_ROLE_TYPE_shopper_two_LEAST_MONEY_KEY= "BONUS_MEMBER_ROLE_TYPE_shopper_two_LEAST_MONEY_KEY";
	public static final String  BONUS_MEMBER_ROLE_TYPE_shopper_three_LEAST_MONEY_KEY= "BONUS_MEMBER_ROLE_TYPE_shopper_three_LEAST_MONEY_KEY";
	public static final String  BONUS_MEMBER_ROLE_TYPE_shopper_four_LEAST_MONEY_KEY= "BONUS_MEMBER_ROLE_TYPE_shopper_four_LEAST_MONEY_KEY";
	
	
	//称呼:0 普通会员，1 分公司，2 代理商，3 分销商
	public static final int  BONUS_MEMBER_ROLE_NAME_TYPE_MEMBER =0;
	public static final int  BONUS_MEMBER_ROLE_NAME_TYPE_FGS =1;
	public static final int  BONUS_MEMBER_ROLE_NAME_TYPE_DLS=2;
	public static final int  BONUS_MEMBER_ROLE_NAME_TYPE_FXS =3;
	
	
	//上级推荐人和上级管理者的分隔符
	public static final String	INVITED_MEMBER_IDS_SPLIT = "#-";
	
	
	
	
	public static final String BONUS_WITHDRAW_USER_RATE ="BONUS_WITHDRAW_USER_RATE";//用户手续 
	public static final String BONUS_WITHDRAW_USER_SYS_RATE ="BONUS_WITHDRAW_USER_SYS_RATE";//3%计入公司运营，3%计入共识机制，4%计入合伙人
	public static final String BONUS_WITHDRAW_USER_GS_RATE ="BONUS_WITHDRAW_USER_GS_RATE";//3%计入公司运营，3%计入共识机制，4%计入合伙人
	public static final String BONUS_WITHDRAW_USER_HH_RATE ="BONUS_WITHDRAW_USER_HH_RATE";//3%计入公司运营，3%计入共识机制，4%计入合伙人
	
	
	public static final String BONUS_WITHDRAW_USER_TX_RATE ="BONUS_WITHDRAW_USER_TX_RATE";//用户提现
	
	
}