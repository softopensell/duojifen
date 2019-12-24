package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 t_payment_out
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
public class PaymentOutEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //ID
    private Long id;
    //交易编号
    private String outTradeNo;
    //付款方式
    private Integer method;
    //付款账户类型 公司
    private Integer payerType;
    //付款账户
    private String payer;
    //收款账户类型
    private Integer receiptAccountType;
    //收款账户
    private String receiptAccount;
    //收款账户真实姓名
    private String receiptAccountRealName;
    //费用类型：0 在线支付，1 现金
    private Integer amountType;
    //支付money
    private BigDecimal amount;
    //手续费
    private BigDecimal fee;
    //希望到款日期 截止23：50：50
    private Date amountPrePayDate;
    //提款时 当时余额
    private BigDecimal currBalance;
    //收款标题：eg 轻曼【提现】
    private String showPayTitle;
    //收款备注
    private String showBodyDesc;
    //用户ID
    private Integer userId;
    //用户姓名
    private String userName;
    //是否是提现本人账号
    private Integer isPayMember;
    //用户申请备注
    private String userApplyReason;
    //操作用户ID
    private Integer operatorId;
    //操作用户
    private String operatorName;
    //支付时间
    private Date paymentDate;
    //审核状态,-1;//不需要审核,实时支付;0;//审核
    private Integer auditStatu;
    //public static final Integer PAYMENT_OUT_STATU_APPLY=1;//提款申请
	public static final Integer PAYMENT_OUT_STATU_SUCCESS=2;//提款支付成功
	public static final Integer PAYMENT_OUT_STATU_FAILL=3;// 提款失败
	public static final Integer PAYMENT_OUT_STATU_DELETE=4;//提款删除
    private Integer status;
    //三方交易号
    private String snTradeNo;
    //三方交易描述
    private String snTradeNoDesc;
    //保留字段，支持不同系统 数据存储问题
    private String webApp;
    //保留字段，支持同系统，不同子公司，数据存储问题
    private String companySn;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    
    //昵称
    private String nickname;
    //等级
    private Integer userLevelType;
    //剩余资产
    private BigDecimal surplusInvestMoney;
    //服务中心
    private String fwName;

    /**
     * 设置：ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：ID
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：交易编号
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    /**
     * 获取：交易编号
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }
    /**
     * 设置：付款方式
     */
    public void setMethod(Integer method) {
        this.method = method;
    }

    /**
     * 获取：付款方式
     */
    public Integer getMethod() {
        return method;
    }
    /**
     * 设置：付款账户类型 公司
     */
    public void setPayerType(Integer payerType) {
        this.payerType = payerType;
    }

    /**
     * 获取：付款账户类型 公司
     */
    public Integer getPayerType() {
        return payerType;
    }
    /**
     * 设置：付款账户
     */
    public void setPayer(String payer) {
        this.payer = payer;
    }

    /**
     * 获取：付款账户
     */
    public String getPayer() {
        return payer;
    }
    /**
     * 设置：收款账户类型
     */
    public void setReceiptAccountType(Integer receiptAccountType) {
        this.receiptAccountType = receiptAccountType;
    }

    /**
     * 获取：收款账户类型
     */
    public Integer getReceiptAccountType() {
        return receiptAccountType;
    }
    /**
     * 设置：收款账户
     */
    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount;
    }

    /**
     * 获取：收款账户
     */
    public String getReceiptAccount() {
        return receiptAccount;
    }
    /**
     * 设置：收款账户真实姓名
     */
    public void setReceiptAccountRealName(String receiptAccountRealName) {
        this.receiptAccountRealName = receiptAccountRealName;
    }

    /**
     * 获取：收款账户真实姓名
     */
    public String getReceiptAccountRealName() {
        return receiptAccountRealName;
    }
    /**
     * 设置：费用类型：0 在线支付，1 现金
     */
    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    /**
     * 获取：费用类型：0 在线支付，1 现金
     */
    public Integer getAmountType() {
        return amountType;
    }
    /**
     * 设置：支付money
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：支付money
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 设置：手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取：手续费
     */
    public BigDecimal getFee() {
        return fee;
    }
    /**
     * 设置：希望到款日期 截止23：50：50
     */
    public void setAmountPrePayDate(Date amountPrePayDate) {
        this.amountPrePayDate = amountPrePayDate;
    }

    /**
     * 获取：希望到款日期 截止23：50：50
     */
    public Date getAmountPrePayDate() {
        return amountPrePayDate;
    }
    /**
     * 设置：提款时 当时余额
     */
    public void setCurrBalance(BigDecimal currBalance) {
        this.currBalance = currBalance;
    }

    /**
     * 获取：提款时 当时余额
     */
    public BigDecimal getCurrBalance() {
        return currBalance;
    }
    /**
     * 设置：收款标题：eg 轻曼【提现】
     */
    public void setShowPayTitle(String showPayTitle) {
        this.showPayTitle = showPayTitle;
    }

    /**
     * 获取：收款标题：eg 轻曼【提现】
     */
    public String getShowPayTitle() {
        return showPayTitle;
    }
    /**
     * 设置：收款备注
     */
    public void setShowBodyDesc(String showBodyDesc) {
        this.showBodyDesc = showBodyDesc;
    }

    /**
     * 获取：收款备注
     */
    public String getShowBodyDesc() {
        return showBodyDesc;
    }
    /**
     * 设置：用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户ID
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：用户姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：用户姓名
     */
    public String getUserName() {
        return userName;
    }
    /**
     * 设置：是否是提现本人账号
     */
    public void setIsPayMember(Integer isPayMember) {
        this.isPayMember = isPayMember;
    }

    /**
     * 获取：是否是提现本人账号
     */
    public Integer getIsPayMember() {
        return isPayMember;
    }
    /**
     * 设置：用户申请备注
     */
    public void setUserApplyReason(String userApplyReason) {
        this.userApplyReason = userApplyReason;
    }

    /**
     * 获取：用户申请备注
     */
    public String getUserApplyReason() {
        return userApplyReason;
    }
    /**
     * 设置：操作用户ID
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取：操作用户ID
     */
    public Integer getOperatorId() {
        return operatorId;
    }
    /**
     * 设置：操作用户
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取：操作用户
     */
    public String getOperatorName() {
        return operatorName;
    }
    /**
     * 设置：支付时间
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * 获取：支付时间
     */
    public Date getPaymentDate() {
        return paymentDate;
    }
    /**
     * 设置：审核状态,-1;//不需要审核,实时支付;0;//审核
     */
    public void setAuditStatu(Integer auditStatu) {
        this.auditStatu = auditStatu;
    }

    /**
     * 获取：审核状态,-1;//不需要审核,实时支付;0;//审核
     */
    public Integer getAuditStatu() {
        return auditStatu;
    }
    /**
     * 设置：public static final Integer PAYMENT_OUT_STATU_APPLY=1;//提款申请
	public static final Integer PAYMENT_OUT_STATU_SUCCESS=2;//提款支付成功
	public static final Integer PAYMENT_OUT_STATU_FAILL=3;// 提款失败
	public static final Integer PAYMENT_OUT_STATU_DELETE=4;//提款删除
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：public static final Integer PAYMENT_OUT_STATU_APPLY=1;//提款申请
	public static final Integer PAYMENT_OUT_STATU_SUCCESS=2;//提款支付成功
	public static final Integer PAYMENT_OUT_STATU_FAILL=3;// 提款失败
	public static final Integer PAYMENT_OUT_STATU_DELETE=4;//提款删除
     */
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置：三方交易号
     */
    public void setSnTradeNo(String snTradeNo) {
        this.snTradeNo = snTradeNo;
    }

    /**
     * 获取：三方交易号
     */
    public String getSnTradeNo() {
        return snTradeNo;
    }
    /**
     * 设置：三方交易描述
     */
    public void setSnTradeNoDesc(String snTradeNoDesc) {
        this.snTradeNoDesc = snTradeNoDesc;
    }

    /**
     * 获取：三方交易描述
     */
    public String getSnTradeNoDesc() {
        return snTradeNoDesc;
    }
    /**
     * 设置：保留字段，支持不同系统 数据存储问题
     */
    public void setWebApp(String webApp) {
        this.webApp = webApp;
    }

    /**
     * 获取：保留字段，支持不同系统 数据存储问题
     */
    public String getWebApp() {
        return webApp;
    }
    /**
     * 设置：保留字段，支持同系统，不同子公司，数据存储问题
     */
    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    /**
     * 获取：保留字段，支持同系统，不同子公司，数据存储问题
     */
    public String getCompanySn() {
        return companySn;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getUserLevelType() {
		return userLevelType;
	}

	public void setUserLevelType(Integer userLevelType) {
		this.userLevelType = userLevelType;
	}

	public BigDecimal getSurplusInvestMoney() {
		return surplusInvestMoney;
	}

	public void setSurplusInvestMoney(BigDecimal surplusInvestMoney) {
		this.surplusInvestMoney = surplusInvestMoney;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

    
}
