package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 t_user_audit
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-30 15:38:31
 */
public class UserAuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //id
    private Integer id;
    //申请人ID
    private Integer applyUserId;
    //申请类型：默认1申请代理2、
    private Integer applyType;
    //1分销商2一级分公司3二级分公司
    private Integer applyLevel;
    //申请时间
    private Date applyTime;
    //1分销商2一级分公司3二级分公司
    private Integer nowLevel;
    //打款金额
    private BigDecimal payAmount;
    //审核人ID
    private Integer auditUserId;
    //审核时间
    private Date auditTime;
    //1待审核2已通过3已拒绝
    private Integer auditStatus;
    //审核意见
    private String auditOpinion;
    
    private String applyUserName;

    /**
     * 设置：id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：id
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：申请人ID
     */
    public void setApplyUserId(Integer applyUserId) {
        this.applyUserId = applyUserId;
    }

    /**
     * 获取：申请人ID
     */
    public Integer getApplyUserId() {
        return applyUserId;
    }
    /**
     * 设置：申请类型：默认1申请代理2、
     */
    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    /**
     * 获取：申请类型：默认1申请代理2、
     */
    public Integer getApplyType() {
        return applyType;
    }
    /**
     * 设置：1分销商2一级分公司3二级分公司
     */
    public void setApplyLevel(Integer applyLevel) {
        this.applyLevel = applyLevel;
    }

    /**
     * 获取：1分销商2一级分公司3二级分公司
     */
    public Integer getApplyLevel() {
        return applyLevel;
    }
    /**
     * 设置：申请时间
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取：申请时间
     */
    public Date getApplyTime() {
        return applyTime;
    }
    /**
     * 设置：1分销商2一级分公司3二级分公司
     */
    public void setNowLevel(Integer nowLevel) {
        this.nowLevel = nowLevel;
    }

    /**
     * 获取：1分销商2一级分公司3二级分公司
     */
    public Integer getNowLevel() {
        return nowLevel;
    }
    /**
     * 设置：打款金额
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 获取：打款金额
     */
    public BigDecimal getPayAmount() {
        return payAmount;
    }
    /**
     * 设置：审核人ID
     */
    public void setAuditUserId(Integer auditUserId) {
        this.auditUserId = auditUserId;
    }

    /**
     * 获取：审核人ID
     */
    public Integer getAuditUserId() {
        return auditUserId;
    }
    /**
     * 设置：审核时间
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    /**
     * 获取：审核时间
     */
    public Date getAuditTime() {
        return auditTime;
    }
    /**
     * 设置：1待审核2已通过3已拒绝
     */
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取：1待审核2已通过3已拒绝
     */
    public Integer getAuditStatus() {
        return auditStatus;
    }
    /**
     * 设置：审核意见
     */
    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    /**
     * 获取：审核意见
     */
    public String getAuditOpinion() {
        return auditOpinion;
    }

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
    
    
}
