package com.platform.dao;

import java.util.Map;

import com.platform.entity.PaymentOutEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
public interface PaymentOutDao extends BaseDao<PaymentOutEntity> {
	Map<String,Object> queryStat(Map<String, Object> params);

}
