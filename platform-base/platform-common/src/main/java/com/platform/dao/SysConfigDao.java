package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.SysConfigEntity;

/**
 * 系统配置信息
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2016年12月4日 下午6:46:16
 */
public interface SysConfigDao extends BaseDao<SysConfigEntity> {

    /**
     * 根据key，查询value
     */
    String queryByKey(String paramKey);

    /**
     * 根据key，更新value
     *
     * @param key
     * @param value
     * @return
     */
    int updateValueByKey(@Param("key") String key, @Param("value") String value);

    SysConfigEntity queryObjectByKey(@Param("key") String key);

}
