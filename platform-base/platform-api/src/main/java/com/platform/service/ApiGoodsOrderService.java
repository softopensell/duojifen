package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.platform.dao.ApiAddressMapper;
import com.platform.dao.ApiGoodsCartMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiGoodsOrderDetailMapper;
import com.platform.dao.ApiGoodsOrderMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.GoodsCartVo;
import com.platform.entity.GoodsOrderDetailVo;
import com.platform.entity.GoodsOrderVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.UserVo;
import com.platform.util.CommonUtil;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.util.constants.PayStatusConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.utils.MoneyFormatUtils;
import com.platform.validator.ApiAssert;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 01:05:05
 */
@Service
public class ApiGoodsOrderService {
    @Autowired
    private ApiGoodsOrderMapper goodsOrderMapper;
    @Autowired
    private ApiGoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private ApiGoodsCartMapper goodsCartMapper;
    @Autowired
    private ApiGoodsMapper goodsMapper;
    @Autowired
    private ApiAddressMapper addressMapper;

    public GoodsOrderVo queryObject(Integer id) {
        return goodsOrderMapper.queryObject(id);
    }
    public GoodsOrderVo queryObjectByNo(String orderNo) {
    	return goodsOrderMapper.queryObjectByNo(orderNo);
    }

    public List<GoodsOrderVo> queryList(Map<String, Object> map) {
        return goodsOrderMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return goodsOrderMapper.queryTotal(map);
    }

    public int save(GoodsOrderVo goodsOrder) {
        return goodsOrderMapper.save(goodsOrder);
    }

    public int update(GoodsOrderVo goodsOrder) {
        return goodsOrderMapper.update(goodsOrder);
    }

    public int delete(Integer id) {
        return goodsOrderMapper.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return goodsOrderMapper.deleteBatch(ids);
    }
    
    @Transactional
  	public Map<String, Object> submit(JSONObject jsonParam, UserVo loginUser) {
  		Map<String, Object> resultObj = new HashMap<String, Object>();
  		String type = jsonParam.getString("type");
  		ApiAssert.isNull(type, "提交类型不能为空");
  		Integer payType = jsonParam.getInteger("payType");
  		Integer addressId = jsonParam.getInteger("addressId");
  		Integer goods_id = jsonParam.getInteger("goods_id");
  		ApiAssert.isNull(addressId, "收货人地址ID不能为空");
  		AddressVo addressVo = addressMapper.queryObject(addressId);
  		// * 获取要购买的商品
  		List<GoodsCartVo> checkedGoodsList = new ArrayList<>();
  		BigDecimal goodsTotalPrice = new BigDecimal(0.00);// 商品总价
  		Integer totalGoodsCount = 0;
  		if (type.equals("cart")) {
  			Map<String, Object> param = new HashMap<String, Object>();
  			param.put("user_id", loginUser.getUserId());
  			param.put("checked", 1);
  			checkedGoodsList = goodsCartMapper.queryList(param);
  			if (null == checkedGoodsList) {
  				resultObj.put("errno", 400);
  				resultObj.put("errmsg", "请选择商品");
  				return resultObj;
  			}
  			// 统计商品总价
  			for (GoodsCartVo cartItem : checkedGoodsList) {
  				goodsTotalPrice = goodsTotalPrice.add(MoneyFormatUtils.getMultiply(cartItem.getGoodsPrice(),new BigDecimal(cartItem.getGoodsCount())));
  				totalGoodsCount = totalGoodsCount +	cartItem.getGoodsCount();
  			}
  			BigDecimal totalIntegralPrice =  jsonParam.getBigDecimal("totalIntegralPrice");
  			if(payType.equals(OrderConstant.ORDER_PAY_TYPE_ONLINE_INTEGRAL)){
  				goodsTotalPrice = totalIntegralPrice;
  			}
  		}else{
  			if(goods_id!=null){//默认一件固定商品
  				GoodsVo goodsVo = goodsMapper.queryObject(goods_id);
  				goodsTotalPrice =goodsVo.getRetailPrice();
  				totalGoodsCount = 1;
  				GoodsCartVo cartVo = new GoodsCartVo();
  				 //店铺ID
  			  cartVo.setShopId(goodsVo.getShopId());
  			    //用户名称
  			  cartVo.setUserId(loginUser.getUserId());
  			    //商品id
  			  cartVo.setGoodsId(goods_id);
  			    //商品名称 
  			  cartVo.setGoodsName(goodsVo.getName());
  			    //商品封面图片
  			  cartVo.setGoodsImgUrl(goodsVo.getImgUrl());
  			    //商品列表图片
  			  cartVo.setGoodsListImgUrl(goodsVo.getImgListUrl());
  			    //商品规格
  			  cartVo.setSpecification(goodsVo.getSpecification());

  				//商品单价
  			  cartVo.setGoodsPrice(goodsVo.getRetailPrice());
  			    //goods表中的零售价格
  			  cartVo.setRetailGoodsPrice(goodsVo.getRetailPrice());
  			    //小计总价格 
  			  cartVo.setTotalPrice(goodsVo.getRetailPrice());
  			    //数量
  			  cartVo.setGoodsCount(1);
  			    //选中状态: 0 未选中 1 选中
  			  cartVo.setChecked(1);
  			    //创建时间
  			  cartVo.setCreateTime(new Date());
  			  checkedGoodsList.add(cartVo);
  			}
  		}
  		// 订单价格计算
  		Long currentTime = System.currentTimeMillis() / 1000;
  		String orderNo = CommonUtil.generateOrderNumber();
  		GoodsOrderVo orderInfo = new GoodsOrderVo();
  		orderInfo.setOrderNo(orderNo);
  		orderInfo.setUserId(loginUser.getUserId());
  		// 收货地址和运费
  		//订单状态：	1待付款、2待发货、3待收货、4已完成、  5、用户主动取消   6 超时未支付取消7、审核员取消订单8、退货9、超时确认收货
  		orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_CONFRIM);
  		orderInfo.setPayStatus(TradeStatus.WAIT_BUYER_PAY.code);
  		// //收货信息ID
  		orderInfo.setAddressId(addressId);
  		// //支付订单号
  	    //支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
  		orderInfo.setPayType(payType);
  		  //店铺ID
  	     Integer shopId =  jsonParam.getInteger("shopId");
  	     orderInfo.setShopId(shopId);
  	    
