package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 platform_withdraw_share_order
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 22:19:20
 */
public class PlatformWithdrawShareOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //序号
    private String orderDateNo;
    //本次奖励总额
    private BigDecimal totalPayMoney;
    //开始统计时间
    private Date shareStartDate;
    //开始统计时间
    private Date shareEndDate;
    //级别分成 系统
    private BigDecimal shareUserDaySys;
    //级别分成一星
    private BigDecimal shareUserDayV1;
    //级别分成二星
    private BigDecimal shareUserDayV2;
    //级别分成三星
    private BigDecimal shareUserDayV3;
    //级别分成四星
    private BigDecimal shareUserDayV4;
    //级别分成五星
    private BigDecimal shareUserDayV5;
    //级别分成一钻
    private BigDecimal shareUserDayV6;
    //级别分成二钻
    private BigDecimal shareUserDayV7;
    //级别分成三钻
    private BigDecimal shareUserDayV8;
    //级别分成四钻
    private BigDecimal shareUserDayV9;
    //级别分成五钻
    private BigDecimal shareUserDayV10;
    //状态，0 OK 1 删除
    private Integer state;
    //添加，不允许更改
    private Date createTime;
    //更新时间
    private Date updateTime;

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
     * 设置：序号
     */
    public void setOrderDateNo(String orderDateNo) {
        this.orderDateNo = orderDateNo;
    }

    /**
     * 获取：序号
     */
    public String getOrderDateNo() {
        return orderDateNo;
    }
    /**
     * 设置：本次奖励总额
     */
    public void setTotalPayMoney(BigDecimal totalPayMoney) {
        this.totalPayMoney = totalPayMoney;
    }

    /**
     * 获取：本次奖励总额
     */
    public BigDecimal getTotalPayMoney() {
        return totalPayMoney;
    }
    /**
     * 设置：开始统计时间
     */
    public void setShareStartDate(Date shareStartDate) {
        this.shareStartDate = shareStartDate;
    }

    /**
     * 获取：开始统计时间
     */
    public Date getShareStartDate() {
        return shareStartDate;
    }
    /**
     * 设置：开始统计时间
     */
    public void setShareEndDate(Date shareEndDate) {
        this.shareEndDate = shareEndDate;
    }

    /**
     * 获取：开始统计时间
     */
    public Date getShareEndDate() {
        return shareEndDate;
    }
    /**
     * 设置：级别分成 系统
     */
    public void setShareUserDaySys(BigDecimal shareUserDaySys) {
        this.shareUserDaySys = shareUserDaySys;
    }

    /**
     * 获取：级别分成 系统
     */
    public BigDecimal getShareUserDaySys() {
        return shareUserDaySys;
    }
    /**
     * 设置：级别分成一星
     */
    public void setShareUserDayV1(BigDecimal shareUserDayV1) {
        this.shareUserDayV1 = shareUserDayV1;
    }

    /**
     * 获取：级别分成一星
     */
    public BigDecimal getShareUserDayV1() {
        return shareUserDayV1;
    }
    /**
     * 设置：级别分成二星
     */
    public void setShareUserDayV2(BigDecimal shareUserDayV2) {
        this.shareUserDayV2 = shareUserDayV2;
    }

    /**
     * 获取：级别分成二星
     */
    public BigDecimal getShareUserDayV2() {
        return shareUserDayV2;
    }
    /**
     * 设置：级别分成三星
     */
    public void setShareUserDayV3(BigDecimal shareUserDayV3) {
        this.shareUserDayV3 = shareUserDayV3;
    }

    /**
     * 获取：级别分成三星
     */
    public BigDecimal getShareUserDayV3() {
        return shareUserDayV3;
    }
    /**
     * 设置：级别分成四星
     */
    public void setShareUserDayV4(BigDecimal shareUserDayV4) {
        this.shareUserDayV4 = shareUserDayV4;
    }

    /**
     * 获取：级别分成四星
     */
    public BigDecimal getShareUserDayV4() {
        return shareUserDayV4;
    }
    /**
     * 设置：级别分成五星
     */
    public void setShareUserDayV5(BigDecimal shareUserDayV5) {
        this.shareUserDayV5 = shareUserDayV5;
    }

    /**
     * 获取：级别分成五星
     */
    public BigDecimal getShareUserDayV5() {
        return shareUserDayV5;
    }
    /**
     * 设置：级别分成一钻
     */
    public void setShareUserDayV6(BigDecimal shareUserDayV6) {
        this.shareUserDayV6 = shareUserDayV6;
    }

    /**
     * 获取：级别分成一钻
     */
    public BigDecimal getShareUserDayV6() {
        return shareUserDayV6;
    }
    /**
     * 设置：级别分成二钻
     */
    public void setShareUserDayV7(BigDecimal shareUserDayV7) {
        this.shareUserDayV7 = shareUserDayV7;
    }

    /**
     * 获取：级别分成二钻
     */
    public BigDecimal getShareUserDayV7() {
        return shareUserDayV7;
    }
    /**
     * 设置：级别分成三钻
     */
    public void setShareUserDayV8(BigDecimal shareUserDayV8) {
        this.shareUserDayV8 = shareUserDayV8;
    }

    /**
     * 获取：级别分成三钻
     */
    public BigDecimal getShareUserDayV8() {
        return shareUserDayV8;
    }
    /**
     * 设置：级别分成四钻
     */
    public void setShareUserDayV9(BigDecimal shareUserDayV9) {
        this.shareUserDayV9 = shareUserDayV9;
    }

    /**
     * 获取：级别分成四钻
     */
    public BigDecimal getShareUserDayV9() {
        return shareUserDayV9;
    }
    /**
     * 设置：级别分成五钻
     */
    public void setShareUserDayV10(BigDecimal shareUserDayV10) {
        this.shareUserDayV10 = shareUserDayV10;
    }

    /**
     * 获取：级别分成五钻
     */
    public BigDecimal getShareUserDayV10() {
        return shareUserDayV10;
    }
    /**
     * 设置：状态，0 OK 1 删除
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取：状态，0 OK 1 删除
     */
    public Integer getState() {
        return state;
    }
    /**
     * 设置：添加，不允许更改
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：添加，不允许更改
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}
