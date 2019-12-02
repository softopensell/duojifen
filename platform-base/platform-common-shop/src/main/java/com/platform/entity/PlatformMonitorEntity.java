package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 platform_monitor
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 20:50:37
 */
public class PlatformMonitorEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //中间编号，日期，序号
    private String monitorDateNumber;
    //统计类型
    private Integer monitorType;
    //异常会员数量：余额 和统计余额对应不上
    private Integer monitorUserAbnormalSum;
    //统计时间
    private Date monitorDate;
    //添加，不允许更改
    private Date createTime;

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
     * 设置：中间编号，日期，序号
     */
    public void setMonitorDateNumber(String monitorDateNumber) {
        this.monitorDateNumber = monitorDateNumber;
    }

    /**
     * 获取：中间编号，日期，序号
     */
    public String getMonitorDateNumber() {
        return monitorDateNumber;
    }
    /**
     * 设置：统计类型
     */
    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    /**
     * 获取：统计类型
     */
    public Integer getMonitorType() {
        return monitorType;
    }
    /**
     * 设置：异常会员数量：余额 和统计余额对应不上
     */
    public void setMonitorUserAbnormalSum(Integer monitorUserAbnormalSum) {
        this.monitorUserAbnormalSum = monitorUserAbnormalSum;
    }

    /**
     * 获取：异常会员数量：余额 和统计余额对应不上
     */
    public Integer getMonitorUserAbnormalSum() {
        return monitorUserAbnormalSum;
    }
    /**
     * 设置：统计时间
     */
    public void setMonitorDate(Date monitorDate) {
        this.monitorDate = monitorDate;
    }

    /**
     * 获取：统计时间
     */
    public Date getMonitorDate() {
        return monitorDate;
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
}
