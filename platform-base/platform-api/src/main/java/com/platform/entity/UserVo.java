package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 * 表名 t_user
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 01:21:35
 */
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer userId;
    //名称
    private String userName;
    //真实姓名
    private String realName;
    //电话
    private String mobile;
    //邮箱
    private String email;
    //密码
    private String password;
    //角色
    private Integer roleId;
    //状态0正常1禁用
    private Integer state;
    //头像照片
    private String photo;
    //来源
    private String fromNo;
    //来源类型
    private Integer fromType;
    //性别1男2女
    private String sex;
    //生日
    private Date birthday;
    //国籍
    private String country;
    //省
    private String province;
    //市
    private String city;
    //地址
    private String address;
    //用户微信openID
    private String weixinOpenid;
    //昵称
    private String nickname;
    //IP
    private String registerIp;
    //微信头像
    private String avatar;
    //注册时间
    private Date registerTime;
    //最后登录时间
    private Date lastLoginTime;
    //最后登录IP
    private String lastLoginIp;
    //最后登录位置
    private String lastPosition;
    //系统应用 同一个账号体系唯一
    private String webApp;
    //审核状态
    private Integer authTypeStatu;
    //余额钱包
    private BigDecimal balance;
    //消费金额
    private BigDecimal amount;
    //累积购物钱包
    private BigDecimal totalPoint;
    //购物余额钱包
    private BigDecimal point;
    //累积收益
    private BigDecimal totalIncome;
    //等待收益
    private BigDecimal waitingIncome;
    //邀请码
    private String invitationCode;
    //积分数
    private Integer integralScore;
    //冻结余额
    private BigDecimal freezeBalance;
    //创建时间
    private Date createtime;
    //修改时间
    private Date updatetime;

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
     * 设置：名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：名称
     */
    public String getUserName() {
        return userName;
    }
    /**
     * 设置：真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取：真实姓名
     */
    public String getRealName() {
        return realName;
    }
    /**
     * 设置：电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取：电话
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * 设置：邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取：邮箱
     */
    public String getEmail() {
        return email;
    }
    /**
     * 设置：密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取：密码
     */
    public String getPassword() {
        return password;
    }
    /**
     * 设置：角色
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取：角色
     */
    public Integer getRoleId() {
        return roleId;
    }
    /**
     * 设置：状态0正常1禁用
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取：状态0正常1禁用
     */
    public Integer getState() {
        return state;
    }
    /**
     * 设置：头像照片
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取：头像照片
     */
    public String getPhoto() {
        return photo;
    }
    /**
     * 设置：来源
     */
    public void setFromNo(String fromNo) {
        this.fromNo = fromNo;
    }

    /**
     * 获取：来源
     */
    public String getFromNo() {
        return fromNo;
    }
    /**
     * 设置：来源类型
     */
    public void setFromType(Integer fromType) {
        this.fromType = fromType;
    }

    /**
     * 获取：来源类型
     */
    public Integer getFromType() {
        return fromType;
    }
    /**
     * 设置：性别1男2女
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取：性别1男2女
     */
    public String getSex() {
        return sex;
    }
    /**
     * 设置：生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取：生日
     */
    public Date getBirthday() {
        return birthday;
    }
    /**
     * 设置：国籍
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取：国籍
     */
    public String getCountry() {
        return country;
    }
    /**
     * 设置：省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取：省
     */
    public String getProvince() {
        return province;
    }
    /**
     * 设置：市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取：市
     */
    public String getCity() {
        return city;
    }
    /**
     * 设置：地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：地址
     */
    public String getAddress() {
        return address;
    }
    /**
     * 设置：用户微信openID
     */
    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid;
    }

    /**
     * 获取：用户微信openID
     */
    public String getWeixinOpenid() {
        return weixinOpenid;
    }
    /**
     * 设置：昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取：昵称
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * 设置：IP
     */
    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    /**
     * 获取：IP
     */
    public String getRegisterIp() {
        return registerIp;
    }
    /**
     * 设置：微信头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取：微信头像
     */
    public String getAvatar() {
        return avatar;
    }
    /**
     * 设置：注册时间
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * 获取：注册时间
     */
    public Date getRegisterTime() {
        return registerTime;
    }
    /**
     * 设置：最后登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取：最后登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }
    /**
     * 设置：最后登录IP
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * 获取：最后登录IP
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    /**
     * 设置：最后登录位置
     */
    public void setLastPosition(String lastPosition) {
        this.lastPosition = lastPosition;
    }

    /**
     * 获取：最后登录位置
     */
    public String getLastPosition() {
        return lastPosition;
    }
    /**
     * 设置：系统应用 同一个账号体系唯一
     */
    public void setWebApp(String webApp) {
        this.webApp = webApp;
    }

    /**
     * 获取：系统应用 同一个账号体系唯一
     */
    public String getWebApp() {
        return webApp;
    }
    /**
     * 设置：审核状态
     */
    public void setAuthTypeStatu(Integer authTypeStatu) {
        this.authTypeStatu = authTypeStatu;
    }

    /**
     * 获取：审核状态
     */
    public Integer getAuthTypeStatu() {
        return authTypeStatu;
    }
    /**
     * 设置：余额钱包
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取：余额钱包
     */
    public BigDecimal getBalance() {
        return balance;
    }
    /**
     * 设置：消费金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：消费金额
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 设置：累积购物钱包
     */
    public void setTotalPoint(BigDecimal totalPoint) {
        this.totalPoint = totalPoint;
    }

    /**
     * 获取：累积购物钱包
     */
    public BigDecimal getTotalPoint() {
        return totalPoint;
    }
    /**
     * 设置：购物余额钱包
     */
    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    /**
     * 获取：购物余额钱包
     */
    public BigDecimal getPoint() {
        return point;
    }
    /**
     * 设置：累积收益
     */
    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    /**
     * 获取：累积收益
     */
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }
    /**
     * 设置：等待收益
     */
    public void setWaitingIncome(BigDecimal waitingIncome) {
        this.waitingIncome = waitingIncome;
    }

    /**
     * 获取：等待收益
     */
    public BigDecimal getWaitingIncome() {
        return waitingIncome;
    }
    /**
     * 设置：邀请码
     */
    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    /**
     * 获取：邀请码
     */
    public String getInvitationCode() {
        return invitationCode;
    }
    /**
     * 设置：积分数
     */
    public void setIntegralScore(Integer integralScore) {
        this.integralScore = integralScore;
    }

    /**
     * 获取：积分数
     */
    public Integer getIntegralScore() {
        return integralScore;
    }
    /**
     * 设置：冻结余额
     */
    public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    /**
     * 获取：冻结余额
     */
    public BigDecimal getFreezeBalance() {
        return freezeBalance;
    }
    /**
     * 设置：创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }
}
