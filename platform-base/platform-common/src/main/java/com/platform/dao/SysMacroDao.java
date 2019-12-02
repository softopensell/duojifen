package com.platform.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.SysMacroEntity;

/**
 * 通用字典表Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-08-22 11:48:16
 */
public interface SysMacroDao extends BaseDao<SysMacroEntity> {

    /**
     * 查询数据字段
     *
     * @param value
     * @return
     */
    List<SysMacroEntity> queryMacrosByValue(@Param("value") String value);
    List<SysMacroEntity> queryMacrosByValueAndCode(@Param("value") String value,@Param("code") String code);
}