  	    //买的类型 0 默认值为线上商城订单, 1线下手工录入订单
  	    orderInfo.setOrderType(OrderConstant.ORDER_TYPE_SHOPPING);
  	    //用户id
  	    //购买商品总数量
  	  orderInfo.setGoodsCount(totalGoodsCount);
  	    //快递费用
  	    BigDecimal expressPrice =  new BigDecimal(0);
  	  	 expressPrice =  jsonParam.getBigDecimal("expressPrice");
  	  	orderInfo.setExpressPrice(expressPrice);
  	    //使用积分数
  	    BigDecimal totalIntegralNum =  new BigDecimal(0);
  	     totalIntegralNum =  jsonParam.getBigDecimal("totalIntegralNum");
  	    orderInfo.setTotalIntegralNum(totalIntegralNum);
  	    //使用积分抵扣价
  	    BigDecimal totalIntegralPrice =  new BigDecimal(0);
  	    totalIntegralPrice =  jsonParam.getBigDecimal("totalIntegralPrice");
  	  orderInfo.setTotalIntegralPrice(totalIntegralPrice);
  	    //在线支付总费用
  	    orderInfo.setTotalPayPrice(goodsTotalPrice);
  	    //总价
  	  orderInfo.setTotalPrice(goodsTotalPrice);
  	    //默认关联地址
  	  orderInfo.setAddressId(addressId);;
  	    //省
  	  orderInfo.setProvinceId(addressVo.getProvinceId());
  	    //市
  	  orderInfo.setCityId(addressVo.getCityId());
  	    //区
  	  orderInfo.setRegionId(addressVo.getRegionId());
  	    //地址
  	  orderInfo.setAddress(addressVo.getAddress());
  	    //总和地址
  	  orderInfo.setPcrdetail(addressVo.getPcrdetail());
  	    //电话
  	  orderInfo.setTelephone(addressVo.getContactMobile());
  	    //联系人
  	  orderInfo.setContactName(addressVo.getContactName());
  	    //邮编
  	  orderInfo.setZipcode(addressVo.getZipcode());
  		
  	  orderInfo.setCreateTime(new Date());

  		// 开启事务，插入订单信息和订单商品
  		int orderId = goodsOrderMapper.save(orderInfo);
  		if (null == orderInfo.getId()) {
  			resultObj.put("errno", 1);
  			resultObj.put("errmsg", "订单提交失败");
  			return resultObj;
  		}
  		// 统计商品总价
  		List<GoodsOrderDetailVo> orderGoodsData = new ArrayList<GoodsOrderDetailVo>();
  		for (GoodsCartVo goodsItem : checkedGoodsList) {
  			GoodsOrderDetailVo orderGoodsVo = new GoodsOrderDetailVo();
  			//订单Id
  			orderGoodsVo.setOrderId(orderId);
  			// 订单号
  			orderGoodsVo.setOrderNo(orderNo);
  			// //用户id
  			orderGoodsVo.setUserId(goodsItem.getUserId());
  			// //商品ID
  			orderGoodsVo.setGoodsId(goodsItem.getGoodsId());
  			//商品名称
  			orderGoodsVo.setGoodsName(goodsItem.getGoodsName());
  			
  		    //商品封面图片
  		    orderGoodsVo.setGoodsImgUrl(goodsItem.getGoodsImgUrl());
  		    //商品列表图片
	  		  orderGoodsVo.setGoodsListImgUrl(goodsItem.getGoodsListImgUrl());
	  		    //规格
	  		  orderGoodsVo.setSpecification(goodsItem.getSpecification());
	  		    //数量
	  		  orderGoodsVo.setGoodsCount(goodsItem.getGoodsCount());
	  		    //销售价格
	  		  orderGoodsVo.setPrice(goodsItem.getGoodsPrice());
	  		    //小计总价格 
	  		  orderGoodsVo.setTotalPrice(goodsItem.getTotalPrice());
  			orderGoodsData.add(orderGoodsVo);
  			goodsOrderDetailMapper.save(orderGoodsVo);
  			
  		}
  		

  		// 清空已购买的商品
  		goodsCartMapper.deleteByCart(loginUser.getUserId(), 1);
  		resultObj.put("errno", 0);
  		resultObj.put("errmsg", "订单提交成功");
  		//
  		Map<String, GoodsOrderVo> orderInfoMap = new HashMap<String, GoodsOrderVo>();
  		orderInfo.setOrderDetailList(orderGoodsData);
  		orderInfoMap.put("orderInfo", orderInfo);
  		//
  		resultObj.put("data", orderInfoMap);
  		
  		return resultObj;
  	
  }
}
