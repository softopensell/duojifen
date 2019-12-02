package com.platform.thirdparty.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yunpian.sdk.model.ResultDO;
import com.yunpian.sdk.model.SendSingleSmsInfo;
import com.yunpian.sdk.service.SmsOperator;
import com.yunpian.sdk.service.YunpianRestClient;
@Service("yunSmsService")
public class YunSmsServiceImpl extends YunSmsService{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	static YunpianRestClient client;
	static{
		client = new YunpianRestClient();
	}
	public int send(String sign, String content,String phoneNum) {
		SmsOperator smsOperator = client.getSmsOperator();
		sign = "【" + sign + "】";
		logger.info("手机号码【{}】发送内容【{}】发送方式立刻、扩展字段默认null、唯一标识默认null", phoneNum, content);
		// 单条发送
		 ResultDO<SendSingleSmsInfo>  rs= smsOperator.singleSend(phoneNum,sign+content);
		//发送短信，如果是以负号开头就是发送失败。
		if (!rs.isSuccess()){
			logger.info("手机号码【{}】发送内容【{}】失败.错误码【{}】", phoneNum, content, rs.getE());
			return -1;
		}else{
		    logger.info("手机号码【{}】发送内容【{}】发送成功.返回数据【{}】", phoneNum, content, rs.toString());
		}
		return 0;
	}
	
	public static void main(String[] args) {
		YunSmsServiceImpl serviceImpl=new YunSmsServiceImpl();
		serviceImpl.send("宇辰互创", "尊敬的用户，您的验证码是6686。如非本人操作，请忽略本短信", "15901556686");
	}
	
}
