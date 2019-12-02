package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.GoodsCartEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-13 10:44:41
 */
public interface GoodsCartDao extends BaseDao<GoodsCartEntity> {
	     void updateCheck(@Param("productIds") String[] productIds,
             @Param("isChecked") Integer isChecked, @Param("userId") Integer userId);
		void deleteByProductIds(@Param("productIds") String[] productIds);
		
		void deleteByUserAndProductIds(@Param("user_id") Integer user_id,@Param("productIds") String[] productIds);
		
		void deleteByCart(@Param("user_id") Integer user_id,  @Param("checked") Integer checked);
}