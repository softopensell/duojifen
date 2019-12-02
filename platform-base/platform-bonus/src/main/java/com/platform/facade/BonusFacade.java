package com.platform.facade;

import java.math.BigDecimal;

import com.platform.entity.UserEntity;
import com.platform.mq.model.BonusTaskVo;
import com.platform.utils.R;




public interface BonusFacade {
	
	/**
	 * 奖励分红计算
	 * @param visitor
	 * @param consumedMemberId
	 * @param consumedPointMoney
	 * @return
	 */
	public void  bonusShare(BonusTaskVo bonusTaskVo);
	
	/**
	 * 确认注册 
	 * @param userId
	 * @return
	 */
//	public R  confirmSignUp(Integer userId);
	
	/**
	 * 根据消费升级
	 * @param userEntity
	 * @param consumedMoney
	 * @return
	 */
	public R  updateUserLevel(UserEntity userEntity,BigDecimal consumedMoney);
	
	
	public R  buyInvestBonus(int userId,String orderNo,BigDecimal investMoney);
	
	
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
	
}
