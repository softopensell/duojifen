package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiGoodsOrderDetailMapper;
import com.platform.entity.GoodsOrderDetailVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:33
 */
@Service
public class ApiGoodsOrderDetailService {
    @Autowired
    private ApiGoodsOrderDetailMapper goodsOrderDetailDao;

    public GoodsOrderDetailVo queryObject(Integer id) {
        return goodsOrderDetailDao.queryObject(id);
    }

    public List<GoodsOrderDetailVo> queryList(Map<String, Object> map) {
        return goodsOrderDetailDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return goodsOrderDetailDao.queryTotal(map);
    }

    public int save(GoodsOrderDetailVo goodsOrderDetail) {
        return goodsOrderDetailDao.save(goodsOrderDetail);
    }

    public int update(GoodsOrderDetailVo goodsOrderDetail) {
        return goodsOrderDetailDao.update(goodsOrderDetail);
    }

    public int delete(Integer id) {
        return goodsOrderDetailDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return goodsOrderDetailDao.deleteBatch(ids);
    }
}
