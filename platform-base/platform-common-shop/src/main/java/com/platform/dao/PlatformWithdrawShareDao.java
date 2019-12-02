package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.PlatformWithdrawShareEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
public interface PlatformWithdrawShareDao extends BaseDao<PlatformWithdrawShareEntity> {
	List<PlatformWithdrawShareEntity> queryByUserId(Map<String,Object> params);
}
