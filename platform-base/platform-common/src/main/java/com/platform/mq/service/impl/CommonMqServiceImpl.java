package com.platform.mq.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.platform.mq.model.CommonLogVo;
import com.platform.mq.producer.TaskProducer;
import com.platform.mq.service.ICommonMqService;
import com.platform.utils.StringUtils;

/**
 * 向第三方发送消息的接口
 * 
 * @version 1.0
 */
@Service
public class CommonMqServiceImpl implements ICommonMqService {
	protected final static Log LOG = LogFactory.getLog(CommonMqServiceImpl.class);
	@Override
	public void writeCommonLogToDB(String operator, String operation,
			String content, String ip, String parameter) {
		if (StringUtils.isEmpty(operator) && StringUtils.isEmpty(operation)
				&& StringUtils.isEmpty(content)) {
			return;
		}
		CommonLogVo commonLogVo = new CommonLogVo(operator, operation, content,
				ip, parameter);
		LOG.info("commonLogVo=" + commonLogVo.toString());
		TaskProducer.addCommonLog(commonLogVo, true);
	}

}
