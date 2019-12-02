package com.platform.util.wechat;

public class GlobalConstant {
	
	public static String WEBAPP = "water";
	public static String WEBLOGO = "duojifen.huacaijie.net";
	public static String WEBLOGO_CN = "积分兑换";
	public static String WEB_SITE_PATH = "http://duojifen.huacaijie.net/";
	public static String M_SITE_PATH = "http://duojifen.huacaijie.net/";
	public static boolean IS_WATER_CENTER = false;
	public static String STATIC_LOCAL_PATH = "/www/nginx/jyzyszx_static";
	public static String WEB_STATIC_SITE_PATH = "http://duojifen.huacaijie.net/";
	public static String UC_LOGIN_URL = "";
	public static String UC_LOGOUT_URL = "";
	public static String UC_HOME_URL = "";
	public static String ADMIN_UC_LOGIN_URL = "";
	public static String SYSERROR = "";
	
	public static String IS_OPEN_WEIXIN = "1";
	
	public static String IS_OPEN_WEIXIN_OPEN = "1";
	public static String IS_OPEN_WEIXIN_CLOSE = "0";
	
	public static String IS_UPLOAD_STATIC = "0";
	
	public static String IS_UPLOAD_STATIC_YES = "1";
	public static String IS_UPLOAD_STATIC_NO = "0";
	
	public static String IS_OPEN_REDIS = "0";
	
	public static String IS_OPEN_REDIS_YES = "1";
	public static String IS_OPEN_REDIS_NO = "0";
	
	public static String IS_BIND_PHONE = "0";
	
	public static String IS_BIND_PHONE_YES = "1";
	public static String IS_BIND_PHONE_NO = "0";
	
	public static String IS_TEST_WX_PAY = "1";
	public static String IS_TEST_WX_PAY_YES = "1";
	public static String IS_TEST_WX_PAY_NO = "0";
	
	
	public static String getIS_TEST_WX_PAY() {
		return IS_TEST_WX_PAY;
	}

	public static void setIS_TEST_WX_PAY(String iS_TEST_WX_PAY) {
		IS_TEST_WX_PAY = iS_TEST_WX_PAY;
	}

	/**
	 * @return the weblogo
	 */
	public static String getWeblogo() {
		return WEBLOGO;
	}

	/**
	 * @return the weblogoCn
	 */
	public static String getWeblogoCn() {
		return WEBLOGO_CN;
	}

	/**
	 * @return the wEB_SITE_PATH
	 */
	public static String getWEB_SITE_PATH() {
		return WEB_SITE_PATH;
	}

	/**
	 * @return the isWaterCenter
	 */
	public static boolean isWaterCenter() {
		return IS_WATER_CENTER;
	}


	/**
	 * @return the sTATIC_LOCAL_PATH
	 */
	public static String getSTATIC_LOCAL_PATH() {
		return STATIC_LOCAL_PATH;
	}

	/**
	 * @return the wEB_STATIC_SITE_PATH
	 */
	public static String getWEB_STATIC_SITE_PATH() {
		return WEB_STATIC_SITE_PATH;
	}

	/**
	 * @param wEB_SITE_PATH
	 *            the wEB_SITE_PATH to set
	 */
	public static void setWEB_SITE_PATH(String wEB_SITE_PATH) {
		WEB_SITE_PATH = wEB_SITE_PATH;
	}


	/**
	 * @param sTATIC_LOCAL_PATH
	 *            the sTATIC_LOCAL_PATH to set
	 */
	public static void setSTATIC_LOCAL_PATH(String sTATIC_LOCAL_PATH) {
		STATIC_LOCAL_PATH = sTATIC_LOCAL_PATH;
	}

	/**
	 * @param wEB_STATIC_SITE_PATH
	 *            the wEB_STATIC_SITE_PATH to set
	 */
	public static void setWEB_STATIC_SITE_PATH(String wEB_STATIC_SITE_PATH) {
		WEB_STATIC_SITE_PATH = wEB_STATIC_SITE_PATH;
	}




	/**
	 * @return the sYSERROR
	 */
	public static String getSYSERROR() {
		return SYSERROR;
	}

	/**
	 * @param sYSERROR
	 *            the sYSERROR to set
	 */
	public static void setSYSERROR(String sYSERROR) {
		SYSERROR = sYSERROR;
	}

	/**
	 * @return the uC_LOGIN_URL
	 */
	public static String getUC_LOGIN_URL() {
		return UC_LOGIN_URL;
	}

	/**
	 * @return the aDMIN_UC_LOGIN_URL
	 */
	public static String getADMIN_UC_LOGIN_URL() {
		return ADMIN_UC_LOGIN_URL;
	}

	/**
	 * @param uC_LOGIN_URL
	 *            the uC_LOGIN_URL to set
	 */
	public static void setUC_LOGIN_URL(String uC_LOGIN_URL) {
		UC_LOGIN_URL = uC_LOGIN_URL;
	}

