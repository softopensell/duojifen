package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_user_black
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 03:14:08
 */
public class UserBlackEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //会员
    private Integer userId;
    //会员名称
    private String userName;
    //类别：0 黑名单 1 白名单
    private Integer blackType;
    //状态 0 有效 1 删除
    private Integer statu;
    //创建时间
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
     * 设置：会员
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：会员
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：会员名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：会员名称
     */
    public String getUserName() {
        return userName;
    }
    /**
     * 设置：类别：0 黑名单 1 白名单
     */
    public void setBlackType(Integer blackType) {
        this.blackType = blackType;
    }

    /**
     * 获取：类别：0 黑名单 1 白名单
     */
    public Integer getBlackType() {
        return blackType;
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
