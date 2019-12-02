package com.platform.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 实体
 * 表名 t_bonus_invest_order
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 01:26:17
 */
public class BonusInvestOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //会员
    private Integer userId;
    //流水号
    private String investOrderNo;
    //消费订单：根据消费订单，产生订单号
    private String consumedOrderNo;
    //消费额度
    private BigDecimal consumedMoney;
    //最大额度
    private BigDecimal maxMoney;
    //购买额度
    private BigDecimal buyMoney;
    //支付类型:0 货到付款 1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
    private Integer payType;
    //支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
    private Integer payStatus;
    //状态 0 有效 1 下线
    private Integer statu;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //总收益
    private BigDecimal totalIncomMoney;
    //已经收益
    private BigDecimal incomeMoney;
    //剩余收益
    private BigDecimal surplusMoney;
    //最后收益时间 ---凌晨
    private Date shareLastTime;
    
    private String userName;
    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：会员
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：会员
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：流水号
     */
    public void setInvestOrderNo(String investOrderNo) {
        this.investOrderNo = investOrderNo;
    }

    /**
     * 获取：流水号
     */
    public String getInvestOrderNo() {
        return investOrderNo;
    }
    /**
     * 设置：消费订单：根据消费订单，产生订单号
     */
    public void setConsumedOrderNo(String consumedOrderNo) {
        this.consumedOrderNo = consumedOrderNo;
    }

    /**
     * 获取：消费订单：根据消费订单，产生订单号
     */
    public String getConsumedOrderNo() {
        return consumedOrderNo;
    }
    /**
     * 设置：消费额度
     */
    public void setConsumedMoney(BigDecimal consumedMoney) {
        this.consumedMoney = consumedMoney;
    }

    /**
     * 获取：消费额度
     */
    public BigDecimal getConsumedMoney() {
        return consumedMoney;
    }
    /**
     * 设置：最大额度
     */
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }

    /**
     * 获取：最大额度
     */
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    /**
     * 设置：购买额度
     */
    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    /**
     * 获取：购买额度
     */
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }
    /**
     * 设置：支付类型:0 货到付款 1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 获取：支付类型:0 货到付款 1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
     */
    public Integer getPayType() {
        return payType;
    }
    /**
     * 设置：支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取：支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
     */
    public Integer getPayStatus() {
        return payStatus;
    }
    /**
     * 设置：状态 0 有效 1 删除
     */
    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    /**
     * 获取：状态 0 有效 1 删除
     */
    public Integer getStatu() {
        return statu;
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
    /**
     * 设置：总收益
     */
    public void setTotalIncomMoney(BigDecimal totalIncomMoney) {
        this.totalIncomMoney = totalIncomMoney;
    }

    /**
     * 获取：总收益
     */
    public BigDecimal getTotalIncomMoney() {
        return totalIncomMoney;
    }
    /**
     * 设置：已经收益
     */
    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    /**
     * 获取：已经收益
     */
    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }
    /**
     * 设置：剩余收益
     */
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    /**
     * 获取：剩余收益
     */
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    /**
     * 设置：最后收益时间 ---凌晨
     */
    public void setShareLastTime(Date shareLastTime) {
        this.shareLastTime = shareLastTime;
    }

    /**
     * 获取：最后收益时间 ---凌晨
     */
    public Date getShareLastTime() {
        return shareLastTime;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
