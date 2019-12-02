package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.UserInvestLevelEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-15 03:15:07
 */
public interface UserInvestLevelDao extends BaseDao<UserInvestLevelEntity> {
	 public UserInvestLevelEntity queryByLevelType(@Param("userLevelType") Integer userLevelType);
}
