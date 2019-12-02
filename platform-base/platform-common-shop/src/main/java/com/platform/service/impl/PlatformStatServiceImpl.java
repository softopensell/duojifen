package com.platform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.platform.dao.PlatformStatDao;
import com.platform.entity.PlatformStatEntity;
import com.platform.service.PlatformStatService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-05 01:17:12
 */
@Service("platformStatService")
public class PlatformStatServiceImpl implements PlatformStatService {
    @Autowired
    private PlatformStatDao platformStatDao;
    @Resource
   	private JdbcTemplate jdbcTemplate;
    @Override
    public PlatformStatEntity queryObject(Integer id) {
        return platformStatDao.queryObject(id);
    }

    @Override
    public List<PlatformStatEntity> queryList(Map<String, Object> map) {
        return platformStatDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return platformStatDao.queryTotal(map);
    }

    @Override
    public int save(PlatformStatEntity platformStat) {
        return platformStatDao.save(platformStat);
    }

    @Override
    public int update(PlatformStatEntity platformStat) {
        return platformStatDao.update(platformStat);
    }

    @Override
    public int delete(Integer id) {
        return platformStatDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return platformStatDao.deleteBatch(ids);
    }
}
