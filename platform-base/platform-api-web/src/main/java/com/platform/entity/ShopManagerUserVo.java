package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_shop_manager_user
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:50
 */
public class ShopManagerUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //ID
    private Integer id;
    //商店ID
    private Integer shopId;
    //创建用户id
    private Integer createUserId;
    //用户Id
    private Integer userId;
    //用户名
    private String userName;
    //创建时间
    private Date createTime;
    //状态0正常1停用
    private Integer statu;

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
     * 设置：商店ID
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取：商店ID
     */
    public Integer getShopId() {
        return shopId;
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
     * 设置：用户Id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户Id
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：用户名
     */
    public String getUserName() {
        return userName;
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
     * 设置：状态0正常1停用
     */
    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    /**
     * 获取：状态0正常1停用
     */
    public Integer getStatu() {
        return statu;
    }
}
