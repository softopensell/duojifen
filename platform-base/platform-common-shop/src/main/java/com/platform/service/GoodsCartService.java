package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.entity.GoodsCartEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-13 10:44:41
 */
public interface GoodsCartService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    GoodsCartEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<GoodsCartEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param goodsCart 实体
     * @return 保存条数
     */
    int save(GoodsCartEntity goodsCart);

    /**
     * 根据主键更新实体
     *
     * @param goodsCart 实体
     * @return 更新条数
     */
    int update(GoodsCartEntity goodsCart);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);
    void updateCheck(String[] productIds, Integer isChecked, Integer userId);
    void deleteByProductIds(String[] productIds) ;
    void deleteByUserAndProductIds(Integer userId, String[] productIds);
    void deleteByCart(Integer user_id,   Integer checked);
}
