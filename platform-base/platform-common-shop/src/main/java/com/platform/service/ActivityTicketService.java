package com.platform.service;

import com.platform.entity.ActivityTicketEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
public interface ActivityTicketService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    ActivityTicketEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<ActivityTicketEntity> queryList(Map<String, Object> map);

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
     * @param activityTicket 实体
     * @return 保存条数
     */
    int save(ActivityTicketEntity activityTicket);

    /**
     * 根据主键更新实体
     *
     * @param activityTicket 实体
     * @return 更新条数
     */
    int update(ActivityTicketEntity activityTicket);

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
