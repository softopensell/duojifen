package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.GoodsVo;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:51
 */
public interface ApiGoodsMapper extends BaseDao<GoodsVo> {
	List<GoodsVo> queryHotGoodsList(Map<String, Object> params);

    List<GoodsVo> queryCatalogProductList(Map<String, Object> params);
}