	/**
	 * @param aDMIN_UC_LOGIN_URL
	 *            the aDMIN_UC_LOGIN_URL to set
	 */
	public static void setADMIN_UC_LOGIN_URL(String aDMIN_UC_LOGIN_URL) {
		ADMIN_UC_LOGIN_URL = aDMIN_UC_LOGIN_URL;
	}

	/**
	 * @return the uC_HOME_URL
	 */
	public static String getUC_HOME_URL() {
		return UC_HOME_URL;
	}

	/**
	 * @param uC_HOME_URL
	 *            the uC_HOME_URL to set
	 */
	public static void setUC_HOME_URL(String uC_HOME_URL) {
		UC_HOME_URL = uC_HOME_URL;
	}

	/**
	 * @return the wEBLOGO
	 */
	public static String getWEBLOGO() {
		return WEBLOGO;
	}

	/**
	 * @return the wEBLOGO_CN
	 */
	public static String getWEBLOGO_CN() {
		return WEBLOGO_CN;
	}

	/**
	 * @return the iS_WATER_CENTER
	 */
	public static boolean isIS_WATER_CENTER() {
		return IS_WATER_CENTER;
	}

	/**
	 * @param wEBLOGO the wEBLOGO to set
	 */
	public static void setWEBLOGO(String wEBLOGO) {
		WEBLOGO = wEBLOGO;
	}

	/**
	 * @param wEBLOGO_CN the wEBLOGO_CN to set
	 */
	public static void setWEBLOGO_CN(String wEBLOGO_CN) {
		WEBLOGO_CN = wEBLOGO_CN;
	}

	/**
	 * @param iS_WATER_CENTER the iS_WATER_CENTER to set
	 */
	public static void setIS_WATER_CENTER(boolean iS_WATER_CENTER) {
		IS_WATER_CENTER = iS_WATER_CENTER;
	}

	/**
	 * @return the m_SITE_PATH
	 */
	public static String getM_SITE_PATH() {
		return M_SITE_PATH;
	}

	/**
	 * @param m_SITE_PATH the m_SITE_PATH to set
	 */
	public static void setM_SITE_PATH(String m_SITE_PATH) {
		M_SITE_PATH = m_SITE_PATH;
	}

	/**
	 * @return the uC_LOGOUT_URL
	 */
	public static String getUC_LOGOUT_URL() {
		return UC_LOGOUT_URL;
	}

	/**
	 * @param uC_LOGOUT_URL the uC_LOGOUT_URL to set
	 */
	public static void setUC_LOGOUT_URL(String uC_LOGOUT_URL) {
		UC_LOGOUT_URL = uC_LOGOUT_URL;
	}

	/**
	 * @return the iS_OPEN_WEIXIN
	 */
	public static String getIS_OPEN_WEIXIN() {
		return IS_OPEN_WEIXIN;
	}

	/**
	 * @param iS_OPEN_WEIXIN the iS_OPEN_WEIXIN to set
	 */
	public static void setIS_OPEN_WEIXIN(String iS_OPEN_WEIXIN) {
		IS_OPEN_WEIXIN = iS_OPEN_WEIXIN;
	}

	/**
	 * @return the iS_UPLOAD_STATIC
	 */
	public static String getIS_UPLOAD_STATIC() {
		return IS_UPLOAD_STATIC;
	}

	/**
	 * @param iS_UPLOAD_STATIC the iS_UPLOAD_STATIC to set
	 */
	public static void setIS_UPLOAD_STATIC(String iS_UPLOAD_STATIC) {
		IS_UPLOAD_STATIC = iS_UPLOAD_STATIC;
	}

	/**
	 * @return the iS_OPEN_REDIS
	 */
	public static String getIS_OPEN_REDIS() {
		return IS_OPEN_REDIS;
	}

	/**
	 * @param iS_OPEN_REDIS the iS_OPEN_REDIS to set
	 */
	public static void setIS_OPEN_REDIS(String iS_OPEN_REDIS) {
		IS_OPEN_REDIS = iS_OPEN_REDIS;
	}

	/**
	 * @return the wEBAPP
	 */
	public static String getWEBAPP() {
		return WEBAPP;
	}

	/**
	 * @param wEBAPP the wEBAPP to set
	 */
	public static void setWEBAPP(String wEBAPP) {
		WEBAPP = wEBAPP;
	}

	/**
	 * @return the iS_BIND_PHONE
	 */
	public static String getIS_BIND_PHONE() {
		return IS_BIND_PHONE;
	}

	/**
	 * @param iS_BIND_PHONE the iS_BIND_PHONE to set
	 */
	public static void setIS_BIND_PHONE(String iS_BIND_PHONE) {
		IS_BIND_PHONE = iS_BIND_PHONE;
	}
}