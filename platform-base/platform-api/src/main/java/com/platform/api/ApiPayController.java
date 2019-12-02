package com.platform.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.alipay.AlipayBaseApi;
import com.platform.annotation.FrequencyLimit;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.annotation.SingleLock;
import com.platform.cache.IdWorkCache;
import com.platform.cache.J2CacheUtils;
import com.platform.constants.PluginConstant;
import com.platform.entity.GoodsOrderDetailVo;
import com.platform.entity.GoodsOrderVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserVo;
import com.platform.facade.MallFacade;
import com.platform.facade.PayWorkCallable;
import com.platform.facade.PaymentPluginFacade;
import com.platform.mq.model.BonusTaskVo;
import com.platform.mq.producer.TaskBonusProducer;
import com.platform.service.ApiGoodsOrderDetailService;
import com.platform.service.ApiGoodsOrderService;
import com.platform.service.ApiGoodsService;
import com.platform.service.PaymentInfoService;
import com.platform.service.PaymentLogService;
import com.platform.service.PaymentOutService;
import com.platform.service.UserService;
import com.platform.thirdparty.sms.YunSmsService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ObjToStringUtil;
import com.platform.util.Sha1Util;
import com.platform.util.ShopConstant;
import com.platform.util.constants.CompanyConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.util.constants.PayStatusConstant;
import com.platform.util.constants.ProductConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.util.wechat.ApiWeixinBase;
import com.platform.util.wechat.GlobalConstant;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.CharUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.MapUtils;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.RandomUtil;
import com.platform.utils.ResourceUtil;
import com.platform.utils.XmlUtil;
import com.platform.validator.ApiAssert;
import com.platform.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author softopensell <br>
 * 时间: 2019-04-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商户支付")
@RestController
@RequestMapping("/api/pay")
public class ApiPayController extends ApiBaseAction {
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
    private UserService userService;
    
    @Resource
	private PaymentPluginFacade paymentPluginFacade;
    @Resource
    private MallFacade mallFacade;
    @Autowired
	PaymentInfoService paymentService;
	@Autowired
	PaymentOutService paymentOutService;
	@Autowired
	private PaymentLogService paymentLogService;
	@Autowired
	private IdWorkCache idWorkCache ;
    /**
     */
    @ApiOperation(value = "跳转")
    @PostMapping("index")
    public Object index() {
        //
        return toResponsSuccess("");
    }

