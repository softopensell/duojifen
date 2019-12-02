package com.platform.mq.service;


public interface ICommonMqService {
	/**
	 * 
	 * @param operator 
	 * @param operation
	 * @param content
	 * @param ip
	 * @param parameter
	 */
	public void writeCommonLogToDB(String operator,String operation,String content,String ip,String parameter);
}
