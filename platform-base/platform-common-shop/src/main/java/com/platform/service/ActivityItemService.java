package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.entity.ActivityItemEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public interface ActivityItemService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    ActivityItemEntity queryObject(Integer id);
    ActivityItemEntity queryObjectByItemNo(String itemNo);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<ActivityItemEntity> queryList(Map<String, Object> map);

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
     * @param activityItem 实体
     * @return 保存条数
     */
    int save(ActivityItemEntity activityItem);

    /**
     * 根据主键更新实体
     *
     * @param activityItem 实体
     * @return 更新条数
     */
    int update(ActivityItemEntity activityItem);

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
}
