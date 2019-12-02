package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_user_invited_chg_log
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-17 17:09:31
 */
public class UserInvitedChgLogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long id;
    //用户ID
    private Integer userId;
    //原推荐人
    private Integer oldInvitedUserId;
    //新推荐人
    private Integer newInvitedUserId;
    //原父级集合
    private String oldInvitedParentUserIds;
    //新父级集合
    private String newInvitedParentUserIds;
    //创建时间
    private Date createTime;

    /**
     * 设置：主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Long getId() {
        return id;
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
     * 设置：原推荐人
     */
    public void setOldInvitedUserId(Integer oldInvitedUserId) {
        this.oldInvitedUserId = oldInvitedUserId;
    }

    /**
     * 获取：原推荐人
     */
    public Integer getOldInvitedUserId() {
        return oldInvitedUserId;
    }
    /**
     * 设置：新推荐人
     */
    public void setNewInvitedUserId(Integer newInvitedUserId) {
        this.newInvitedUserId = newInvitedUserId;
    }

    /**
     * 获取：新推荐人
     */
    public Integer getNewInvitedUserId() {
        return newInvitedUserId;
    }
    /**
     * 设置：原父级集合
     */
    public void setOldInvitedParentUserIds(String oldInvitedParentUserIds) {
        this.oldInvitedParentUserIds = oldInvitedParentUserIds;
    }

    /**
     * 获取：原父级集合
     */
    public String getOldInvitedParentUserIds() {
        return oldInvitedParentUserIds;
    }
    /**
     * 设置：新父级集合
     */
    public void setNewInvitedParentUserIds(String newInvitedParentUserIds) {
        this.newInvitedParentUserIds = newInvitedParentUserIds;
    }

    /**
     * 获取：新父级集合
     */
    public String getNewInvitedParentUserIds() {
        return newInvitedParentUserIds;
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
}
