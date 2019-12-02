package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_address
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-13 10:46:39
 */
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //用户id
    private Integer userId;
    //省
    private Integer provinceId;
    //城市
    private Integer cityId;
    //区域
    private Integer regionId;
    //省市详情eg 河北 邢台市
    private String pcrdetail;
    //地址
    private String address;
    //纬度
    private String latitude;
    //经度
    private String longitude;
    //是否默认地址
    private String isDefault;
    //邮编
    private String zipcode;
    //联系人
    private String contactName;
    //联系电话
    private String contactMobile;
    //添加时间
    private Date createTime;
    //修改时间
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
     * 设置：用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户id
     */
    public Integer getUserId() {
        return userId;
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
     * 设置：城市
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取：城市
     */
    public Integer getCityId() {
        return cityId;
    }
    /**
     * 设置：区域
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取：区域
     */
    public Integer getRegionId() {
        return regionId;
    }
    /**
     * 设置：省市详情eg 河北 邢台市
     */
    public void setPcrdetail(String pcrdetail) {
        this.pcrdetail = pcrdetail;
    }

    /**
     * 获取：省市详情eg 河北 邢台市
     */
    public String getPcrdetail() {
        return pcrdetail;
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
     * 设置：纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取：纬度
     */
    public String getLatitude() {
        return latitude;
    }
    /**
     * 设置：经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取：经度
     */
    public String getLongitude() {
        return longitude;
    }
    /**
     * 设置：是否默认地址
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * 获取：是否默认地址
     */
    public String getIsDefault() {
        return isDefault;
    }
    /**
     * 设置：邮编
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * 获取：邮编
     */
    public String getZipcode() {
        return zipcode;
    }
    /**
     * 设置：联系人
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取：联系人
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * 设置：联系电话
     */
    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    /**
     * 获取：联系电话
     */
    public String getContactMobile() {
        return contactMobile;
    }
    /**
     * 设置：添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：添加时间
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
