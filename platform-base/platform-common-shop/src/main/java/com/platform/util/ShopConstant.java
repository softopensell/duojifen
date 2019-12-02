package com.platform.util;

import java.util.Date;

import com.platform.utils.DateUtil;
import com.platform.utils.DateUtils;

public class ShopConstant {
	
	public static final int ORDER_PAY_TYPE_HOME = 0;//货到付款
	public static final int ORDER_PAY_TYPE_ONLINE = 1;//线上
	
	
	public static final int SHOP_USER_STATU_INIT=0;//刚注册信息
	public static final int SHOP_USER_STATU_SUCCESS=1;//注册成功
	public static final int SHOP_USER_STATU_FAIL=2;//失败
	public static final int SHOP_USER_STATU_OFFLINE=3;//失效
	
	
	
	public static final String PAY_METHOD_PLUGIN_TYPE_ALI="ALIPAY_Plugin";
	public static final String PAY_METHOD_PLUGIN_TYPE_ALI_NAME="支付宝支付";
	/**
	 * 商品默认商家
	 */
	public static final Integer DEFAULT_SHOP_ID=1;
	public static final String DEFAULT_SHOP_NAME="平台自营店";
	
	/**商品状态**/
	public static final Integer GOODS_SELL_STATUS_DOWN=0;//下架
	public static final Integer GOODS_SELL_STATUS_UP=1;//上架
	public static final Integer GOODS_SELL_STATUS_OVER=2;//售罄
	
	/**
	 * 支付方式
	 */
	
	public static final Integer PAY_TYPE_HDFK=0;//货到付款
	public static final Integer PAY_TYPE_WX=1;//微信
	public static final Integer PAY_TYPE_ZFB=2;//支付宝
	public static final Integer PAY_TYPE_YE=3;//余额
	public static final Integer PAY_TYPE_JF=4;//积分支付
	public static final Integer PAY_TYPE_XJ=5;//线下现金
	public static final Integer PAY_TYPE_SK=6;//线下刷卡
	
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
	
	/**
	 * 订单状态
	 */
	public static final Integer ORDER_STATUS_CONFRIM=1;//确定订单
	public static final Integer ORDER_STATUS_NOPAY=1;//待付款
	public static final Integer ORDER_STATUS_NOSHIPMENT=2;//支付成功，待发货 
	public static final Integer ORDER_STATUS_SHIPMENT=3;//待收货  已发货
	public static final Integer ORDER_STATUS_OK=4;//已完成 已收货
	public static final Integer ORDER_STATUS_CANCEL=5;//用户主动取消
	public static final Integer ORDER_STATUS_PAYTIMEOUT=6;//超时未支付取消
	public static final Integer ORDER_STATUS_ADMINCANCEL=7;//审核员取消订单
	public static final Integer ORDER_STATUS_REFUND=8;//退货
	public static final Integer ORDER_STATUS_TIMEOUTCONFIRM=9;//超时确认收货
	
	/**
	 * 发货状态
	 */
	public static final Integer SEND_STATUS_N=0;//未发货
	public static final Integer SEND_STATUS_Y=1;//已发货
	public static final Integer SEND_STATUS_QS=2;//已签收
	
	/**
	 * 提现申请状态
	 */
	public static final Integer PAYMENT_OUT_STATU_APPLY=1;//提款申请/未打款
	public static final Integer PAYMENT_OUT_STATU_SUCCESS=2;//提款支付成功/已打款
	public static final Integer PAYMENT_OUT_STATU_FAILL=3;// 提款失败//打款失败
	public static final Integer PAYMENT_OUT_STATU_DELETE=4;//提款删除
	public static final Integer PAYMENT_OUT_STATU_ERROR=999;//系统交互异常-需要补发重新审核提交支付
	
	
	public static final Integer SHOP_AUDIT_STATUS_SUCCESS=1;//商铺审核通过
	public static final Integer SHOP_AUDIT_STATUS_FAILL=2;//商铺审核拒绝
	
	public static final Long SHOP_ROLE_ID=7L;//商铺角色id
	
	
	public static final String DJF_STORAGE_PAY_KEY="DJF_STORAGE_PAY_KEY";
	
	public static final Date DJF_TJ_DATE=DateUtils.parseDateYYMMddHHmm("2019-12-02 04:30");//统计的其实时间
	
	
}
