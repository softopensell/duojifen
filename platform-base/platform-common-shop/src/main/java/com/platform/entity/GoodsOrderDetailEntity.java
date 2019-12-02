package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 * 表名 t_goods_order_detail
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-27 23:13:22
 */
public class GoodsOrderDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //订单Id
    private Integer orderId;
    //订单号
    private String orderNo;
    //店铺ID
    private Integer shopId;
    //用户ID
    private Integer userId;
    //商品id
    private Integer goodsId;
    //商品序列号/编号
    private String goodsSn;
    //商品名称 
    private String goodsName;
    //商品封面图片
    private String goodsImgUrl;
    //商品列表图片
    private String goodsListImgUrl;
    //规格
    private String specification;
    //数量
    private Integer goodsCount;
    //销售价格
    private BigDecimal price;
    //小计总价格 
    private BigDecimal totalPrice;
  //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    private String index;
    
    private String author;
    private String goodTags;
    
    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：订单Id
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：订单Id
     */
    public Integer getOrderId() {
        return orderId;
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
     * 设置：店铺ID
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取：店铺ID
     */
    public Integer getShopId() {
        return shopId;
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
     * 设置：商品id
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取：商品id
     */
    public Integer getGoodsId() {
        return goodsId;
    }
    /**
     * 设置：商品序列号/编号
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    /**
     * 获取：商品序列号/编号
     */
    public String getGoodsSn() {
        return goodsSn;
    }
    /**
     * 设置：商品名称 
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 获取：商品名称 
     */
    public String getGoodsName() {
        return goodsName;
    }
    /**
     * 设置：商品封面图片
     */
    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    /**
     * 获取：商品封面图片
     */
    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }
    /**
     * 设置：商品列表图片
     */
    public void setGoodsListImgUrl(String goodsListImgUrl) {
        this.goodsListImgUrl = goodsListImgUrl;
    }

    /**
     * 获取：商品列表图片
     */
    public String getGoodsListImgUrl() {
        return goodsListImgUrl;
    }
    /**
     * 设置：规格
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * 获取：规格
     */
    public String getSpecification() {
        return specification;
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
     * 设置：销售价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取：销售价格
     */
    public BigDecimal getPrice() {
        return price;
    }
    /**
     * 设置：小计总价格 
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取：小计总价格 
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
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
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getGoodTags() {
		return goodTags;
	}

	public void setGoodTags(String goodTags) {
		this.goodTags = goodTags;
	}
    
    
}
