package com.platform.api.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.platform.api.entity.BonusPoolVo;
import com.platform.dao.BaseDao;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 01:26:17
 */
public interface ApiBonusPoolMapper extends BaseDao<BonusPoolVo> {
	public int incrBonusPoolMoney(@Param("poolNumber")String poolNumber,@Param("incrMoney") BigDecimal incrMoney);
	public BonusPoolVo queryByPoolNumber(@Param("poolNumber")String poolNumber);
}
