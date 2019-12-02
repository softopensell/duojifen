package com.platform.dao;

import com.platform.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * 定时任务
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2016年12月1日 下午10:29:57
 */
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {

    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);
}
