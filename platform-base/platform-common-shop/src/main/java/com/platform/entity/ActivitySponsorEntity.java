package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 activity_sponsor
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:45
 */
public class ActivitySponsorEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //活动itemNo
    private String itemNo;
    //赞助商名称
    private String sponsorName;
    //图标
    private String sponsorLogo;
    //描述
    private String sponsorDesc;
    //赞助金额
    private Integer sponsorMoney;
    //状态
    private Integer statu;

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
     * 设置：活动itemNo
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 获取：活动itemNo
     */
    public String getItemNo() {
        return itemNo;
    }
    /**
     * 设置：赞助商名称
     */
    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    /**
     * 获取：赞助商名称
     */
    public String getSponsorName() {
        return sponsorName;
    }
    /**
     * 设置：图标
     */
    public void setSponsorLogo(String sponsorLogo) {
        this.sponsorLogo = sponsorLogo;
    }

    /**
     * 获取：图标
     */
    public String getSponsorLogo() {
        return sponsorLogo;
    }
    /**
     * 设置：描述
     */
    public void setSponsorDesc(String sponsorDesc) {
        this.sponsorDesc = sponsorDesc;
    }

    /**
     * 获取：描述
     */
    public String getSponsorDesc() {
        return sponsorDesc;
    }
    /**
     * 设置：赞助金额
     */
    public void setSponsorMoney(Integer sponsorMoney) {
        this.sponsorMoney = sponsorMoney;
    }

    /**
     * 获取：赞助金额
     */
    public Integer getSponsorMoney() {
        return sponsorMoney;
    }
    /**
     * 设置：状态
     */
    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    /**
     * 获取：状态
     */
    public Integer getStatu() {
        return statu;
    }
}
