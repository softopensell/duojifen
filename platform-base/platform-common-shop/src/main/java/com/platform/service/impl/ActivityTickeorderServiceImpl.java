package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.ActivityTickeorderDao;
import com.platform.entity.ActivityTickeorderEntity;
import com.platform.service.ActivityTickeorderService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@Service("activityTickeorderService")
public class ActivityTickeorderServiceImpl implements ActivityTickeorderService {
    @Autowired
    private ActivityTickeorderDao activityTickeorderDao;

    @Override
    public ActivityTickeorderEntity queryObject(Integer id) {
        return activityTickeorderDao.queryObject(id);
    }

    @Override
    public List<ActivityTickeorderEntity> queryList(Map<String, Object> map) {
        return activityTickeorderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return activityTickeorderDao.queryTotal(map);
    }

    @Override
    public int save(ActivityTickeorderEntity activityTickeorder) {
        return activityTickeorderDao.save(activityTickeorder);
    }

    @Override
    public int update(ActivityTickeorderEntity activityTickeorder) {
        return activityTickeorderDao.update(activityTickeorder);
    }

    @Override
    public int delete(Integer id) {
        return activityTickeorderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return activityTickeorderDao.deleteBatch(ids);
    }
}
