package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 platform_withdraw_share
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
public class PlatformWithdrawShareEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //会员
    private Integer userId;
    private String userName;
    //类别:1 是 共识机制 0 共识股东
    private Integer withdrawType;
    //星级类型
    private Integer withdrawTypeStar;
    //状态，0 OK 1 删除
    private Integer state;
    //添加，不允许更改
    private Date createTime;
    //添加，更改
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
     * 设置：类别:1 是 共识机制 0 共识股东
     */
    public void setWithdrawType(Integer withdrawType) {
        this.withdrawType = withdrawType;
    }

    /**
     * 获取：类别:1 是 共识机制 0 共识股东
     */
    public Integer getWithdrawType() {
        return withdrawType;
    }
    /**
     * 设置：星级类型
     */
    public void setWithdrawTypeStar(Integer withdrawTypeStar) {
        this.withdrawTypeStar = withdrawTypeStar;
    }

    /**
     * 获取：星级类型
     */
    public Integer getWithdrawTypeStar() {
        return withdrawTypeStar;
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
     * 设置：添加，更改
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：添加，更改
     */
    public Date getUpdateTime() {
        return updateTime;
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
