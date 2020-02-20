package com.platform.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.cache.RedisCacheUtil;
import com.platform.cache.UserBlackCacheUtil;
import com.platform.entity.SysUserEntity;
import com.platform.entity.TokenEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.SysUserService;
import com.platform.service.TokenService;
import com.platform.service.UserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.HttpClientUtil;
import com.platform.util.wechat.WeixinCacheService;
import com.platform.utils.CharUtil;
import com.platform.utils.auth.GoogleAuthenticator;
import com.platform.validator.ApiAssert;

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
    
    private boolean  checkGoogleCode(String secret,long code) throws ServletException, IOException {
		 long t = System.currentTimeMillis(); 
		 GoogleAuthenticator ga = new GoogleAuthenticator(); 
		 ga.setWindowSize(5); 
		 boolean r = ga.check_code(secret, code, t); 
       return r;
   }
    
    /**
     * 获取用户信息
     */
    @ApiOperation(value = "fwsappp信息")
    @PostMapping("toLoginAppFws")
    public Object toLoginAppFws(@LoginUser UserVo loginUser) {
    	 Map<String, Object> resultObj = new HashMap<String, Object>();
    	 UserEntity userInfo = userService.queryObject(loginUser.getUserId());
    	 String djfToken=CharUtil.getRandomString(32);
    	 RedisCacheUtil.put(djfToken, 10*1000, userInfo.getUserId());
    	 String apiFwsUrl="http://fws.huacaijie.cn/api/auth/addAppToken";
    	 
    	 Map<String, String> params=new HashMap<>();
    	 params.put("djfToken", djfToken);
    	 params.put("userId", userInfo.getAppFwsUserId().toString());
    	 HttpClientUtil.doGet(apiFwsUrl, params);
    	 
    	 resultObj.put("djfToken", djfToken);
    	 
    	 return toResponsSuccess(resultObj);
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
       if(UserBlackCacheUtil.getIsBlackUser(userEntity.getUserId())) {
       	return toResponsFail("对不起，禁止访问，请联系管理员!");
       }
        //生成token
        Map<String, Object> map = tokenService.createToken(userEntity.getUserId());
        userEntity.setAppFwsUserId(0);
        userEntity.setPassword(null);
        userEntity.setPayPassword(null);
        map.put("userInfo", userEntity);
        return toResponsSuccess(map);
    }
    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("sysLoginByToken")
//    @ApiOperation(value = "后台账号免密登录登")
    public Object sysLoginByToken() {
    	JSONObject jsonParams = getJsonRequest();
		String token = jsonParams.getString("token");
		String userName = jsonParams.getString("userName");
		
		SysUserEntity sysUserEntity=sysUserService.queryByUserName(userName);
		UserEntity userEntity=new UserEntity();
        //生成token
        Map<String, Object> map = tokenService.createToken(userEntity.getUserId());
        userEntity.setAppFwsUserId(0);
        userEntity.setPassword(null);
        userEntity.setPayPassword(null);
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
    
}
