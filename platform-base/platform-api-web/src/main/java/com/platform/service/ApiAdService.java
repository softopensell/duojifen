package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiAdMapper;
import com.platform.entity.AdVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-22 18:15:00
 */
@Service
public class ApiAdService {
    @Autowired
    private ApiAdMapper adDao;

    public AdVo queryObject(Integer id) {
        return adDao.queryObject(id);
    }

    public List<AdVo> queryList(Map<String, Object> map) {
        return adDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return adDao.queryTotal(map);
    }

    public int save(AdVo ad) {
        return adDao.save(ad);
    }

    public int update(AdVo ad) {
        return adDao.update(ad);
    }

    public int delete(Integer id) {
        return adDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return adDao.deleteBatch(ids);
    }
}
