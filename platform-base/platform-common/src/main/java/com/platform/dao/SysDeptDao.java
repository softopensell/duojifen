package com.platform.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.platform.entity.SysDeptEntity;
import com.platform.entity.UserWindowDto;

/**
 * 部门管理
 *
 * @author liepngjun
 * @email softopensell@outlook.com
 * @date 2017-09-17 23:58:47
 */
@Mapper
public interface SysDeptDao extends BaseDao<SysDeptEntity> {

    /**
     * 查询子部门ID列表
     *
     * @param parentId 上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);


    /**
     * 根据实体条件查询
     *
     * @return
     */
    List<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto);
}
