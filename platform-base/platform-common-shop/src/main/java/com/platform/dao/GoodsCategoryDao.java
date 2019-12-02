package com.platform.dao;

import com.platform.entity.GoodsCategoryEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:08
 */
public interface GoodsCategoryDao extends BaseDao<GoodsCategoryEntity> {
	public int deleteByParentBatch(Object[] id);
}
