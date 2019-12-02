package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.entity.PlatformWithdrawShareEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
public interface PlatformWithdrawShareService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    PlatformWithdrawShareEntity queryObject(Integer id);
    PlatformWithdrawShareEntity queryByUserId(Integer userId);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<PlatformWithdrawShareEntity> queryList(Map<String, Object> map);
    List<PlatformWithdrawShareEntity> queryByWithdrawTypeStar(Integer withdrawTypeStar);

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
     * @param platformWithdrawShare 实体
     * @return 保存条数
     */
    int save(PlatformWithdrawShareEntity platformWithdrawShare);

    /**
     * 根据主键更新实体
     *
     * @param platformWithdrawShare 实体
     * @return 更新条数
     */
    int update(PlatformWithdrawShareEntity platformWithdrawShare);

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
