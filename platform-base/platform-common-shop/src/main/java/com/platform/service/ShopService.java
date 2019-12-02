package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.entity.ShopEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell_javadream@163.com
 * @date 2019-06-28 23:37:18
 */
public interface ShopService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    ShopEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<ShopEntity> queryList(Map<String, Object> map);

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
     * @param shop 实体
     * @return 保存条数
     */
    int save(ShopEntity shop);

    /**
     * 根据主键更新实体
     *
     * @param shop 实体
     * @return 更新条数
     */
    int update(ShopEntity shop);

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
    
    void saveAudit(ShopEntity shop);
}
