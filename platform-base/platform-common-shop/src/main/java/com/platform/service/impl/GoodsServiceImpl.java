package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.platform.dao.GoodsDao;
import com.platform.entity.GoodsEntity;
import com.platform.service.GoodsService;
import com.platform.util.ShopConstant;
import com.platform.utils.RRException;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:07
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public GoodsEntity queryObject(Integer id) {
        return goodsDao.queryObject(id);
    }

    @Override
    public List<GoodsEntity> queryList(Map<String, Object> map) {
        return goodsDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsDao.queryTotal(map);
    }

    @Override
    public int save(GoodsEntity goods) {
        return goodsDao.save(goods);
    }

    @Override
    public int update(GoodsEntity goods) {
        return goodsDao.update(goods);
    }

    @Override
    public int delete(Integer id) {
        return goodsDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return goodsDao.deleteBatch(ids);
    }
    @Override
    public int enSale(Integer id) {
    	GoodsEntity goodsEntity = queryObject(id);
        if (ShopConstant.GOODS_SELL_STATUS_UP == goodsEntity.getSellStatus()) {
            throw new RRException("此商品已处于上架状态！");
        }
        goodsEntity.setSellStatus(ShopConstant.GOODS_SELL_STATUS_UP);
        goodsEntity.setUpdatetime(new Date());
        return goodsDao.update(goodsEntity);
    }

    @Override
    public int unSale(Integer id) {
    	GoodsEntity goodsEntity = queryObject(id);
        if (ShopConstant.GOODS_SELL_STATUS_DOWN == goodsEntity.getSellStatus()) {
            throw new RRException("此商品已处于下架状态！");
        }
        goodsEntity.setSellStatus(ShopConstant.GOODS_SELL_STATUS_DOWN);
        goodsEntity.setUpdatetime(new Date());
        return goodsDao.update(goodsEntity);
    }
}
