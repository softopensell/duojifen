package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 activity_ticket_order
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public class ActivityTickeorderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    //订单号
    private String orderNo;
    //用户id
    private Integer memberId;
    //姓名
    private String memberName;
    //电话
    private String memberPhone;
    //地址
    private String memberAddress;
    //备注
    private String memberRemark;
    //票
    private Integer ticketId;
    //ticket_name
    private String ticketName;
    //价格
    private Integer ticketPrice;
    //人数 或者 门票张数量
    private Integer ticketSum;
    //总价 
    private Integer totalPrice;
    //支付类型
    private Integer payStatu;
    //状态 0 有效 1 删除
    private Integer memberStatu;
    //状态 0 有效 1 咨询过，2 发货 
    private Integer orderStatu;
    //状态 0 有效 1 删除
    private Integer statu;
    private Date createTime;
    private Date updateTime;
    private String itemNo;
    private Integer orderType;
    private String itemTitle;
    private String companySn;

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
     * 设置：姓名
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    /**
     * 获取：姓名
     */
    public String getMemberName() {
        return memberName;
    }
    /**
     * 设置：电话
     */
    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    /**
     * 获取：电话
     */
    public String getMemberPhone() {
        return memberPhone;
    }
    /**
     * 设置：邮寄地址
     */
    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    /**
     * 获取：邮寄地址
     */
    public String getMemberAddress() {
        return memberAddress;
    }
    /**
     * 设置：备注
     */
    public void setMemberRemark(String memberRemark) {
        this.memberRemark = memberRemark;
    }

    /**
     * 获取：备注
     */
    public String getMemberRemark() {
        return memberRemark;
    }
    /**
     * 设置：票
     */
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * 获取：票
     */
    public Integer getTicketId() {
        return ticketId;
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
     * 设置：总价 
     */
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取：总价 
     */
    public Integer getTotalPrice() {
        return totalPrice;
    }
    /**
     * 设置：状态 0 有效 1 删除
     */
    public void setPayStatu(Integer payStatu) {
        this.payStatu = payStatu;
    }

    /**
     * 获取：状态 0 有效 1 删除
     */
    public Integer getPayStatu() {
        return payStatu;
    }
    /**
     * 设置：临时使用 购买支付方式
     */
    public void setMemberStatu(Integer memberStatu) {
        this.memberStatu = memberStatu;
    }

    /**
     * 获取：状态 0 有效 1 删除
     */
    public Integer getMemberStatu() {
        return memberStatu;
    }
    /**
     * 设置：状态 0 有效 1 咨询过，2 发货 
     */
    public void setOrderStatu(Integer orderStatu) {
        this.orderStatu = orderStatu;
    }

    /**
     * 获取：状态 0 有效 1 咨询过，2 发货 
     */
    public Integer getOrderStatu() {
        return orderStatu;
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
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 获取：
     */
    public String getItemNo() {
        return itemNo;
    }
    /**
     * 设置：
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取：
     */
    public Integer getOrderType() {
        return orderType;
    }
    /**
     * 设置：
     */
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    /**
     * 获取：
     */
    public String getItemTitle() {
        return itemTitle;
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
}
