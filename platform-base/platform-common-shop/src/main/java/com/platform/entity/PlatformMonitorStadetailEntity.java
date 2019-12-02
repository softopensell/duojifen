package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 platform_monitor_stat_detail
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 20:50:37
 */
public class PlatformMonitorStadetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //中间编号，日期，序号
    private String monitorDateNumber;
    //用户
    private Integer monitorMemberId;
    private String monitorMemberName;
    //监控类型
    private Integer monitorType;
    //异常说明
    private String monitorContent;
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
     * 设置：用户
     */
    public void setMonitorMemberId(Integer monitorMemberId) {
        this.monitorMemberId = monitorMemberId;
    }

    /**
     * 获取：用户
     */
    public Integer getMonitorMemberId() {
        return monitorMemberId;
    }
    /**
     * 设置：监控类型
     */
    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    /**
     * 获取：监控类型
     */
    public Integer getMonitorType() {
        return monitorType;
    }
    /**
     * 设置：异常说明
     */
    public void setMonitorContent(String monitorContent) {
        this.monitorContent = monitorContent;
    }

    /**
     * 获取：异常说明
     */
    public String getMonitorContent() {
        return monitorContent;
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

	public String getMonitorMemberName() {
		return monitorMemberName;
	}

	public void setMonitorMemberName(String monitorMemberName) {
		this.monitorMemberName = monitorMemberName;
	}
}
