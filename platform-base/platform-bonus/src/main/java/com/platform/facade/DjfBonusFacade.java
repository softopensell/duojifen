package com.platform.facade;

import java.math.BigDecimal;

import com.platform.entity.PlatformFwManagerEntity;
import com.platform.mq.model.BonusTaskVo;
import com.platform.utils.R;




public interface DjfBonusFacade {
	
	
	
	/**
	 * 确认注册 
	 * @param userId
	 * @return
	 */
//	public R  confirmSignUp(Integer userId);
	
	public PlatformFwManagerEntity getLastFwUserId(int userId);
	/**
	 * 根据消费升级
	 * @param userEntity
	 * @param consumedMoney
	 * @return
	 */
	public R  updateUserLevel(int  userId,BigDecimal consumedMoney);
	
	/**
	 * 消费订单 生成资产
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public R  buyInvestBonus(int userId,String orderNo);
	/**
	 * 充值 生成资产
	 * @param userId
	 * @param orderNo
	 * @param investMoney
	 * @return
	 */
	public R  buyDirectInvestBonus(int userId,String orderNo,BigDecimal investMoney);
	/**
	 * 增加统计会员节点业绩
	 * @param inverstUserId
	 * @param incomeMoney
	 */
	public void updateNodedConsumedTeamBonusPonit(int inverstUserId,BigDecimal incomeMoney);
	/**
	 * 购买推荐奖励
	 * @param bonusTaskVo
	 */
	public void  bonusConsumedMallOrderShare(BonusTaskVo bonusTaskVo);
	/**
	 * 每日返利订单处理
	 * @param bonusTaskVo
	 */
	public void  bonusEveryShare();
	
	
	
	/**
	 * 断掉节点，清除父节点业绩数据 ，推荐关系id=0,节点关系为0 生成一级子链
	 * @param userId
	 */
	public R brokeNodeByUserId(int userId);
	
	/**
	 * 挂载独立节点的父节点
	 * @param curUserId
	 * @param parentNodeUserId
	 * @param parentInvitedUserId
	 */
	public R addNodesToNodeBySubNodeUserId(int curUserId,int parentNodeUserId,Integer parentInvitedUserId);
	
}
