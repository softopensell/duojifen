package com.platform.constants;
public class PluginConstant {
	
	// // 获取：支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
	
	public static final String PAY_METHOD_PLUGIN_TYPE_ALI="ALIPAY_Plugin";
	public static final String PAY_METHOD_PLUGIN_TYPE_ALI_NAME="支付宝支付";
	
	public static final String PAY_METHOD_PLUGIN_TYPE_WX="WXPAY_Plugin";
	public static final String PAY_METHOD_PLUGIN_TYPE_WX_NAME="微信支付";
	
	public static final String PAY_METHOD_PLUGIN_TYPE_MEMBER_WALLET="MEMBER_WALLET_Plugin";
	public static final String PAY_METHOD_PLUGIN_TYPE_MEMBER_WALLET_NAME="会员钱包支付";
	
	public static final String PAY_METHOD_PLUGIN_TYPE_INTEGRAL="INTEGRAL_Plugin";
	public static final String PAY_METHOD_PLUGIN_TYPE_INTEGRAL_NAME="积分支付";
	
	public static final String PAY_METHOD_PLUGIN_TYPE_OFFLINE="OFFLINE_Plugin";
	public static final String PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME="线下支付";
	
	
	public static final Integer PAY_METHOD_TIMEOUT=1*60*24;//支付宝支付
	
	
	public static final Integer PAYMENT_TYPE_OUT=1;//出账
	public static final Integer PAYMENT_TYPE_IN=0;//进账
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_NO=0;//钱包的状态默认 0 订单类型，消费类
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_OUT_CONSUMED=1;//余额支付
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_DONATE_OUT=2;//转增 出
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_DONATE_IN=3;//转增 入
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS=4;//分红
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED=41;//销售提成
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_DAY=42;//收益奖励
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_JQ=43;//资产权益收益
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_CONSUMED=400;//消费奖励
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED_CONSUMED=410;//消费推荐奖励
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED_INVEST=411;//直接分享收益
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_CONSUMED=420;//节点消费推荐奖励
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST=421;//幸运星收益
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_CONSUMED_POLT=430;//节点量碰消费推荐奖励
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST_POLT=431;//分享社区收益
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_FUND=4000;//基金
	
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_OUT=5;//提现
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE=6;//支付到钱包 充值
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE_JF=61;//积分充值
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_OUT_JF=62;//兑换积分
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_OUT_ZC=620;//消费 资产

	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_BACK_JF=63;//退款积分
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_BACK_MONEY=64;//退款
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_BACK_ZC=65;//退款 资产
	
	
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BID=8;//拍卖分红
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BID_RECOMMED=81;//拍卖推荐奖励
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BID_JOIN_PROFIT=82;//拍卖参与分红
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_BID_JOIN_PROFIT_RECOMMED=83;//拍卖分红之推荐奖励	
	
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL=9;//积分收益
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_ADD=91;//添加积分
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_PAY=92;//消耗积分
	
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_FW_MANAGER_PAY=1010;//服务中心分红
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_FW_MANAGER_PAY_ADD=1011;//服务中心流水
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_IN_FW_TX_PAY=1020;//提现分红
	
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_SYS_IN_BALANCE=10000;//平台增加余额
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_SYS_IN_ZC=10010;//平台增加资产
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_SYS_IN_JF=10020;//平台增加积分
	
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_SYS_OUT_BALANCE=10001;//平台扣除余额
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_SYS_OUT_ZC=10011;//平台扣除资产
	public static final Integer PAYMENT_MONEY_TYPE_WALLET_SYS_OUT_JF=10021;//平台扣除积分
	
	
	
	public static final Integer PAYMENT_OUT_MEHD_WX=1;//微信支付
	public static final Integer PAYMENT_OUT_MEHD_ALIY=2;//支付宝支付
	public static final Integer PAYMENT_OUT_MEHD_WALLET=3;//余额支付
	public static final Integer PAYMENT_OUT_MEHD_INTEGRAL=4;//积分支付
	
	public static final Integer PAYMENT_OUT_AMOUNT_TYPE_ALY=1;//支付宝
	public static final Integer PAYMENT_OUT_AMOUNT_TYPE_WX=2;//微信支付
	public static final Integer PAYMENT_OUT_AMOUNT_TYPE_WALLET=3;//硬币支付
	public static final Integer PAYMENT_OUT_AMOUNT_TYPE_INTEGRAL=4;//积分支付
	
	public static final Integer PAYMENT_OUT_AMOUNT_TYPE_ONLINE=5;//现金支付
	
	public static final Integer PAYMENT_OUT_STATU_APPLY=1;//提款申请
	public static final Integer PAYMENT_OUT_STATU_SUCCESS=2;//提款支付成功
	public static final Integer PAYMENT_OUT_STATU_FAILL=3;// 提款失败
	public static final Integer PAYMENT_OUT_STATU_DELETE=4;//提款删除
	public static final Integer PAYMENT_OUT_STATU_ERROR=999;//系统交互异常-需要补发重新审核提交支付
	
	public static final Integer PAYMENT_OUT_PAYER_TYPE_COMPANY=0;//公司
	public static final Integer PAYMENT_OUT_PAYER_TYPE_PERSON=1;//个人
	
	public static final Integer PAYMENT_OUT_AUDIT_STATU_NO=-1;//不需要审核,实时支付
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES=0;//审核
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_PASS=10;//审核通过
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_NO_PASS=20;//审核失败
	

	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_DEPARTMENT_MANAGER=2;//领导审批通过 
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_COMPANY_AUDIT=3;//审批通过
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_FINAN_AUDIT=5;//财务审批
	
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_SUCCESS=10;//审核通过
	public static final Integer PAYMENT_OUT_AUDIT_STATU_YES_FAIL=20;//审核失败
	
	
	public static final Integer STORAGEPLUGIN_TYPE_LOCAL=1;//
	public static final Integer STORAGEPLUGIN_TYPE_REMOTE=2;
	public static final Integer STORAGEPLUGIN_TYPE_ALIYUN=3;
	
	
	public static final Integer PAYMENT_ADUIT_STATU_OK=0;
	public static final Integer PAYMENT_ADUIT_STATU_DELETE=1;
	
	public static final Integer PAYMENT_ADUIT_AUDIT_STATU_INIT=-1;//未审核
	public static final Integer PAYMENT_ADUIT_AUDIT_STATU_YES_SUCCESS=1;//审核通过
	public static final Integer PAYMENT_ADUIT_AUDIT_STATU_YES_FAIL=2;//审核失败
	
	public static final Integer PAYMENT_AUDIT_AUDIT_STATU_TYPE_DEFAULT=0;//审批权
	public static final Integer PAYMENT_AUDIT_AUDIT_STATU_TYPE_EXECUTE=1;//执行权
	public static final Integer PAYMENT_AUDIT_AUDIT_STATU_TYPE_DEPARTMENT_MANAGER=10;//部门领导
	
	public static final Integer PAYMENT_AUDIT_TYPE_OUT=1;//支出
	public static final Integer PAYMENT_AUDIT_TYPE_INT=0;//收入
	
	
	
	
	public static final int DB_STAT_DAY=0;//日
	public static final int DB_STAT_WEEK=1;//周
	public static final int DB_STAT_MON=2;//月
	public static final int DB_STAT_YEAR=3;//年
	
	
	
	

	
	public static final Integer PAY_CARD_NAME_ALI=1;//支付宝支付
	public static final Integer PAY_CARD_NAME_WEX=2;//微信支付
	public static final Integer PAY_CARD_NAME_BANK=3;//银行支付
	
}