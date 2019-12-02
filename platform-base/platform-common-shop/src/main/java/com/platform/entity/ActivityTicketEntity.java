package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 activity_ticket
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public class ActivityTicketEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //用户id
    private Integer memberId;
    //分类
    private Integer ticketKind;
    //大活动NO
    private String itemNo;
    //ticket_name
    private String ticketName;
    //ticket_logo
    private String ticketLogo;
    //description
    private String description;
    //价格
    private Integer ticketPrice;
    //原价格
    private Integer ticketOrgPrice;
    //max_sum
    private Integer maxSum;
    //sell_sum
    private Integer sellSum;
    //状态 0 有效 1 删除
    private Integer statu;
    //
    private Date createTime;
    //
    private Date updateTime;
    //
    private Date endTime;

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
     * 设置：分类
     */
    public void setTicketKind(Integer ticketKind) {
        this.ticketKind = ticketKind;
    }

    /**
     * 获取：分类
     */
    public Integer getTicketKind() {
        return ticketKind;
    }
    /**
     * 设置：大活动NO
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 获取：大活动NO
     */
    public String getItemNo() {
        return itemNo;
    }
    /**
     * 设置：ticket_name
     */
    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    /**
     * 获取：ticket_name
     */
    public String getTicketName() {
        return ticketName;
    }
    /**
     * 设置：ticket_logo
     */
    public void setTicketLogo(String ticketLogo) {
        this.ticketLogo = ticketLogo;
    }

    /**
     * 获取：ticket_logo
     */
    public String getTicketLogo() {
        return ticketLogo;
    }
    /**
     * 设置：description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：description
     */
    public String getDescription() {
        return description;
    }
    /**
     * 设置：价格
     */
    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    /**
     * 获取：价格
     */
    public Integer getTicketPrice() {
        return ticketPrice;
    }
    /**
     * 设置：原价格
     */
    public void setTicketOrgPrice(Integer ticketOrgPrice) {
        this.ticketOrgPrice = ticketOrgPrice;
    }

    /**
     * 获取：原价格
     */
    public Integer getTicketOrgPrice() {
        return ticketOrgPrice;
    }
    /**
     * 设置：max_sum
     */
    public void setMaxSum(Integer maxSum) {
        this.maxSum = maxSum;
    }

    /**
     * 获取：max_sum
     */
    public Integer getMaxSum() {
        return maxSum;
    }
    /**
     * 设置：sell_sum
     */
    public void setSellSum(Integer sellSum) {
        this.sellSum = sellSum;
    }

    /**
     * 获取：sell_sum
     */
    public Integer getSellSum() {
        return sellSum;
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
}
