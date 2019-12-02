package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiSmsLogMapper;
import com.platform.entity.SmsLogEntity;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-24 22:57:12
 */
@Service
public class ApiSmsLogService {
    @Autowired
    private ApiSmsLogMapper smsLogDao;

    public SmsLogEntity queryObject(Integer id) {
        return smsLogDao.queryObject(id);
    }

    public List<SmsLogEntity> queryList(Map<String, Object> map) {
        return smsLogDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return smsLogDao.queryTotal(map);
    }

    public int save(SmsLogEntity smsLog) {
        return smsLogDao.save(smsLog);
    }

    public int update(SmsLogEntity smsLog) {
        return smsLogDao.update(smsLog);
    }

    public int delete(Integer id) {
        return smsLogDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return smsLogDao.deleteBatch(ids);
    }
}
