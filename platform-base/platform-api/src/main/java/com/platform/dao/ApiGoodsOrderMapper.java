package com.platform.dao;

import com.platform.entity.GoodsOrderVo;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 01:05:05
 */
public interface ApiGoodsOrderMapper extends BaseDao<GoodsOrderVo> {
	 public GoodsOrderVo queryObjectByNo(String orderNo);
}
