package com.platform.entity;

import java.io.Serializable;

/**
 * 实体
 * 表名 t_sms_log
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:51
 */
public class SmsLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //
    private Integer userId;
    //
    private String phone;
    //
    private Long logDate;
    //
    private String smsCode;
    //
    private Long sendStatus;
    //1成功 0失败
    private String smsText;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取：
     */
    public String getPhone() {
        return phone;
    }
    /**
     * 设置：
     */
    public void setLogDate(Long logDate) {
        this.logDate = logDate;
    }

    /**
     * 获取：
     */
    public Long getLogDate() {
        return logDate;
    }
    /**
     * 设置：
     */
    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    /**
     * 获取：
     */
    public String getSmsCode() {
        return smsCode;
    }
    /**
     * 设置：
     */
    public void setSendStatus(Long sendStatus) {
        this.sendStatus = sendStatus;
    }

    /**
     * 获取：
     */
    public Long getSendStatus() {
        return sendStatus;
    }
    /**
     * 设置：1成功 0失败
     */
    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    /**
     * 获取：1成功 0失败
     */
    public String getSmsText() {
        return smsText;
    }
}
