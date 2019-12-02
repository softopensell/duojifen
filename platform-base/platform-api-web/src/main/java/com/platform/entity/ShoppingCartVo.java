package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车实体
 * 表名 wt_shopping_cart
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-24 22:57:11
 */
public class ShoppingCartVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //id 
    private Integer id;
    //商品ID
    private Integer goodsId;
    //用户ID
    private Integer userId;
    //商品数量
    private Integer goodsCount;
    //水桶数量
    private Integer pailCount;
    //选中状态
    private Integer checked;

	

	//创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    
    //商品编码
    private String goodsCode;
    //商品名称
    private String goodsName;
    //规格
    private String standard;
    //商品价格
    private BigDecimal goodsPrice;
    
	private String userLevelCode;
    
    //押桶价格
    private BigDecimal pailPrice;
    
    //商品列表图
    private String listImageUrl;
    
    public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}
    public String getUserLevelCode() {
		return userLevelCode;
	}

	public void setUserLevelCode(String userLevelCode) {
		this.userLevelCode = userLevelCode;
	}

    
  public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public BigDecimal getPailPrice() {
		return pailPrice;
	}

	public void setPailPrice(BigDecimal pailPrice) {
		this.pailPrice = pailPrice;
	}

	public String getListImageUrl() {
		return listImageUrl;
	}

	public void setListImageUrl(String listImageUrl) {
		this.listImageUrl = listImageUrl;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	

    /**
     * 设置：id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：id 
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：商品ID
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取：商品ID
     */
    public Integer getGoodsId() {
        return goodsId;
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
     * 设置：数量
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    /**
     * 获取：数量
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }
    /**
     * 获取：水桶数量
     */
    public Integer getPailCount() {
		return pailCount;
	}
    /**
     * 设置：水桶数量
     */
	public void setPailCount(Integer pailCount) {
		this.pailCount = pailCount;
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
}
