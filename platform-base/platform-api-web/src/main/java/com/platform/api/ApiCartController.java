package com.platform.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.entity.AddressVo;
import com.platform.entity.GoodsCartVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiAddressService;
import com.platform.service.ApiGoodsCartService;
import com.platform.service.ApiGoodsService;
import com.platform.util.ApiBaseAction;
import com.qiniu.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author softopensell <br>
 * 时间: 2019-03-25 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/api/cart")
public class ApiCartController extends ApiBaseAction {
    @Autowired
    private ApiGoodsCartService cartService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiAddressService addressService;

    /**
     * 获取购物车中的数据
     */
    @ApiOperation(value = "获取购物车中的数据")
    @PostMapping("getCart")
    public Object getCart(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap();
        //查询列表数据
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("checked_status", 1);
        List<GoodsCartVo> cartList = cartService.queryList(param);
        //获取购物车统计信息
        Integer goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0.00);
        Integer checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
        for (GoodsCartVo cartItem : cartList) {
            goodsCount += cartItem.getGoodsCount();
            goodsAmount = goodsAmount.add(cartItem.getGoodsPrice().multiply(new BigDecimal(cartItem.getGoodsCount())));
           
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                checkedGoodsCount += cartItem.getGoodsCount();
                checkedGoodsAmount = checkedGoodsAmount.add(cartItem.getGoodsPrice().multiply(new BigDecimal(cartItem.getGoodsCount())));
            }
        }
        resultObj.put("cartList", cartList);
        //
        Map<String, Object> cartTotal = new HashMap();
        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", goodsAmount);
        
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);
        //
        resultObj.put("cartTotal", cartTotal);
        return resultObj;
    }

    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     */
    @ApiOperation(value = "获取购物车信息")
    @PostMapping("index")
    public Object index(@LoginUser UserVo loginUser) {
        return toResponsSuccess(getCart(loginUser));
    }

    private String[] getSpecificationIdsArray(String ids) {
        String[] idsArray = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ids)) {
            String[] tempArray = ids.split("_");
            if (null != tempArray && tempArray.length > 0) {
                idsArray = tempArray;
            }
        }
        return idsArray;
    }

    /**
     * 添加商品到购物车
     */
    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("add")
    public Object add(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer goodsCount = jsonParam.getInteger("goodsCount");

        //判断商品是否可以购买
        GoodsVo goodsInfo = goodsService.queryObject(goodsId);
        if (null == goodsInfo || goodsInfo.getSellStatus() == 0 ) {
            return this.toResponsObject(400, "商品未上架", "");
        }
        //判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("goods_id", goodsId);
        cartParam.put("user_id", loginUser.getUserId());
        List<GoodsCartVo> cartInfoList = cartService.queryList(cartParam);
        GoodsCartVo cartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        if (null == cartInfo) {
            cartInfo = new GoodsCartVo();
            cartInfo.setGoodsCount(goodsCount);
            cartInfo.setChecked(1);
            //用户名称
            cartInfo.setUserId(loginUser.getUserId());
            //商品id
            cartInfo.setGoodsId(goodsId);
            //商品名称 
            cartInfo.setGoodsName(goodsInfo.getName());
            //商品封面图片
            cartInfo.setGoodsImgUrl(goodsInfo.getImages());
            //商品列表图片
            cartInfo.setGoodsListImgUrl(goodsInfo.getImgListUrl());
            //商品规格
            cartInfo.setSpecification(goodsInfo.getSpecification());
        	//商品单价
            cartInfo.setGoodsPrice(goodsInfo.getRetailPrice());
            //goods表中的零售价格
            cartInfo.setRetailGoodsPrice(goodsInfo.getRetailPrice());
            //小计总价格 
            cartInfo.setTotalPrice( (goodsInfo.getRetailPrice()).multiply(new BigDecimal(goodsCount)));
            //数量 
            cartInfo.setGoodsCount(goodsCount);
            //选中状态: 0 未选中 1 选中
            cartInfo.setChecked(1);
            //创建时间
            cartInfo.setCreateTime(new Date());
            cartService.save(cartInfo);
        } else {
            cartInfo.setGoodsCount(cartInfo.getGoodsCount()+goodsCount);
            cartInfo.setUserId(loginUser.getUserId());
            cartInfo.setGoodsId(goodsId);
            cartService.update(cartInfo);
        }
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 减少商品到购物车
     */
    @ApiOperation(value = "减少商品到购物车")
    @PostMapping("minus")
    public Object minus(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer goodsCount = jsonParam.getInteger("goodsCount");
        //判断购物车中是否存在此规格商品
        Map cartParam = new HashMap();
        cartParam.put("goods_id", goodsId);
        cartParam.put("user_id", loginUser.getUserId());
        List<GoodsCartVo> cartInfoList = cartService.queryList(cartParam);
        GoodsCartVo cartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        int cart_num = 0;
        if (null != cartInfo) {
            if (cartInfo.getGoodsCount() > goodsCount) {
                cartInfo.setGoodsCount(cartInfo.getGoodsCount() - goodsCount);
                cartService.update(cartInfo);
                cart_num = cartInfo.getGoodsCount();
            } else if (cartInfo.getGoodsCount() == 1) {
                cartService.delete(cartInfo.getId());
                cart_num = 0;
            }
        }
//        return toResponsSuccess(cart_num);
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 更新指定的购物车信息
     */
    @ApiOperation(value = "更新指定的购物车信息")
    @PostMapping("update")
    public Object update(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer goodsCount = jsonParam.getInteger("goodsCount");
        Integer id = jsonParam.getInteger("id");
        //判断是否已经存在product_id购物车商品
        GoodsCartVo cartInfo = cartService.queryObject(id);
        //只是更新number
        if (cartInfo.getGoodsId() == goodsId) {
            cartInfo.setGoodsCount(goodsCount);
            cartService.update(cartInfo);
            return toResponsSuccess(getCart(loginUser));
        }

        Map cartParam = new HashMap();
        cartParam.put("goodsId", goodsId);
        List<GoodsCartVo> cartInfoList = cartService.queryList(cartParam);
        GoodsCartVo newcartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        if (null == newcartInfo) {
        	 cartInfo = new GoodsCartVo();
             cartInfo.setGoodsId(goodsId);
             cartInfo.setGoodsCount(goodsCount);
             
            cartService.update(cartInfo);
        } else {
            //合并购物车已有的product信息，删除已有的数据
            cartService.delete(newcartInfo.getId());
            cartInfo.setGoodsId(goodsId);
            cartInfo.setGoodsCount(goodsCount);
            cartService.update(cartInfo);
        }
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 是否选择商品，如果已经选择，则取消选择，批量操作
     */
    @ApiOperation(value = "是否选择商品")
    @PostMapping("checked")
    public Object checked(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        String productIds = jsonParam.getString("goodsIds");
        Integer isChecked = jsonParam.getInteger("isChecked");
        if (StringUtils.isNullOrEmpty(productIds)) {
            return this.toResponsFail("删除出错");
        }
        String[] productIdArray = productIds.split(",");
        cartService.updateCheck(productIdArray, isChecked, loginUser.getUserId());
        return toResponsSuccess(getCart(loginUser));
    }

    //删除选中的购物车商品，批量删除
    @ApiOperation(value = "删除商品")
    @PostMapping("delete")
    public Object delete(@LoginUser UserVo loginUser) {
        Integer userId = loginUser.getUserId();

        JSONObject jsonObject = getJsonRequest();
        String goodsIds = jsonObject.getString("goodsIds");
        
        if (StringUtils.isNullOrEmpty(goodsIds)) {
            return toResponsFail("删除出错");
        }
        String[] productIdsArray = goodsIds.split(",");
        cartService.deleteByUserAndProductIds(userId, productIdsArray);

        return toResponsSuccess(getCart(loginUser));
    }

    //  获取购物车商品的总件件数
    @ApiOperation(value = "获取购物车商品的总件件数")
    @PostMapping("goodscount")
    public Object goodscount(@LoginUser UserVo loginUser) {
        if (null == loginUser || null == loginUser.getUserId()) {
            return toResponsFail("未登录");
        }
        Map<String, Object> resultObj = new HashMap();
        //查询列表数据
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        List<GoodsCartVo> cartList = cartService.queryList(param);
        //获取购物车统计信息
        Integer goodsTotalCount = 0;
        for (GoodsCartVo cartItem : cartList) {
        	goodsTotalCount += cartItem.getGoodsCount();
        }
        resultObj.put("cartList", cartList);
        //
        Map<String, Object> cartTotal = new HashMap();
        cartTotal.put("goodsTotalCount", goodsTotalCount);
        //
        resultObj.put("cartTotal", cartTotal);
        return toResponsSuccess(resultObj);
    }

    /**
     * 订单提交前的检验和填写相关订单信息
     */
    @ApiOperation(value = "订单提交前的检验和填写相关订单信息")
    @PostMapping("checkout")
    public Object checkout(@LoginUser UserVo loginUser, Integer couponId, @RequestParam(defaultValue = "cart") String type) {
        Map<String, Object> resultObj = new HashMap();
        //根据收货地址计算运费

        BigDecimal freightPrice = new BigDecimal(0.00);
        //默认收货地址
        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        List addressEntities = addressService.queryList(param);

        if (null == addressEntities || addressEntities.size() == 0) {
            resultObj.put("checkedAddress", new AddressVo());
        } else {
            resultObj.put("checkedAddress", addressEntities.get(0));
        }
        // * 获取要购买的商品和总价
        ArrayList checkedGoodsList = new ArrayList();
        BigDecimal goodsAmount;
        if (type.equals("cart")) {
            Map<String, Object> cartData = (Map<String, Object>) this.getCart(loginUser);

            for (GoodsCartVo cartEntity : (List<GoodsCartVo>) cartData.get("cartList")) {
                    checkedGoodsList.add(cartEntity);
            }
            goodsAmount = (BigDecimal) ((HashMap) cartData.get("cartTotal")).get("goodsAmount");
        } else { // 是直接购买的
        	GoodsCartVo GoodsCartVo = (GoodsCartVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "");
        	GoodsVo goodsVo = goodsService.queryObject(GoodsCartVo.getGoodsId());
            //计算订单的费用
            //商品总价
        	goodsAmount = goodsVo.getRetailPrice().multiply(new BigDecimal(GoodsCartVo.getGoodsCount()));

            GoodsCartVo cartVo = new GoodsCartVo();
            cartVo.setGoodsId(GoodsCartVo.getGoodsId());
            cartVo.setGoodsCount(GoodsCartVo.getGoodsCount());
            checkedGoodsList.add(cartVo);
        }
        //订单的总价
        BigDecimal orderTotalPrice = goodsAmount.add(freightPrice);
        resultObj.put("freightPrice", freightPrice);
        resultObj.put("checkedGoodsList", checkedGoodsList);
        resultObj.put("goodsTotalPrice", orderTotalPrice);
        resultObj.put("orderTotalPrice", orderTotalPrice);
        resultObj.put("actualPrice", orderTotalPrice);
        return toResponsSuccess(resultObj);
    }

}
