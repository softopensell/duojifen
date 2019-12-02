package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.GoodsOrderDetailDao;
import com.platform.entity.GoodsOrderDetailEntity;
import com.platform.service.GoodsOrderDetailService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 15:10:15
 */
@Service("goodsOrderDetailService")
public class GoodsOrderDetailServiceImpl implements GoodsOrderDetailService {
    @Autowired
    private GoodsOrderDetailDao goodsOrderDetailDao;

    @Override
    public GoodsOrderDetailEntity queryObject(Integer id) {
        return goodsOrderDetailDao.queryObject(id);
    }

    @Override
    public List<GoodsOrderDetailEntity> queryList(Map<String, Object> map) {
        return goodsOrderDetailDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsOrderDetailDao.queryTotal(map);
    }

    @Override
    public int save(GoodsOrderDetailEntity goodsOrderDetail) {
        return goodsOrderDetailDao.save(goodsOrderDetail);
    }

    @Override
    public int update(GoodsOrderDetailEntity goodsOrderDetail) {
        return goodsOrderDetailDao.update(goodsOrderDetail);
    }

    @Override
    public int delete(Integer id) {
        return goodsOrderDetailDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return goodsOrderDetailDao.deleteBatch(ids);
    }
}
