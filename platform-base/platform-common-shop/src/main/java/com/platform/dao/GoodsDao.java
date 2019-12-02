package com.platform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.GoodsEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:07
 */
public interface GoodsDao extends BaseDao<GoodsEntity> {
	GoodsEntity queryByGoodSn(@Param("goodSn") String goodSn);
	List<GoodsEntity> queryFileList(Map<String, Object> params);
	List<GoodsEntity> queryHotGoodsList(Map<String, Object> params);
    List<GoodsEntity> queryCatalogProductList(Map<String, Object> params);

}
