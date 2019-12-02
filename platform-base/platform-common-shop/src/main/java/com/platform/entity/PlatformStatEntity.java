package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 platform_stat
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-06 03:34:27
 */
public class PlatformStatEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //中间编号，日期，序号
    private String statDateNumber;
    //统计类型
    private Long statDateType;
    //用户
    private Long statMemberId;
    //会员总数
    private Long statUserTotalSum;
    //未激活总数
    private Long statUserV0Sum;
    //V1总数
    private Long statUserV1Sum;
    //V2总数
    private Long statUserV2Sum;
    //V3总数
    private Long statUserV3Sum;
    //V4总数
    private Long statUserV4Sum;
    //新增总数
    private Long statUserDayAddSum;
    //新增未激活总数
    private Long statUserDayAddV0Sum;
    //新增V1总数
    private Long statUserDayAddV1Sum;
    //新增V2总数
    private Long statUserDayAddV2Sum;
    //新增V3总数
    private Long statUserDayAddV3Sum;
    //新增V4总数
    private Long statUserDayAddV4Sum;
    
    //用户总余额
    private BigDecimal statUserTotalZc;
    //用户总充值
    private BigDecimal statUserTotalCz;
    //用户积分
    private BigDecimal statUserTotalJf;
    //用户总剩余资产
    private BigDecimal statUserTotalSyZc;
    //用户基金
    private BigDecimal statUserTotalFund;
    //今日分成比例
    private BigDecimal statDayRate;
    //今日余额支付数量
    private Long statDayPayBalanceSum;
    //今日余额支付总额
    private BigDecimal statDayPayBalance;
    //今日积分支付数量
    private Long statDayPayJfSum;
    //今日积分支付总额
    private BigDecimal statDayPayJf;
    //今日充值数量
    private Long statDayMoneyRechargeSum;
    //今日充值金额
    private BigDecimal statDayMoneyRecharge;
    //今日余额转入数量
    private Long statDayBalanceZzInSum;
    //今日余额转入总额
    private BigDecimal statDayBalanceZzIn;
    //今日余额转出数量
    private Long statDayBalanceZzOutSum;
    //今日余额转出总额
    private BigDecimal statDayBalanceZzOut;
    //今日提现数量
    private Long statDayMoneyTxSum;
    //今日提现金额
    private BigDecimal statDayMoneyTx;
    //今日转账数量
    private Long statDayJfZzSum;
    //今日转账金额
    private BigDecimal statDayJfZz;
    //积分兑换数量
    private Long statDayJfDhSum;
    //积分兑换金额
    private BigDecimal statDayJfDh;
    //今日权益数量
    private Long statDayMoneyQySum;
    //今日权益金额
    private BigDecimal statDayMoneyQy;
    //今日服务数量
    private Long statDayMoneyFwSum;
    //今日服务金额
    private BigDecimal statDayMoneyFw;
    //今日星星数量
    private Long statDayMoneyXxSum;
    //今日星星金额
    private BigDecimal statDayMoneyXx;
    //今日社区数量
    private Long statDayMoneySqSum;
    //今日社区金额
    private BigDecimal statDayMoneySq;
    //今日基金数量
    private Long statDayMoneyFundSum;
    //今日基金金额
    private BigDecimal statDayMoneyFund;
    //统计时间
    private Date statDate;
    //添加，不允许更改
    private Date createTime;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：中间编号，日期，序号
     */
    public void setStatDateNumber(String statDateNumber) {
        this.statDateNumber = statDateNumber;
    }

    /**
     * 获取：中间编号，日期，序号
     */
    public String getStatDateNumber() {
        return statDateNumber;
    }
    /**
     * 设置：统计类型
     */
    public void setStatDateType(Long statDateType) {
        this.statDateType = statDateType;
    }

    /**
     * 获取：统计类型
     */
    public Long getStatDateType() {
        return statDateType;
    }
    /**
     * 设置：用户
     */
    public void setStatMemberId(Long statMemberId) {
        this.statMemberId = statMemberId;
    }

    /**
     * 获取：用户
     */
    public Long getStatMemberId() {
        return statMemberId;
    }
    /**
     * 设置：会员总数
     */
    public void setStatUserTotalSum(Long statUserTotalSum) {
        this.statUserTotalSum = statUserTotalSum;
    }

    /**
     * 获取：会员总数
     */
    public Long getStatUserTotalSum() {
        return statUserTotalSum;
    }
    /**
     * 设置：未激活总数
     */
    public void setStatUserV0Sum(Long statUserV0Sum) {
        this.statUserV0Sum = statUserV0Sum;
    }

    /**
     * 获取：未激活总数
     */
    public Long getStatUserV0Sum() {
        return statUserV0Sum;
    }
    /**
     * 设置：V1总数
     */
    public void setStatUserV1Sum(Long statUserV1Sum) {
        this.statUserV1Sum = statUserV1Sum;
    }

    /**
     * 获取：V1总数
     */
    public Long getStatUserV1Sum() {
        return statUserV1Sum;
    }
    /**
     * 设置：V2总数
     */
    public void setStatUserV2Sum(Long statUserV2Sum) {
        this.statUserV2Sum = statUserV2Sum;
    }

    /**
     * 获取：V2总数
     */
    public Long getStatUserV2Sum() {
        return statUserV2Sum;
    }
    /**
     * 设置：V3总数
     */
    public void setStatUserV3Sum(Long statUserV3Sum) {
        this.statUserV3Sum = statUserV3Sum;
    }

    /**
     * 获取：V3总数
     */
    public Long getStatUserV3Sum() {
        return statUserV3Sum;
    }
    /**
     * 设置：V4总数
     */
    public void setStatUserV4Sum(Long statUserV4Sum) {
        this.statUserV4Sum = statUserV4Sum;
    }

    /**
     * 获取：V4总数
     */
    public Long getStatUserV4Sum() {
        return statUserV4Sum;
    }
    /**
     * 设置：新增总数
     */
    public void setStatUserDayAddSum(Long statUserDayAddSum) {
        this.statUserDayAddSum = statUserDayAddSum;
    }

    /**
     * 获取：新增总数
     */
    public Long getStatUserDayAddSum() {
        return statUserDayAddSum;
    }
    /**
     * 设置：新增未激活总数
     */
    public void setStatUserDayAddV0Sum(Long statUserDayAddV0Sum) {
        this.statUserDayAddV0Sum = statUserDayAddV0Sum;
    }

    /**
     * 获取：新增未激活总数
     */
    public Long getStatUserDayAddV0Sum() {
        return statUserDayAddV0Sum;
    }
    /**
     * 设置：新增V1总数
     */
    public void setStatUserDayAddV1Sum(Long statUserDayAddV1Sum) {
        this.statUserDayAddV1Sum = statUserDayAddV1Sum;
    }

    /**
     * 获取：新增V1总数
     */
    public Long getStatUserDayAddV1Sum() {
        return statUserDayAddV1Sum;
    }
    /**
     * 设置：新增V2总数
     */
    public void setStatUserDayAddV2Sum(Long statUserDayAddV2Sum) {
        this.statUserDayAddV2Sum = statUserDayAddV2Sum;
    }

    /**
     * 获取：新增V2总数
     */
    public Long getStatUserDayAddV2Sum() {
        return statUserDayAddV2Sum;
    }
    /**
     * 设置：新增V3总数
     */
    public void setStatUserDayAddV3Sum(Long statUserDayAddV3Sum) {
        this.statUserDayAddV3Sum = statUserDayAddV3Sum;
    }

    /**
     * 获取：新增V3总数
     */
    public Long getStatUserDayAddV3Sum() {
        return statUserDayAddV3Sum;
    }
    /**
     * 设置：新增V4总数
     */
    public void setStatUserDayAddV4Sum(Long statUserDayAddV4Sum) {
        this.statUserDayAddV4Sum = statUserDayAddV4Sum;
    }

    /**
     * 获取：新增V4总数
     */
    public Long getStatUserDayAddV4Sum() {
        return statUserDayAddV4Sum;
    }
    /**
     * 设置：用户总资产
     */
    public void setStatUserTotalZc(BigDecimal statUserTotalZc) {
        this.statUserTotalZc = statUserTotalZc;
    }

    /**
     * 获取：用户总资产
     */
    public BigDecimal getStatUserTotalZc() {
        return statUserTotalZc;
    }
    /**
     * 设置：用户积分
     */
    public void setStatUserTotalJf(BigDecimal statUserTotalJf) {
        this.statUserTotalJf = statUserTotalJf;
    }

    /**
     * 获取：用户积分
     */
    public BigDecimal getStatUserTotalJf() {
        return statUserTotalJf;
    }
    /**
     * 设置：用户总剩余资产
     */
    public void setStatUserTotalSyZc(BigDecimal statUserTotalSyZc) {
        this.statUserTotalSyZc = statUserTotalSyZc;
    }

    /**
     * 获取：用户总剩余资产
     */
    public BigDecimal getStatUserTotalSyZc() {
        return statUserTotalSyZc;
    }
    /**
     * 设置：用户基金
     */
    public void setStatUserTotalFund(BigDecimal statUserTotalFund) {
        this.statUserTotalFund = statUserTotalFund;
    }

    /**
     * 获取：用户基金
     */
    public BigDecimal getStatUserTotalFund() {
        return statUserTotalFund;
    }
    /**
     * 设置：今日分成比例
     */
    public void setStatDayRate(BigDecimal statDayRate) {
        this.statDayRate = statDayRate;
    }

    /**
     * 获取：今日分成比例
     */
    public BigDecimal getStatDayRate() {
        return statDayRate;
    }
    /**
     * 设置：今日余额支付数量
     */
    public void setStatDayPayBalanceSum(Long statDayPayBalanceSum) {
        this.statDayPayBalanceSum = statDayPayBalanceSum;
    }

    /**
     * 获取：今日余额支付数量
     */
    public Long getStatDayPayBalanceSum() {
        return statDayPayBalanceSum;
    }
    /**
     * 设置：今日余额支付总额
     */
    public void setStatDayPayBalance(BigDecimal statDayPayBalance) {
        this.statDayPayBalance = statDayPayBalance;
    }

    /**
     * 获取：今日余额支付总额
     */
    public BigDecimal getStatDayPayBalance() {
        return statDayPayBalance;
    }
    /**
     * 设置：今日积分支付数量
     */
    public void setStatDayPayJfSum(Long statDayPayJfSum) {
        this.statDayPayJfSum = statDayPayJfSum;
    }

    /**
     * 获取：今日积分支付数量
     */
    public Long getStatDayPayJfSum() {
        return statDayPayJfSum;
    }
    /**
     * 设置：今日积分支付总额
     */
    public void setStatDayPayJf(BigDecimal statDayPayJf) {
        this.statDayPayJf = statDayPayJf;
    }

    /**
     * 获取：今日积分支付总额
     */
    public BigDecimal getStatDayPayJf() {
        return statDayPayJf;
    }
    /**
     * 设置：今日充值数量
     */
    public void setStatDayMoneyRechargeSum(Long statDayMoneyRechargeSum) {
        this.statDayMoneyRechargeSum = statDayMoneyRechargeSum;
    }

    /**
     * 获取：今日充值数量
     */
    public Long getStatDayMoneyRechargeSum() {
        return statDayMoneyRechargeSum;
    }
    /**
     * 设置：今日充值金额
     */
    public void setStatDayMoneyRecharge(BigDecimal statDayMoneyRecharge) {
        this.statDayMoneyRecharge = statDayMoneyRecharge;
    }

    /**
     * 获取：今日充值金额
     */
    public BigDecimal getStatDayMoneyRecharge() {
        return statDayMoneyRecharge;
    }
    /**
     * 设置：今日余额转入数量
     */
    public void setStatDayBalanceZzInSum(Long statDayBalanceZzInSum) {
        this.statDayBalanceZzInSum = statDayBalanceZzInSum;
    }

    /**
     * 获取：今日余额转入数量
     */
    public Long getStatDayBalanceZzInSum() {
        return statDayBalanceZzInSum;
    }
    /**
     * 设置：今日余额转入总额
     */
    public void setStatDayBalanceZzIn(BigDecimal statDayBalanceZzIn) {
        this.statDayBalanceZzIn = statDayBalanceZzIn;
    }

    /**
     * 获取：今日余额转入总额
     */
    public BigDecimal getStatDayBalanceZzIn() {
        return statDayBalanceZzIn;
    }
    /**
     * 设置：今日余额转出数量
     */
    public void setStatDayBalanceZzOutSum(Long statDayBalanceZzOutSum) {
        this.statDayBalanceZzOutSum = statDayBalanceZzOutSum;
    }

    /**
     * 获取：今日余额转出数量
     */
    public Long getStatDayBalanceZzOutSum() {
        return statDayBalanceZzOutSum;
    }
    /**
     * 设置：今日余额转出总额
     */
    public void setStatDayBalanceZzOut(BigDecimal statDayBalanceZzOut) {
        this.statDayBalanceZzOut = statDayBalanceZzOut;
    }

    /**
     * 获取：今日余额转出总额
     */
    public BigDecimal getStatDayBalanceZzOut() {
        return statDayBalanceZzOut;
    }
    /**
     * 设置：今日提现数量
     */
    public void setStatDayMoneyTxSum(Long statDayMoneyTxSum) {
        this.statDayMoneyTxSum = statDayMoneyTxSum;
    }

    /**
     * 获取：今日提现数量
     */
    public Long getStatDayMoneyTxSum() {
        return statDayMoneyTxSum;
    }
    /**
     * 设置：今日提现金额
     */
    public void setStatDayMoneyTx(BigDecimal statDayMoneyTx) {
        this.statDayMoneyTx = statDayMoneyTx;
    }

    /**
     * 获取：今日提现金额
     */
    public BigDecimal getStatDayMoneyTx() {
        return statDayMoneyTx;
    }
    /**
     * 设置：今日转账数量
     */
    public void setStatDayJfZzSum(Long statDayJfZzSum) {
        this.statDayJfZzSum = statDayJfZzSum;
    }

    /**
     * 获取：今日转账数量
     */
    public Long getStatDayJfZzSum() {
        return statDayJfZzSum;
    }
    /**
     * 设置：今日转账金额
     */
    public void setStatDayJfZz(BigDecimal statDayJfZz) {
        this.statDayJfZz = statDayJfZz;
    }

    /**
     * 获取：今日转账金额
     */
    public BigDecimal getStatDayJfZz() {
        return statDayJfZz;
    }
    /**
     * 设置：积分兑换数量
     */
    public void setStatDayJfDhSum(Long statDayJfDhSum) {
        this.statDayJfDhSum = statDayJfDhSum;
    }

    /**
     * 获取：积分兑换数量
     */
    public Long getStatDayJfDhSum() {
        return statDayJfDhSum;
    }
    /**
     * 设置：积分兑换金额
     */
    public void setStatDayJfDh(BigDecimal statDayJfDh) {
        this.statDayJfDh = statDayJfDh;
    }

    /**
     * 获取：积分兑换金额
     */
    public BigDecimal getStatDayJfDh() {
        return statDayJfDh;
    }
    /**
     * 设置：今日权益数量
     */
    public void setStatDayMoneyQySum(Long statDayMoneyQySum) {
        this.statDayMoneyQySum = statDayMoneyQySum;
    }

    /**
     * 获取：今日权益数量
     */
    public Long getStatDayMoneyQySum() {
        return statDayMoneyQySum;
    }
    /**
     * 设置：今日权益金额
     */
    public void setStatDayMoneyQy(BigDecimal statDayMoneyQy) {
        this.statDayMoneyQy = statDayMoneyQy;
    }

    /**
     * 获取：今日权益金额
     */
    public BigDecimal getStatDayMoneyQy() {
        return statDayMoneyQy;
    }
    /**
     * 设置：今日服务数量
     */
    public void setStatDayMoneyFwSum(Long statDayMoneyFwSum) {
        this.statDayMoneyFwSum = statDayMoneyFwSum;
    }

    /**
     * 获取：今日服务数量
     */
    public Long getStatDayMoneyFwSum() {
        return statDayMoneyFwSum;
    }
    /**
     * 设置：今日服务金额
     */
    public void setStatDayMoneyFw(BigDecimal statDayMoneyFw) {
        this.statDayMoneyFw = statDayMoneyFw;
    }

    /**
     * 获取：今日服务金额
     */
    public BigDecimal getStatDayMoneyFw() {
        return statDayMoneyFw;
    }
    /**
     * 设置：今日星星数量
     */
    public void setStatDayMoneyXxSum(Long statDayMoneyXxSum) {
        this.statDayMoneyXxSum = statDayMoneyXxSum;
    }

    /**
     * 获取：今日星星数量
     */
    public Long getStatDayMoneyXxSum() {
        return statDayMoneyXxSum;
    }
    /**
     * 设置：今日星星金额
     */
    public void setStatDayMoneyXx(BigDecimal statDayMoneyXx) {
        this.statDayMoneyXx = statDayMoneyXx;
    }

    /**
     * 获取：今日星星金额
     */
    public BigDecimal getStatDayMoneyXx() {
        return statDayMoneyXx;
    }
    /**
     * 设置：今日社区数量
     */
    public void setStatDayMoneySqSum(Long statDayMoneySqSum) {
        this.statDayMoneySqSum = statDayMoneySqSum;
    }

    /**
     * 获取：今日社区数量
     */
    public Long getStatDayMoneySqSum() {
        return statDayMoneySqSum;
    }
    /**
     * 设置：今日社区金额
     */
    public void setStatDayMoneySq(BigDecimal statDayMoneySq) {
        this.statDayMoneySq = statDayMoneySq;
    }

    /**
     * 获取：今日社区金额
     */
    public BigDecimal getStatDayMoneySq() {
        return statDayMoneySq;
    }
    /**
     * 设置：今日基金数量
     */
    public void setStatDayMoneyFundSum(Long statDayMoneyFundSum) {
        this.statDayMoneyFundSum = statDayMoneyFundSum;
    }

    /**
     * 获取：今日基金数量
     */
    public Long getStatDayMoneyFundSum() {
        return statDayMoneyFundSum;
    }
    /**
     * 设置：今日基金金额
     */
    public void setStatDayMoneyFund(BigDecimal statDayMoneyFund) {
        this.statDayMoneyFund = statDayMoneyFund;
    }

    /**
     * 获取：今日基金金额
     */
    public BigDecimal getStatDayMoneyFund() {
        return statDayMoneyFund;
    }
    /**
     * 设置：统计时间
     */
    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    /**
     * 获取：统计时间
     */
    public Date getStatDate() {
        return statDate;
    }
    /**
     * 设置：添加，不允许更改
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getStatUserTotalCz() {
		return statUserTotalCz;
	}

	public void setStatUserTotalCz(BigDecimal statUserTotalCz) {
		this.statUserTotalCz = statUserTotalCz;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
     * 获取：添加，不允许更改
     */
    public Date getCreateTime() {
        return createTime;
    }
}
