package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.ActivityItemEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public interface ActivityItemDao extends BaseDao<ActivityItemEntity> {
	
	
	public ActivityItemEntity queryObjectByItemNo(@Param("itemNo")String itemNo);

}
