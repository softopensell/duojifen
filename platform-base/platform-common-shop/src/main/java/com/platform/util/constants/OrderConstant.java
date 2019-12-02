package com.platform.util.constants;

public class OrderConstant {
	
	public static final String YunSmsSignName_1 = "积分兑换";

	public static final int ORDER_PAY_TYPE_HOME = 0;//货到付款
	public static final int ORDER_PAY_TYPE_ONLINE = 1;//线上
	
	public static final int ORDER_TYPE_SHOPPING = 1;//商品购买
	public static final int ORDER_TYPE_ARTIST = 2;//
	public static final int ORDER_TYPE_RECHARGE = 3;//充值
	public static final int ORDER_TYPE_RECHARGE_JF = 31;//充值
	
	public static final int ORDER_TYPE_TX = 5;//提现
	
	public static final int ORDER_TYPE_SERVICE =6;//维修服务
	public static final int ORDER_TYPE_CHOU =8;//
	public static final int ORDER_TYPE_BID = 10;//拍卖
	public static final int ORDER_TYPE_IDENTIFY = 11;//鉴定
	public static final int ORDER_TYPE_IDENTIFY_PHONE = 110;//查看手机号
	public static final int ORDER_TYPE_IDENTIFY_CONTENT = 111;//查看内容
	public static final int ORDER_TYPE_ARTIST_SHOPPING = 12;
	public static final int ORDER_TYPE_ACTIVTIY = 60;//门票服务
	public static final int ORDER_TYPE_INVERST = 10000;
	public static final int ORDER_TYPE_SYS = 10001;//系统消费
		
	
	//-1 代表线上   0货到付款 1 微信支付 2 支付宝 3 余额支付4积分支付
	public static final int ORDER_PAY_TYPE_ONLINE_WXPAY_H5 = 102;//微信支付-H5
	public static final int ORDER_PAY_TYPE_ONLINE_WXPAY = 1;//微信支付-APP
	public static final int ORDER_PAY_TYPE_ONLINE_ALPAY = 2;//支付宝
	public static final int ORDER_PAY_TYPE_ONLINE_YEPay = 3;//代表余额支付
	public static final int ORDER_PAY_TYPE_ONLINE_INTEGRAL = 4;//代表积分支付
	public static final int ORDER_PAY_TYPE_OFFLINE_CASH = 5;//代表线下现金交易
	public static final int ORDER_PAY_TYPE_OFFLINE_CARD = 6;//代表线下刷卡交易
	
	
	
	public static final int ORDER_INVEST_STATU_NO = 0;//未投资
	public static final int ORDER_INVEST_STATU_YES = 1;//已投资

}
