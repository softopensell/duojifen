package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.ActivityJoinDao;
import com.platform.entity.ActivityJoinEntity;
import com.platform.service.ActivityJoinService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@Service("activityJoinService")
public class ActivityJoinServiceImpl implements ActivityJoinService {
    @Autowired
    private ActivityJoinDao activityJoinDao;

    @Override
    public ActivityJoinEntity queryObject(Integer id) {
        return activityJoinDao.queryObject(id);
    }

    @Override
    public List<ActivityJoinEntity> queryList(Map<String, Object> map) {
        return activityJoinDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return activityJoinDao.queryTotal(map);
    }

    @Override
    public int save(ActivityJoinEntity activityJoin) {
        return activityJoinDao.save(activityJoin);
    }

    @Override
    public int update(ActivityJoinEntity activityJoin) {
        return activityJoinDao.update(activityJoin);
    }

    @Override
    public int delete(Integer id) {
        return activityJoinDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return activityJoinDao.deleteBatch(ids);
    }
}
