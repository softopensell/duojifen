package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
/**
 * 实体
 * 表名 t_user
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:07:38
 */
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    //账号
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
    
    private Integer userRoleType;//分销级别
    private String userRoleTypeName;//分销级别
    private Integer userNamedType;//经销商级别
    private String userNamedTypeName;//经销商级别
    
    
    private Integer userLevelType;//会员级别
   
    
    //投资级别
    private Integer userNodeBonusLevel;//node 享受级别
    
    
    //支付密码
    private String payPassword;
    //π包
    private BigDecimal paibag;
    //奖励
    private BigDecimal fund;
    //代注册人id
    private Integer createUserId;
    //联系电话
    private String addrPhone;
    //联系人
    private String addrLinkName;
    //注册时推荐人 仅仅记录数据
    private String signupInvitedPhone;
    //仅仅记录节点phone
    private String signupNodePhone;
    //注册时的vip 级别
    private Integer signupUserLevelType;
   
    
    private Integer totalInverstOrderSum;// 消费订单数量
    //总投资额度
    private BigDecimal totalInvestMoney;// 消费订单总额 
    //资产总收益
    private BigDecimal totalInvestIncomeMoney;// 10*totalInvestMoney
    //已经资产收益
    private BigDecimal investIncomeMoney;//
    //剩余资产收益
    private BigDecimal surplusInvestMoney;//剩余
    //最后资产收益时间 ---凌晨
    private Date shareInvestLastTime;//时间
    
    
    private String googleSecret;//goole 验证器 密钥
    
    private String blockchainSecret;//区块链地址
    
    
    
    private String userLevelTypeName;//会员名称
    
    
    //团队业绩
    private BigDecimal bonusTeamInvitedPoints;
    //个人业绩 直接业绩
    private BigDecimal bonusMeInvitedPoints;
	
    private BigDecimal userPreBalance;
    
