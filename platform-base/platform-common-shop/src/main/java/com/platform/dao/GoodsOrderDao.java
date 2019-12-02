package com.platform.dao;

import java.util.Map;

import com.platform.entity.GoodsOrderEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 15:10:15
 */
public interface GoodsOrderDao extends BaseDao<GoodsOrderEntity> {
	GoodsOrderEntity queryObjectByNo(String orderNo);
	int queryTotalByStatus(Integer orderStatus);
	Map<String,Object> queryTotalByDate(Map<String,Object> map);
	Map<String,Object> queryTotalPrice(Map<String,Object> map);
}
