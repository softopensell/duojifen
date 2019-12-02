package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.entity.SysRoleEntity;
import com.platform.entity.UserWindowDto;
import com.platform.page.Page;


/**
 * 角色
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2016年9月18日 上午9:42:52
 */
public interface SysRoleService {

    SysRoleEntity queryObject(Long roleId);

    List<SysRoleEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);

    /**
     * 分页查询角色审批选择范围
     * @return
     */
    Page<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto, int pageNmu);
}
