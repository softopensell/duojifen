package com.platform.entity;

public class APIMemberQr {
	private String qrCode;
	private int qrStatus;//0 表示金额不足，1 OK 自己二维码 ，2 归属二维码
	private String tipMessage;
	private String qrCodeUrl;//二维码链接地址
	/**
	 * 
	 */
	public APIMemberQr() {
	}
	/**
	 * @return the qrCode
	 */
	public String getQrCode() {
		return qrCode;
	}
	/**
	 * @param qrCode the qrCode to set
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	/**
	 * @return the qrStatus
	 */
	public int getQrStatus() {
		return qrStatus;
	}
	/**
	 * @param qrStatus the qrStatus to set
	 */
	public void setQrStatus(int qrStatus) {
		this.qrStatus = qrStatus;
	}
	/**
	 * @return the tipMessage
	 */
	public String getTipMessage() {
		return tipMessage;
	}
	/**
	 * @param tipMessage the tipMessage to set
	 */
	public void setTipMessage(String tipMessage) {
		this.tipMessage = tipMessage;
	}
	/**
	 * @return the qrCodeUrl
	 */
	public String getQrCodeUrl() {
		return qrCodeUrl;
	}
	/**
	 * @param qrCodeUrl the qrCodeUrl to set
	 */
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}
	
}
  
