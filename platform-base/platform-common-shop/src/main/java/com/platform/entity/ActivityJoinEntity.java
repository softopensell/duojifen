package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 activity_join
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public class ActivityJoinEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //活动itemNo
    private String itemNo;
    //状态
    private Integer statu;
    //
    private Integer joinType;
    //
    private String joinInviteNumber;
    //
    private String joinInviteName;
    //
    private String joinInviteLogo;
    //
    private String joinInviteTitle;
    //
    private Integer joinInviteContenttype;
    //
    private String joinInviteDesc;
    //
    private String joinInviteImage;
    //
    private String joinInviteWriteName;
    //
    private Date joinInviteWriteDate;
    //
    private Integer joinInviteReadStatu;
    //
    private Integer joinMemberId;
    //
    private Integer joinAuditStatu;
    //
    private String joinAuditRefuse;
    //
    private Date createTime;
    //
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
    /**
     * 设置：
     */
    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    /**
     * 获取：
     */
    public Integer getJoinType() {
        return joinType;
    }
    /**
     * 设置：
     */
    public void setJoinInviteNumber(String joinInviteNumber) {
        this.joinInviteNumber = joinInviteNumber;
    }

    /**
     * 获取：
     */
    public String getJoinInviteNumber() {
        return joinInviteNumber;
    }
    /**
     * 设置：
     */
    public void setJoinInviteName(String joinInviteName) {
        this.joinInviteName = joinInviteName;
    }

    /**
     * 获取：
     */
    public String getJoinInviteName() {
        return joinInviteName;
    }
    /**
     * 设置：
     */
    public void setJoinInviteLogo(String joinInviteLogo) {
        this.joinInviteLogo = joinInviteLogo;
    }

    /**
     * 获取：
     */
    public String getJoinInviteLogo() {
        return joinInviteLogo;
    }
    /**
     * 设置：
     */
    public void setJoinInviteTitle(String joinInviteTitle) {
        this.joinInviteTitle = joinInviteTitle;
    }

    /**
     * 获取：
     */
    public String getJoinInviteTitle() {
        return joinInviteTitle;
    }
    /**
     * 设置：
     */
    public void setJoinInviteContenttype(Integer joinInviteContenttype) {
        this.joinInviteContenttype = joinInviteContenttype;
    }

    /**
     * 获取：
     */
    public Integer getJoinInviteContenttype() {
        return joinInviteContenttype;
    }
    /**
     * 设置：
     */
    public void setJoinInviteDesc(String joinInviteDesc) {
        this.joinInviteDesc = joinInviteDesc;
    }

    /**
     * 获取：
     */
    public String getJoinInviteDesc() {
        return joinInviteDesc;
    }
    /**
     * 设置：
     */
    public void setJoinInviteImage(String joinInviteImage) {
        this.joinInviteImage = joinInviteImage;
    }

    /**
     * 获取：
     */
    public String getJoinInviteImage() {
        return joinInviteImage;
    }
    /**
     * 设置：
     */
    public void setJoinInviteWriteName(String joinInviteWriteName) {
        this.joinInviteWriteName = joinInviteWriteName;
    }

    /**
     * 获取：
     */
    public String getJoinInviteWriteName() {
        return joinInviteWriteName;
    }
    /**
     * 设置：
     */
    public void setJoinInviteWriteDate(Date joinInviteWriteDate) {
        this.joinInviteWriteDate = joinInviteWriteDate;
    }

    /**
     * 获取：
     */
    public Date getJoinInviteWriteDate() {
        return joinInviteWriteDate;
    }
    /**
     * 设置：
     */
    public void setJoinInviteReadStatu(Integer joinInviteReadStatu) {
        this.joinInviteReadStatu = joinInviteReadStatu;
    }

    /**
     * 获取：
     */
    public Integer getJoinInviteReadStatu() {
        return joinInviteReadStatu;
    }
    /**
     * 设置：
     */
    public void setJoinMemberId(Integer joinMemberId) {
        this.joinMemberId = joinMemberId;
    }

    /**
     * 获取：
     */
    public Integer getJoinMemberId() {
        return joinMemberId;
    }
    /**
     * 设置：
     */
    public void setJoinAuditStatu(Integer joinAuditStatu) {
        this.joinAuditStatu = joinAuditStatu;
    }

    /**
     * 获取：
     */
    public Integer getJoinAuditStatu() {
        return joinAuditStatu;
    }
    /**
     * 设置：
     */
    public void setJoinAuditRefuse(String joinAuditRefuse) {
        this.joinAuditRefuse = joinAuditRefuse;
    }

    /**
     * 获取：
     */
    public String getJoinAuditRefuse() {
        return joinAuditRefuse;
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
}
