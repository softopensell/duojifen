package com.platform.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.constants.PluginConstant;
import com.platform.entity.AddressVo;
import com.platform.entity.GoodsOrderDetailVo;
import com.platform.entity.GoodsOrderEntity;
import com.platform.entity.GoodsOrderVo;
import com.platform.entity.UserEntity;
import com.platform.entity.UserVo;
import com.platform.facade.DjfBonusFacade;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.ApiAddressService;
import com.platform.service.ApiGoodsOrderDetailService;
import com.platform.service.ApiGoodsOrderService;
import com.platform.service.GoodsOrderService;
import com.platform.service.UserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.util.ShopConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.utils.JsonUtil;
import com.platform.utils.Query;
import com.platform.utils.StringUtils;
import com.platform.validator.ApiAssert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author softopensell <br>
 * 时间: 2019-03-25 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/order")
public class ApiOrderController extends ApiBaseAction {
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private ApiGoodsOrderService apiGoodsOrderService;
	@Autowired
	private ApiGoodsOrderDetailService orderDetailService;
	
	
	 @Autowired
	 private ApiAddressService addressService;
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private DjfBonusFacade djfBonusFacade;

	/**
	 */
	@ApiOperation(value = "订单首页")
	@IgnoreAuth
	@PostMapping("index")
	public Object index() {
		return toResponsSuccess("");
	}

	/**
	 * 获取订单列表
	 */
	@ApiOperation(value = "获取订单列表")
	@PostMapping("list")
	public Object list(@LoginUser UserVo loginUser) {
		JSONObject jsonParams = getJsonRequest();
		Integer page  = 1 ;  page = jsonParams.getInteger("page");
		Integer size = 10 ;  size = jsonParams.getInteger("size");
		Integer orderStatus = 0 ;  orderStatus = jsonParams.getInteger("orderStatus");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", loginUser.getUserId());
			
		if(orderStatus!=0){
			params.put("orderStatus", orderStatus);
		}
		params.put("page", page);
		params.put("limit", size);
		params.put("sidx", "create_time");
		params.put("order", "desc");
		// 查询列表数据
		Query query = new Query(params);
		List<GoodsOrderVo> orderEntityList = apiGoodsOrderService.queryList(query);
		int total = apiGoodsOrderService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(orderEntityList, total, query.getLimit(), query.getPage());
		//
		for (GoodsOrderVo item : orderEntityList) {
			Map orderGoodsParam = new HashMap();
			orderGoodsParam.put("order_no", item.getOrderNo());
			// 订单的商品
			List<GoodsOrderDetailVo> orderDetailList = orderDetailService.queryList(orderGoodsParam);
			item.setOrderDetailList(orderDetailList);
			AddressVo address = addressService.queryObject(item.getAddressId());
			if(address!=null){
				item.setAddress(address.getAddress());
				item.setTelephone(address.getContactMobile());
			}else{
				item.setAddress("");
				item.setTelephone("");
			}
		}
		return toResponsSuccess(pageUtil);
	}

	/**
	 * 获取订单详情
	 */
	@ApiOperation(value = "获取订单详情")
	@PostMapping("detail")
	public Object detail() {
		Map<String, Object> resultObj = new HashMap<String, Object>();
		JSONObject jsonParams = getJsonRequest();
		String orderNo = jsonParams.getString("orderNo");
		ApiAssert.isNull(orderNo, "订单号不能为空");
		GoodsOrderVo orderInfo = apiGoodsOrderService.queryObjectByNo(orderNo);
		if (null == orderInfo) {
			return toResponsObject(400, "订单不存在", "");
		}
		//收货人位置
		AddressVo address = addressService.queryObject(orderInfo.getAddressId());
		if(null != address){
			orderInfo.setTelephone(address.getContactMobile());
			orderInfo.setAddress(address.getAddress());
		}
		Map<String, Object> orderGoodsParam = new HashMap<String, Object>();
		orderGoodsParam.put("order_no", orderNo);
		// 订单的商品
		List<GoodsOrderDetailVo> orderDetailList = orderDetailService.queryList(orderGoodsParam);
		orderInfo.setOrderDetailList(orderDetailList);
		resultObj.put("orderInfo", orderInfo);
		return toResponsSuccess(resultObj);
	}

