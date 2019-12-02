package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.entity.SysLogEntity;

/**
 * 系统日志
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-03-08 10:40:56
 */
public interface SysLogService {

    SysLogEntity queryObject(Long id);

    List<SysLogEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(SysLogEntity sysLog);

    void update(SysLogEntity sysLog);

    void delete(Long id);

    void deleteBatch(Long[] ids);
}
