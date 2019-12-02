package com.platform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.GoodsCartDao;
import com.platform.entity.GoodsCartEntity;
import com.platform.service.GoodsCartService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-13 10:44:41
 */
@Service("goodsCartService")
public class GoodsCartServiceImpl implements GoodsCartService {
    @Autowired
    private GoodsCartDao goodsCartDao;

    @Override
    public GoodsCartEntity queryObject(Integer id) {
        return goodsCartDao.queryObject(id);
    }

    @Override
    public List<GoodsCartEntity> queryList(Map<String, Object> map) {
        return goodsCartDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsCartDao.queryTotal(map);
    }

    @Override
    public int save(GoodsCartEntity goodsCart) {
        return goodsCartDao.save(goodsCart);
    }

    @Override
    public int update(GoodsCartEntity goodsCart) {
        return goodsCartDao.update(goodsCart);
    }

    @Override
    public int delete(Integer id) {
        return goodsCartDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return goodsCartDao.deleteBatch(ids);
    }
    

    @Override
    public void updateCheck(String[] productIds, Integer isChecked, Integer userId) {
    	goodsCartDao.updateCheck(productIds, isChecked, userId);
        // 判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("user_id", userId);
        List<GoodsCartEntity> cartInfoList = goodsCartDao.queryList(cartParam);
        Map crashParam = new HashMap();
        List<Integer> goods_ids = new ArrayList();
        List<GoodsCartEntity> cartUpdateList = new ArrayList();
        for (GoodsCartEntity cartItem : cartInfoList) {
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                goods_ids.add(cartItem.getGoodsId());
            }
            if (!cartItem.getGoodsPrice().equals(cartItem.getRetailGoodsPrice())) {
                cartItem.setGoodsPrice(cartItem.getRetailGoodsPrice());
                cartUpdateList.add(cartItem);
            }
        }
        if (null != goods_ids && goods_ids.size() > 0) {
            crashParam.put("goods_ids", goods_ids);
            for (GoodsCartEntity cartItem : cartInfoList) {
                // 存在原始的
                if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                    for (GoodsCartEntity cartCrash : cartInfoList) {
                        if (null != cartItem.getChecked() && 1 == cartItem.getChecked() && !cartCrash.getId().equals(cartItem.getId())) {
                            cartUpdateList.add(cartCrash);
                            break;
                        }
                    }
                }
            }
        }
        if (null != cartUpdateList && cartUpdateList.size() > 0) {
            for (GoodsCartEntity cartItem : cartUpdateList) {
                goodsCartDao.update(cartItem);
            }
        }
    }
    @Override
    public void deleteByProductIds(String[] productIds) {
        goodsCartDao.deleteByProductIds(productIds);
    }
    @Override
    public void deleteByUserAndProductIds(Integer userId, String[] productIds) {
        goodsCartDao.deleteByUserAndProductIds(userId, productIds);
    }
    @Override
    public void deleteByCart(Integer user_id,   Integer checked) {
        goodsCartDao.deleteByCart(user_id,   checked);
    }
}
