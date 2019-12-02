package com.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ActivityItemDao;
import com.platform.entity.ActivityItemEntity;
import com.platform.service.ActivityItemService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@Service("activityItemService")
public class ActivityItemServiceImpl implements ActivityItemService {
    @Autowired
    private ActivityItemDao activityItemDao;

    @Override
    public ActivityItemEntity queryObject(Integer id) {
        return activityItemDao.queryObject(id);
    }
    @Override
    public ActivityItemEntity queryObjectByItemNo(String itemNo) {
    	return activityItemDao.queryObjectByItemNo(itemNo);
    }
    @Override
    public List<ActivityItemEntity> queryList(Map<String, Object> map) {
        return activityItemDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return activityItemDao.queryTotal(map);
    }

    @Override
    public int save(ActivityItemEntity activityItem) {
        return activityItemDao.save(activityItem);
    }

    @Override
    public int update(ActivityItemEntity activityItem) {
        return activityItemDao.update(activityItem);
    }

    @Override
    public int delete(Integer id) {
        return activityItemDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return activityItemDao.deleteBatch(ids);
    }
}
