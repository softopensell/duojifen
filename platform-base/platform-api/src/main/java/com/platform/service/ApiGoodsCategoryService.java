package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiGoodsCategoryMapper;
import com.platform.entity.GoodsCategoryVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:34
 */
@Service
public class ApiGoodsCategoryService {
    @Autowired
    private ApiGoodsCategoryMapper goodsCategoryDao;

    public GoodsCategoryVo queryObject(Integer id) {
        return goodsCategoryDao.queryObject(id);
    }

    public List<GoodsCategoryVo> queryList(Map<String, Object> map) {
        return goodsCategoryDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return goodsCategoryDao.queryTotal(map);
    }

    public int save(GoodsCategoryVo goodsCategory) {
        return goodsCategoryDao.save(goodsCategory);
    }

    public int update(GoodsCategoryVo goodsCategory) {
        return goodsCategoryDao.update(goodsCategory);
    }

    public int delete(Integer id) {
        return goodsCategoryDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return goodsCategoryDao.deleteBatch(ids);
    }
}
