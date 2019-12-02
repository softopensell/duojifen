package com.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiGoodsCartMapper;
import com.platform.entity.GoodsCartVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:34
 */
@Service
public class ApiGoodsCartService {
    @Autowired
    private ApiGoodsCartMapper goodsCartDao;

    public GoodsCartVo queryObject(Integer id) {
        return goodsCartDao.queryObject(id);
    }

    public List<GoodsCartVo> queryList(Map<String, Object> map) {
        return goodsCartDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return goodsCartDao.queryTotal(map);
    }

    public int save(GoodsCartVo goodsCart) {
        return goodsCartDao.save(goodsCart);
    }

    public int update(GoodsCartVo goodsCart) {
        return goodsCartDao.update(goodsCart);
    }

    public int delete(Integer id) {
        return goodsCartDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return goodsCartDao.deleteBatch(ids);
    }
    

    public void updateCheck(String[] productIds, Integer isChecked, Integer userId) {
    	goodsCartDao.updateCheck(productIds, isChecked, userId);

        // 判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("user_id", userId);
        List<GoodsCartVo> cartInfoList = goodsCartDao.queryList(cartParam);
        Map crashParam = new HashMap();
        List<Integer> goods_ids = new ArrayList();
        List<GoodsCartVo> cartUpdateList = new ArrayList();
        for (GoodsCartVo cartItem : cartInfoList) {
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
            for (GoodsCartVo cartItem : cartInfoList) {
                // 存在原始的
                if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                    for (GoodsCartVo cartCrash : cartInfoList) {
                        if (null != cartItem.getChecked() && 1 == cartItem.getChecked() && !cartCrash.getId().equals(cartItem.getId())) {
                            cartUpdateList.add(cartCrash);
                            break;
                        }
                    }
                }
            }
        }
        if (null != cartUpdateList && cartUpdateList.size() > 0) {
            for (GoodsCartVo cartItem : cartUpdateList) {
                goodsCartDao.update(cartItem);
            }
        }
    }

    public void deleteByProductIds(String[] productIds) {
        goodsCartDao.deleteByProductIds(productIds);
    }

    public void deleteByUserAndProductIds(Integer userId, String[] productIds) {
        goodsCartDao.deleteByUserAndProductIds(userId, productIds);
    }

    public void deleteByCart(Integer user_id,   Integer checked) {
        goodsCartDao.deleteByCart(user_id,   checked);
    }
   
}
