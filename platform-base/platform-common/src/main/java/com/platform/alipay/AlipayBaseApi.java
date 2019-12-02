package com.platform.alipay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.platform.utils.ObjToStringUtil;
import com.platform.utils.StringUtils;


public class AlipayBaseApi {
	protected final static Log LOG = LogFactory
			.getLog(AlipayBaseApi.class);
	private static String  TIME_EXPRESS="90m";
	private static String  CHARSET="utf-8";
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("alipay.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  AliPayBaseConfig getAliPayBaseConfig() {
		 String appId =props.getProperty("appId");// "wxa06286a707aa31c2";
		 String ali_public_key =props.getProperty("ali_public_key");// "fcc6a42d840d50030b52e0dba6797077";
	     String private_key =props.getProperty("private_key") ;//"1246740401";
	     
	     
	     String feeRate =props.getProperty("fee_rate");
	     String notifyUrl =props.getProperty("notify_url");
	     
	     AliPayBaseConfig aliPayBaseConfig = new AliPayBaseConfig();
	     aliPayBaseConfig.setAppId(appId);
	     aliPayBaseConfig.setAli_public_key(ali_public_key);
	     aliPayBaseConfig.setPrivate_key(private_key);
		 
		 if(StringUtils.isEmpty(feeRate)){
			 feeRate="0";
		 }
		 aliPayBaseConfig.setFee_rate(Double.parseDouble(feeRate));
		 aliPayBaseConfig.setNotify_url(notifyUrl);
		 
		return aliPayBaseConfig;
	}
	
	
	/**
	 * 转账订单 
	 * @param paySn 支付订单随机号
	 * @param aliAccount 支付宝账号
	 * @param realName 支付宝用户真实姓名
	 * @param amount 金额 double 0.01
	 * @param showPayTitle 显示名称
	 * @param payBodyDesc 备注内容
	 * @return
	 */
//	public static AlipayFundTransToaccountTransferResponse getAlipayFundTransToaccountTransferRequest(String paySn,String aliAccount,String realName,String amount,String showPayTitle,String payBodyDesc) throws AlipayApiException {
		public static AlipayFundTransToaccountTransferResponse getAlipayFundTransToaccountTransferRequest(String paySn,String aliAccount,String realName,String amount,String showPayTitle,String payBodyDesc)  {
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",aliPayBaseConfig.getAppId(),aliPayBaseConfig.getPrivate_key(),"json",CHARSET,aliPayBaseConfig.getAli_public_key(),"RSA2");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		
		AlipayFundTransToaccountTransferModel bizModel=new AlipayFundTransToaccountTransferModel();
		bizModel.setOutBizNo(paySn);
		bizModel.setAmount(amount);
		bizModel.setPayeeAccount(aliAccount);
		bizModel.setPayeeType("ALIPAY_LOGONID");
		bizModel.setPayeeRealName(realName);
		bizModel.setPayerShowName(showPayTitle);
		bizModel.setRemark(payBodyDesc);
		
		request.setBizModel(bizModel);
		
		AlipayFundTransToaccountTransferResponse response=null;
		try {
			response = alipayClient.execute(request);
			LOG.info("-----------调用支付宝后返回的数据-----response-----------------"+ObjToStringUtil.objToString(response));
			LOG.info("---------getCode-------------"+response.getCode());
			LOG.info("---------getMsg--------------"+response.getMsg());
			LOG.info("---------getSubCode-------------"+response.getSubCode());
			LOG.info("----------getSubMsg-------------"+response.getSubMsg());
			LOG.info("----------getBody-------------"+response.getBody());
//			if(response.isSuccess()){
//				System.out.println("调用成功");
//				LOG.info(ObjToStringUtil.objToString(response));
//				return response;
//				} else {
//				System.out.println("调用失败");
//				LOG.error(ObjToStringUtil.objToString(response));
//			}
		} catch (AlipayApiException e) {
			 //throw new AlipayApiException(e);// update by softopensell for Exception 
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 查询交易订单
	 * @param paySn 我们的生产订单号
	 * @param aliTradeNo 支付宝交易订单号
	 * @return
	 */
	public static AlipayTradeQueryResponse getAlipayTradeQueryRequest(String paySn,String aliTradeNo){
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",aliPayBaseConfig.getAppId(),aliPayBaseConfig.getPrivate_key(),"json",CHARSET,aliPayBaseConfig.getAli_public_key(),"RSA2");
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		
		request.setBizContent("{" +
		"\"out_trade_no\":\""+paySn+"\"," +
		"\"trade_no\":\""+aliTradeNo+"\"" +
		"  }");
		
		AlipayTradeQueryResponse response=null;
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				System.out.println("调用成功");
				LOG.info(ObjToStringUtil.objToString(response));
				return response;
			  } else {
				System.out.println("调用失败");
				LOG.error(ObjToStringUtil.objToString(response));
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	

	/**
	 * 退款
	 * @param paySn 交易号
	 * @param aliTradeNo 支付流水号
	 * @param refundAmount 退款金额
	 * @param refundReason 退款原因
	 * @param outRequestNo 退款序号
	 * @param operatorId 操作员
	 * @param storeId 店号
	 * @param terminalId 门号
	 * @return
	 */
	public AlipayTradeRefundResponse getAlipayTradeRefundResponse(String paySn,String aliTradeNo,String refundAmount,String refundReason,String outRequestNo,String operatorId,String storeId,String terminalId ){
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",aliPayBaseConfig.getAppId(),aliPayBaseConfig.getPrivate_key(),"json",CHARSET,aliPayBaseConfig.getAli_public_key(),"RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
		"\"out_trade_no\":\""+paySn+"\"," +
		"\"trade_no\":\""+aliTradeNo+"\"," +
		"\"refund_amount\":"+refundAmount+"," +
		"\"refund_reason\":\""+refundReason+"\"," +
		"\"out_request_no\":\""+outRequestNo+"\"," +
		"\"operator_id\":\""+operatorId+"\"," +
		"\"store_id\":\""+storeId+"\"," +
		"\"terminal_id\":\""+terminalId+"\"" +
		"  }");
		
		AlipayTradeRefundResponse response = null;
		
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				System.out.println("调用成功");
				LOG.info(ObjToStringUtil.objToString(response));
				return response;
			  } else {
				System.out.println("调用失败");
				LOG.error(ObjToStringUtil.objToString(response));
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	/**
	 * 
	 * @param paySn 交易号
	 * @param aliTradeNo 支付流水号
	 * @param outRequestNo 退款序号
	 * @return
	 */
	public AlipayTradeFastpayRefundQueryResponse getAlipayTradeFastpayRefundQueryRequest(String paySn,String aliTradeNo,String outRequestNo){
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",aliPayBaseConfig.getAppId(),aliPayBaseConfig.getPrivate_key(),"json",CHARSET,aliPayBaseConfig.getAli_public_key(),"RSA2");
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		AlipayTradeFastpayRefundQueryModel bizModel=new AlipayTradeFastpayRefundQueryModel();
		bizModel.setOutRequestNo(outRequestNo);
		bizModel.setTradeNo(aliTradeNo);
		bizModel.setOutRequestNo(outRequestNo);
		request.setBizModel(bizModel);
		
		AlipayTradeFastpayRefundQueryResponse response =null;
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				System.out.println("调用成功");
				LOG.info(ObjToStringUtil.objToString(response));
				return response;
			} else {
				System.out.println("调用失败");
				LOG.error(ObjToStringUtil.objToString(response));
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * 生成APP 支付订单数据
	 * @param paySn
	 * @param paySubject 标题
	 * @param payBody
	 * @param totalAmount
	 * @param notifyUrl
	 * @return
	 */
	public static String getAlipayTradeAppPayRequest(String paySn,String paySubject,String payBody,String totalAmount,String notifyUrl){
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",aliPayBaseConfig.getAppId(),aliPayBaseConfig.getPrivate_key(),"json",CHARSET,aliPayBaseConfig.getAli_public_key(),"RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(payBody);
		model.setSubject(paySubject);
		model.setOutTradeNo(paySn);
		model.setTimeoutExpress(TIME_EXPRESS);
		model.setTotalAmount(totalAmount);
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		        System.out.println(response.getBody());
		        //就是orderString 可以直接给客户端请求，无需再做处理。
		        return response.getBody();
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param paySn 订单号
	 * @param paySubject 标题
	 * @param payBody 描述
	 * @param totalAmount 价格
	 * @param operatorId 操作员
	 * @param storeId 店面
	 * @param terminalId 终端
	 * @param notifyUrl 反馈url
	 * @param goodsDetailes 商品详情
	 * @return
	 */
	public static AlipayTradePrecreateResponse getAlipayTradePrecreateRequest(String paySn,String paySubject,String payBody,String totalAmount,String operatorId,String storeId,String terminalId,String notifyUrl,List<GoodsDetail> goodsDetailes){
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",aliPayBaseConfig.getAppId(),aliPayBaseConfig.getPrivate_key(),"json",CHARSET,aliPayBaseConfig.getAli_public_key(),"RSA2");
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		model.setOutTradeNo(paySn);
		model.setSubject(paySubject);
		model.setBody(payBody);
		model.setTimeoutExpress(TIME_EXPRESS);
		model.setTotalAmount(totalAmount);
		model.setGoodsDetail(goodsDetailes);
		model.setOperatorId(operatorId);
		model.setStoreId(storeId);
		model.setTerminalId(terminalId);
		
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		
		AlipayTradePrecreateResponse response;
		try {
			response = alipayClient.execute(request);
			if(response.isSuccess()){
				System.out.println("调用成功");
				return response;
				} else {
				System.out.println("调用失败");
				return null;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static  Map<String,String>  getNotifyUrl(HttpServletRequest request){
		AliPayBaseConfig aliPayBaseConfig=getAliPayBaseConfig();
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  	}
		    //乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		try {
			boolean flag = AlipaySignature.rsaCheckV1(params, aliPayBaseConfig.getAli_public_key(), CHARSET,"RSA2");
			if(flag){
				System.out.println("验证成功");
				LOG.info(ObjToStringUtil.objToString(params));
				return params;
			}else{
				System.out.println("验证失败");
				LOG.info(ObjToStringUtil.objToString(params));
				return params;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return params;
	}
	
	
}
  
