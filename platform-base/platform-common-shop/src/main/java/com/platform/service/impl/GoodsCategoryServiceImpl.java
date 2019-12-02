package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.GoodsCategoryDao;
import com.platform.entity.GoodsCategoryEntity;
import com.platform.service.GoodsCategoryService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:08
 */
@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;

    @Override
    public GoodsCategoryEntity queryObject(Integer id) {
        return goodsCategoryDao.queryObject(id);
    }

    @Override
    public List<GoodsCategoryEntity> queryList(Map<String, Object> map) {
        return goodsCategoryDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsCategoryDao.queryTotal(map);
    }

    @Override
    public int save(GoodsCategoryEntity goodsCategory) {
        return goodsCategoryDao.save(goodsCategory);
    }

    @Override
    public int update(GoodsCategoryEntity goodsCategory) {
        return goodsCategoryDao.update(goodsCategory);
    }

    @Override
    public int delete(Integer id) {
        return goodsCategoryDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
    	goodsCategoryDao.deleteByParentBatch(ids);
        return goodsCategoryDao.deleteBatch(ids);
    }
}
