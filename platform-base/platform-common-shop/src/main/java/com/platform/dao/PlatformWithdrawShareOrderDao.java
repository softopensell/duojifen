package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.PlatformWithdrawShareOrderEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 22:19:20
 */
public interface PlatformWithdrawShareOrderDao extends BaseDao<PlatformWithdrawShareOrderEntity> {
	public PlatformWithdrawShareOrderEntity queryByDateNo(@Param("orderDateNo")String orderDateNo);
	
}
