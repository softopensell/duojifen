package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.ActivitySponsorDao;
import com.platform.entity.ActivitySponsorEntity;
import com.platform.service.ActivitySponsorService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:45
 */
@Service("activitySponsorService")
public class ActivitySponsorServiceImpl implements ActivitySponsorService {
    @Autowired
    private ActivitySponsorDao activitySponsorDao;

    @Override
    public ActivitySponsorEntity queryObject(Integer id) {
        return activitySponsorDao.queryObject(id);
    }

    @Override
    public List<ActivitySponsorEntity> queryList(Map<String, Object> map) {
        return activitySponsorDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return activitySponsorDao.queryTotal(map);
    }

    @Override
    public int save(ActivitySponsorEntity activitySponsor) {
        return activitySponsorDao.save(activitySponsor);
    }

    @Override
    public int update(ActivitySponsorEntity activitySponsor) {
        return activitySponsorDao.update(activitySponsor);
    }

    @Override
    public int delete(Integer id) {
        return activitySponsorDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return activitySponsorDao.deleteBatch(ids);
    }
}
