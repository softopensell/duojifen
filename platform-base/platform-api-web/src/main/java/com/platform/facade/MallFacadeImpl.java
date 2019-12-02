package com.platform.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.entity.GoodsOrderDetailVo;
import com.platform.entity.GoodsOrderVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserVo;
import com.platform.mq.model.BonusTaskVo;
import com.platform.mq.producer.TaskBonusProducer;
import com.platform.service.ApiGoodsOrderDetailService;
import com.platform.service.ApiGoodsOrderService;
import com.platform.service.ApiGoodsService;
import com.platform.service.ApiUserService;
import com.platform.thirdparty.sms.YunSmsService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.util.constants.PayStatusConstant;
import com.platform.util.constants.ProductConstant;
import com.platform.util.constants.TradeStatus;
@Component
public class MallFacadeImpl  implements MallFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApiGoodsOrderService orderService;
    
    @Autowired
	private ApiGoodsOrderDetailService orderDetailService;
    @Autowired
    private ApiGoodsService goodsService;
    
    
    @Autowired
 	private YunSmsService yunSmsService;
    
    @Autowired
    private ApiUserService userService;

	@Override
	public void paySuccessOrder(String orderNo, int payType) {
		
		GoodsOrderVo orderInfo = orderService.queryObjectByNo(orderNo);
		 // 付款成功
        orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_NOSHIPMENT);
        orderInfo.setPayStatus(TradeStatus.TRADE_SUCCESS.code);
        orderService.update(orderInfo);
		/*找到特价产品*/
    	Map<String, Object> orderGoodsParam = new HashMap<String, Object>();
		orderGoodsParam.put("order_no", orderNo);
		// 订单的商品
		List<GoodsOrderDetailVo> orderDetailList = orderDetailService.queryList(orderGoodsParam);
		
		if(orderDetailList.size()>0){
			for(GoodsOrderDetailVo orderDetail :orderDetailList){
				GoodsVo goodsVo = goodsService.queryObject(orderDetail.getGoodsId());
				//是特价产品的情况下，进行参与分红奖励
				if(goodsVo.getGoodsType() == ProductConstant.PRODUCT_TYPE_SPECIAL){
					logger.info(" onSuccess 支付成功，参与分红奖励！");
					   /**
					 * 消费分
					 */
					 BonusTaskVo bonusTaskVo=new BonusTaskVo(orderInfo.getOrderNo(),orderInfo.getOrderType(),orderInfo.getUserId(),orderInfo.getTotalPrice());
					 TaskBonusProducer.addBonusTaskVo(bonusTaskVo, true);
					 
					logger.info(" onSuccess 支付成功，参与分红奖励！");
					
					  //加入分红日志记录
//					 String content=JsonUtils.toJson(bonusTaskVo);
//					 commonMqService.writeCommonLogToDB("分红", ""+payment.getMemberId(), content, "11", null);
	
				}
			} 
			
		}
        
		String msgContent = "恭喜您的订单已支付成功，请及时关注订单详情！" ;
        UserVo userVo = userService.queryObject(orderInfo.getUserId());
        int flag=yunSmsService.send(OrderConstant.YunSmsSignName_1,msgContent,userVo.getMobile());
        if (flag==0) {
        	 logger.info("--------微信订单回调接口-- 短信发送成功--");
        	SmsLogEntity  smsLogVo = new SmsLogEntity();
            smsLogVo.setLogDate(System.currentTimeMillis() / 1000);
            smsLogVo.setPhone(userVo.getMobile());
            smsLogVo.setSmsCode(OrderConstant.YunSmsSignName_1);
            smsLogVo.setSmsText(msgContent);
            userService.saveSmsCodeLog(smsLogVo);
        }
	}

	@Override
	public void balancePay(String orderNo) {
		
	}

	@Override
	public void sysCancelOrder() {
		
	}
 
	 
}
