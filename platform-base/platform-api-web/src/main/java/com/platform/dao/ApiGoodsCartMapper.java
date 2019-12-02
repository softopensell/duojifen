package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.GoodsCartVo;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:51
 */
public interface ApiGoodsCartMapper extends BaseDao<GoodsCartVo> {
	 void updateCheck(@Param("productIds") String[] productIds,
             @Param("isChecked") Integer isChecked, @Param("userId") Integer userId);

		void deleteByProductIds(@Param("productIds") String[] productIds);
		
		void deleteByUserAndProductIds(@Param("user_id") Integer user_id,@Param("productIds") String[] productIds);
		
		void deleteByCart(@Param("user_id") Integer user_id,  @Param("checked") Integer checked);
}
