package com.platform.service.impl;

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
import com.platform.dao.AddressDao;
import com.platform.dao.GoodsCartDao;
import com.platform.dao.GoodsDao;
import com.platform.dao.GoodsOrderDao;
import com.platform.dao.GoodsOrderDetailDao;
import com.platform.dao.UserDao;
import com.platform.dao.UserInvestLevelDao;
import com.platform.entity.AddressEntity;
import com.platform.entity.GoodsCartEntity;
import com.platform.entity.GoodsEntity;
import com.platform.entity.GoodsOrderDetailEntity;
import com.platform.entity.GoodsOrderEntity;
import com.platform.entity.UserEntity;
import com.platform.service.GoodsOrderService;
import com.platform.service.UserInvestLevelService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.CommonUtil;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.R;
import com.platform.validator.ApiAssert;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 15:10:15
 */
@Service("goodsOrderService")
public class GoodsOrderServiceImpl implements GoodsOrderService {
    @Autowired
    private GoodsOrderDao goodsOrderDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private GoodsCartDao goodsCartDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInvestLevelDao userInvestLevelDao;
    @Autowired
    private GoodsOrderDetailDao goodsOrderDetailDao;

    @Override
    public GoodsOrderEntity queryObject(Integer id) {
        return goodsOrderDao.queryObject(id);
    }
    @Override
    public GoodsOrderEntity queryObjectByNo(String orderNo) {
    	return goodsOrderDao.queryObjectByNo(orderNo);
    }
    @Override
    public List<GoodsOrderEntity> queryList(Map<String, Object> map) {
        return goodsOrderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsOrderDao.queryTotal(map);
    }

    @Override
    public int save(GoodsOrderEntity goodsOrder) {
        return goodsOrderDao.save(goodsOrder);
    }

    @Override
    public int update(GoodsOrderEntity goodsOrder) {
        return goodsOrderDao.update(goodsOrder);
    }

    @Override
    public int delete(Integer id) {
        return goodsOrderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return goodsOrderDao.deleteBatch(ids);
    }
    
    @Override
	public int queryTotalByStatus(Integer orderStatus) {
		return goodsOrderDao.queryTotalByStatus(orderStatus);
	}

	@Override
	public Map<String, Object> queryTotalByDate(Map<String, Object> map) {
		return goodsOrderDao.queryTotalByDate(map);
	}
	
	@Override
	public Map<String, Object> queryTotalPrice(Map<String, Object> map) {
		return goodsOrderDao.queryTotalPrice(map);
	}
	
