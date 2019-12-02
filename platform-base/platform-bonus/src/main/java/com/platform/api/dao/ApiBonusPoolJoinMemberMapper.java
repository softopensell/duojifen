package com.platform.api.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.platform.api.entity.BonusPoolJoinMemberVo;
import com.platform.dao.BaseDao;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 02:33:06
 */
public interface ApiBonusPoolJoinMemberMapper extends BaseDao<BonusPoolJoinMemberVo> {
	
	public BonusPoolJoinMemberVo queryByDateNumberAndMemberId(@Param("poolDateNumber")String poolDateNumber,@Param("poolJoinMemberId")Integer poolJoinMemberId);
	public int incrBonusPoolJoinMemberMoney(@Param("poolDateNumber")String poolDateNumber,@Param("poolJoinMemberId")Integer poolJoinMemberId,@Param("incrMoney") BigDecimal incrMoney);
	
}
