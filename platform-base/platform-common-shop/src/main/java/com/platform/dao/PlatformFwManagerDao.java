package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.PlatformFwManagerEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
public interface PlatformFwManagerDao extends BaseDao<PlatformFwManagerEntity> {
	
	List<PlatformFwManagerEntity> queryByUserId(Map<String,Object> params);
}
