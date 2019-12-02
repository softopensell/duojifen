package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 实体
 * 表名 t_bonus_points_log
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-05 15:23:33
 */
public class BonusPointsLogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long id;
    //用户ID
    private Integer userId;
    //消费金额
    private BigDecimal pointAmonut;
    //订单号
    private String orderNo;
    //订单ID
    private Integer orderId;
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
     * 设置：消费金额
     */
    public void setPointAmonut(BigDecimal pointAmonut) {
        this.pointAmonut = pointAmonut;
    }

    /**
     * 获取：消费金额
     */
    public BigDecimal getPointAmonut() {
        return pointAmonut;
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
     * 设置：订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：订单ID
     */
    public Integer getOrderId() {
        return orderId;
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
