package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 t_goods_cart
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-13 10:44:41
 */
public class GoodsCartEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Integer id;
    //店铺ID
    private Integer shopId;
    //用户名称
    private Integer userId;
    //商品id
    private Integer goodsId;
    //商品名称 
    private String goodsName;
    //商品封面图片
    private String goodsImgUrl;
    //商品列表图片
    private String goodsListImgUrl;
    //商品单价
    private BigDecimal goodsPrice;
    //goods表中的零售价格
    private BigDecimal retailGoodsPrice;
    //规格
    private String specification;
    //小计总价格 
    private BigDecimal totalPrice;
    //数量
    private Integer goodsCount;
    //选中状态: 0 未选中 1 选中
    private Integer checked;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

    /**
     * 设置：主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键ID
     */
    public Integer getId() {
        return id;
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
     * 设置：用户名称
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户名称
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
     * 设置：商品单价
     */
    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    /**
     * 获取：商品单价
     */
    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }
    /**
     * 设置：goods表中的零售价格
     */
    public void setRetailGoodsPrice(BigDecimal retailGoodsPrice) {
        this.retailGoodsPrice = retailGoodsPrice;
    }

    /**
     * 获取：goods表中的零售价格
     */
    public BigDecimal getRetailGoodsPrice() {
        return retailGoodsPrice;
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
     * 设置：选中状态: 0 未选中 1 选中
     */
    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    /**
     * 获取：选中状态: 0 未选中 1 选中
     */
    public Integer getChecked() {
        return checked;
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
