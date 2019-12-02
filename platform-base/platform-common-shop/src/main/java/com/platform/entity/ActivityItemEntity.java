package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 activity_item
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public class ActivityItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //活动号
    private String itemNo;
    //用户id
    private Integer memberId;
    //标题
    private String title;
    //演出节目
    private String itemList;
    //活动描述
    private String description;
    //状态 0 有效 1 删除
    private Integer statu;
    //省份
    private Integer provinceId;
    //城市
    private Integer cityId;
    //
    private Integer regionId;
    //
    private String address;
    //
    private Date createTime;
    //
    private Date updateTime;
    //主办方
    private String organizerName;
    //
    private String organizerLinkName;
    //
    private String organizerLinkPhone;
    //
    private Date startTime;
    //
    private Date endTime;
    //点击次数
    private Integer clickSum;
    //分类
    private Integer itemKind;
    //轮番图片
    private String itemLogo;
    //
    private Integer jobsSum;
    private Integer ticketSum;
    //类型
    private Integer itemType;
    //
    private Integer applyPeopleSum;
    //
    private Integer productSum;
    //
    private Integer virtualProductSum;
    //
    private Integer virtualApplyPeopleSum;
    //
    private Date applyEndTime;
    //
    private Integer applyStatu;
    //
    private String applyIntro;
    //
    private Integer productPrice;
    //
    private Integer itemSequence;
    //
    private Integer itemAuditStatu;
    //
    private String itemAuditRefuse;
    //
    private Integer praiseSum;
    private Integer itemMaxSum;
    private String companySn;

    private Integer itemPayType;
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
     * 设置：活动号
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 获取：活动号
     */
    public String getItemNo() {
        return itemNo;
    }
    /**
     * 设置：用户id
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取：用户id
     */
    public Integer getMemberId() {
        return memberId;
    }
    /**
     * 设置：标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取：标题
     */
    public String getTitle() {
        return title;
    }
    /**
     * 设置：演出节目
     */
    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    /**
     * 获取：演出节目
     */
    public String getItemList() {
        return itemList;
    }
    /**
     * 设置：活动描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：活动描述
     */
    public String getDescription() {
        return description;
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
     * 设置：省份
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取：省份
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
     * 设置：
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取：
     */
    public Integer getRegionId() {
        return regionId;
    }
    /**
     * 设置：
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：
     */
    public String getAddress() {
        return address;
    }
    /**
     * 设置：
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置：主办方
     */
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    /**
     * 获取：主办方
     */
    public String getOrganizerName() {
        return organizerName;
    }
    /**
     * 设置：
     */
    public void setOrganizerLinkName(String organizerLinkName) {
        this.organizerLinkName = organizerLinkName;
    }

    /**
     * 获取：
     */
    public String getOrganizerLinkName() {
        return organizerLinkName;
    }
    /**
     * 设置：
     */
    public void setOrganizerLinkPhone(String organizerLinkPhone) {
        this.organizerLinkPhone = organizerLinkPhone;
    }

    /**
     * 获取：
     */
    public String getOrganizerLinkPhone() {
        return organizerLinkPhone;
    }
    /**
     * 设置：
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取：
     */
    public Date getStartTime() {
        return startTime;
    }
    /**
     * 设置：
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取：
     */
    public Date getEndTime() {
        return endTime;
    }
    /**
     * 设置：点击次数
     */
    public void setClickSum(Integer clickSum) {
        this.clickSum = clickSum;
    }

    /**
     * 获取：点击次数
     */
    public Integer getClickSum() {
        return clickSum;
    }
    /**
     * 设置：分类
     */
    public void setItemKind(Integer itemKind) {
        this.itemKind = itemKind;
    }

    /**
     * 获取：分类
     */
    public Integer getItemKind() {
        return itemKind;
    }
    /**
     * 设置：轮番图片
     */
    public void setItemLogo(String itemLogo) {
        this.itemLogo = itemLogo;
    }

    /**
     * 获取：轮番图片
     */
    public String getItemLogo() {
        return itemLogo;
    }
    /**
     * 设置：
     */
    public void setJobsSum(Integer jobsSum) {
        this.jobsSum = jobsSum;
    }

    /**
     * 获取：
     */
    public Integer getJobsSum() {
        return jobsSum;
    }
    /**
     * 设置：
     */
    public void setTicketSum(Integer ticketSum) {
        this.ticketSum = ticketSum;
    }

    /**
     * 获取：
     */
    public Integer getTicketSum() {
        return ticketSum;
    }
    /**
     * 设置：类型
     */
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取：类型
     */
    public Integer getItemType() {
        return itemType;
    }
    /**
     * 设置：
     */
    public void setApplyPeopleSum(Integer applyPeopleSum) {
        this.applyPeopleSum = applyPeopleSum;
    }

    /**
     * 获取：
     */
    public Integer getApplyPeopleSum() {
        return applyPeopleSum;
    }
    /**
     * 设置：
     */
    public void setProductSum(Integer productSum) {
        this.productSum = productSum;
    }

    /**
     * 获取：
     */
    public Integer getProductSum() {
        return productSum;
    }
    /**
     * 设置：
     */
    public void setVirtualProductSum(Integer virtualProductSum) {
        this.virtualProductSum = virtualProductSum;
    }

    /**
     * 获取：
     */
    public Integer getVirtualProductSum() {
        return virtualProductSum;
    }
    /**
     * 设置：
     */
    public void setVirtualApplyPeopleSum(Integer virtualApplyPeopleSum) {
        this.virtualApplyPeopleSum = virtualApplyPeopleSum;
    }

    /**
     * 获取：
     */
    public Integer getVirtualApplyPeopleSum() {
        return virtualApplyPeopleSum;
    }
    /**
     * 设置：
     */
    public void setApplyEndTime(Date applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    /**
     * 获取：
     */
    public Date getApplyEndTime() {
        return applyEndTime;
    }
    /**
     * 设置：
     */
    public void setApplyStatu(Integer applyStatu) {
        this.applyStatu = applyStatu;
    }

    /**
     * 获取：
     */
    public Integer getApplyStatu() {
        return applyStatu;
    }
    /**
     * 设置：
     */
    public void setApplyIntro(String applyIntro) {
        this.applyIntro = applyIntro;
    }

    /**
     * 获取：
     */
    public String getApplyIntro() {
        return applyIntro;
    }
    /**
     * 设置：
     */
    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * 获取：
     */
    public Integer getProductPrice() {
        return productPrice;
    }
    /**
     * 设置：
     */
    public void setItemSequence(Integer itemSequence) {
        this.itemSequence = itemSequence;
    }

    /**
     * 获取：
     */
    public Integer getItemSequence() {
        return itemSequence;
    }
    /**
     * 设置：
     */
    public void setItemAuditStatu(Integer itemAuditStatu) {
        this.itemAuditStatu = itemAuditStatu;
    }

    /**
     * 获取：
     */
    public Integer getItemAuditStatu() {
        return itemAuditStatu;
    }
    /**
     * 设置：
     */
    public void setItemAuditRefuse(String itemAuditRefuse) {
        this.itemAuditRefuse = itemAuditRefuse;
    }

    /**
     * 获取：
     */
    public String getItemAuditRefuse() {
        return itemAuditRefuse;
    }
    /**
     * 设置：
     */
    public void setPraiseSum(Integer praiseSum) {
        this.praiseSum = praiseSum;
    }

    /**
     * 获取：
     */
    public Integer getPraiseSum() {
        return praiseSum;
    }
    /**
     * 设置：
     */
    public void setItemMaxSum(Integer itemMaxSum) {
        this.itemMaxSum = itemMaxSum;
    }

    /**
     * 获取：
     */
    public Integer getItemMaxSum() {
        return itemMaxSum;
    }
    /**
     * 设置：
     */
    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    /**
     * 获取：
     */
    public String getCompanySn() {
        return companySn;
    }

	public Integer getItemPayType() {
		return itemPayType;
	}

	public void setItemPayType(Integer itemPayType) {
		this.itemPayType = itemPayType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
