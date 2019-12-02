package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiAdPositionMapper;
import com.platform.entity.AdPositionVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-24 22:57:12
 */
@Service
public class ApiAdPositionService {
    @Autowired
    private ApiAdPositionMapper adPositionDao;

    public AdPositionVo queryObject(Integer id) {
        return adPositionDao.queryObject(id);
    }

    public List<AdPositionVo> queryList(Map<String, Object> map) {
        return adPositionDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return adPositionDao.queryTotal(map);
    }

    public int save(AdPositionVo adPosition) {
        return adPositionDao.save(adPosition);
    }

    public int update(AdPositionVo adPosition) {
        return adPositionDao.update(adPosition);
    }

    public int delete(Integer id) {
        return adPositionDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return adPositionDao.deleteBatch(ids);
    }
}
