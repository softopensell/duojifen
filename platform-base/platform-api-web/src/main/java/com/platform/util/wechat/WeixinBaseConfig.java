package com.platform.util.wechat;

public class WeixinBaseConfig {
	private String appId;
	private String appSecret;
	private String callBackUrl;
	private String partner = "";
	//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private String partnerkey = "";
	//openId 是微信用户针对公众号的标识，授权的部分这里不解释
	private String openId = "";
	private String redirectUri="";
	private String weixinRefundPath="";
	public double fee_rate;
	public String notify_url;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCallBackUrl() {
		return callBackUrl;
	}
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}		
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ProjectConfig[appId=").append(appId)
		.append(",callBackUrl=").append(callBackUrl)
		.append(",appSecret=").append(appSecret);
		sb.append("]");
		return sb.toString();
	}
	/**
	 * @return the partner
	 */
	public String getPartner() {
		return partner;
	}
	/**
	 * @param partner the partner to set
	 */
	public void setPartner(String partner) {
		this.partner = partner;
	}
	/**
	 * @return the partnerkey
	 */
	public String getPartnerkey() {
		return partnerkey;
	}
	/**
	 * @param partnerkey the partnerkey to set
	 */
	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * @return the redirectUri
	 */
	public String getRedirectUri() {
		return redirectUri;
	}
	/**
	 * @param redirectUri the redirectUri to set
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	/**
	 * @return the weixinRefundPath
	 */
	public String getWeixinRefundPath() {
		return weixinRefundPath;
	}
	/**
	 * @param weixinRefundPath the weixinRefundPath to set
	 */
	public void setWeixinRefundPath(String weixinRefundPath) {
		this.weixinRefundPath = weixinRefundPath;
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
