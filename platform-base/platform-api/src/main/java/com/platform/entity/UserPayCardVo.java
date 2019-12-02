package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_user_pay_card
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:50
 */
public class UserPayCardVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //卡类型
    private Integer cardType;
    //名称：招商银行
    private String cardName;
    //卡号
    private String cardNumber;
    //真实姓名
    private String cardRealName;
    //验证状态
    private Integer cardStatu;
    //用户ID
    private Integer userId;
    //用户名称
    private String userName;
    //这个可以默认 CompanyConstant.COMPANY_SN_GJZB
    private String companySn;
    //状态：0正常 1停用
    private Integer status;
    //创建时间
    private Date createDate;
    //修改时间
    private Date updateDate;

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
     * 设置：卡类型
     */
    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    /**
     * 获取：卡类型
     */
    public Integer getCardType() {
        return cardType;
    }
    /**
     * 设置：名称：招商银行
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * 获取：名称：招商银行
     */
    public String getCardName() {
        return cardName;
    }
    /**
     * 设置：卡号
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * 获取：卡号
     */
    public String getCardNumber() {
        return cardNumber;
    }
    /**
     * 设置：真实姓名
     */
    public void setCardRealName(String cardRealName) {
        this.cardRealName = cardRealName;
    }

    /**
     * 获取：真实姓名
     */
    public String getCardRealName() {
        return cardRealName;
    }
    /**
     * 设置：验证状态
     */
    public void setCardStatu(Integer cardStatu) {
        this.cardStatu = cardStatu;
    }

    /**
     * 获取：验证状态
     */
    public Integer getCardStatu() {
        return cardStatu;
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
     * 设置：用户名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：用户名称
     */
    public String getUserName() {
        return userName;
    }
    /**
     * 设置：这个可以默认 CompanyConstant.COMPANY_SN_GJZB
     */
    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    /**
     * 获取：这个可以默认 CompanyConstant.COMPANY_SN_GJZB
     */
    public String getCompanySn() {
        return companySn;
    }
    /**
     * 设置：状态：0正常 1停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态：0正常 1停用
     */
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }
}