	@ApiOperation(value = "修改订单")
	@PostMapping("updateSuccess")
	public Object updateSuccess() {
		JSONObject jsonParams = getJsonRequest();
		String orderNo = jsonParams.getString("orderNo");
		ApiAssert.isNull(orderNo, "订单号不能为空");
		GoodsOrderVo orderInfo = apiGoodsOrderService.queryObjectByNo(orderNo);

		if (orderInfo == null) {
			return toResponsFail("订单不存在");
		} else if (orderInfo.getOrderStatus() != ShopConstant.ORDER_STATUS_CONFRIM) {
			return toResponsFail("订单状态不正确orderStatus" + orderInfo.getOrderStatus() + "payStatus" + orderInfo.getPayStatus());
		}
		orderInfo.setOrderNo(orderNo);
		orderInfo.setPayStatus(TradeStatus.WAIT_BUYER_PAY.code);
		orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_CANCEL);
		orderInfo.setUpdateTime(new Date());
		int num = apiGoodsOrderService.update(orderInfo);
		if (num > 0) {
			return toResponsMsgSuccess("修改订单成功");
		} else {
			return toResponsFail("修改订单失败");
		}
	}
	/**
	 * 获取订单列表
	 */
	@ApiOperation(value = "订单提交")
	@PostMapping("submit")
	public Object submit(@LoginUser UserVo loginUser) {
		Map resultObj = null;
		try {
			resultObj = apiGoodsOrderService.submit(getJsonRequest(), loginUser);
			if (null != resultObj) {
				return toResponsObject(MapUtils.getInteger(resultObj, "errno"), MapUtils.getString(resultObj, "errmsg"),
						resultObj.get("data"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponsFail("提交失败");
	}
	@ApiOperation(value = "直接购买并支付")
	@PostMapping("immediateBuy")
	public Object immediateBuy(@LoginUser UserVo loginUser) {
		  Map resultObj = null;
		try {
			JSONObject jsonParams = getJsonRequest();
			Integer goodId = jsonParams.getInteger("goodId");
			Integer sum = jsonParams.getInteger("sum");
			Integer toPayType = jsonParams.getInteger("toPayType");
			ApiAssert.isNull(goodId, "商品不能为空");
			if(sum==null) {
				sum=1;
			}
			int payType=0;
			int orderPayType=ShopConstant.PAY_TYPE_JF;
			if(toPayType==null) {
				payType=0;//钱包支付
				orderPayType=ShopConstant.PAY_TYPE_YE;//钱包支付
			}else if(toPayType==5) {//积分支付
				payType=1;
				orderPayType=ShopConstant.PAY_TYPE_JF;
			}
		
			UserEntity userEntity=userService.queryObject(loginUser.getUserId());
			
			if(StringUtils.isEmpty(userEntity.getAddress()))toResponsFail("请先完善收货信息！");
			if(StringUtils.isEmpty(userEntity.getAddrLinkName()))toResponsFail("请先完善收货信息！");
			if(StringUtils.isEmpty(userEntity.getAddrPhone()))toResponsFail("请先完善收货信息！");
			
			
			resultObj = goodsOrderService.toSubmitBuyInverstGoodOrder(userEntity, goodId,sum, orderPayType);
			
			logger.info("--------add------buy-----resultObj---"+JsonUtil.getJsonByObj(resultObj));
			if (null != resultObj) {
				//购买成功 返利
				int errno=MapUtils.getInteger(resultObj, "errno");
				if(errno==0) {
					//userService
					Map<String, GoodsOrderEntity> orderInfoMap =(Map<String, GoodsOrderEntity>) resultObj.get("data");
					GoodsOrderEntity orderInfo =(GoodsOrderEntity) orderInfoMap.get("orderInfo");
					BigDecimal amount=orderInfo.getTotalPayPrice();
					
					if(payType==0) {//钱包扣除
					//扣除用户金额
					userService.reduceUserBalance(loginUser.getUserId(), amount);
					//写日志
					 PaymentTask paymentTask=new PaymentTask();
					 paymentTask.setAmount(amount);
					 paymentTask.setFee(0);
					 paymentTask.setUserId(userEntity.getUserId());
					 paymentTask.setPayer(userEntity.getUserName());
					 paymentTask.setMemo("商城购买消费");
					 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_NO);
					 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
					 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
					 //增加父节点与自己的业绩
					 djfBonusFacade.updateNodedConsumedTeamBonusPonit(loginUser.getUserId(), amount);
					 
					}else if(payType==1) {//积分
						//扣除用户金额
						userService.reduceUserIntegralScore(loginUser.getUserId(), amount.intValue());
						//写日志
						PaymentTask paymentTask=new PaymentTask();
						paymentTask.setAmount(amount);
						paymentTask.setFee(0);
						paymentTask.setUserId(userEntity.getUserId());
						paymentTask.setPayer(userEntity.getUserName());
						paymentTask.setMemo("商城积分消费");
						paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_PAY);
						paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
						TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
					}
					 
					//消费订单 推荐奖励
//					BonusTaskVo bonusTaskVo=new BonusTaskVo(orderInfo.getOrderNo(), orderInfo.getOrderType(), userEntity.getUserId(), orderInfo.getTotalPayPrice());
//					TaskBonusProducer.addBonusTaskVo(bonusTaskVo, true);
					return this.toResponsSuccess(resultObj.get("data"));
				}
				return toResponsObject(errno, MapUtils.getString(resultObj, "errmsg"),
						resultObj.get("data"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponsFail("购买失败");
	}

	/**
	 * 取消订单
	 */
	@ApiOperation(value = "取消订单")
	@PostMapping("cancelOrder")
	public Object cancelOrder() {
		try {
			JSONObject jsonParams = getJsonRequest();
			String orderNo = jsonParams.getString("orderNo");
			ApiAssert.isNull(orderNo, "订单号不能为空");
			GoodsOrderVo orderInfo = apiGoodsOrderService.queryObjectByNo(orderNo);
			
			if  (orderInfo.getOrderStatus() != ShopConstant.ORDER_STATUS_CONFRIM) {
				return toResponsFail("已发货，不能取消");
			}  
			orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_CANCEL);
			apiGoodsOrderService.update(orderInfo);
			return toResponsSuccess("取消成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponsFail("提交失败");
	}
 
	/**
	 * 确认收货
	 */
	@ApiOperation(value = "确认收货")
	@PostMapping("confirmOrder")
	public Object confirmOrder() {
		JSONObject jsonParams = getJsonRequest();
		String orderNo = jsonParams.getString("orderNo");
		ApiAssert.isNull(orderNo, "订单号不能为空");
		try {
			GoodsOrderVo GoodsOrderVo = apiGoodsOrderService.queryObjectByNo(orderNo);
			GoodsOrderVo.setOrderStatus(ShopConstant.ORDER_STATUS_OK);
			GoodsOrderVo.setUpdateTime(new Date());
			apiGoodsOrderService.update(GoodsOrderVo);
			return toResponsSuccess("确认收货成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponsFail("提交失败");
	}
}