package com.platform.mq.model;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 支付钱包流水记录 必须是成功的
 * @version 1.0
 */
public class PaymentTask extends ThirdPartyTask {
    private static Logger logger = LoggerFactory.getLogger(PaymentTask.class);
	private String sn;
	private Integer userId;//用户
	private String userName;//用户名称
	/** 对钱包影响 */
	/** 无影响 eg：支付订单  增加钱包金额 1 如充值 消费钱包 2  
	 * 钱包的影响 0 无影响 如 直接使用三方支付消费 1 余额支付  2 :转增 出 3:转赠-入 4：分红收益 5：提现
	 * */
	private Integer moneyTypeWallet;//流水类型
	/** 收款账号 */
	private String account;
	/** 支付手续费 */
	private double fee;
	/** 付款金额 */
	private BigDecimal amount;
	private String memo; //备注----系统备注
	private Integer orderType;
	private String orderNo;
	private String orderDesc;//订单简述----前端显示
	/**三方支付关联付款人 */
	private String payer;
	/**支付类型：0 进账 1 出账**/
	private Integer paymentType;
	/**
	 * 
	 */
	public PaymentTask() {
	}
	
	/**
	 * @param sn
	 * @param userId
	 * @param userName
	 * @param moneyTypeWallet
	 * @param account
	 * @param fee
	 * @param amount
	 * @param memo
	 * @param orderType
	 * @param orderNo
	 * @param payer
	 * @param paymentType
	 */
	public PaymentTask(String sn, Integer userId, String userName, Integer moneyTypeWallet, String account,
			double fee, BigDecimal amount, String memo, Integer orderType, String orderNo, String payer,
			Integer paymentType) {
		super();
		this.sn = sn;
		this.userId = userId;
		this.userName = userName;
		this.moneyTypeWallet = moneyTypeWallet;
		this.account = account;
		this.fee = fee;
		this.amount = amount;
		this.memo = memo;
		this.orderType = orderType;
		this.orderNo = orderNo;
		this.payer = payer;
		this.paymentType = paymentType;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the moneyTypeWallet
	 */
	public Integer getMoneyTypeWallet() {
		return moneyTypeWallet;
	}
	/**
	 * @param moneyTypeWallet the moneyTypeWallet to set
	 */
	public void setMoneyTypeWallet(Integer moneyTypeWallet) {
		this.moneyTypeWallet = moneyTypeWallet;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the fee
	 */
	public double getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(double fee) {
		this.fee = fee;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the orderType
	 */
	public Integer getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the payer
	 */
	public String getPayer() {
		return payer;
	}
	/**
	 * @param payer the payer to set
	 */
	public void setPayer(String payer) {
		this.payer = payer;
	}
	/**
	 * @return the paymentType
	 */
	public Integer getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
  
	

}