    /**
     * 获取微信支付的请求参数
     */
    @ApiOperation(value = "获取微信支付的请求参数")
    @PostMapping("wx/prepay")
    public Object payPrepay(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	String orderNo= jsonParams.getString("orderNo");
		String paySn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		paySn=idWorkCache.getIdDayHHMMEndRadom("QA", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
    	
		ApiAssert.isNull(orderNo, "订单号不能为空");
		Map<String, Object> responseData = getWxpayParams(loginUser, orderNo,paySn);
        return responseData;
    }

	private Map<String, Object> getWxpayParams(UserVo loginUser, String orderNo,String paySn) {
		GoodsOrderVo orderInfo = orderService.queryObjectByNo(orderNo);
		
        if (orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_CANCEL
        		||orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_PAYTIMEOUT
        		||orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_ADMINCANCEL
        		) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getPayStatus() != 0) {
            return toResponsObject(400, "订单已支付，请不要重复操作", "");
        }

        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
//          //小程序的appID
//          parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));//小程序的appID
            //公众号的appID
            parame.put("appid", ResourceUtil.getConfigByName("mp.appId"));//公众号的appID
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", paySn);
            // 商品描述
            parame.put("body", "商品购买-支付");
            
            String flag = ResourceUtil.getConfigByName("wx.openPayStatus");
            int total_fee = MoneyFormatUtils.getMultiply(orderInfo.getTotalPrice(),new BigDecimal(100)).intValue();
            if("1".equals(flag)){
            	total_fee = 1;
            }
            //支付金额
            parame.put("total_fee", total_fee);
            // 回调地址
            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getClientIp());
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            logger.info("--------获取支付的请求参数-----请求xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            logger.info("--------获取支付的请求参数--return_code:" + return_code);
            logger.info("--------获取支付的请求参数--return_msg:" + return_msg);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return toResponsFail("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
            		//获取prepay_id后，拼接最后请求支付所需要的package
            		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
            		String timestamp = Sha1Util.getTimeStamp();
            		finalpackage.put("appid", ResourceUtil.getConfigByName("mp.appId"));  
            		finalpackage.put("partnerid", ResourceUtil.getConfigByName("wx.mchId"));  
            		finalpackage.put("noncestr", nonceStr);  
            		finalpackage.put("package", "Sign=WXPay");  
            		finalpackage.put("prepayid", prepay_id);  
            		finalpackage.put("timestamp", timestamp);  
            		//要签名
            		String finalsign = WechatUtil.createSign(finalpackage);
            		finalpackage.put("sign", finalsign);
                    
                    // 业务处理
                    orderInfo.setPayNo(prepay_id);
                    // 付款中
                    orderInfo.setPayStatus(TradeStatus.WAIT_BUYER_PAY_ING.code);
                    orderService.update(orderInfo);
                    return toResponsObject(0, "充值-微信统一下单成功", finalpackage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
	}

    @ApiOperation(value = "充值")
    @PostMapping("rechargeMoney")
    @FrequencyLimit(count=1,time=5,action="rechargeMoney",forbiddenTime=60*10)
    public Object rechargeMoney(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	Integer payType= jsonParams.getInteger("payType");
    	ApiAssert.isNull(payType, "支付类型不能为空");
    	BigDecimal money= jsonParams.getBigDecimal("money");
    	ApiAssert.isNull(money, "充值金额不能为空");
    	BigDecimal amountBank = new BigDecimal(0);
    	amountBank=money;
    	/* String flag = ResourceUtil.getConfigByName("wx.openPayStatus");
    	 if("1".equals(flag)){
    		 amountBank = new BigDecimal(1);
         }*/
    	BigDecimal feeBank = new BigDecimal(0);;//手续费
    	PaymentInfoEntity payment=null;
		String payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX;
		String payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME;
	   
    	 if (amountBank.compareTo(new BigDecimal(0)) <= 0) {
			   return toResponsObject(400, "充值金额不能小于等于0", "");
			} 
    	 	
		   // 获取：支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
		   if(payType==OrderConstant.ORDER_PAY_TYPE_ONLINE_WXPAY) {
			   payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX;
				payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME;
				feeBank=MoneyFormatUtils.getFeeBigDecimal(amountBank,  ApiWeixinBase.getWeixinBaseConfig().getFee_rate());
			}else if(payType==OrderConstant.ORDER_PAY_TYPE_ONLINE_ALPAY){
				payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_ALI;
				payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_ALI_NAME;
				feeBank=MoneyFormatUtils.getFeeBigDecimal(amountBank,  AlipayBaseApi.getAliPayBaseConfig().getFee_rate());
			} 
		   
		   logger.info("---------充值--开始-------");
			
		   logger.info("----------充值信息，创建充值流水----开始-----");
		    String paySn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		    paySn=idWorkCache.getIdDayHHMMEndRadom("QA", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
	    	
		    String descriptionBank="";
			String orderDesc="充值";
			UserEntity loginUserEntity=userService.queryObject(loginUser.getUserId());
			payment=paymentPluginFacade.submitPayOrder(CompanyConstant.COMPANY_SN_GJZB, paySn, 
					payMethodPluginType, 
					payMethodPluginTypeName,
					GlobalConstant.WEBLOGO_CN, 
					OrderConstant.ORDER_TYPE_RECHARGE, 
					paySn, 
					amountBank, 
					feeBank,
					orderDesc,
					loginUserEntity,
					loginUser.getUserId(),
					loginUser.getUserName() ,
					orderDesc, 
					TradeStatus.WAIT_BUYER_PAY, 
					PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE,
					PluginConstant.PAYMENT_TYPE_IN
					);
//        } 
			
		    logger.info("----------充值，创建充值流水----结束-----");
		    logger.info("----------充值，订单支付方式----开始-----");
		    
		    logger.info("----------充值，获取支付订单信息，创建支付流水----结束-----");
			   logger.info("----------充值，订单支付方式----开始-----");
			   
			   if(payType==OrderConstant.ORDER_PAY_TYPE_ONLINE_WXPAY&&payment!=null){//微信支付
//					Map<String, Object> responseData = getWxpayParams(loginUser, orderNo);
					String nonceStr = CharUtil.getRandomString(32);

			        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
			        Map<Object, Object> resultObj = new TreeMap();

			        try {
			            Map<Object, Object> parame = new TreeMap<Object, Object>();
//			          //小程序的appID
//			          parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));//小程序的appID
			            //公众号的appID
			            parame.put("appid", ResourceUtil.getConfigByName("mp.appId"));//公众号的appID
			            // 商家账号。
			            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
			            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
			            // 随机字符串
			            parame.put("nonce_str", randomStr);
			            // 商户订单编号
			            parame.put("out_trade_no", paySn);
			            // 商品描述
			            parame.put("body", "充值-支付");
			            
			            String openPayStatus_flag  = ResourceUtil.getConfigByName("wx.openPayStatus");
			            int total_fee = MoneyFormatUtils.getMultiply(amountBank,new BigDecimal(100)).intValue();
			            if("1".equals(openPayStatus_flag)){
			            	total_fee = 1;
			            }
			            //支付金额
			            parame.put("total_fee", total_fee);
			            // 回调地址
			            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
			            // 交易类型APP
			            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
			            parame.put("spbill_create_ip", getClientIp());
//			            parame.put("openid", loginUser.getWeixinOpenid());
			            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
			            // 数字签证
			            parame.put("sign", sign);

			            String xml = MapUtils.convertMap2Xml(parame);
			            logger.info("--------充值-获取支付的请求参数-----请求xml:" + xml);
			            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
			            // 响应报文
			            String return_code = MapUtils.getString("return_code", resultUn);
			            String return_msg = MapUtils.getString("return_msg", resultUn);
			            logger.info("--------充值-获取微信支付的请求参数--return_code:" + return_code);
			            logger.info("--------充值-获取支付的请求参数--return_msg:" + return_msg);
			            //
			            if (return_code.equalsIgnoreCase("FAIL")) {
			                return toResponsFail("充值-支付失败," + return_msg);
			            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
			                // 返回数据
			                String result_code = MapUtils.getString("result_code", resultUn);
			                String err_code_des = MapUtils.getString("err_code_des", resultUn);
			                if (result_code.equalsIgnoreCase("FAIL")) {
			                    return toResponsFail("充值-支付失败," + err_code_des);
			                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
			                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
			                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
			                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
			                    /*resultObj.put("appId", ResourceUtil.getConfigByName("mp.appId"));
			                    resultObj.put("partnerid", ResourceUtil.getConfigByName("wx.mchId"));
			                    resultObj.put("prepayid",  prepay_id); 
			                    resultObj.put("package", "Sign=WXPay");
			                    resultObj.put("nonceStr", nonceStr);
			                    resultObj.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
			                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
			                    resultObj.put("sign", paySign);*/
			                    
			                    

			            		//获取prepay_id后，拼接最后请求支付所需要的package
			            		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
			            		String timestamp = Sha1Util.getTimeStamp();
			            		finalpackage.put("appid", ResourceUtil.getConfigByName("mp.appId"));  
			            		finalpackage.put("partnerid", ResourceUtil.getConfigByName("wx.mchId"));  
			            		finalpackage.put("noncestr", nonceStr);  
			            		finalpackage.put("package", "Sign=WXPay");  
			            		finalpackage.put("prepayid", prepay_id);  
			            		finalpackage.put("timestamp", timestamp);  
			            		//要签名
			            		String finalsign = WechatUtil.createSign(finalpackage);
			            		finalpackage.put("sign", finalsign);
			                    
			                    
			                    return toResponsObject(0, "充值-微信统一下单成功", finalpackage);
//			                    return toResponsObject(0, "充值-微信统一下单成功", resultObj);
			                }
			            }
			        } catch (Exception e) {
			            e.printStackTrace();
			            return toResponsFail("充值-微信下单失败,error=" + e.getMessage());
			        }
			        return toResponsFail("充值-微信下单失败");
					
					}					
			   else if(payType==OrderConstant.ORDER_PAY_TYPE_ONLINE_ALPAY&&payment!=null){//支付宝支付
					String notifyUrl= AlipayBaseApi.getAliPayBaseConfig().getNotify_url();
					String paySubject=payment.getOrderDesc();
					String payBody="充值-支付宝支付";
					String amount=MoneyFormatUtils.formatBigDecimalToDoubleStr(payment.getAmount());
					String aliPayInfo=AlipayBaseApi.getAlipayTradeAppPayRequest(paySn, paySubject, payBody, amount, notifyUrl);
					logger.info("==============AlipayBaseApi.getAlipayTradeAppPayRequest===========aliPayInfo==="+aliPayInfo);
					Map<Object, Object> resultObj = new TreeMap();
						if(aliPayInfo!=null){
							resultObj.put("orderInfo", aliPayInfo);
							 return toResponsObject(0, "充值-支付宝支付订单下单成功", resultObj);
						}
					}
			   return toResponsFail("充值-下单失败:支付方式:"+payType);
		 
    }
    @ApiOperation(value = "订单支付")
    @PostMapping("payOrder")
    @SingleLock
    public Object payOrder(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();


    	String orderNo= jsonParams.getString("orderNo");
    	ApiAssert.isNull(orderNo, "订单号不能为空");
		GoodsOrderVo orderInfo = orderService.queryObjectByNo(orderNo);
		 if (orderInfo == null) {
	            return toResponsObject(400, "订单不存在", "");
	        }
		 
		  if (orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_CANCEL
	        		||orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_PAYTIMEOUT
	        		||orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_ADMINCANCEL
	        		) {
	            return toResponsObject(400, "订单已取消", "");
	        }

	        if (orderInfo.getPayStatus() != TradeStatus.WAIT_BUYER_PAY.code) {
	            return toResponsObject(400, "订单已支付，请不要重复操作", "");
	        }
		 
		    
		   
		   
	        BigDecimal feeBank = new BigDecimal(0);
			BigDecimal amountBank = new BigDecimal(0);
			PaymentInfoEntity payment=null;
			String payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX;
			String payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME;
			Integer moneyTypeWallet=PluginConstant.PAYMENT_MONEY_TYPE_WALLET_NO;
		   
		   // 获取：支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
			 if(orderInfo.getPayType()==OrderConstant.ORDER_PAY_TYPE_ONLINE_WXPAY) {
				   payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX;
					payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME;
					feeBank=MoneyFormatUtils.getFeeBigDecimal(amountBank,  ApiWeixinBase.getWeixinBaseConfig().getFee_rate());
			}else if(orderInfo.getPayType()==OrderConstant.ORDER_PAY_TYPE_ONLINE_ALPAY){
				payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_ALI;
				payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_ALI_NAME;
				feeBank=MoneyFormatUtils.getFeeBigDecimal(amountBank,  AlipayBaseApi.getAliPayBaseConfig().getFee_rate());
			} else if(orderInfo.getPayType()== OrderConstant.ORDER_PAY_TYPE_ONLINE_YEPay) {
				payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_MEMBER_WALLET;
				payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_MEMBER_WALLET_NAME;
			}else if(orderInfo.getPayType()== OrderConstant.ORDER_PAY_TYPE_ONLINE_INTEGRAL) {
				payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_INTEGRAL;
				payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_INTEGRAL_NAME;
				moneyTypeWallet = PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_PAY;
			}
		   
		   String paySn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		   paySn=idWorkCache.getIdDayHHMMEndRadom("QA", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
	    	
		   int orderType=orderInfo.getOrderType();
		 
		   String orderDesc="";
		   logger.info("----------获取支付订单信息，创建支付流水----开始-----");
		   if(orderType == OrderConstant.ORDER_TYPE_SHOPPING){
			   orderDesc="商城支付订单";
			 
//			   amountBank =  MoneyFormatUtils.getBigDecimalFee(orderInfo.getTotalPayPrice());
			   amountBank =  orderInfo.getTotalPayPrice();
				
			   UserEntity loginUserEntity=userService.queryObject(loginUser.getUserId());
			   
			   if (amountBank.compareTo(new BigDecimal(0)) <= 0) {
				   return toResponsObject(400, "支付金额不能小于等于0", "");
				} 
			   if (orderInfo.getPayStatus() == TradeStatus.WAIT_BUYER_PAY.code) {
					 payment=paymentPluginFacade.submitPayOrder(CompanyConstant.COMPANY_SN_GJZB, paySn, 
								payMethodPluginType, 
								payMethodPluginTypeName,
								GlobalConstant.WEBLOGO_CN, 
								OrderConstant.ORDER_TYPE_SHOPPING, 
								orderNo, 
								amountBank, 
								feeBank,
								orderDesc,
								loginUserEntity,
								orderInfo.getUserId(),
								orderInfo.getContactName(), 
								orderDesc, 
								TradeStatus.WAIT_BUYER_PAY, 
								moneyTypeWallet,
								PluginConstant.PAYMENT_TYPE_OUT
								);
			        } 
		   }
		   logger.info("----------获取支付订单信息，创建支付流水----结束-----");
		   logger.info("----------订单支付方式----开始-----");
		   
		   if(orderInfo.getPayType()==OrderConstant.ORDER_PAY_TYPE_ONLINE_WXPAY&&payment!=null){//微信支付
				Map<String, Object> responseData = getWxpayParams(loginUser, orderNo,paySn);
		        	return responseData;
				}					
		   else if(orderInfo.getPayType()==OrderConstant.ORDER_PAY_TYPE_ONLINE_ALPAY&&payment!=null){//支付宝支付
				String notifyUrl= AlipayBaseApi.getAliPayBaseConfig().getNotify_url();
				String paySubject=payment.getOrderDesc();
				String payBody="支付宝支付";
				 String flag = ResourceUtil.getConfigByName("wx.openPayStatus");
		    	 if("1".equals(flag)){
		    		 amountBank = new BigDecimal(1);
		    		 payment.setAmount(amountBank);
		         }
				String amount=MoneyFormatUtils.formatBigDecimalToDoubleStr(payment.getAmount());
				
				
				String aliPayInfo=AlipayBaseApi.getAlipayTradeAppPayRequest(paySn, paySubject, payBody, amount, notifyUrl);
				logger.info("==============AlipayBaseApi.getAlipayTradeAppPayRequest===========orderInfo==="+orderInfo);
				Map<Object, Object> resultObj = new TreeMap();
					if(aliPayInfo!=null){
						resultObj.put("orderInfo", aliPayInfo);
						 return toResponsObject(0, "支付宝支付订单下单成功", resultObj);
					}
				}
		   else if(orderInfo.getPayType()==OrderConstant.ORDER_PAY_TYPE_ONLINE_YEPay&&payment!=null){//余额支付
			    	if(payment!=null&&loginUser.getBalance().compareTo(payment.getAmount()) == 1){
						try {
							
							paymentPluginFacade.dealOrder( payment, TradeStatus.TRADE_FINISHED, new PayWorkCallable<PaymentInfoEntity>() {
										@Override
										public void onSuccess(PaymentInfoEntity payment) {
											BigDecimal memberPoint=loginUser.getBalance();
											BigDecimal amount=payment.getAmount();
											//商城钱包支付
											userService.reduceUserBalance(loginUser.getUserId(), amount);
											
											 // 付款成功
							                orderInfo.setOrderStatus(ShopConstant.PAY_STATUS_PAYOK);
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
//													     int money=MoneyFormatUtils.formatBigDecimalToInt(payment.getAmount());
							        					 BonusTaskVo bonusTaskVo=new BonusTaskVo(orderInfo.getOrderNo(),orderInfo.getOrderType(),orderInfo.getUserId(),orderInfo.getTotalPrice());
							        					 TaskBonusProducer.addBonusTaskVo(bonusTaskVo, true);
														 
							        					logger.info(" onSuccess 余额-支付成功！");
							        					
							        					  //加入分红日志记录
//														 String content=JsonUtils.toJson(bonusTaskVo);
//														 commonMqService.writeCommonLogToDB("分红", ""+payment.getMemberId(), content, "11", null);
										
							        				}
							        			} 
							        			
							        		}
							        		
									}
									@Override
									public void onFail(PaymentInfoEntity obj) {
										logger.info(" onFail 扣款失败！");
									}
							});
							
							
									
								} catch (Exception e) {
									e.printStackTrace();
									logger.info(" 系统处理异常",e);
									return toResponsFail("系统处理异常");
							}
						return toResponsMsgSuccess("余额-支付成功");
					
			    	}else{
			    		return toResponsObject(400, "个人余额钱包不足，请及时充值或更换其他支付方式", "");
			    	}
		    }
		   
		   
		   else if(orderInfo.getPayType()==OrderConstant.ORDER_PAY_TYPE_ONLINE_INTEGRAL&&payment!=null){//积分支付
		    	if(payment!=null&&new BigDecimal(loginUser.getIntegralScore()).compareTo(payment.getAmount()) == 1){
					try {
						
						paymentPluginFacade.dealOrder( payment, TradeStatus.TRADE_FINISHED, new PayWorkCallable<PaymentInfoEntity>() {
									@Override
									public void onSuccess(PaymentInfoEntity payment) {
//										BigDecimal memberPoint=loginUser.getBalance();
										BigDecimal amount=payment.getAmount();
										//商城积分支付
										userService.reduceUserIntegralScore(loginUser.getUserId(), amount.intValue());
										
										 // 付款成功
						                orderInfo.setOrderStatus(ShopConstant.PAY_STATUS_PAYOK);
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
//												     int money=MoneyFormatUtils.formatBigDecimalToInt(payment.getAmount());
						        					 BonusTaskVo bonusTaskVo=new BonusTaskVo(orderInfo.getOrderNo(),orderInfo.getOrderType(),orderInfo.getUserId(),orderInfo.getTotalPrice());
						        					 TaskBonusProducer.addBonusTaskVo(bonusTaskVo, true);
													 
						        					logger.info(" onSuccess 余额-支付成功！");
						        					
						        					  //加入分红日志记录
//													 String content=JsonUtils.toJson(bonusTaskVo);
//													 commonMqService.writeCommonLogToDB("分红", ""+payment.getMemberId(), content, "11", null);
									
						        				}
						        			} 
						        			
						        		}
						        		
								}
								@Override
								public void onFail(PaymentInfoEntity obj) {
									logger.info(" onFail 扣款失败！");
								}
						});
						
						
								
							} catch (Exception e) {
								e.printStackTrace();
								logger.info(" 系统处理异常",e);
								return toResponsFail("系统处理异常");
						}
					return toResponsMsgSuccess("积分-支付成功");
				
		    	}else{
		    		return toResponsObject(400, "个人积分数量不足，请及时充值或更换其他支付方式", "");
		    	}
	    }
		   
		   return toResponsMsgSuccess("订单确认成功");
    }
    
    
 

	@ApiOperation(value = "取消微信支付")
    @PostMapping("wx/canclePay")
    public Object canclewxPay() {
    	JSONObject jsonParams = getJsonRequest();


    	String orderNo= jsonParams.getString("orderNo");

		Assert.isNull(orderNo, "订单号不能为空");
		GoodsOrderVo orderInfo = orderService.queryObjectByNo(orderNo);
		 if (orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_CANCEL
	        		||orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_PAYTIMEOUT
	        		||orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_ADMINCANCEL
	        		) {
	            return toResponsObject(400, "订单已取消", "");
	        }

        
        if (orderInfo.getPayStatus() != TradeStatus.WAIT_BUYER_PAY_ING.code) {
            return toResponsObject(400, "订单已经支付完成，无法取消", "");
        }
    	
        if (orderInfo == null) {
            return toResponsFail("订单不存在");
        } else   if (orderInfo.getPayStatus() != TradeStatus.WAIT_BUYER_PAY_ING.code) {
            return toResponsFail("订单状态不正确orderStatus" + orderInfo.getOrderStatus() + "payStatus" + orderInfo.getPayStatus());
        }

        // 业务处理
        // 取消改为未付款
        orderInfo.setPayStatus(TradeStatus.WAIT_BUYER_PAY.code);
        int updateNum = orderService.update(orderInfo);
        
        
        if (updateNum > 0) {
            return toResponsMsgSuccess("修改订单取消支付成功");
        } else {
            return toResponsFail("修改订单取消支付失败");
        }
    } 
    
    /**
     * 微信查询订单状态
     */
    @ApiOperation(value = "查询订单状态")
    @PostMapping("wx/query")
    public Object orderQuerywx(@LoginUser UserVo loginUser,Integer orderId) {
    	JSONObject jsonParams = getJsonRequest();
    	String orderNo= jsonParams.getString("orderNo");

		Assert.isNull(orderNo, "订单号不能为空");
		GoodsOrderVo orderInfo = orderService.queryObjectByNo(orderNo);
        if (orderInfo == null) {
            return toResponsFail("订单不存在");
        }
        Map<Object, Object> parame = new TreeMap<Object, Object>();
      //小程序的appID
//        parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));//小程序的appID
        //公众号的appID
        parame.put("appid", ResourceUtil.getConfigByName("mp.appId"));//公众号的appID
        // 商家账号。
        parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", orderInfo.getOrderNo());

        String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        logger.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.orderquery"), xml));
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);
        logger.info("return_code:" + return_code);
        logger.info("return_msg:" + return_msg);
        if (!"SUCCESS".equals(return_code)) {
            return toResponsFail("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
//            GoodsOrderVo orderInfo = new GoodsOrderVo();
//            orderInfo.setId(orderId);
//            orderInfo.setPay_status(2);
//            orderInfo.setOrder_status(201);
//            orderInfo.setShipping_status(0);
//            orderInfo.setPay_time(new Date());
//            orderService.update(orderInfo);
        	
            // 付款成功
            orderInfo.setPayStatus(TradeStatus.TRADE_SUCCESS.code);
//            orderInfo.setPayStatus(OrderStatusConstant.Order_Pay_Status_Yes);
            orderService.update(orderInfo);
            
        	
            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            Integer num = (Integer) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId + "");
            if (num == null) {
                J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId + "", 1);
                this.orderQuerywx(loginUser, orderId);
            } else if (num <= 3) {
                J2CacheUtils.remove(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId);
                this.orderQuerywx(loginUser, orderId);
            } else {
                return toResponsFail("查询失败,error=" + trade_state);
            }

        } else {
            // 失败
            return toResponsFail("查询失败,error=" + trade_state);
        }

        return toResponsFail("查询失败，未知错误");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "wx/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void wxnotify(HttpServletRequest request, HttpServletResponse response) {
    	  logger.info("--------微信订单回调接口-- 开始了--");
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");
            logger.info("--------微信订单回调接口-----请求reponseXml:" + reponseXml);
            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);
            String paySn=result.getOut_trade_no();
    		PaymentInfoEntity localPayment = paymentService.findBySn(paySn);
            String result_code = result.getResult_code();
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付失败");
    			paymentPluginFacade.PaymentLogSubmit(paySn, 24, "notify 返回验证失败", request,reponseXml);
    			paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_FAILURE);
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付成功");
               /* // 业务处理
                GoodsOrderVo orderInfo = orderService.queryObjectByNo(out_trade_no);
                
                // 付款成功
                orderInfo.setOrderStatus(OrderStatusConstant.Order_Status_fahuo);
                orderInfo.setPayStatus(PayStatusConstant.Pay_Status_Pay_Success);
                orderService.update(orderInfo);*/
                
                

                PaymentInfoEntity payment=paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_FINISHED);
  			    dealPayFinished(localPayment, payment,OrderConstant.ORDER_PAY_TYPE_ONLINE_WXPAY);
                
               
                logger.info("--------微信订单回调接口-- 结束后返回数据--"+setXml("SUCCESS", "OK"));
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
    /**
     * 支付宝订单回调接口
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "支付宝订单回调接口")
    @RequestMapping(value = "ali/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void alinotify(HttpServletRequest request, HttpServletResponse response) {
    	  logger.info("--------支付宝回调接口-- 开始了--");
        try {

			logger.info("支付宝支付回调数据开始");
			String notifyContext="";
			String paySn=request.getParameter("out_trade_no");
			paymentPluginFacade.PaymentLogSubmit(paySn, 2, "notify 第二步：支付宝支付返回", request);
			
			PaymentInfoEntity localPayment = paymentService.findBySn(paySn);
			if (localPayment == null) {
				logger.info("localPayment is null");
				paymentPluginFacade.PaymentLogSubmit(paySn, 24, "notify 返回验证失败", request);
				notifyContext="success";
				this.buildBussinessText(response, notifyContext);;
			}
			logger.info("localPayment"+ObjToStringUtil.objToString(localPayment));
			
			String trade_status = request.getParameter("trade_status");
			
			if(trade_status.equals("TRADE_FINISHED")){
				if (localPayment.getStatus().equals(TradeStatus.TRADE_FINISHED.code)){
					paymentPluginFacade.PaymentLogSubmit(paySn, 2, "第二步：已经交易完成【TRADE_FINISHED】，不做任何操作处理，记录日志" + trade_status,request);
				}else{
					 PaymentInfoEntity payment=paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_FINISHED);
					 dealPayFinished(localPayment, payment,OrderConstant.ORDER_PAY_TYPE_ONLINE_ALPAY);
					 paymentPluginFacade.PaymentLogSubmit(paySn, 2, "第二步：已经交易完成【TRADE_FINISHED】",request);
					logger.info("deail alpay success over");
				}
				
			}else if(trade_status.equals("TRADE_SUCCESS")){
				if (localPayment.getStatus().equals(TradeStatus.TRADE_SUCCESS.code)){
					paymentPluginFacade.PaymentLogSubmit(paySn, 2, "第二步：已经交易成功【TRADE_SUCCESS】，不做任何操作处理，记录日志" + trade_status,request);
				}else{
					PaymentInfoEntity payment=paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_SUCCESS);
					dealPayFinished(localPayment, payment,OrderConstant.ORDER_PAY_TYPE_ONLINE_ALPAY);
					paymentPluginFacade.PaymentLogSubmit(paySn, 2, "第二步：已经交易成功【TRADE_SUCCESS】",request);
					logger.info("deail alpay success over");
				}
				
			}else if(trade_status.equals("TRADE_CLOSED")){
				
				if (localPayment.getStatus().equals(TradeStatus.TRADE_FINISHED.code)||
						localPayment.getStatus().equals(TradeStatus.TRADE_SUCCESS.code)
							){
						paymentPluginFacade.PaymentLogSubmit(paySn, 2, "第二步：已经交易完成【TRADE_FINISHED】，不做任何操作处理，记录日志" + trade_status,
								request);
					}else{
						paymentPluginFacade.PaymentLogSubmit(paySn, 3, "第二步：已经交易完成【TRADE_CLOSED】" + trade_status,
								request);
						paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_CLOSED);
						logger.info("deail wxpay fail over");
						this.buildBussinessText(response, "failure");
					}
			}else {
				if (localPayment.getStatus().equals(TradeStatus.TRADE_FINISHED.code)||
					localPayment.getStatus().equals(TradeStatus.TRADE_SUCCESS.code)
						){
					paymentPluginFacade.PaymentLogSubmit(paySn, 2, "第二步：已经交易完成【TRADE_FINISHED】，不做任何操作处理，记录日志" + trade_status,
							request);
				}else{
					paymentPluginFacade.PaymentLogSubmit(paySn, 3, "第二步：已经交易完成【TRADE_FAILURE】" + trade_status,
							request);
					paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_FAILURE);
					logger.info("deail wxpay fail over");
					this.buildBussinessText(response, "failure");
				}
			}
			this.buildBussinessText(response, "success");
	
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
	 * 
	 * @param localPayment 
	 * @param payment
	 * 
	 * ProductOrderConstant.ORDER_PAY_TYPE_ONLINE_WXPAY
	 */
	private void dealPayFinished(PaymentInfoEntity localPayment,PaymentInfoEntity payment,final int payType) {
		 if(payment.getOrderType().equals(OrderConstant.ORDER_TYPE_SHOPPING)){
				
				try {
					paymentPluginFacade.dealOrder(localPayment, TradeStatus.TRADE_FINISHED, new PayWorkCallable<PaymentInfoEntity>() {
								@Override
								public void onSuccess(PaymentInfoEntity payment) {
									
									mallFacade.paySuccessOrder(payment.getOrderNo(),payType);
									 
								}
								@Override
								public void onFail(PaymentInfoEntity obj) {
									logger.info(" onFail 支付成功！");
								}
							});
						} catch (Exception e) {
					}
				}else if(payment.getOrderType().equals(OrderConstant.ORDER_TYPE_ARTIST_SHOPPING)){
					
					try {
						paymentPluginFacade.dealOrder( localPayment, TradeStatus.TRADE_FINISHED, new PayWorkCallable<PaymentInfoEntity>() {
									@Override
									public void onSuccess(PaymentInfoEntity payment) {
										
										mallFacade.paySuccessOrder(payment.getOrderNo(),payType);
										 
									}
									@Override
									public void onFail(PaymentInfoEntity obj) {
										logger.info(" onFail 支付成功！");
									}
								});
							} catch (Exception e) {
						}
				}else if(payment.getOrderType().equals(OrderConstant.ORDER_TYPE_RECHARGE)){
					
					paymentPluginFacade.dealOrder( localPayment, TradeStatus.TRADE_FINISHED, new PayWorkCallable<PaymentInfoEntity>() {
								@Override
								public void onSuccess(PaymentInfoEntity payment) {
									//充值成功
									int memberId=payment.getUserId().intValue();
//									int addBalance=MoneyFormatUtils.formatBigDecimalToInt(payment.getAmount());//获取充值金额 分
									userService.addUserBalance(memberId, payment.getAmount());
									
								}
								@Override
								public void onFail(PaymentInfoEntity obj) {
									logger.info(" onFail 支付成功！");
								}
							});
				} 
		
		
		
	}
	

	/**
     * 订单退款请求
     */
    @ApiOperation(value = "订单退款请求")
    @PostMapping("wx/refund")
    public Object wxrefund(Integer orderId) {
        //
        GoodsOrderVo orderInfo = orderService.queryObject(orderId);

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getOrderStatus() == 401 || orderInfo.getOrderStatus() == 402) {
            return toResponsObject(400, "订单已退款", "");
        }

//        if (orderInfo.getPay_status() != 2) {
//            return toResponsObject(400, "订单未付款，不能退款", "");
//        }

//        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getId().toString(),
//                orderInfo.getActual_price().doubleValue(), orderInfo.getActual_price().doubleValue());
        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getId().toString(),
                10.01, 10.01);
        if (result.getResult_code().equals("SUCCESS")) {
            if (orderInfo.getOrderStatus() == 201) {
                orderInfo.setOrderStatus(401);
            } else if (orderInfo.getOrderStatus() == 300) {
                orderInfo.setOrderStatus(402);
            }
            orderService.update(orderInfo);
            return toResponsObject(400, "成功退款", "");
        } else {
            return toResponsObject(400, "退款失败", "");
        }
    }


    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }
    
    //---------------------------------------华丽的分割线--------------------------------------------------------
}