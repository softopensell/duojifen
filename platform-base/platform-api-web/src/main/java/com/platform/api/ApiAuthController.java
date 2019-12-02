package com.platform.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.APIMemberGetWeixinOpenId;
import com.platform.entity.FullUserInfo;
import com.platform.entity.SysUserEntity;
import com.platform.entity.TokenEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserInfo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.SysUserService;
import com.platform.service.TokenService;
import com.platform.service.UserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiSignUtil;
import com.platform.util.ApiUserUtils;
import com.platform.util.CommonUtil;
import com.platform.util.ObjToStringUtil;
import com.platform.util.Sha1Util;
import com.platform.util.wechat.ApiWeixinBase;
import com.platform.util.wechat.WechatUserApiResult;
import com.platform.util.wechat.WeixinCacheService;
import com.platform.utils.CharUtil;
import com.platform.utils.JsonUtil;
import com.platform.utils.ResourceUtil;
import com.platform.utils.StringUtil;
import com.platform.utils.XmlUtil;
import com.platform.utils.auth.GoogleAuthenticator;
import com.platform.validator.ApiAssert;
import com.qiniu.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API登录授权
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-03-23 15:31
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController extends ApiBaseAction {
    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
	private WeixinCacheService weixinCache;
    private boolean  checkGoogleCode(String secret,long code) throws ServletException, IOException {
		 long t = System.currentTimeMillis(); 
		 GoogleAuthenticator ga = new GoogleAuthenticator(); 
		 ga.setWindowSize(5); 
		 boolean r = ga.check_code(secret, code, t); 
       return r;
   }
    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public Object login() {
    	
    	JSONObject jsonParams = getJsonRequest();
		String userName = jsonParams.getString("userName");
		String password = jsonParams.getString("password");
		Long code = jsonParams.getLong("code");
    	
        ApiAssert.isBlank(userName, "账号不能为空");
        ApiAssert.isBlank(password, "密码不能为空");
        ApiAssert.isNull(code, "Goole验证码不能为空");
        UserEntity userEntity=userService.queryByUserName(userName);
        if (null == userEntity) {
            return toResponsFail("账号不存在");
        }
        if(code!=1111) {
        	try {
			boolean flag=checkGoogleCode(userEntity.getGoogleSecret(),code);
			if(!flag)return this.toResponsFail("Goole验证失败");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        if(!DigestUtils.sha256Hex(password).equals(userEntity.getPassword())){
        	return toResponsFail("密码错误");
        }
        
        //生成token
        Map<String, Object> map = tokenService.createToken(userEntity.getUserId());
        map.put("userInfo", userEntity);
        return toResponsSuccess(map);
    }
    
    
    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("sysLoginByToken")
    @ApiOperation(value = "后台账号免密登录登")
    public Object sysLoginByToken() {
    	JSONObject jsonParams = getJsonRequest();
		String token = jsonParams.getString("token");
		String userName = jsonParams.getString("userName");
		
		SysUserEntity sysUserEntity=sysUserService.queryByUserName(userName);
//		if(sysUserEntity.getUserId().equals(1111)) {
//			
//		}
		
		UserEntity userEntity=new UserEntity();
        //生成token
        Map<String, Object> map = tokenService.createToken(userEntity.getUserId());
        map.put("userInfo", userEntity);
        return toResponsSuccess(map);
    }
    
    /**
     * 退出注销
     */
    @PostMapping("logout")
    @ApiOperation(value = "退出注销接口")
    public Object logout(@LoginUser UserVo loginUser)  {
        //当前时间
        Date now = new Date();

        //过期时间
        Date expireTime = new Date(now.getTime());
    	TokenEntity tokenEntity = tokenService.queryByUserId(loginUser.getUserId());
         tokenEntity.setUpdateTime(now);
         tokenEntity.setExpireTime(expireTime);
         //更新token
         tokenService.update(tokenEntity);
    	
        logger.info("------退出注销成功------------------------------------------------------------------------------------");
        return toResponsSuccess("退出注销成功");
    }
    

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin() {
        JSONObject jsonParam = this.getJsonRequest();
        String jsonByObj = JsonUtil.getJsonByObj(jsonParam);
        logger.info("----微信授权登录login_by_weixin--请求参数----jsonParam----------:" + jsonByObj);
        
        FullUserInfo fullUserInfo = null;
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        if (null != jsonParam.get("userInfo")) {
            fullUserInfo = jsonParam.getObject("userInfo", FullUserInfo.class);
        }
        if (null == fullUserInfo) {
            return toResponsFail("登录失败");
        }

        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        UserInfo userInfo = fullUserInfo.getUserInfo();

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》组合token为：" + requestUrl);
        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);
        logger.info("----微信授权登录login_by_weixin--过程对象--CommonUtil.httpsRequest-sessionData---:" + JsonUtil.getJsonByObj(sessionData));

        if (null == sessionData || StringUtils.isNullOrEmpty(sessionData.getString("openid"))) {
            return toResponsFail("登录失败");
        }
        //验证用户信息完整性
        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
        if (!fullUserInfo.getSignature().equals(sha1)) {
            return toResponsFail("登录失败");
        }
        Date nowTime = new Date();
        UserVo userVo = apiUserService.queryByOpenId(sessionData.getString("openid"));
        logger.info("----微信授权登录login_by_weixin--过程对象--1111----userVo--111-----:" + JsonUtil.getJsonByObj(userVo));

        if (null == userVo) {
            userVo = new UserVo();
            userVo.setUserName("微信用户" + CharUtil.getRandomString(12));
            userVo.setPassword(sessionData.getString("openid"));
            userVo.setRegisterTime(nowTime);
            userVo.setRegisterIp(this.getClientIp());
            userVo.setLastLoginIp(userVo.getRegisterIp());
            userVo.setLastLoginTime(userVo.getRegisterTime());
            userVo.setWeixinOpenid(sessionData.getString("openid"));
            userVo.setAvatar(userInfo.getAvatarUrl());
            userVo.setSex(userInfo.getGender().toString()); // //性别 0：未知、1：男、2：女
            userVo.setNickname(userInfo.getNickName());
            apiUserService.save(userVo);
        } else {
            userVo.setLastLoginIp(this.getClientIp());
            userVo.setLastLoginTime(nowTime);
            apiUserService.update(userVo);
        }
        logger.info("----微信授权登录login_by_weixin--过程对象------userVo-------:" + JsonUtil.getJsonByObj(userVo));
        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (null == fullUserInfo || StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        resultObj.put("token", token);
        resultObj.put("userInfo", fullUserInfo);
        resultObj.put("userId", userVo.getUserId());
        logger.info("----微信授权登录login_by_weixin--返回参数--------------:" + JsonUtil.getJsonByObj(resultObj));
        return toResponsSuccess(resultObj);
    }
    

    /**
     * 获取微信Openid
     */
    @ApiOperation(value = "获取微信Openid")
    @IgnoreAuth
    @PostMapping("getOpenId")
    public Object getOpenId() {
        JSONObject jsonParam = this.getJsonRequest();
        String jsonByObj = JsonUtil.getJsonByObj(jsonParam);
        logger.info("---- 获取微信getOpenId--请求参数----jsonParam----------:" + jsonByObj);
        
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        ApiAssert.isBlank(code, "code不能为空");

        Map<String, Object> resultObj = new HashMap<String, Object>();

        //获取openid
//        String requestUrl = ApiUserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
//        logger.info("》》》组合token为：" + requestUrl);
//        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);
        //-------------------------------------------------------------
		
		Map<String, Object> resMap = ApiWeixinBase.getUserAccessToken(code);
		// 验证用户是否通过token
		String accessToken = (String) resMap.get("access_token");
		// 刷新Token
		String refreshToken = (String) resMap.get("refresh_token");
		// 微信ID
		String openid = (String) resMap.get("openid");
		logger.info("微信授权登录getOpenId--过程对象--resMap=:"+ ObjToStringUtil.objToString(resMap));
		
		APIMemberGetWeixinOpenId apiMemberGetWeixinOpenId=new APIMemberGetWeixinOpenId();
		apiMemberGetWeixinOpenId.setAccessToken(accessToken);
		apiMemberGetWeixinOpenId.setOpenid(openid);
		apiMemberGetWeixinOpenId.setRefreshToken(refreshToken);
		

        if (null == resMap || StringUtils.isNullOrEmpty(openid)) {
            return toResponsFail("获取OpenId失败");
        }

        resultObj.put("openid", openid);
        resultObj.put("apiMemberGetWeixinOpenId", apiMemberGetWeixinOpenId);
        logger.info("----微信授权登录login_by_weixin--返回参数--------------:" + JsonUtil.getJsonByObj(resultObj));
        return toResponsSuccess(resultObj);
    }
    
    /**
     * 获取微信配置信息
     */
    @ApiOperation(value = "获取微信配置信息")
    @IgnoreAuth
    @PostMapping("getWxConfig")
    public Object getWxConfig() {
        JSONObject jsonParam = this.getJsonRequest();
        String jsonByObj = JsonUtil.getJsonByObj(jsonParam);
        logger.info("---- 获取微信getWxConfig--请求参数----jsonParam----------:" + jsonByObj);
        
        String url = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("url"))) {
        	url = jsonParam.getString("url");
        }
        ApiAssert.isBlank(url, "url不能为空");

        Map<String, Object> resultObj = new HashMap<String, Object>();

        //获取openid
        String get_weixinToken = weixinCache.getWeixinTokenCache().get_weixinToken();
        logger.info("---- 获取微信getWxConfig--get_weixinToken---------:" + get_weixinToken);
        String jsTicket = weixinCache.getWeixinJsApiTicketCache().get_jsApiTicket();
        logger.info("---- 获取微信getWxConfig--jsTicket---------:" + jsTicket);
        
        
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        String timestamp = Sha1Util.getTimeStamp();
		packageParams.put("noncestr", UUID.randomUUID().toString());
		packageParams.put("jsapi_ticket", jsTicket);
		packageParams.put("timestamp", timestamp);
		packageParams.put("url", url);
        
		String createSign = ApiWeixinBase.createSign(packageParams);
		logger.info("---- 获取微信getWxConfig--createSign---------:" + createSign);
		
		packageParams.put("signature",createSign);
		packageParams.put("appId", ResourceUtil.getConfigByName("mp.appId"));//公众号的appID
		

        resultObj.put("packageParams", packageParams);
        logger.info("----微信授权登录getWxConfig--返回参数--------------:" + JsonUtil.getJsonByObj(resultObj));
        return toResponsSuccess(resultObj);
    }
    
    /**
     * 微信获取用户位置通知接口
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "微信获取用户位置回调接口")
    @RequestMapping(value = "/position/notify", method={RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
   public void positionNotify(HttpServletRequest request, HttpServletResponse response) {
    	  logger.info("--------微信订单回调接口-- 开始了--");
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
   		
            String signature = request.getParameter("signature");/// 微信加密签名
    		String timestamp = request.getParameter("timestamp");/// 时间戳
    		String nonce = request.getParameter("nonce"); /// 随机数
    		String echostr = request.getParameter("echostr"); // 随机字符串
    		  if (ApiSignUtil.checkSignature(signature, timestamp, nonce)) {
        	response.getWriter().print(echostr);
			}
        	response.getWriter().close();
    		
    		
             if( null !=request&&null!=request.getHeader(HttpHeaders.CONTENT_TYPE)){ 
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
	            logger.info("--------微信获取用户位置回调接口------请求reponseXml:" + reponseXml);
	            WechatUserApiResult result = (WechatUserApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatUserApiResult.class);
	            String userOpenId = result.getFromUserName();
	            if(!StringUtil.isBlank(userOpenId)){
	            	UserVo userVo = apiUserService.queryByOpenId(userOpenId);
	            	 
	            	if(!StringUtil.isBlank(result.getLatitude())){
	            		String userPosition = result.getLongitude()+","+result.getLatitude();
	            		logger.info("------------userPosition--------------------:"+userPosition);
	            		userVo.setLastPosition(userPosition);
	            		apiUserService.update(userVo);
	            	}
	            }
            }  
            logger.info("--------------------------------微信获取用户位置回调接口- 结束后返回数据--"+setXml("SUCCESS", "OK"));
            response.getWriter().write(setXml("SUCCESS", "OK"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
    } 
    
    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}
