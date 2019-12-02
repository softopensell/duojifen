package com.platform.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.JsonObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.TokenEntity;
import com.platform.service.TokenService;
import com.platform.utils.ApiRRException;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.ObjToStringUtil;
import com.platform.utils.RequestUtil;

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

    @Autowired
    private TokenService tokenService;

    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
    public static final String LOGIN_TOKEN_KEY = "X-Nideshop-Token";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	 String requestParams = JsonUtil.getJsonByObj(RequestUtil.getParameters(request));
    	log.info("----------AuthorizationInterceptor----------preHandle--------参数请求---------:" + requestParams);
    	String originHeader = request.getHeader("Origin");
        //支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authentication,x-requested-with,X-Nideshop-Token,X-URL-PATH");
//        response.setHeader("Access-Control-Allow-Origin", "*");
        
        response.setHeader("Access-Control-Allow-Origin", originHeader);
		response.setHeader("Access-Control-Max-Age", "0");  
		response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,X-URL-PATH,X-Nideshop-Token");  
		response.setHeader("Access-Control-Allow-Credentials", "true");  
		response.setHeader("XDomainRequestAllowed","1");   
		response.setHeader("XDomainRequestAllowed","1");  
    	

        IgnoreAuth annotation;
        //系统为何时间
        Date todayDate = new Date();
		Date startDate=DateUtils.getStartOfDate(todayDate);
		startDate=DateUtils.getDateBetweenHour(startDate, 0);
		Date endDate=DateUtils.getDateBetweenHour(startDate, 9);
		boolean flag=DateUtils.isBetweenHour(startDate, endDate,todayDate);
        if (flag) {
        	
        	 Map<String, Object> resultObj = new HashMap<String, Object>();
        	 
    		 resultObj.put("errno", 488);
    		 resultObj.put("errmsg", "系统结算与维护中!");
    	      String jsonByObj = JsonUtil.getJsonByObj(resultObj);
    	     log.info("------toResponsObject----系统结算与维护中----------:" + jsonByObj);
    	     response.setContentType("text/html;charset=UTF-8");
    	     response.setCharacterEncoding("UTF-8");
    	      response.getWriter().write(jsonByObj);
    		 return false;
//            throw new ApiRRException("系统结算与为维护中", 488);
        }
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        //如果有@IgnoreAuth注解，则不验证token
        if (annotation != null) {
            return true;
        }

        //从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        
        log.info("----------AuthorizationInterceptor---------从header中获取token---getHeader.token--:"+token);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(LOGIN_TOKEN_KEY);
            log.info("----------AuthorizationInterceptor---------如果header中不存在token，则从参数中获取token---getParameter.token--:"+token);
        }

        //token为空
        if (StringUtils.isBlank(token)) {
        	log.info("----------AuthorizationInterceptor----------preHandle----用户未登录--请先登录-------:");
//            throw new ApiRRException("请先登录", 401);
            
             Map<String, Object> resultObj = new HashMap<String, Object>();
    		 resultObj.put("errno", 401);
    		 resultObj.put("errmsg", "请先登录!");
    	      String jsonByObj = JsonUtil.getJsonByObj(resultObj);
    	     response.setContentType("text/html;charset=UTF-8");
    	     response.setCharacterEncoding("UTF-8");
    	      response.getWriter().write(jsonByObj);
        }

        //查询token信息
        TokenEntity tokenEntity = tokenService.queryByToken(token);
        log.info("----------AuthorizationInterceptor----------preHandle-------查询token信息--tokenEntity------:"+ObjToStringUtil.objToString(tokenEntity));
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
        	log.info("----------AuthorizationInterceptor----------preHandle-------token失效，请重新登录--------:");
            throw new ApiRRException("token失效，请重新登录", 401);
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(LOGIN_USER_KEY, tokenEntity.getUserId());
//    	log.info("----------AuthorizationInterceptor----------preHandle--------结束了--------:");
        return true;
    }
}
