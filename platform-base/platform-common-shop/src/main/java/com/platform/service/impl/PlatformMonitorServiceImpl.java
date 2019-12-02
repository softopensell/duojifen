package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.PlatformMonitorDao;
import com.platform.entity.PlatformMonitorEntity;
import com.platform.service.PlatformMonitorService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 20:50:37
 */
@Service("platformMonitorService")
public class PlatformMonitorServiceImpl implements PlatformMonitorService {
    @Autowired
    private PlatformMonitorDao platformMonitorDao;

    @Override
    public PlatformMonitorEntity queryObject(Integer id) {
        return platformMonitorDao.queryObject(id);
    }

    @Override
    public List<PlatformMonitorEntity> queryList(Map<String, Object> map) {
        return platformMonitorDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return platformMonitorDao.queryTotal(map);
    }

    @Override
    public int save(PlatformMonitorEntity platformMonitor) {
        return platformMonitorDao.save(platformMonitor);
    }

    @Override
    public int update(PlatformMonitorEntity platformMonitor) {
        return platformMonitorDao.update(platformMonitor);
    }

    @Override
    public int delete(Integer id) {
        return platformMonitorDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return platformMonitorDao.deleteBatch(ids);
    }
}
