package com.platform.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 实体
 * 表名 bonus_pool
 *企业奖励
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 01:26:17
 */
public class BonusPoolVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //中间编号，日期，序号
    private String poolNumber;
    //奖金池金额
    private BigDecimal poolMoney;
    //类别：0 公司量碰 ，
    private Integer poolType;
    //说明
    private String poolRemark;
    //添加时间
    private Date createtime;
    //更新时间
    private Date updatetime;
    //状态
    private Integer status;

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
    public void setPoolNumber(String poolNumber) {
        this.poolNumber = poolNumber;
    }

    /**
     * 获取：中间编号，日期，序号
     */
    public String getPoolNumber() {
        return poolNumber;
    }
    /**
     * 设置：奖金池金额
     */
    public void setPoolMoney(BigDecimal poolMoney) {
        this.poolMoney = poolMoney;
    }

    /**
     * 获取：奖金池金额
     */
    public BigDecimal getPoolMoney() {
        return poolMoney;
    }
    /**
     * 设置：类别：0 公司量碰 ，
     */
    public void setPoolType(Integer poolType) {
        this.poolType = poolType;
    }

    /**
     * 获取：类别：0 公司量碰 ，
     */
    public Integer getPoolType() {
        return poolType;
    }
    /**
     * 设置：说明
     */
    public void setPoolRemark(String poolRemark) {
        this.poolRemark = poolRemark;
    }

    /**
     * 获取：说明
     */
    public String getPoolRemark() {
        return poolRemark;
    }
    /**
     * 设置：添加时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取：添加时间
     */
    public Date getCreatetime() {
        return createtime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
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
}
