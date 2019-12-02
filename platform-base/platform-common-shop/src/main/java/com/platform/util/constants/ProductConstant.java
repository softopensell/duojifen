package com.platform.util.constants;

public class ProductConstant {
	
	public static final int PRODUCT_STATU_NEW=0;
	public static final int PRODUCT_STATU_DELETE=1;
	
	public static final int PRODUCT_AUDIT_STATU_NEW=0;
	public static final int PRODUCT_AUDIT_STATU_NOPASS=1;
	public static final int PRODUCT_AUDIT_STATU_RENEW=2;
	public static final int PRODUCT_AUDIT_STATU_PASS=3;
	
	public static final int PRODUCT_SELL_STATU_NEW=0;
	public static final int PRODUCT_SELL_STATU_ONLINE=1;//销售中
	public static final int PRODUCT_SELL_STATU_SELLOUT=2;//卖光
	public static final int PRODUCT_SELL_STATU_OFFLINE=3;//下线
	
	
	public static final int PRODUCT_STOCK_NO_LIMINT=-1;
	
	
	public static final int PRODUCT_IS_SALE_NO=0;//不推荐
	public static final int PRODUCT_IS_SALE_YES=1;//推荐
	public static final int PRODUCT_IS_NEW_NO=0;//默认
	public static final int PRODUCT_IS_NEW_YES=1;//新品
	
	
	public static final int PRODUCT_TYPE_GOOD_DEFAUL=0;//普通商品
	public static final int PRODUCT_TYPE_GOOD_COPY_ART=1;//版画
	public static final int PRODUCT_TYPE_GOOD_KUANG_MAP=10;//画框
	
	
	public static final int PRODUCT_TYPE_SPECIAL=1;//代表特价产品
	
	
	public static final int PRODUCT_STOCk_NUMBERS=1;//默认库存数量
	public static final int PLATFORM_MEMBER_ID=1000;//平台用户
}
