package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.PaymentInfoEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
public interface PaymentInfoDao extends BaseDao<PaymentInfoEntity> {
	Map<String,Object> queryTotalByDate(Map<String,Object> map);
	Map<String,Object> queryTodayIncome(Map<String,Object> params);
	PaymentInfoEntity findBySn(String sn);

	Map<String,Object> queryStatByDay(Map<String,Object> params);
	Map<String,Object> queryStatByUserId(Map<String,Object> params);
	List<Map<String,Object>> queryGroupStat(Map<String,Object> params);
	
}
