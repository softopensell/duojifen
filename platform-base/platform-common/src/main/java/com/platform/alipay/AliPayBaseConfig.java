package com.platform.alipay;


public class AliPayBaseConfig {
	public String appId="";
	public String private_key="";
	public String ali_public_key="";
	public double fee_rate;
	public String notify_url;
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * @return the private_key
	 */
	public String getPrivate_key() {
		return private_key;
	}
	/**
	 * @return the ali_public_key
	 */
	public String getAli_public_key() {
		return ali_public_key;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @param private_key the private_key to set
	 */
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	/**
	 * @param ali_public_key the ali_public_key to set
	 */
	public void setAli_public_key(String ali_public_key) {
		this.ali_public_key = ali_public_key;
	}
	/**
	 * @return the fee_rate
	 */
	public double getFee_rate() {
		return fee_rate;
	}
	/**
	 * @return the notify_url
	 */
	public String getNotify_url() {
		return notify_url;
	}
	/**
	 * @param fee_rate the fee_rate to set
	 */
	public void setFee_rate(double fee_rate) {
		this.fee_rate = fee_rate;
	}
	/**
	 * @param notify_url the notify_url to set
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
	
}
