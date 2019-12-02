package com.platform.service;

import com.platform.entity.PlatformFwManagerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Hash;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
public interface PlatformFwManagerService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    PlatformFwManagerEntity queryObject(Integer id);
    PlatformFwManagerEntity queryByUserId(Integer userId);
    

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<PlatformFwManagerEntity> queryList(Map<String, Object> map);
    HashMap<Integer,PlatformFwManagerEntity> queryAll();

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
     * @param platformFwManager 实体
     * @return 保存条数
     */
    int save(PlatformFwManagerEntity platformFwManager);

    /**
     * 根据主键更新实体
     *
     * @param platformFwManager 实体
     * @return 更新条数
     */
    int update(PlatformFwManagerEntity platformFwManager);

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
