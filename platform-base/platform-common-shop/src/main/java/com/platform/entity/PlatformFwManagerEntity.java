package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 platform_fw_manager
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
public class PlatformFwManagerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //服务中心名称
    private String fwName;
    //服务中心
    private Integer fwUserId;
    private String fwUserName;
    //服务中心当前业绩
    private BigDecimal fwCurYj;
    //开始统计时间
    private Date fwCurDate;
    //重置次数
    private Integer fwTotalResetTime;
    //累计业绩
    private BigDecimal fwTotalYj;
    //累计奖励
    private BigDecimal fwTotalPayMoney;
    //状态，0 OK 1 删除
    private Integer state;
    //添加，不允许更改
    private Date createTime;
    //添加，不允许更改
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
     * 设置：服务中心名称
     */
    public void setFwName(String fwName) {
        this.fwName = fwName;
    }

    /**
     * 获取：服务中心名称
     */
    public String getFwName() {
        return fwName;
    }
    /**
     * 设置：服务中心
     */
    public void setFwUserId(Integer fwUserId) {
        this.fwUserId = fwUserId;
    }

    /**
     * 获取：服务中心
     */
    public Integer getFwUserId() {
        return fwUserId;
    }
    /**
     * 设置：服务中心当前业绩
     */
    public void setFwCurYj(BigDecimal fwCurYj) {
        this.fwCurYj = fwCurYj;
    }

    /**
     * 获取：服务中心当前业绩
     */
    public BigDecimal getFwCurYj() {
        return fwCurYj;
    }
    /**
     * 设置：开始统计时间
     */
    public void setFwCurDate(Date fwCurDate) {
        this.fwCurDate = fwCurDate;
    }

    /**
     * 获取：开始统计时间
     */
    public Date getFwCurDate() {
        return fwCurDate;
    }
    /**
     * 设置：重置次数
     */
    public void setFwTotalResetTime(Integer fwTotalResetTime) {
        this.fwTotalResetTime = fwTotalResetTime;
    }

    /**
     * 获取：重置次数
     */
    public Integer getFwTotalResetTime() {
        return fwTotalResetTime;
    }
    /**
     * 设置：累计业绩
     */
    public void setFwTotalYj(BigDecimal fwTotalYj) {
        this.fwTotalYj = fwTotalYj;
    }

    /**
     * 获取：累计业绩
     */
    public BigDecimal getFwTotalYj() {
        return fwTotalYj;
    }
    /**
     * 设置：累计奖励
     */
    public void setFwTotalPayMoney(BigDecimal fwTotalPayMoney) {
        this.fwTotalPayMoney = fwTotalPayMoney;
    }

    /**
     * 获取：累计奖励
     */
    public BigDecimal getFwTotalPayMoney() {
        return fwTotalPayMoney;
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
     * 设置：添加，不允许更改
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：添加，不允许更改
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	public String getFwUserName() {
		return fwUserName;
	}

	public void setFwUserName(String fwUserName) {
		this.fwUserName = fwUserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
