package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 t_user_invest_level
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-15 03:15:07
 */
public class UserInvestLevelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //会员级别Type
    private Integer userLevelType;
    //名称
    private String userLevelName;
    //消费额度
    private BigDecimal userLevelMoneyValue;
    //10,11,12,13,14
    private Integer userLevelNodeLevel;
    //状态 0 有效 1 删除
    private Integer statu;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //绑定商品ID
    private String goodsSn;
    
  //最小消费额度
    private BigDecimal userLevelConsumedMin;
    
  //最大消费额度
    private BigDecimal userLevelConsumedMax;
    private Integer userLevelTime;

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
     * 设置：会员级别Type
     */
    public void setUserLevelType(Integer userLevelType) {
        this.userLevelType = userLevelType;
    }

    /**
     * 获取：会员级别Type
     */
    public Integer getUserLevelType() {
        return userLevelType;
    }
    /**
     * 设置：名称
     */
    public void setUserLevelName(String userLevelName) {
        this.userLevelName = userLevelName;
    }

    /**
     * 获取：名称
     */
    public String getUserLevelName() {
        return userLevelName;
    }
    /**
     * 设置：消费额度
     */
    public void setUserLevelMoneyValue(BigDecimal userLevelMoneyValue) {
        this.userLevelMoneyValue = userLevelMoneyValue;
    }

    /**
     * 获取：消费额度
     */
    public BigDecimal getUserLevelMoneyValue() {
        return userLevelMoneyValue;
    }
    /**
     * 设置：10,11,12,13,14
     */
    public void setUserLevelNodeLevel(Integer userLevelNodeLevel) {
        this.userLevelNodeLevel = userLevelNodeLevel;
    }

    /**
     * 获取：10,11,12,13,14
     */
    public Integer getUserLevelNodeLevel() {
        return userLevelNodeLevel;
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
     * 设置：绑定商品ID
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    /**
     * 获取：绑定商品ID
     */
    public String getGoodsSn() {
        return goodsSn;
    }

	public BigDecimal getUserLevelConsumedMin() {
		return userLevelConsumedMin;
	}

	public void setUserLevelConsumedMin(BigDecimal userLevelConsumedMin) {
		this.userLevelConsumedMin = userLevelConsumedMin;
	}

	public BigDecimal getUserLevelConsumedMax() {
		return userLevelConsumedMax;
	}

	public void setUserLevelConsumedMax(BigDecimal userLevelConsumedMax) {
		this.userLevelConsumedMax = userLevelConsumedMax;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getUserLevelTime() {
		return userLevelTime;
	}

	public void setUserLevelTime(Integer userLevelTime) {
		this.userLevelTime = userLevelTime;
	}
}
