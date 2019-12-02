package com.platform.mq.model;


/**
 * 待写入的日志对象
 *
 * @version 1.1
 * 	2015-07-08  增加日志写入功能  add by softopensell
 */
public class CommonLogVo extends ThirdPartyTask {
	String operator;
	String operation;
	String content;
	String ip;
	String parameter;
	/**
	 * 
	 * @param operator
	 * @param operation
	 * @param content
	 * @param ip
	 * @param parameter
	 */
	public CommonLogVo(String operator,String operation,String content,String ip,String parameter ) {
		super();
		this.operator = operator;
		this.operation = operation;
		this.content = content;
		this.ip = ip;
		this.parameter = parameter;
	}



	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}



	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}



	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}



	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}



	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}



	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}



	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}



	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}



	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}



	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public String toString() {
		return "日志参数 [commonLogId="+ this.getTaskID() + 
				", operator=" + operator + 
				", operation=" + operation + 
				", content=" + content +
				", ip=" + ip + 
				", parameter=" + parameter +  "]";
	}
	
}
