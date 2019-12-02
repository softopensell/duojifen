package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 实体
 * 表名 t_payment_info
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-03 23:37:22
 */
public class PaymentInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //ID
    private Long id;
    //收款账号：默认公司：GJZB
    private String account;
    //支付总额
    private BigDecimal amount;
    //不用了
    private String bank;
    //超时时间
    private Date expire;
    //手续费
    private BigDecimal fee;
    //操作员ID visitor
    private Integer operatorId;
    //visitor name
    private String operatorName;
    //付款时间
    private Date paymentDate;
    //支付插件ID :PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX,
    private String paymentPluginId;
    //支付方式：eg:PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME, 
								
    private String paymentMethod;
    //流水号
    private String sn;
    //状态
    private Integer status;
    //order ----user_id 
    private Long userId;
    //order ----user_Name
    private String payer;
    //订单类型：1：shopping
    private Integer orderType;
    //订单号
    private String orderNo;
    //订单简述
    private String orderDesc;
    //三方个 流水号
    private String paymentserialnumber;
    //会员名称
    private String userName;
    //
    private String logisticsName;
    //
    private String logisticsNumber;
    //
    private String logisticsType;
    //
    private String webApp;
    //公司标识
    private String companySn;
    //备注
    private String memo;
    //钱包的影响 0 无影响 如 直接使用三方支付消费 1 余额支付  2 :转增 出3:转赠-入 4：分红收益 5：提现
    private Integer moneyTypeWallet;
    //支付类型：0 进账 1 出账
    private Integer paymentType;
    //新增时间
    private Date createTime;
    //修改时间
    private Date updateTime;

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
     * 设置：收款账号：默认公司：GJZB
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取：收款账号：默认公司：GJZB
     */
    public String getAccount() {
        return account;
    }
    /**
     * 设置：支付总额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：支付总额
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 设置：不用了
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取：不用了
     */
    public String getBank() {
        return bank;
    }
    /**
     * 设置：超时时间
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * 获取：超时时间
     */
    public Date getExpire() {
        return expire;
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
     * 设置：操作员ID visitor
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取：操作员ID visitor
     */
    public Integer getOperatorId() {
        return operatorId;
    }
    /**
     * 设置：visitor name
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取：visitor name
     */
    public String getOperatorName() {
        return operatorName;
    }
    /**
     * 设置：付款时间
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * 获取：付款时间
     */
    public Date getPaymentDate() {
        return paymentDate;
    }
    /**
     * 设置：支付插件ID :PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX,
     */
    public void setPaymentPluginId(String paymentPluginId) {
        this.paymentPluginId = paymentPluginId;
    }

    /**
     * 获取：支付插件ID :PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX,
     */
    public String getPaymentPluginId() {
        return paymentPluginId;
    }
    /**
     * 设置：支付方式：eg:PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME, 
								
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 获取：支付方式：eg:PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME, 
								
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }
    /**
     * 设置：流水号
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取：流水号
     */
    public String getSn() {
        return sn;
    }
    /**
     * 设置：状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态
     */
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置：order ----user_id 
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：order ----user_id 
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * 设置：order ----user_Name
     */
    public void setPayer(String payer) {
        this.payer = payer;
    }

    /**
     * 获取：order ----user_Name
     */
    public String getPayer() {
        return payer;
    }
    /**
     * 设置：订单类型：1：shopping
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取：订单类型：1：shopping
     */
    public Integer getOrderType() {
        return orderType;
    }
    /**
     * 设置：订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取：订单号
     */
    public String getOrderNo() {
        return orderNo;
    }
    /**
     * 设置：订单简述
     */
    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    /**
     * 获取：订单简述
     */
    public String getOrderDesc() {
        return orderDesc;
    }
    /**
     * 设置：三方个 流水号
     */
    public void setPaymentserialnumber(String paymentserialnumber) {
        this.paymentserialnumber = paymentserialnumber;
    }

    /**
     * 获取：三方个 流水号
     */
    public String getPaymentserialnumber() {
        return paymentserialnumber;
    }
    /**
     * 设置：会员名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：会员名称
     */
    public String getUserName() {
        return userName;
    }
    /**
     * 设置：
     */
    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    /**
     * 获取：
     */
    public String getLogisticsName() {
        return logisticsName;
    }
    /**
     * 设置：
     */
    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    /**
     * 获取：
     */
    public String getLogisticsNumber() {
        return logisticsNumber;
    }
    /**
     * 设置：
     */
    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    /**
     * 获取：
     */
    public String getLogisticsType() {
        return logisticsType;
    }
    /**
     * 设置：
     */
    public void setWebApp(String webApp) {
        this.webApp = webApp;
    }

    /**
     * 获取：
     */
    public String getWebApp() {
        return webApp;
    }
    /**
     * 设置：公司标识
     */
    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    /**
     * 获取：公司标识
     */
    public String getCompanySn() {
        return companySn;
    }
    /**
     * 设置：备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取：备注
     */
    public String getMemo() {
        return memo;
    }
    /**
     * 设置：钱包的影响 0 无影响 如 直接使用三方支付消费 1 余额支付  2 :转增 出
3:转赠-入 4：分红收益 5：提现
     */
    public void setMoneyTypeWallet(Integer moneyTypeWallet) {
        this.moneyTypeWallet = moneyTypeWallet;
    }

    /**
     * 获取：钱包的影响 0 无影响 如 直接使用三方支付消费 1 余额支付  2 :转增 出
3:转赠-入 4：分红收益 5：提现
     */
    public Integer getMoneyTypeWallet() {
        return moneyTypeWallet;
    }
    /**
     * 设置：支付类型：0 进账 1 出账
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * 获取：支付类型：0 进账 1 出账
     */
    public Integer getPaymentType() {
        return paymentType;
    }
    /**
     * 设置：新增时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：新增时间
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
}
