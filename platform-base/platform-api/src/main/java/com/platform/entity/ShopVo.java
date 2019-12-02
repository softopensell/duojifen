package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_shop
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:50
 */
public class ShopVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //ID
    private Integer id;
    //商家类别 1、个人 2、企业
    private String shopType;
    //商店名称
    private String shopName;
    //商店LOGO
    private String shopLogo;
    //商店描述
    private String shopDesc;
    //联系人
    private String shopContactName;
    //联系方式
    private String shopContactPhone;
    //省
    private Integer provinceId;
    //市
    private Integer cityId;
    //区
    private Integer regionId;
    //地址
    private String address;
    //注意备注
    private String remark;
    //0正常1停运
    private Integer statu;
    //创建用户id
    private Integer createUserId;
    //审核状态0待审核1审核通过2审核失败
    private Integer auditStatu;
    //商品数量
    private Integer goodsSum;
    //订单数量
    private Integer orderSum;
    //公司编码
    private String companySn;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

    /**
     * 设置：ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：ID
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：商家类别 1、个人 2、企业
     */
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    /**
     * 获取：商家类别 1、个人 2、企业
     */
    public String getShopType() {
        return shopType;
    }
    /**
     * 设置：商店名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 获取：商店名称
     */
    public String getShopName() {
        return shopName;
    }
    /**
     * 设置：商店LOGO
     */
    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    /**
     * 获取：商店LOGO
     */
    public String getShopLogo() {
        return shopLogo;
    }
    /**
     * 设置：商店描述
     */
    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    /**
     * 获取：商店描述
     */
    public String getShopDesc() {
        return shopDesc;
    }
    /**
     * 设置：联系人
     */
    public void setShopContactName(String shopContactName) {
        this.shopContactName = shopContactName;
    }

    /**
     * 获取：联系人
     */
    public String getShopContactName() {
        return shopContactName;
    }
    /**
     * 设置：联系方式
     */
    public void setShopContactPhone(String shopContactPhone) {
        this.shopContactPhone = shopContactPhone;
    }

    /**
     * 获取：联系方式
     */
    public String getShopContactPhone() {
        return shopContactPhone;
    }
    /**
     * 设置：省
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取：省
     */
    public Integer getProvinceId() {
        return provinceId;
    }
    /**
     * 设置：市
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取：市
     */
    public Integer getCityId() {
        return cityId;
    }
    /**
     * 设置：区
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取：区
     */
    public Integer getRegionId() {
        return regionId;
    }
    /**
     * 设置：地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：地址
     */
    public String getAddress() {
        return address;
    }
    /**
     * 设置：注意备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：注意备注
     */
    public String getRemark() {
        return remark;
    }
    /**
     * 设置：0正常1停运
     */
    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    /**
     * 获取：0正常1停运
     */
    public Integer getStatu() {
        return statu;
    }
    /**
     * 设置：创建用户id
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取：创建用户id
     */
    public Integer getCreateUserId() {
        return createUserId;
    }
    /**
     * 设置：审核状态0待审核1审核通过2审核失败
     */
    public void setAuditStatu(Integer auditStatu) {
        this.auditStatu = auditStatu;
    }

    /**
     * 获取：审核状态0待审核1审核通过2审核失败
     */
    public Integer getAuditStatu() {
        return auditStatu;
    }
    /**
     * 设置：商品数量
     */
    public void setGoodsSum(Integer goodsSum) {
        this.goodsSum = goodsSum;
    }

    /**
     * 获取：商品数量
     */
    public Integer getGoodsSum() {
        return goodsSum;
    }
    /**
     * 设置：订单数量
     */
    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    /**
     * 获取：订单数量
     */
    public Integer getOrderSum() {
        return orderSum;
    }
    /**
     * 设置：公司编码
     */
    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    /**
     * 获取：公司编码
     */
    public String getCompanySn() {
        return companySn;
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
