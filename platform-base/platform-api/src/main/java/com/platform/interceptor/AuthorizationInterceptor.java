package com.platform.interceptor;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.platform.annotation.FrequencyLimit;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.SingleLock;
import com.platform.cache.RedisCacheUtil;
import com.platform.cache.UserBlackCacheUtil;
import com.platform.cache.UserCacheLockUtil;
import com.platform.entity.TokenEntity;
import com.platform.service.TokenService;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.RequestUtil;
import com.platform.utils.ResourceUtil;

/**
 * 权限(Token)验证
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-03-23 15:38
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private static final Log log = LogFactory.getLog(HandlerInterceptorAdapter.class);
    private String userLockKey="userLockKey_";
    
	@Autowired
    private TokenService tokenService;

    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
    public static final String LOGIN_TOKEN_KEY = "X-Nideshop-Token";
    @Override
   	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
   			throws Exception {
    	 log.info("----------AuthorizationInterceptor----------afterCompletion-----------------" );
    	 SingleLock singleLock=null;
         if (handler instanceof HandlerMethod) {
         	singleLock = ((HandlerMethod) handler).getMethodAnnotation(SingleLock.class);
         	
         	Integer userId=(Integer) request.getAttribute(LOGIN_USER_KEY);
         	
         	if(singleLock!=null&&userId!=null) {
         		String key=userLockKey+userId;
             	Lock lock =UserCacheLockUtil.getKey(key);
             	lock.unlock();
             	log.info("----------------"+key+"-------------lock.unlock()----" );
             }
         }
        
   		super.afterCompletion(request, response, handler, ex);
   	}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
        String requestParams = JsonUtil.getJsonByObj(RequestUtil.getParameters(request));
    	log.info("----------AuthorizationInterceptor----------preHandle--------参数请求---------:" + requestParams);
    	String originHeader = request.getHeader("Origin");
        //支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Origin", originHeader);
		response.setHeader("Access-Control-Max-Age", "0");  
		response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,X-URL-PATH,X-Nideshop-Token");  
		response.setHeader("Access-Control-Allow-Credentials", "true");  
		response.setHeader("XDomainRequestAllowed","1");   
		response.setHeader("XDomainRequestAllowed","1");  
    	
        IgnoreAuth annotation;
        //系统为何时间
        Date todayDate = new Date();
		Date tempDate=DateUtils.getStartOfDate(todayDate);
		Date startDate=DateUtils.getDateBetweenHour(tempDate, 8);
		Date endDate=DateUtils.getDateBetweenHour(tempDate, 23);
		boolean flag=DateUtils.isBetweenHour(startDate, endDate,todayDate);
		
		boolean isOnline=true;
		if(ResourceUtil.getConfigByName("sys.demo").equals("1")){
			isOnline=false;
        }
        if (!flag&&isOnline) {
        	if (handler instanceof HandlerMethod) {
                annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
            } else {
                return true;
            }
    	     returnJson(response,488,"系统结算与维护中!");
    		 return false;
        }
        
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
          //如果有@IgnoreAuth注解，则不验证token
            if (annotation != null) {
                return true;
            }
        } 
        
        //从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(LOGIN_TOKEN_KEY);
            log.info("-------------------如果header中不存在token，则从参数中获取token---getParameter.token--:"+token);
        }
        //token为空
        if (StringUtils.isBlank(token)) {
    	     returnJson(response,401,"请先登录!");
    	     return false;
        }
        //查询token信息
        TokenEntity tokenEntity = tokenService.queryByToken(token);
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            returnJson(response,401,"token失效,请先登录!");
   	     	return false;
        }
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(LOGIN_USER_KEY, tokenEntity.getUserId());
        
        if(UserBlackCacheUtil.getIsBlackUser(tokenEntity.getUserId())) {
       	  returnJson(response,405,"对不起，禁止访问，请联系管理员!");
   	      return false;
        }
        
        FrequencyLimit frequencyLimit=null;
        if (handler instanceof HandlerMethod) {
        	frequencyLimit = ((HandlerMethod) handler).getMethodAnnotation(FrequencyLimit.class);
        	if(frequencyLimit!=null) {
        		int userId=tokenEntity.getUserId();
            	String action=frequencyLimit.action();
        		int forbiddenTime=frequencyLimit.forbiddenTime();
        		int maxtime=frequencyLimit.time();
        		int maxCount=frequencyLimit.count();
        		boolean pass=RedisCacheUtil.frequencyLimit(action, userId, maxtime, maxCount, forbiddenTime);
        		log.info("----------frequencyLimit---------pass:"+pass);
            	if(!pass) {
            		 returnJson(response,405,"有效时间类访问太频繁，稍后再试!");
            	     return false;
            	}
            }
        }
        SingleLock singleLock=null;
        if (handler instanceof HandlerMethod) {
        	singleLock = ((HandlerMethod) handler).getMethodAnnotation(SingleLock.class);
        	if(singleLock!=null&&tokenEntity.getUserId()!=null) {
        		String key=userLockKey+tokenEntity.getUserId();
            	Lock lock =UserCacheLockUtil.getKey(key);
            	lock.lock();
            	log.info("----------------"+key+"-------------lock.lock()----" );
            }
        }
        
        return true;
    }
    public void returnJson(HttpServletResponse response,int code,String message) {
    	 Map<String, Object> resultObj = new HashMap<String, Object>();
		 resultObj.put("errno", code);
		 resultObj.put("errmsg", message);
	     String jsonByObj = JsonUtil.getJsonByObj(resultObj);
	     response.setContentType("text/html;charset=UTF-8");
	     response.setCharacterEncoding("UTF-8");
	     try {
	    	log.info("------toResponsObject------------:" + jsonByObj);
			response.getWriter().write(jsonByObj);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
