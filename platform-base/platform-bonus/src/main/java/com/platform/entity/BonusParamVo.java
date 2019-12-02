package com.platform.entity;

import java.io.Serializable;

/**
 * 实体
 * 表名 t_bonus_param
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-05 23:13:21
 */
public class BonusParamVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Long id;
    //参数KEY
    private String paramKey;
    //参数值
    private String paramValue;
    //状态码值0代表正常1代表停用
    private Integer status;
    //说明
    private String remark;

    /**
     * 设置：主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键ID
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：参数KEY
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * 获取：参数KEY
     */
    public String getParamKey() {
        return paramKey;
    }
    /**
     * 设置：参数值
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * 获取：参数值
     */
    public String getParamValue() {
        return paramValue;
    }
    /**
     * 设置：状态码值0代表正常1代表停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态码值0代表正常1代表停用
     */
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置：说明
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：说明
     */
    public String getRemark() {
        return remark;
    }
}
