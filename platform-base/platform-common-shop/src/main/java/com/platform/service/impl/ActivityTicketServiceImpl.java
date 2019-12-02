package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.ActivityTicketDao;
import com.platform.entity.ActivityTicketEntity;
import com.platform.service.ActivityTicketService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@Service("activityTicketService")
public class ActivityTicketServiceImpl implements ActivityTicketService {
    @Autowired
    private ActivityTicketDao activityTicketDao;

    @Override
    public ActivityTicketEntity queryObject(Integer id) {
        return activityTicketDao.queryObject(id);
    }

    @Override
    public List<ActivityTicketEntity> queryList(Map<String, Object> map) {
        return activityTicketDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return activityTicketDao.queryTotal(map);
    }

    @Override
    public int save(ActivityTicketEntity activityTicket) {
        return activityTicketDao.save(activityTicket);
    }

    @Override
    public int update(ActivityTicketEntity activityTicket) {
        return activityTicketDao.update(activityTicket);
    }

    @Override
    public int delete(Integer id) {
        return activityTicketDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return activityTicketDao.deleteBatch(ids);
    }
}
