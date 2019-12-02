package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 实体
 * 表名 t_bonus_points
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-09 11:03:39
 */
public class BonusPointsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Long id;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //用户ID
    private Integer userId;
    //团队人数
    private Integer bonusInvitedSum;
    //团队业绩
    private BigDecimal bonusTeamInvitedPoints;
    //个人业绩 直接业绩
    private BigDecimal bonusMeInvitedPoints;
    //
    private Integer status;
    //邀请人ID
    private Integer invitedUserId;
    //推荐关系父类节点关系 上级所有userId,约至少不要超过5000层，超过得扩容 |
    private String invitedParentUserIds;
    //层级关系 默认invited_user_id= invited_point_user_id
    private Integer invitedPointUserId;
    //上级所有userId,约至少不要超过5000层，超过得扩容 |
    private String invitedPointUserIds;
    //职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
    private Integer userRoleType;
    //称呼:0 普通会员，1 分公司，2 代理商，3 分销商
    private Integer userNamedType;
    //经理数量
    private Integer bonusTeamRoleManagerSum;
    //一星店长数量
    private Integer bonusTeamRoleShopperOneSum;
    //二星店长数量
    private Integer bonusTeamRoleShopperTwoSum;
    //三星店长数量
    private Integer bonusTeamRoleShopperThreeSum;
    //四星店长数量
    private Integer bonusTeamRoleShopperFourSum;
    //是否可以生成二维码，1可以
    private String canGenerateQr;
    //关系树类型：0 多元 1 二叉树 
    private Integer bloodType;
    //升级类型：0 无 1 消费总额 2 
    private Integer bloodUpType;
    //父类的右节点
    private Integer invitedRightUserId;
    
    private String userName;
    private String userPhoto;

    
    
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
     * 设置：团队人数
     */
    public void setBonusInvitedSum(Integer bonusInvitedSum) {
        this.bonusInvitedSum = bonusInvitedSum;
    }

    /**
     * 获取：团队人数
     */
    public Integer getBonusInvitedSum() {
        return bonusInvitedSum;
    }
    /**
     * 设置：团队业绩
     */
    public void setBonusTeamInvitedPoints(BigDecimal bonusTeamInvitedPoints) {
        this.bonusTeamInvitedPoints = bonusTeamInvitedPoints;
    }

    /**
     * 获取：团队业绩
     */
    public BigDecimal getBonusTeamInvitedPoints() {
        return bonusTeamInvitedPoints;
    }
    /**
     * 设置：个人业绩 直接业绩
     */
    public void setBonusMeInvitedPoints(BigDecimal bonusMeInvitedPoints) {
        this.bonusMeInvitedPoints = bonusMeInvitedPoints;
    }

    /**
     * 获取：个人业绩 直接业绩
     */
    public BigDecimal getBonusMeInvitedPoints() {
        return bonusMeInvitedPoints;
    }
    /**
     * 设置：
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：
     */
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置：邀请人ID
     */
    public void setInvitedUserId(Integer invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    /**
     * 获取：邀请人ID
     */
    public Integer getInvitedUserId() {
        return invitedUserId;
    }
    /**
     * 设置：推荐关系父类节点关系 上级所有userId,约至少不要超过5000层，超过得扩容 |
     */
    public void setInvitedParentUserIds(String invitedParentUserIds) {
        this.invitedParentUserIds = invitedParentUserIds;
    }

    /**
     * 获取：推荐关系父类节点关系 上级所有userId,约至少不要超过5000层，超过得扩容 |
     */
    public String getInvitedParentUserIds() {
        return invitedParentUserIds;
    }
    /**
     * 设置：经销商层级关系 默认invited_user_id= invited_point_user_id
     */
    public void setInvitedPointUserId(Integer invitedPointUserId) {
        this.invitedPointUserId = invitedPointUserId;
    }

    /**
     * 获取：经销商层级关系 默认invited_user_id= invited_point_user_id
     */
    public Integer getInvitedPointUserId() {
        return invitedPointUserId;
    }
    /**
     * 设置：经销商上级所有userId,约至少不要超过5000层，超过得扩容 |
     */
    public void setInvitedPointUserIds(String invitedPointUserIds) {
        this.invitedPointUserIds = invitedPointUserIds;
    }

    /**
     * 获取：经销商上级所有userId,约至少不要超过5000层，超过得扩容 |
     */
    public String getInvitedPointUserIds() {
        return invitedPointUserIds;
    }
    /**
     * 设置：职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
     */
    public void setUserRoleType(Integer userRoleType) {
        this.userRoleType = userRoleType;
    }

    /**
     * 获取：职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
     */
    public Integer getUserRoleType() {
        return userRoleType;
    }
    /**
     * 设置：称呼:0 普通会员，1 分公司，2 代理商，3 分销商
     */
    public void setUserNamedType(Integer userNamedType) {
        this.userNamedType = userNamedType;
    }

    /**
     * 获取：称呼:0 普通会员，1 分公司，2 代理商，3 分销商
     */
    public Integer getUserNamedType() {
        return userNamedType;
    }
    /**
     * 设置：经理数量
     */
    public void setBonusTeamRoleManagerSum(Integer bonusTeamRoleManagerSum) {
        this.bonusTeamRoleManagerSum = bonusTeamRoleManagerSum;
    }

    /**
     * 获取：经理数量
     */
    public Integer getBonusTeamRoleManagerSum() {
        return bonusTeamRoleManagerSum;
    }
    /**
     * 设置：一星店长数量
     */
    public void setBonusTeamRoleShopperOneSum(Integer bonusTeamRoleShopperOneSum) {
        this.bonusTeamRoleShopperOneSum = bonusTeamRoleShopperOneSum;
    }

    /**
     * 获取：一星店长数量
     */
    public Integer getBonusTeamRoleShopperOneSum() {
        return bonusTeamRoleShopperOneSum;
    }
    /**
     * 设置：二星店长数量
     */
    public void setBonusTeamRoleShopperTwoSum(Integer bonusTeamRoleShopperTwoSum) {
        this.bonusTeamRoleShopperTwoSum = bonusTeamRoleShopperTwoSum;
    }

    /**
     * 获取：二星店长数量
     */
    public Integer getBonusTeamRoleShopperTwoSum() {
        return bonusTeamRoleShopperTwoSum;
    }
    /**
     * 设置：三星店长数量
     */
    public void setBonusTeamRoleShopperThreeSum(Integer bonusTeamRoleShopperThreeSum) {
        this.bonusTeamRoleShopperThreeSum = bonusTeamRoleShopperThreeSum;
    }

    /**
     * 获取：三星店长数量
     */
    public Integer getBonusTeamRoleShopperThreeSum() {
        return bonusTeamRoleShopperThreeSum;
    }
    /**
     * 设置：四星店长数量
     */
    public void setBonusTeamRoleShopperFourSum(Integer bonusTeamRoleShopperFourSum) {
        this.bonusTeamRoleShopperFourSum = bonusTeamRoleShopperFourSum;
    }

    /**
     * 获取：四星店长数量
     */
    public Integer getBonusTeamRoleShopperFourSum() {
        return bonusTeamRoleShopperFourSum;
    }
    /**
     * 设置：是否可以生成二维码，1可以
     */
    public void setCanGenerateQr(String canGenerateQr) {
        this.canGenerateQr = canGenerateQr;
    }

    /**
     * 获取：是否可以生成二维码，1可以
     */
    public String getCanGenerateQr() {
        return canGenerateQr;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public Integer getBloodType() {
		return bloodType;
	}

	public void setBloodType(Integer bloodType) {
		this.bloodType = bloodType;
	}

	public Integer getBloodUpType() {
		return bloodUpType;
	}

	public void setBloodUpType(Integer bloodUpType) {
		this.bloodUpType = bloodUpType;
	}

	public Integer getInvitedRightUserId() {
		return invitedRightUserId;
	}

	public void setInvitedRightUserId(Integer invitedRightUserId) {
		this.invitedRightUserId = invitedRightUserId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
