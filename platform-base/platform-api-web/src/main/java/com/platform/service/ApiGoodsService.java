package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiGoodsMapper;
import com.platform.entity.GoodsVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:34
 */
@Service
public class ApiGoodsService {
    @Autowired
    private ApiGoodsMapper goodsDao;

    public GoodsVo queryObject(Integer id) {
        return goodsDao.queryObject(id);
    }

    public List<GoodsVo> queryList(Map<String, Object> map) {
        return goodsDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return goodsDao.queryTotal(map);
    }

    public int save(GoodsVo goods) {
        return goodsDao.save(goods);
    }

    public int update(GoodsVo goods) {
        return goodsDao.update(goods);
    }

    public int delete(Integer id) {
        return goodsDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return goodsDao.deleteBatch(ids);
    }
    
    public List<GoodsVo> queryHotGoodsList(Map<String, Object> map) {
        return goodsDao.queryHotGoodsList(map);
    }

    public List<GoodsVo> queryCatalogProductList(Map<String, Object> map) {
        return goodsDao.queryCatalogProductList(map);
    }
}
