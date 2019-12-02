package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_payment_log
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
public class PaymentLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //
    private String paysn;
    //
    private Integer steps;
    //
    private String serverip;
    //
    private String clientip;
    //
    private String remark;
    //
    private String reqparams;
    //
    private String webApp;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：
     */
    public void setPaysn(String paysn) {
        this.paysn = paysn;
    }

    /**
     * 获取：
     */
    public String getPaysn() {
        return paysn;
    }
    /**
     * 设置：
     */
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    /**
     * 获取：
     */
    public Integer getSteps() {
        return steps;
    }
    /**
     * 设置：
     */
    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    /**
     * 获取：
     */
    public String getServerip() {
        return serverip;
    }
    /**
     * 设置：
     */
    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    /**
     * 获取：
     */
    public String getClientip() {
        return clientip;
    }
    /**
     * 设置：
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：
     */
    public String getRemark() {
        return remark;
    }
    /**
     * 设置：
     */
    public void setReqparams(String reqparams) {
        this.reqparams = reqparams;
    }

    /**
     * 获取：
     */
    public String getReqparams() {
        return reqparams;
    }
    /**
     * 设置：
     */
    public void setWebApp(String webApp) {
        this.webApp = webApp;
    }

    /**
     * 获取：
     */
    public String getWebApp() {
        return webApp;
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
