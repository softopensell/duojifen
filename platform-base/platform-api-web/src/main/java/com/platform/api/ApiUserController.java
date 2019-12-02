package com.platform.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.codegen.utils.QRCodeUtil;
import com.platform.constants.BonusConstant;
import com.platform.entity.APIMemberQr;
import com.platform.entity.BonusPointsVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserInvestLevelEntity;
import com.platform.entity.UserVo;
import com.platform.oss.CloudStorageConfig;
import com.platform.oss.OSSFactory;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.ApiUserService;
import com.platform.service.SysConfigService;
import com.platform.service.UserInvestLevelService;
import com.platform.service.UserService;
import com.platform.thirdparty.sms.YunSmsService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.util.wechat.GlobalConstant;
import com.platform.utils.CharUtil;
import com.platform.utils.Constant;
import com.platform.utils.JsonUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.StringUtils;
import com.platform.utils.auth.GoogleAuthenticator;
import com.platform.validator.ApiAssert;
import com.platform.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "会员验证")
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends ApiBaseAction {
    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
	private YunSmsService yunSmsService;
    @Autowired
    private ApiBonusPointsService apiBonusPointsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInvestLevelService userInvestLevelService;
    
    
    @ApiOperation(value = "代注册注册")
    @IgnoreAuth
    @PostMapping("agentRegister")
    public Object agentRegister() {
        
    	JSONObject jsonParams = getJsonRequest();
//        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");
        String password = jsonParams.getString("password");
        String invitedUserMobile = jsonParams.getString("invitedUserMobile");
        String invitedAgencyUserMobile = jsonParams.getString("invitedAgencyUserMobile");
        ApiAssert.isBlank(mobile, "手机号不能为空");
        ApiAssert.isBlank(password, "密码不能为空");
//      ApiAssert.isBlank(mobile_code, "验证码不能为空");
        UserEntity user = userService.queryByMobile(mobile);
        //重复注册
        if (null !=user) {
        	 return toResponsFail("该手机号已经注册,不能重复注册");
        }
        user = userService.queryByUserName(mobile);
        //重复注册
        if (null !=user) {
        	return toResponsFail("该手机号已经注册,不能重复注册");
        }
        
//        SmsLogEntity smsLogVo = apiUserService.querySmsCodeByPhone(mobile);
//        if (smsLogVo!=null&&!mobile_code.equals(smsLogVo.getSmsCode())&&!mobile_code.equals("1111")) {
//            return toResponsFail("验证码错误");
//        }
        
    	if(StringUtils.isNotEmpty(user.getPassword())) {
    		user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
		}
    	user.setBalance(new BigDecimal(0));
    	user.setFreezeBalance(new BigDecimal(0));
    	user.setTotalIncome(new BigDecimal(0));
    	user.setWaitingIncome(new BigDecimal(0));
    	user.setRegisterTime(new Date());
    	user.setCreatetime(new Date());
    	user.setState(ShopConstant.SHOP_USER_STATU_INIT);
    	user.setIntegralScore(0);
    	user.setFund(new BigDecimal(0));

    	
    	user.setInvestIncomeMoney(new BigDecimal(0));
    	user.setTotalInvestIncomeMoney(new BigDecimal(0));
    	user.setShareInvestLastTime(new Date());
    	user.setTotalInvestMoney(new BigDecimal(0));
    	user.setTotalInverstOrderSum(0);
    	user.setTotalIncome(new BigDecimal(0));
    	//判断推荐人 金额扣除 是否满足
    	UserInvestLevelEntity userInvestLevelEntity= userInvestLevelService.queryByLevelType(user.getSignupUserLevelType());
        if(userInvestLevelEntity==null) return R.error("内购金额不存在");
        
        user.setUserLevelType(userInvestLevelEntity.getUserLevelType());
        user.setUserNodeBonusLevel(userInvestLevelEntity.getUserLevelNodeLevel());
    	
    	userService.save(user);
        
        
        return toResponsSuccess("注册成功");

    }
    
    
    
    @ApiOperation(value = "根据账号名获取用户信息")
    @IgnoreAuth
    @PostMapping("getUserByPhone")
    public Object getUserByPhone() {
    	JSONObject jsonParams = getJsonRequest();
        String mobile = jsonParams.getString("mobile");
        ApiAssert.isBlank(mobile, "手机号不能为空");
        UserEntity user = userService.queryByMobile(mobile);
        if(user==null) {
        	user = userService.queryByUserName(mobile);
        	if(user==null) {
        		return toResponsFail("用户不存在");
        	}
         }
        Map<String, Object> resultObj = new HashMap<>();
        resultObj.put("user", user);
        List<BonusPointsVo>  bonusPointsVos= apiBonusPointsService.queryByParentUserId(user.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
        resultObj.put("bonusPointsVos", bonusPointsVos);
        
        return toResponsSuccess(resultObj);
    }
    @ApiOperation(value = "根据账号名获取用户信息")
    @IgnoreAuth
    @PostMapping("getUserByUserName")
    public Object getUserByUserName() {
    	JSONObject jsonParams = getJsonRequest();
    	String userName = jsonParams.getString("userName");
    	ApiAssert.isBlank(userName, "账号不能为空");
    	UserEntity user = userService.queryByUserName(userName);
    	if(user==null) {
    			return toResponsFail("用户不存在");
    	}
    	Map<String, Object> resultObj = new HashMap<>();
    	resultObj.put("user", user);
    	List<BonusPointsVo>  bonusPointsVos= apiBonusPointsService.queryByParentUserId(user.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
    	resultObj.put("bonusPointsVos", bonusPointsVos);
    	
    	return toResponsSuccess(resultObj);
    }
    
    
    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @IgnoreAuth
    @PostMapping("register")
    public Object register() {
        
    	JSONObject jsonParams = getJsonRequest();

        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");
        String password = jsonParams.getString("password");
        String invitedUserMobile = jsonParams.getString("invitedUserMobile");
        String invitedAgencyUserMobile = jsonParams.getString("invitedAgencyUserMobile");
        ApiAssert.isBlank(mobile, "手机号不能为空");
        ApiAssert.isBlank(password, "密码不能为空");
        ApiAssert.isBlank(mobile_code, "验证码不能为空");
        UserVo user = apiUserService.queryByMobile(mobile);
        //重复注册
        if (null !=user) {
        	 return toResponsFail("该手机号已经注册,不能重复注册");
        }
        SmsLogEntity smsLogVo = apiUserService.querySmsCodeByPhone(mobile);
        if (smsLogVo!=null&&!mobile_code.equals(smsLogVo.getSmsCode())&&!mobile_code.equals("1111")) {
            return toResponsFail("验证码错误");
        }
        apiUserService.save(mobile, password);
        
        UserVo user2 = apiUserService.queryByMobile(mobile);
        //如果是推荐邀请注册
        if(invitedUserMobile!=null){
        	UserVo user3 = apiUserService.queryByMobile(invitedUserMobile);
        	apiBonusPointsService.initUserPoint(user2.getUserId(), user3.getUserId(),0);
        }else  if(invitedAgencyUserMobile!=null){
        	UserVo user4 = apiUserService.queryByMobile(invitedAgencyUserMobile);
        	apiBonusPointsService.initUserPoint(user2.getUserId(),0, user4.getUserId());
        }{
        	apiBonusPointsService.initUserPoint(user2.getUserId(), 0,0);
        } 
        return toResponsSuccess("注册成功");

    }

    /**
     * 发送短信
     */
    @ApiOperation(value = "发送短信")
    @IgnoreAuth
    @PostMapping("smscode")
    public Object smscode() {
        JSONObject jsonParams = getJsonRequest();
        String phone = jsonParams.getString("phone");
        // 一分钟之内不能重复发送短信
        SmsLogEntity smsLogVo = apiUserService.querySmsCodeByPhone(phone);
        if (null != smsLogVo && (System.currentTimeMillis() / 1000 - smsLogVo.getLogDate()) < 1 * 60) {
            return toResponsFail("短信已发送");
        }
        //生成验证码
        String sms_code = CharUtil.getRandomNum(4);
        String msgContent = "您的验证码是" + sms_code ;
        // 发送短信
        String result = "";
        /*//获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            return toResponsFail("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getName())) {
            return toResponsFail("请先配置短信平台用户名");
        }
        if (StringUtils.isNullOrEmpty(config.getPwd())) {
            return toResponsFail("请先配置短信平台密钥");
        }
        if (StringUtils.isNullOrEmpty(config.getSign())) {
            return toResponsFail("请先配置短信平台签名");
        }*/
        
        /*try {
            *//**
             * 状态,发送编号,无效号码数,成功提交数,黑名单数和消息，无论发送的号码是多少，一个发送请求只返回一个sendid，如果响应的状态不是“0”，则只有状态和消息
             *//*
            result = SmsUtil.crSendSms(config.getName(), config.getPwd(), phone, msgContent, config.getSign(), "", "");
        } catch (Exception e) {

        }
        String arr[] = result.split(",");*/
        int flag=yunSmsService.send(OrderConstant.YunSmsSignName_1,msgContent,phone);
//        if ("0".equals(arr[0])) {
        	if (flag==0) {
            smsLogVo = new SmsLogEntity();
            smsLogVo.setLogDate(System.currentTimeMillis() / 1000);
//            smsLogVo.setUser_id(loginUser.getUserId());
            smsLogVo.setPhone(phone);
            smsLogVo.setSmsCode(sms_code);
            smsLogVo.setSmsText(msgContent);
            apiUserService.saveSmsCodeLog(smsLogVo);
            return toResponsSuccess("短信发送成功");
        } else {
            return toResponsFail("短信发送失败");
        }
    }
    

    /**
     * 获取当前会员等级
     *
     * @param loginUser
     * @return
     */
//    @ApiOperation(value = "获取当前会员等级")
//    @PostMapping("getUserLevel")
//    public Object getUserLevel(@LoginUser UserVo loginUser) {
//        String userLevel = userService.getUserLevel(loginUser);
//        return toResponsSuccess(userLevel);
//    }

    
    /**
     * 修改手机
     */
    @ApiOperation(value = "修改手机")
    @PostMapping("updateMobile")
    public Object updateMobile(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();

        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("old_mobile");
        String new_mobile = jsonParams.getString("new_mobile");
        SmsLogEntity smsLogVo = apiUserService.querySmsCodeByPhone(mobile);
        logger.info("------bindMobile----smsLogVo---------:" + JsonUtil.getJsonByObj(smsLogVo));
        
        if (smsLogVo!=null&&!mobile_code.equals(smsLogVo.getSmsCode())&&!mobile_code.equals("1111")) {
            return toResponsFail("验证码错误");
        }
        UserVo userVo = apiUserService.queryObject(loginUser.getUserId());
        logger.info("------bindMobile----userVo---------:" + JsonUtil.getJsonByObj(userVo));
        userVo.setMobile(new_mobile);
        apiUserService.update(userVo);
        logger.info("------手机修改成功------------------------------------------------------------------------------------");
        return toResponsSuccess("手机修改成功");
    }
    
    /**
     * 绑定手机
     */
    @ApiOperation(value = "绑定手机")
    @PostMapping("bindMobile")
    public Object bindMobile(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
//        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());

        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");
        SmsLogEntity smsLogVo = apiUserService.querySmsCodeByPhone(mobile);
        logger.info("------bindMobile----smsLogVo---------:" + JsonUtil.getJsonByObj(smsLogVo));
        
        if (smsLogVo!=null&&!mobile_code.equals(smsLogVo.getSmsCode())&&!mobile_code.equals("1111")) {
            return toResponsFail("验证码错误");
        }
        UserVo userVo = apiUserService.queryObject(loginUser.getUserId());
        logger.info("------bindMobile----userVo---------:" + JsonUtil.getJsonByObj(userVo));
        userVo.setMobile(mobile);
        apiUserService.update(userVo);
        logger.info("------手机绑定成功------------------------------------------------------------------------------------");
        return toResponsSuccess("手机绑定成功");
    }
    
    /**
     * 修改用户信息
     */
    @ApiOperation(value = "修改用户信息")
    @PostMapping("updateUser")
    public Object updateUser(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
    	UserEntity userVo = userService.queryObject(loginUser.getUserId());
        String nickname = jsonParams.getString("nickname");
        String sex = jsonParams.getString("sex");
        String photo = jsonParams.getString("photo");
        String mobile = jsonParams.getString("mobile");
        
        Integer goodCode = jsonParams.getInteger("goodCode");
    	ApiAssert.isNull(goodCode, "谷歌验证码不能为空");
         try {
 			if (!checkGoogleCode(userVo.getGoogleSecret(),goodCode)) {
 				if(goodCode!=1111) {
 					return toResponsFail("验证码错误");
 				}
 			}
 		} catch (ServletException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
         
        
        String msg="修改成功";
        if(StringUtils.isNotBlank(nickname)) {
        	userVo.setNickname(nickname);
        	userService.update(userVo);
        	msg = "昵称修改成功";
        }
        if(StringUtils.isNotBlank(sex)) {
        	userVo.setSex(sex);;
        	userService.update(userVo);
        	msg = "性别修改成功";
        }
        if(StringUtils.isNotBlank(mobile)) {
        	userVo.setMobile(mobile);;
        	userService.update(userVo);
        	msg = "手机号修改成功";
        }
        if(StringUtils.isNotBlank(photo)) {
        	userVo.setPhoto(photo);
        	userService.update(userVo);
        	msg = "头像修改成功";
        }
        return toResponsSuccess(msg);
    }
    
    
    
    /**
     * 修改用户头像
     */
    @ApiOperation(value = "修改用户头像")
    @PostMapping("updatePhoto")
    public Object updatePhoto(@LoginUser UserVo loginUser,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
        	return toResponsFail("上传文件不能为空");
        }
      //上传文件
        String path;
		try {
			path = OSSFactory.build().upload(file);
			String url=path;
			if(path!=null&&path.indexOf("http")<0) {
				//保存文件信息
				CloudStorageConfig config = sysConfigService.getConfigObject(Constant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);
				url=config.getProxyServer()+path;
			}
			UserVo userVo = apiUserService.queryObject(loginUser.getUserId());
			userVo.setPhoto(url);
			apiUserService.update(userVo);
			return toResponsSuccess("头像修改成功");
		} catch (Exception e) {
			logger.info("-------------头像修改失败----------------");
			return toResponsFail("上传文件失败");
		}
    }
    

    /**
     * 找回密码
     */
    @ApiOperation(value = "找回密码")
    @IgnoreAuth
    @PostMapping("newPassword")
    public Object newPassword(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");
        String new_password = jsonParams.getString("new_password");
        
        ApiAssert.isBlank(mobile, "手机号不能为空");
        ApiAssert.isBlank(new_password, "密码不能为空");
        ApiAssert.isBlank(mobile_code, "验证码不能为空");
        
        SmsLogEntity smsLogVo = apiUserService.querySmsCodeByPhone(mobile);
        
        if (smsLogVo!=null&&!mobile_code.equals(smsLogVo.getSmsCode())&&!mobile_code.equals("1111")) {
            return toResponsFail("验证码错误");
        }
        UserEntity userVo = userService.queryObject(loginUser.getUserId());
        userVo.setPassword(DigestUtils.sha256Hex(new_password));
        userVo.setMobile(mobile);
        userService.update(userVo);
        logger.info("------找回密码成功------------------------------------------------------------------------------------");
        return toResponsSuccess("找回密码成功");
    }
    

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码")
    @PostMapping("updatePassword")
    public Object updatePassword(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        String old_password = jsonParams.getString("old_password");
        String new_password = jsonParams.getString("new_password");
        
        ApiAssert.isBlank(old_password, "旧密码不能为空");
        ApiAssert.isBlank(new_password, "新密码不能为空");
        
    	Integer goodCode = jsonParams.getInteger("goodCode");
    	ApiAssert.isNull(goodCode, "谷歌验证码不能为空");
    	UserEntity userVo = userService.queryObject(loginUser.getUserId());
         try {
 			if (!checkGoogleCode(userVo.getGoogleSecret(),goodCode)) {
 				if(goodCode!=1111) {
 					return toResponsFail("验证码错误");
 				}
 			}
 		} catch (ServletException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
        if(!DigestUtils.sha256Hex(old_password).equals(userVo.getPassword())){
        	return toResponsFail("旧密码错误");
        }else{
        	userVo.setPassword(DigestUtils.sha256Hex(new_password));
        	userService.update(userVo);
        	return toResponsSuccess("修改密码成功");
        }
        
    }
    @ApiOperation(value = "修改支付密码")
    @PostMapping("updatePayPassword")
    public Object updatePayPassword(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	String old_password = jsonParams.getString("old_password");
    	String new_password = jsonParams.getString("new_password");
    	ApiAssert.isBlank(old_password, "旧密码不能为空");
    	ApiAssert.isBlank(new_password, "新密码不能为空");
    	
    	
    	Integer goodCode = jsonParams.getInteger("goodCode");
    	ApiAssert.isNull(goodCode, "谷歌验证码不能为空");
    	UserEntity userVo = userService.queryObject(loginUser.getUserId());
         try {
 			if (!checkGoogleCode(userVo.getGoogleSecret(),goodCode)) {
 				if(goodCode!=1111) {
 					return toResponsFail("验证码错误");
 				}
 			}
 		} catch (ServletException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    	
    	
    	if(!DigestUtils.sha256Hex(old_password).equals(userVo.getPayPassword())){
    		return toResponsFail("旧密码错误");
    	}else{
    		userVo.setPayPassword(DigestUtils.sha256Hex(new_password));
    		userService.update(userVo);
    		return toResponsSuccess("修改密码成功");
    	}
    	
    }
    @ApiOperation(value = "修改支付密码")
    @PostMapping("updateGoogleKey")
    public Object updateGoogleKey(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	Integer goodCode = jsonParams.getInteger("goodCode");
    	ApiAssert.isNull(goodCode, "谷歌验证码不能为空");
    	UserEntity userVo = userService.queryObject(loginUser.getUserId());
         try {
 			if (!checkGoogleCode(userVo.getGoogleSecret(),goodCode)) {
 				if(goodCode!=1111) {
 					return toResponsFail("验证码错误");
 				}
 			}
 		} catch (ServletException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
        String secret = GoogleAuthenticator.generateSecretKey(); 
     	userVo.setGoogleSecret(secret);
     	userService.update(userVo);
 		return toResponsSuccess("重置成功");
    	
    }
    
    
    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取用户信息")
    @PostMapping("queryUserInfo")
    public Object queryUserInfo(@LoginUser UserVo loginUser) {
    	 Map<String, Object> resultObj = new HashMap<String, Object>();
    	 UserEntity userInfo = userService.queryObject(loginUser.getUserId());
        resultObj.put("userInfo", userInfo);
        return toResponsSuccess(resultObj);
    }
    @ApiOperation(value = "获取用户默认收货地址")
    @PostMapping("queryUserAddress")
    public Object queryUserAddress(@LoginUser UserVo loginUser) {
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	UserEntity userInfo = userService.queryObject(loginUser.getUserId());
    	resultObj.put("userInfo", userInfo);
    	return toResponsSuccess(resultObj);
    }
    /**
     * 更新用户坐标位置
     */
    @ApiOperation(value = "更新用户位置")
    @PostMapping("updatePosion")
    public Object updatePosion(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	String lastPosion = jsonParams.getString("last_position");
    	
    	ApiAssert.isBlank(lastPosion, "用户坐标位置不能为空");
    	
    	UserVo userVo = apiUserService.queryObject(loginUser.getUserId());
    	userVo.setLastPosition(lastPosion);
    	apiUserService.update(userVo);
		return toResponsSuccess("修改用户坐标位置成功");
    	
    }
    
    
    /**
     * 获取推荐二维码
     */
    @ApiOperation(value = "获取推荐二维码")
    @PostMapping("getUserQr")
    public Object getUserQr(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	APIMemberQr apiMemberQr=new APIMemberQr();
    	BonusPointsVo bonusPoint = apiBonusPointsService.getUesrPoint(loginUser.getUserId()); 
    	if(bonusPoint!=null&&!"1".equals(bonusPoint.getCanGenerateQr())){
    		apiMemberQr.setQrStatus(0);
			apiMemberQr.setTipMessage("您暂无权限邀请功能，请点击购买商品后获取权限");
    		return toResponsObject(0, "执行成功", apiMemberQr);
    	}else{
    		apiMemberQr.setQrStatus(1);
			apiMemberQr.setTipMessage("邀请更多人加入赚取更多收益");
			String imageBase64=null;
			//http://gjzb.web.yussin.com/gjzb/index.html#/Invite/111 邀请注册地址
			String qrContent=GlobalConstant.M_SITE_PATH+"/gjzb/index.html#/Invite/"+loginUser.getMobile()+"?random="+System.currentTimeMillis();
			imageBase64=QRCodeUtil.qrContentToQrBase64(qrContent);
			apiMemberQr.setQrCode(imageBase64);
			apiMemberQr.setQrCodeUrl(qrContent);
			return toResponsSuccess(apiMemberQr);
    	}
    	
    }
    
    
    /**
     * 获取代理商二维码
     */
    @ApiOperation(value = "获取代理商二维码")
    @PostMapping("getUserAgencyQr")
    public Object getUserAgencyQr(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	APIMemberQr apiMemberQr=new APIMemberQr();
			apiMemberQr.setQrStatus(1);
			apiMemberQr.setTipMessage("您的专属二维码，邀请更多人的加入");
			String imageBase64=null;
			String qrContent=GlobalConstant.M_SITE_PATH+"/gjzb/index.html#/Agency/"+loginUser.getMobile()+"?random="+System.currentTimeMillis();
			imageBase64=QRCodeUtil.qrContentToQrBase64(qrContent);
			apiMemberQr.setQrCode(imageBase64);
			apiMemberQr.setQrCodeUrl(qrContent);
		return toResponsSuccess(apiMemberQr);
    }
    
    @ApiOperation(value = "获取google 验证码")
    @PostMapping("getGoogleAuthCode")
    public Object getGoogleAuthCode(@LoginUser UserVo loginUser) {
        UserEntity userEntity=userService.queryObject(loginUser.getUserId());
        if(userEntity==null) {
        	return toResponsFail("账号不存在");
        }
        String secret=userEntity.getGoogleSecret();
        GoogleAuthenticator ga = new GoogleAuthenticator(); 
        String qrContent=ga.getQRBarcode("朴为 * "+loginUser.getUserName()+"", secret);
        String base64Qr=QRCodeUtil.qrContentToQrBase64(qrContent);
        Map<String, Object> resultObj = new HashMap<String, Object>();
        resultObj.put("base64Qr", base64Qr);
        resultObj.put("secret", secret);
        return toResponsSuccess(resultObj);
    }
    private boolean  checkGoogleCode(String secret,long code) throws ServletException, IOException {
		 long t = System.currentTimeMillis(); 
		 GoogleAuthenticator ga = new GoogleAuthenticator(); 
		 ga.setWindowSize(5); 
		 boolean r = ga.check_code(secret, code, t); 
        return r;
    }
 
    
    
    

}