	@Transactional
  	public Map<String, Object> submit(JSONObject jsonParam, int userId) {
  		Map<String, Object> resultObj = new HashMap<String, Object>();
  		String type = jsonParam.getString("type");
  		ApiAssert.isNull(type, "提交类型不能为空");
  		Integer payType = jsonParam.getInteger("payType");
  		Integer addressId = jsonParam.getInteger("addressId");
  		Integer goods_id = jsonParam.getInteger("goods_id");
  		ApiAssert.isNull(addressId, "收货人地址ID不能为空");
  		AddressEntity addressVo = addressDao.queryObject(addressId);
  		// * 获取要购买的商品
  		List<GoodsCartEntity> checkedGoodsList = new ArrayList<>();
  		BigDecimal goodsTotalPrice = new BigDecimal(0.00);// 商品总价
  		Integer totalGoodsCount = 0;
  		if (type.equals("cart")) {
  			Map<String, Object> param = new HashMap<String, Object>();
  			param.put("user_id", userId);
  			param.put("checked", 1);
  			checkedGoodsList = goodsCartDao.queryList(param);
  			if (null == checkedGoodsList) {
  				resultObj.put("errno", 400);
  				resultObj.put("errmsg", "请选择商品");
  				return resultObj;
  			}
  			// 统计商品总价
  			for (GoodsCartEntity cartItem : checkedGoodsList) {
//  				goodsTotalPrice = goodsTotalPrice.add(cartItem.getGoodsPrice().multiply(new BigDecimal(cartItem.getGoodsCount())));
  				goodsTotalPrice = goodsTotalPrice.add(MoneyFormatUtils.getMultiply(cartItem.getGoodsPrice(), cartItem.getGoodsCount()));
  				totalGoodsCount = totalGoodsCount +	cartItem.getGoodsCount();
  			}
  			BigDecimal totalIntegralPrice =  jsonParam.getBigDecimal("totalIntegralPrice");
  			if(payType.equals(OrderConstant.ORDER_PAY_TYPE_ONLINE_INTEGRAL)){
  				goodsTotalPrice = totalIntegralPrice;
  			}
  		}else{
  			if(goods_id!=null){//默认一件固定商品
  				GoodsEntity goodsVo = goodsDao.queryObject(goods_id);
  				goodsTotalPrice =goodsVo.getRetailPrice();
  				totalGoodsCount = 1;
  				GoodsCartEntity cartVo = new GoodsCartEntity();
  				 //店铺ID
  			  cartVo.setShopId(goodsVo.getShopId());
  			    //用户名称
  			  cartVo.setUserId(userId);
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
  		GoodsOrderEntity orderInfo = new GoodsOrderEntity();
  		orderInfo.setOrderNo(orderNo);
  		orderInfo.setUserId(userId);
  		// 收货地址和运费
  		//订单状态：	1待付款、2待发货、3待收货、4已完成、  5、用户主动取消   6 超时未支付取消7、审核员取消订单8、退货9、超时确认收货
  		orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_NOPAY);
  		//支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
  		orderInfo.setPayStatus(ShopConstant.PAY_STATUS_NOPAY);
  		// //收货信息ID
  		orderInfo.setAddressId(addressId);
  		// //支付订单号
  	    //支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
  		orderInfo.setPayType(payType);
  		  //店铺ID
  	     Integer shopId =  jsonParam.getInteger("shopId");
  	     orderInfo.setShopId(shopId);
  	    //买的类型 0 默认值为线上商城订单, 1线下手工录入订单
  	    orderInfo.setOrderType(0);
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
  		int orderId = goodsOrderDao.save(orderInfo);
  		if (null == orderInfo.getId()) {
  			resultObj.put("errno", 1);
  			resultObj.put("errmsg", "订单提交失败");
  			return resultObj;
  		}
  		// 统计商品总价
  		List<GoodsOrderDetailEntity> orderGoodsData = new ArrayList<GoodsOrderDetailEntity>();
  		for (GoodsCartEntity goodsItem : checkedGoodsList) {
  			GoodsOrderDetailEntity orderGoodsVo = new GoodsOrderDetailEntity();
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
  			goodsOrderDetailDao.save(orderGoodsVo);
  		}
  		// 清空已购买的商品
  		goodsCartDao.deleteByCart(userId, 1);
  		resultObj.put("errno", 0);
  		resultObj.put("errmsg", "订单提交成功");
  		Map<String, GoodsOrderEntity> orderInfoMap = new HashMap<String, GoodsOrderEntity>();
  		orderInfo.setGoodsOrderDetailEntityList(orderGoodsData);
  		orderInfoMap.put("orderInfo", orderInfo);
  		resultObj.put("data", orderInfoMap);
  		return resultObj;
   }
	@Transactional
	public Map<String, Object> toSubmitImmediateBuyGoodOrder(UserEntity userEntity,int goodId,int sum,int payType,String goodTags){
		Map<String, Object> resultObj = new HashMap<String, Object>();
		ApiAssert.isNull(userEntity, "用户不能为空");
		// * 获取要购买的商品
		List<GoodsCartEntity> checkedGoodsList = new ArrayList<>();
		BigDecimal goodsTotalPrice = new BigDecimal(0.00);// 商品总价
		Integer totalGoodsCount = sum;
		GoodsEntity goodsVo = goodsDao.queryObject(goodId);
		
		goodsTotalPrice =MoneyFormatUtils.getMultiply(goodsVo.getRetailPrice(), totalGoodsCount);
		
		if(payType==ShopConstant.PAY_TYPE_YE) {//用户自己支付 就要验证余额
		//判断推荐人 金额扣除 是否满足
		  if(userEntity.getBalance().compareTo(goodsTotalPrice)<0) {
			  return R.error("余额不足");
		  }
		}else if(payType==ShopConstant.PAY_TYPE_JF) {//用户自己支付 积分验证
			//判断推荐人 金额扣除 是否满足
			  if(userEntity.getIntegralScore()<(goodsTotalPrice.intValue())) {
				  return R.error("积分不足");
			  }
		}

    if(totalGoodsCount<=0){
      return R.error("购买数量不能小于1");
    }
		
		GoodsCartEntity cartVo = new GoodsCartEntity();
		//店铺ID
		cartVo.setShopId(goodsVo.getShopId());
		//用户名称
		cartVo.setUserId(userEntity.getUserId());
		//商品id
		cartVo.setGoodsId(goodsVo.getId());
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
		cartVo.setTotalPrice(goodsTotalPrice);
		//数量
		cartVo.setGoodsCount(totalGoodsCount);
		//选中状态: 0 未选中 1 选中
		cartVo.setChecked(1);
		//创建时间
		cartVo.setCreateTime(new Date());
		checkedGoodsList.add(cartVo);
		// 订单价格计算
		String orderNo = CommonUtil.generateOrderNumber();
		GoodsOrderEntity orderInfo = new GoodsOrderEntity();
		orderInfo.setOrderNo(orderNo);
		orderInfo.setUserId(userEntity.getUserId());
		// 收货地址和运费
		//订单状态：	1待付款、2待发货、3待收货、4已完成、  5、用户主动取消   6 超时未支付取消7、审核员取消订单8、退货9、超时确认收货
		orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_NOSHIPMENT);
		//支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
		orderInfo.setPayStatus(ShopConstant.PAY_STATUS_PAYOK);
		// //收货信息ID
		orderInfo.setAddressId(-1);
		// //支付订单号
		//支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
	    orderInfo.setPayType(payType);
		//店铺ID
		//买的类型 0 默认值为线上商城订单, 1线下手工录入订单
		orderInfo.setOrderType(OrderConstant.ORDER_TYPE_INVERST);//投资类订单
		//用户id
		//购买商品总数量
		orderInfo.setGoodsCount(totalGoodsCount);
		//快递费用
		BigDecimal expressPrice =  new BigDecimal(0);
		orderInfo.setExpressPrice(expressPrice);
		//使用积分数
		BigDecimal totalIntegralNum =  new BigDecimal(0);
		orderInfo.setTotalIntegralNum(totalIntegralNum);
		//使用积分抵扣价
		BigDecimal totalIntegralPrice =  new BigDecimal(0);
		orderInfo.setTotalIntegralPrice(totalIntegralPrice);
		//在线支付总费用
		orderInfo.setTotalPayPrice(goodsTotalPrice);
		//总价
		orderInfo.setTotalPrice(goodsTotalPrice);
		//地址
		orderInfo.setAddress(userEntity.getAddress());
		//总和地址
		orderInfo.setPcrdetail(userEntity.getProvince()+userEntity.getCity()+userEntity.getCountry()+userEntity.getAddress());
		//电话
		orderInfo.setTelephone(userEntity.getAddrPhone());
		//联系人
		orderInfo.setContactName(userEntity.getAddrLinkName());
		orderInfo.setCreateTime(new Date()); 
		// 开启事务，插入订单信息和订单商品
		int orderId = goodsOrderDao.save(orderInfo);
		if (null == orderInfo.getId()) {
			resultObj.put("errno", 1);
			resultObj.put("errmsg", "购买提交失败");
			return resultObj;
		}
		//扣除用户的金额
		//可以消费返利
		// 统计商品总价
		List<GoodsOrderDetailEntity> orderGoodsData = new ArrayList<GoodsOrderDetailEntity>();
		for (GoodsCartEntity goodsItem : checkedGoodsList) {
			GoodsOrderDetailEntity orderGoodsVo = new GoodsOrderDetailEntity();
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
			orderGoodsVo.setUpdateTime(new Date());
			orderGoodsVo.setCreateTime(new Date());
			orderGoodsVo.setGoodTags(goodTags);
			orderGoodsData.add(orderGoodsVo);
			goodsOrderDetailDao.save(orderGoodsVo);
		}
		// 清空已购买的商品
		goodsCartDao.deleteByCart(userEntity.getUserId(), 1);
		resultObj.put("errno", 0);
		resultObj.put("errmsg", "购买成功");
		Map<String, GoodsOrderEntity> orderInfoMap = new HashMap<String, GoodsOrderEntity>();
		orderInfo.setGoodsOrderDetailEntityList(orderGoodsData);
		orderInfoMap.put("orderInfo", orderInfo);
		resultObj.put("data", orderInfoMap);
		
		return resultObj;
	}
	
}
