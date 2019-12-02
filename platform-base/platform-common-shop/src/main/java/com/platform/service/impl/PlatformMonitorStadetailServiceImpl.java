package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.PlatformMonitorStadetailDao;
import com.platform.entity.PlatformMonitorStadetailEntity;
import com.platform.service.PlatformMonitorStadetailService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 20:50:37
 */
@Service("platformMonitorStadetailService")
public class PlatformMonitorStadetailServiceImpl implements PlatformMonitorStadetailService {
    @Autowired
    private PlatformMonitorStadetailDao platformMonitorStadetailDao;

    @Override
    public PlatformMonitorStadetailEntity queryObject(Integer id) {
        return platformMonitorStadetailDao.queryObject(id);
    }

    @Override
    public List<PlatformMonitorStadetailEntity> queryList(Map<String, Object> map) {
        return platformMonitorStadetailDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return platformMonitorStadetailDao.queryTotal(map);
    }

    @Override
    public int save(PlatformMonitorStadetailEntity platformMonitorStadetail) {
        return platformMonitorStadetailDao.save(platformMonitorStadetail);
    }

    @Override
    public int update(PlatformMonitorStadetailEntity platformMonitorStadetail) {
        return platformMonitorStadetailDao.update(platformMonitorStadetail);
    }

    @Override
    public int delete(Integer id) {
        return platformMonitorStadetailDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return platformMonitorStadetailDao.deleteBatch(ids);
    }
}
