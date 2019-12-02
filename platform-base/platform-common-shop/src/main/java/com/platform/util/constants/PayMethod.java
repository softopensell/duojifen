package com.platform.util.constants;
/**
 * 方式
 */
public enum PayMethod {
	// 利用构造函数传参
	/** 在线支付 */
	PAY_METHOD_online(1),
	/** 线下支付 */
	PAY_METHOD_offline(2),
	/** 预存款支付 */
	PAY_METHOD_deposit(3),
	/**4 折扣卡支付 discount**/
	PAY_METHOD_discount(4);
    // 定义私有变量
    public int nCode;
    // 构造函数，枚举类型只能为私有
    private PayMethod(int _nCode) {
        this.nCode = _nCode;
    }
    @Override
    public String toString() {
        return String.valueOf(this.nCode);
    }
    public int getnCode(){
    	return this.nCode;
    }
};