//    //属于A区
//    private String aArea;
//    //属于B区
//    private String bArea;
    //父类的右节点
    private Integer invitedRightUserId;
    //邀请人ID 默认 left 节点
    private Integer invitedUserId;
    //服务中心名称
    private String fwName;
    //AB区选择
    private Integer aOrB;
    
    
	public Integer getaOrB() {
		return aOrB;
	}

	public void setaOrB(Integer aOrB) {
		this.aOrB = aOrB;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	public Integer getInvitedRightUserId() {
		return invitedRightUserId;
	}

	public void setInvitedRightUserId(Integer invitedRightUserId) {
		this.invitedRightUserId = invitedRightUserId;
	}

	public Integer getInvitedUserId() {
		return invitedUserId;
	}

	public void setInvitedUserId(Integer invitedUserId) {
		this.invitedUserId = invitedUserId;
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

    public String getGoogleSecret() {
		return googleSecret;
	}

	public String getBlockchainSecret() {
		return blockchainSecret;
	}

	public void setBlockchainSecret(String blockchainSecret) {
		this.blockchainSecret = blockchainSecret;
	}

	public void setGoogleSecret(String googleSecret) {
		this.googleSecret = googleSecret;
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

	public Integer getUserRoleType() {
		return userRoleType;
	}

	public void setUserRoleType(Integer userRoleType) {
		this.userRoleType = userRoleType;
	}

	public String getUserRoleTypeName() {
		return userRoleTypeName;
	}

	public void setUserRoleTypeName(String userRoleTypeName) {
		this.userRoleTypeName = userRoleTypeName;
	}

	public Integer getUserNamedType() {
		return userNamedType;
	}

//	public String getSignUpId() {
//		return signUpId;
//	}
//
//	public void setSignUpId(String signUpId) {
//		this.signUpId = signUpId;
//	}

	public String getUserLevelTypeName() {
		return userLevelTypeName;
	}

	public void setUserLevelTypeName(String userLevelTypeName) {
		this.userLevelTypeName = userLevelTypeName;
	}

	public Integer getUserLevelType() {
		return userLevelType;
	}

	public void setUserLevelType(Integer userLevelType) {
		this.userLevelType = userLevelType;
	}

	public void setUserNamedType(Integer userNamedType) {
		this.userNamedType = userNamedType;
	}

	public String getUserNamedTypeName() {
		return userNamedTypeName;
	}

	public void setUserNamedTypeName(String userNamedTypeName) {
		this.userNamedTypeName = userNamedTypeName;
	}

//	public String getInvitedPhone() {
//		return invitedPhone;
//	}
//
//	public void setInvitedPhone(String invitedPhone) {
//		this.invitedPhone = invitedPhone;
//	}
//
//	public String getInvitedNodePhone() {
//		return invitedNodePhone;
//	}
//
//	public void setInvitedNodePhone(String invitedNodePhone) {
//		this.invitedNodePhone = invitedNodePhone;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public BigDecimal getPaibag() {
		return paibag;
	}

	public void setPaibag(BigDecimal paibag) {
		this.paibag = paibag;
	}

	public BigDecimal getFund() {
		return fund;
	}

	public void setFund(BigDecimal fund) {
		this.fund = fund;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getAddrPhone() {
		return addrPhone;
	}

	public void setAddrPhone(String addrPhone) {
		this.addrPhone = addrPhone;
	}

	public String getAddrLinkName() {
		return addrLinkName;
	}

	public void setAddrLinkName(String addrLinkName) {
		this.addrLinkName = addrLinkName;
	}

	public String getSignupInvitedPhone() {
		return signupInvitedPhone;
	}

	public void setSignupInvitedPhone(String signupInvitedPhone) {
		this.signupInvitedPhone = signupInvitedPhone;
	}

	public String getSignupNodePhone() {
		return signupNodePhone;
	}

	public void setSignupNodePhone(String signupNodePhone) {
		this.signupNodePhone = signupNodePhone;
	}

	public Integer getSignupUserLevelType() {
		return signupUserLevelType;
	}

	public void setSignupUserLevelType(Integer signupUserLevelType) {
		this.signupUserLevelType = signupUserLevelType;
	}


	public Integer getUserNodeBonusLevel() {
		return userNodeBonusLevel;
	}

	public void setUserNodeBonusLevel(Integer userNodeBonusLevel) {
		this.userNodeBonusLevel = userNodeBonusLevel;
	}

	public BigDecimal getTotalInvestIncomeMoney() {
		return totalInvestIncomeMoney;
	}

	public void setTotalInvestIncomeMoney(BigDecimal totalInvestIncomeMoney) {
		this.totalInvestIncomeMoney = totalInvestIncomeMoney;
	}

	public BigDecimal getInvestIncomeMoney() {
		return investIncomeMoney;
	}

	public void setInvestIncomeMoney(BigDecimal investIncomeMoney) {
		this.investIncomeMoney = investIncomeMoney;
	}

	public BigDecimal getSurplusInvestMoney() {
		return surplusInvestMoney;
	}

	public void setSurplusInvestMoney(BigDecimal surplusInvestMoney) {
		this.surplusInvestMoney = surplusInvestMoney;
	}

	public Date getShareInvestLastTime() {
		return shareInvestLastTime;
	}

	public Integer getTotalInverstOrderSum() {
		return totalInverstOrderSum;
	}

	public void setTotalInverstOrderSum(Integer totalInverstOrderSum) {
		this.totalInverstOrderSum = totalInverstOrderSum;
	}

	public void setShareInvestLastTime(Date shareInvestLastTime) {
		this.shareInvestLastTime = shareInvestLastTime;
	}

	public BigDecimal getTotalInvestMoney() {
		return totalInvestMoney;
	}

	public void setTotalInvestMoney(BigDecimal totalInvestMoney) {
		this.totalInvestMoney = totalInvestMoney;
	}

	public BigDecimal getBonusTeamInvitedPoints() {
		return bonusTeamInvitedPoints;
	}

	public void setBonusTeamInvitedPoints(BigDecimal bonusTeamInvitedPoints) {
		this.bonusTeamInvitedPoints = bonusTeamInvitedPoints;
	}

	public BigDecimal getBonusMeInvitedPoints() {
		return bonusMeInvitedPoints;
	}

	public void setBonusMeInvitedPoints(BigDecimal bonusMeInvitedPoints) {
		this.bonusMeInvitedPoints = bonusMeInvitedPoints;
	}

	public BigDecimal getUserPreBalance() {
		return userPreBalance;
	}

	public void setUserPreBalance(BigDecimal userPreBalance) {
		this.userPreBalance = userPreBalance;
	}
    
    
}
