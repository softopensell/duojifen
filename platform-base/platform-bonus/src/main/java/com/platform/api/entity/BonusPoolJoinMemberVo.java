package com.platform.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 实体 今日奖励机制
 * 表名 bonus_pool_join_member
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 02:33:06
 */
public class BonusPoolJoinMemberVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //中间编号，日期，序号
    private String poolDateNumber;
    //用户
    private Integer poolJoinMemberId;
    //产生奖励金额
    private BigDecimal poolJoinMoney;
    //产生奖励次数
    private Integer poolJoinSum;
    //添加，不允许更改
    private Date createtime;
    //状态
    private Integer status;
    //类别:0 用的 ，1 返利日比例
    private Integer poolJoinType;

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
     * 设置：中间编号，日期，序号
     */
    public void setPoolDateNumber(String poolDateNumber) {
        this.poolDateNumber = poolDateNumber;
    }

    /**
     * 获取：中间编号，日期，序号
     */
    public String getPoolDateNumber() {
        return poolDateNumber;
    }
    /**
     * 设置：用户
     */
    public void setPoolJoinMemberId(Integer poolJoinMemberId) {
        this.poolJoinMemberId = poolJoinMemberId;
    }

    /**
     * 获取：用户
     */
    public Integer getPoolJoinMemberId() {
        return poolJoinMemberId;
    }
    /**
     * 设置：产生奖励金额
     */
    public void setPoolJoinMoney(BigDecimal poolJoinMoney) {
        this.poolJoinMoney = poolJoinMoney;
    }

    /**
     * 获取：产生奖励金额
     */
    public BigDecimal getPoolJoinMoney() {
        return poolJoinMoney;
    }
    /**
     * 设置：产生奖励次数
     */
    public void setPoolJoinSum(Integer poolJoinSum) {
        this.poolJoinSum = poolJoinSum;
    }

    /**
     * 获取：产生奖励次数
     */
    public Integer getPoolJoinSum() {
        return poolJoinSum;
    }
    /**
     * 设置：添加，不允许更改
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取：添加，不允许更改
     */
    public Date getCreatetime() {
        return createtime;
    }
    /**
     * 设置：状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态
     */
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置：类别:0 用的 ，1 返利日比例
     */
    public void setPoolJoinType(Integer poolJoinType) {
        this.poolJoinType = poolJoinType;
    }

    /**
     * 获取：类别:0 用的 ，1 返利日比例
     */
    public Integer getPoolJoinType() {
        return poolJoinType;
    }
}
