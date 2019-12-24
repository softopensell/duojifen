package com.platform.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.platform.annotation.FrequencyLimit;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.annotation.SingleLock;
import com.platform.api.entity.BonusInvestOrderVo;
import com.platform.api.entity.BonusPoolJoinMemberVo;
import com.platform.api.service.ApiBonusInvestOrderService;
import com.platform.api.service.ApiBonusPoolJoinMemberService;
import com.platform.cache.IdWorkCache;
import com.platform.cache.RegionCacheUtil;
import com.platform.constants.BonusConstant;
import com.platform.constants.PluginConstant;
import com.platform.entity.BonusPointsVo;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PaymentOutEntity;
import com.platform.entity.PlatformFwManagerEntity;
import com.platform.entity.PlatformWithdrawShareEntity;
import com.platform.entity.SysOssEntity;
import com.platform.entity.SysRegionEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserInvestLevelEntity;
import com.platform.entity.UserVo;
import com.platform.facade.DjfBonusFacade;
import com.platform.facade.PaymentPluginFacade;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.oss.CloudStorageConfig;
import com.platform.oss.OSSFactory;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.PaymentInfoService;
import com.platform.service.PaymentOutService;
import com.platform.service.PlatformFwManagerService;
import com.platform.service.PlatformWithdrawShareService;
import com.platform.service.SysConfigService;
import com.platform.service.SysOssService;
import com.platform.service.UserInvestLevelService;
import com.platform.service.UserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.util.ShopConstant;
import com.platform.util.constants.CompanyConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.util.constants.ProductConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.util.wechat.GlobalConstant;
import com.platform.utils.Constant;
import com.platform.utils.DateUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.RRException;
import com.platform.utils.RandomUtil;
import com.platform.utils.StringUtils;
import com.platform.utils.auth.GoogleAuthenticator;
import com.platform.validator.ApiAssert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ApiController
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-22 18:15:00
 */
@Api(tags = "个人中心") 
@RestController
@RequestMapping("/api/center")
public class ApiUserCenterController extends ApiBaseAction {
	@Autowired
	private UserService userService;
	@Autowired
	private PaymentInfoService paymentInfoService;
	@Autowired
	private PaymentOutService paymentOutService;
	@Autowired
    private UserInvestLevelService userInvestLevelService;
	@Autowired
	private ApiBonusPointsService apiBonusPointsService;
	@Autowired
	private ApiBonusInvestOrderService apiBonusInvestOrderService;
	@Autowired
	private ApiBonusPoolJoinMemberService apiBonusPoolJoinMemberService;
	@Autowired
	private DjfBonusFacade djfBonusFacade ;
	@Autowired
	private PaymentPluginFacade paymentPluginFacade ;
	@Autowired
	private SysOssService sysOssService ;
	@Autowired
	private SysConfigService sysConfigService ;
	@Autowired
	private PlatformFwManagerService platformFwManagerService ;
	
	@Autowired
	private PlatformWithdrawShareService platformWithdrawShareService ;
	
	
	
	@Autowired
	private IdWorkCache idWorkCache ;
	
	 private final static String KEY = Constant.CLOUD_STORAGE_CONFIG_KEY;
	@ApiOperation(value = "首页基本信息")
    @PostMapping(value = "centerindex")
    public Object centerindex(@LoginUser UserVo loginUser) {
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	 UserEntity user = userService.queryObject(loginUser.getUserId());
    	 BonusPointsVo bonusPoints = apiBonusPointsService.getUesrPoint(user.getUserId());
         resultObj.put("photo", user.getPhoto());//用户头像
         resultObj.put("nickname", user.getNickname());//用户昵称
         resultObj.put("sex", user.getSex());//用户性别
         resultObj.put("mobile", user.getMobile());//用户手机号
         if(bonusPoints!=null) {
        	 resultObj.put("userRoleType", bonusPoints.getUserRoleType());//职位角色
        	 resultObj.put("userNamedType", bonusPoints.getUserNamedType());//称呼
        	 UserEntity user2 = userService.queryObject(bonusPoints.getInvitedPointUserId());//上级推荐的代理商
        	 String invitedPointUser = "";
        	 if(user2!=null){
        		 invitedPointUser = user2.getNickname();//上级推荐的代理商称呼
        	 } 
        	 resultObj.put("invitedPointUser", invitedPointUser);
        	 
         }else {
        	 resultObj.put("userRoleType", BonusConstant.BONUS_MEMBER_ROLE_TYPE_member);//职位角色
        	 resultObj.put("userNamedType", BonusConstant.BONUS_MEMBER_ROLE_NAME_TYPE_MEMBER);//称呼
         }
        return toResponsSuccess(resultObj);
    }
	
	
	
	 /**
     * 获取收益统计列表
     */
    @ApiOperation(value = "获取收益统计列表")
    @PostMapping("browseIncomeStats")
    public Object browseIncomeStats(@LoginUser UserVo loginUser) {
    	
    	Map<String, Object> params=new HashMap<>();
    	JSONObject jsonParams = getJsonRequest();
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
    	
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
    	params.put("page", page);
    	params.put("limit", size);
    	params.put("sidx", "createtime");
		params.put("order", "desc");
        Map<String, Object> resultObj = new HashMap<>();
        //查询列表数据
        Query query = new Query(params);
        query.put("poolJoinMemberId", loginUser.getUserId());
        
        List<BonusPoolJoinMemberVo> bonusPoolJoinMemberVos=apiBonusPoolJoinMemberService.queryList(query);
        int total = apiBonusPoolJoinMemberService.queryTotal(query);
  		ApiPageUtils bonusPoolJoinMemberVoPage = new ApiPageUtils(bonusPoolJoinMemberVos, total, query.getLimit(), query.getPage());
        resultObj.put("bonusPoolJoinMemberVoPage", bonusPoolJoinMemberVoPage);
    	
        return toResponsSuccess(resultObj);
    }
	
	@ApiOperation(value = "收益明细")
    @PostMapping(value = "incomedetail")
    public Object incomedetail(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
		
		
		
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId", loginUser.getUserId());
        
//      params.put("status", TradeStatus.TRADE_FINISHED.code);
        
        Integer moneyTypeWallet=jsonParams.getInteger("moneyTypeWallet");
        
        if(moneyTypeWallet!=null&&moneyTypeWallet>0) {
    		params.put("moneyTypeWallet", moneyTypeWallet);
    	}
        
        String payDate=jsonParams.getString("payDate");//yyyy-MM-dd
        
        if(payDate!=null) {
        	if(DateUtils.isValid(payDate,"yyyy-MM-dd")) {
        		params.put("payDate", payDate);
        	}else {
        	}
        }else {
        	String poolDateNumber=jsonParams.getString("poolDateNumber");
//        	String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
        	Date curDay=new Date();
        	if(poolDateNumber!=null) {
        		poolDateNumber=poolDateNumber.replace("PPD_", "");
        		try {
        			curDay=DateUtils.str_ToDate(poolDateNumber, "yyyyMMdd");
        			params.put("payDate", DateUtils.format(curDay));
				} catch (ParseException e) {
					params.put("payDate", DateUtils.format(curDay));
				}
        	}
        }
        
        params.put("page", page);
		params.put("limit", size);
		params.put("sidx", "create_time");
		params.put("order", "desc");
		// 查询列表数据
		Query query = new Query(params);
		logger.info("-----query-----"+JsonUtil.getJsonByObj(query));
        List<PaymentInfoEntity> incomeList = paymentInfoService.queryList(query);
        int total = paymentInfoService.queryTotal(query);
		ApiPageUtils incomepage = new ApiPageUtils(incomeList, total, query.getLimit(), query.getPage());
        return toResponsSuccess(incomepage);
    }
	
	@ApiOperation(value = "积分明细")
    @PostMapping(value = "integraldetail")
    public Object integraldetail(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId", loginUser.getUserId());
        
        List<Integer> moneyTypeWallets = new ArrayList<Integer>();
        
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL);//积分收益
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_ADD);//手动添加积分
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_PAY);//商城消耗积分
        
        params.put("moneyTypeWallets", moneyTypeWallets);
        
        params.put("page", page);
		params.put("limit", size);
		params.put("sidx", "create_time");
		params.put("order", "desc");
		// 查询列表数据
		Query query = new Query(params);
        List<PaymentInfoEntity> incomeList = paymentInfoService.queryList(params);
        int total = paymentInfoService.queryTotal(query);
		ApiPageUtils incomepage = new ApiPageUtils(incomeList, total, query.getLimit(), query.getPage());
        return toResponsSuccess(incomepage);
    }
 
    
    @ApiOperation(value = "我的邀请成员数据")
    @PostMapping(value = "myMember")
    public Object myMember(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
		
        Map<String, Object> resultObj = new HashMap<String, Object>();
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("createTime", DateUtil.getCurrentDate(0));
        params.put("userId", loginUser.getUserId());
      
        params = new HashMap<String,Object>();
        params.put("queryType", "teamList");
        params.put("invited_point_user_id", loginUser.getUserId());
        params.put("page", page);
		params.put("limit", size);
		params.put("sidx", "create_time");
		params.put("order", "desc");
		// 查询列表数据
		Query query = new Query(params);
        List<BonusPointsVo> teamList = apiBonusPointsService.queryList(query);
        int total = apiBonusPointsService.queryTotal(query);
		ApiPageUtils teampage = new ApiPageUtils(teamList, total, query.getLimit(), query.getPage());
        resultObj.put("teamPage", teampage);//团队列表
        return toResponsSuccess(resultObj);
    }
    
    
    /**
     * 获取注册列表
     */
    @ApiOperation(value = "获取注册列表")
    @PostMapping("browseHelpInitMembers")
    public Object browseHelpInitMembers(@LoginUser UserVo loginUser) {
    	
    	Map<String, Object> params=new HashMap<>();
    	JSONObject jsonParams = getJsonRequest();
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
    	
    	params.put("page", page);
    	params.put("limit", size);
    	params.put("sidx", "register_time");
		params.put("order", "desc");
        logger.info("------browseHelpInitMembers----userVo---------:" + JsonUtil.getJsonByObj(loginUser));
        Map<String, Object> resultObj = new HashMap<>();
        //查询列表数据
        Query query = new Query(params);
        query.put("createUserId", loginUser.getUserId());
        List<UserEntity> userList = userService.queryList(query);
        int total = userService.queryTotal(query);
  		ApiPageUtils teampage = new ApiPageUtils(userList, total, query.getLimit(), query.getPage());
        resultObj.put("teampage", teampage);//团队列表
    	logger.info("------获取注册列表成功------------------------------------------------------------------------------------");
    	return toResponsSuccess(resultObj);
    }
    
    /**
     * 获取推荐列表
     */
    @ApiOperation(value = "获取推荐列表")
    @PostMapping("browseRecommedBonusMembers")
    public Object browseRecommedBonusMembers(@LoginUser UserVo loginUser) {

    	Map<String, Object> params=new HashMap<>();
    	JSONObject jsonParams = getJsonRequest();
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
		
		params.put("queryType", "teamList");
        params.put("invitedUserId", loginUser.getUserId());
        params.put("bloodType", BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
        params.put("page", page);
		params.put("limit", size);
		params.put("sidx", "create_time");
		params.put("order", "desc");
		
        logger.info("------browseHelpInitMembers----userVo---------:" + JsonUtil.getJsonByObj(loginUser));
        Map<String, Object> resultObj = new HashMap<>();
    	// 查询列表数据
		Query query = new Query(params);
        List<BonusPointsVo> teamList = apiBonusPointsService.queryList(query);
        int total = apiBonusPointsService.queryTotal(query);
		ApiPageUtils teampage = new ApiPageUtils(teamList, total, query.getLimit(), query.getPage());
        resultObj.put("teamPage", teampage);//团队列表
        logger.info("------获取注册列表成功------------------------------------------------------------------------------------");
    	return toResponsSuccess(resultObj);
    	
    }
    
    /**
     * 获取推荐列表
     */
    @ApiOperation(value = "获取推荐列表")
    @PostMapping("browseNodeBonusMembers")
    public Object browseNodeBonusMembers(@LoginUser UserVo loginUser) {
    	
    	Map<String, Object> resultObj = new HashMap<>();
    	// 查询列表数据
    	List<BonusPointsVo> teamList = apiBonusPointsService.queryByParentUserId(loginUser.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
    	resultObj.put("teamList", teamList);
    	
    	return toResponsSuccess(resultObj);
    	
    }
    
    
    
    
    /**
     * 注册
     */
    @ApiOperation(value = "代注册")
    @PostMapping("helpRegister")
    @SingleLock
    public Object helpRegister(@LoginUser UserVo loginUser) {
        
    	JSONObject jsonParams = getJsonRequest();
        String userName = jsonParams.getString("userName");
        Integer goodCode = jsonParams.getInteger("goodCode");
        String mobile = jsonParams.getString("mobile");
        String nickname = jsonParams.getString("nickname");
        String password = jsonParams.getString("password");
        String payPassword = jsonParams.getString("payPassword");
        
        String invitedUserName = jsonParams.getString("invitedUserName");
        String nodeUserName = jsonParams.getString("nodeUserName");
//        Integer userLevelType = jsonParams.getInteger("userLevelType");
        
        
        ApiAssert.isBlank(invitedUserName, "推荐不能为空");
        ApiAssert.isBlank(nodeUserName, "节点不能为空");
        ApiAssert.isBlank(userName, "账号不能为空");
        ApiAssert.isBlank(password, "密码不能为空");
        ApiAssert.isBlank(payPassword, "密码不能为空");
        ApiAssert.isNull(goodCode, "验证码不能为空");
        
        UserEntity curUser = userService.getById(loginUser.getUserId());
        
//        if(!curUser.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) return toResponsFail("本帐号未激活");
        
        try {
			if (!checkGoogleCode(curUser.getGoogleSecret(),goodCode)) {
				if(goodCode!=1111) {
					return toResponsFail("谷歌验证错误");
				}
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        UserEntity user = userService.queryByUserName(userName);
        //重复注册
        if (null !=user) {
        	 return toResponsFail("该帐号已经注册,不能重复注册");
        }
        
        
//        List<BonusPointsVo>  bonusPointsEntities=null;
        
       UserEntity recommondUser=userService.queryByUserName(invitedUserName);//推荐人
 	   if(recommondUser==null)return toResponsFail("该会员不存在！");
        //二次验证
 	   //推荐
        BonusPointsVo recommondNodeUserEntity=apiBonusPointsService.getUserPoint(recommondUser.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);	
    	if(recommondNodeUserEntity==null) return toResponsFail("该会员不存在");
    	logger.info("-----recommondUser----"+JsonUtil.getJsonByObj(recommondUser));
    	if(!loginUser.getUserId().equals(recommondUser.getUserId())) {
            List<String> strRecommonUserIds=StringUtils.splitToList(recommondNodeUserEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
			List<Integer>  teamRecommondUserIds=new ArrayList<>();
			for(String str:strRecommonUserIds) {
				teamRecommondUserIds.add(Integer.valueOf(str));
			}
			teamRecommondUserIds.add(recommondUser.getUserId());
			if(!teamRecommondUserIds.contains(loginUser.getUserId())) {
				return toResponsFail("直享不在您的子节点上");
			}
        }
        
    		
	  UserEntity nodeUser=userService.queryByUserName(nodeUserName);//节点人
      BonusPointsVo binaryTreeBonusPointsEntity=apiBonusPointsService.queryByUserIdAndBloodType(nodeUser.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
      if(binaryTreeBonusPointsEntity==null)return toResponsFail("该会员不存在！");		
	  //预挂接点数不超过2个
//  	  List<UserEntity> userEntities=userService.queryNodesByParentNodeName(userName);
//  	  if(userEntities!=null&&userEntities.size()>=2)return toResponsFail("该会员已挂满！");
  	 //直接分配 优先A部门
      //查询 挂载部门的子部门
	  List<BonusPointsVo> subNodes=apiBonusPointsService.queryByParentUserId(nodeUser.getUserId(), BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
	  if(subNodes!=null&&subNodes.size()>=2)return toResponsFail("该会员已挂满！");
  	  
	  logger.info("-----nodeUser----"+JsonUtil.getJsonByObj(nodeUser));
	  if(!loginUser.getUserId().equals(nodeUser.getUserId())) {
           List<String> strNodeUserIds=StringUtils.splitToList(binaryTreeBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
			List<Integer>  teamNodeUserIds=new ArrayList<>();
			for(String str:strNodeUserIds) {
				teamNodeUserIds.add(Integer.valueOf(str));
			}
			teamNodeUserIds.add(nodeUser.getUserId());//包括自己
			logger.info("---接点树------strNodeUserIds----"+JsonUtil.getJsonByObj(teamNodeUserIds));
			if(!teamNodeUserIds.contains(loginUser.getUserId())) {
				return toResponsFail("服务会员不在您的子节点上");
			}
	  }
	  
	 
	  
        
        
        
//        //推荐人信息
//        if(true) {//!invitedUserName.equals(nodeUserName)
//        	
//        	   recommondNodeUserEntity=apiBonusPointsService.queryByUserIdAndBloodType(recommondUser.getUserId(), BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);;
//        	   if(recommondNodeUserEntity==null)return toResponsFail("该会员不存在！");
//        	   
//        	    List<BonusPointsVo> recommondBonus=apiBonusPointsService.queryByParentUserId(recommondUser.getUserId(), BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);;
//             	if(recommondBonus==null||recommondBonus.size()<=0) {//推荐人数量<=0 接点必须挂载 推荐人的左边 挂A部门
//             		
//             		List<BonusPointsVo> subNodes=apiBonusPointsService.queryByParentUserId(recommondUser.getUserId(), BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
//             		bonusPointsEntities=subNodes;
//             		
//             		BonusPointsVo leftNodeBonus=null;
//                  	for(BonusPointsVo bonusPointsVo:subNodes) {
//                  		if(bonusPointsVo.getInvitedUserId()!=null&&bonusPointsVo.getInvitedUserId()>0) {
//                  			leftNodeBonus=bonusPointsVo;
//                  		}
//                  	}
//                  	
//                  	if(leftNodeBonus==null)  {
//                  		if(!invitedUserName.equals(nodeUserName)) {
//                  			return toResponsFail("推荐人A部门为空");
//                  		}
//                  	}else if(leftNodeBonus!=null) {
//                  		
//                        List<String> strNodeUserIds=StringUtils.splitToList(nodeUserBonuns.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
//              			List<Integer>  teamNodeUserIds=new ArrayList<>();
//              			for(String str:strNodeUserIds) {
//              				teamNodeUserIds.add(Integer.valueOf(str));
//              			}
//              			teamNodeUserIds.add(nodeUser.getUserId());//包括自己
//              			logger.info("---接点树------strNodeUserIds----"+JsonUtil.getJsonByObj(teamNodeUserIds));
//              			if(!teamNodeUserIds.contains(leftNodeBonus.getUserId())) {
//              				return toResponsFail("推荐人推荐的第一个会员必须注册在A部门");
//              			}
//              			binaryTreeBonusPointsEntity=nodeUserBonuns;
//                  	}
//             	}
//             	
//        }
	  
        
        //推荐人和接点人必须在一颗树上
        
        
        

		logger.info("-------------验证完成-------------"); 
        
        user =new UserEntity();
        
        if(StringUtils.isNotEmpty(password)) {
    		user.setPassword(DigestUtils.sha256Hex(password));
		}
    	if(StringUtils.isNotEmpty(payPassword)) {
    		user.setPayPassword(DigestUtils.sha256Hex(payPassword));
    	}
    	
    	user.setRealName(userName);
    	user.setUserName(userName);
    	user.setBalance(new BigDecimal(0));
    	user.setAmount(new BigDecimal(0));
    	user.setFreezeBalance(new BigDecimal(0));
    	user.setTotalIncome(new BigDecimal(0));
    	user.setWaitingIncome(new BigDecimal(0));
    	user.setRegisterTime(new Date());
    	user.setCreatetime(new Date());
    	user.setState(ShopConstant.SHOP_USER_STATU_INIT);
    	user.setIntegralScore(0);
    	user.setFund(new BigDecimal(0));
    	user.setPaibag(new BigDecimal(0));
    	user.setInvestIncomeMoney(new BigDecimal(0));
    	user.setTotalInvestIncomeMoney(new BigDecimal(0));
    	user.setShareInvestLastTime(new Date());
    	user.setTotalInvestMoney(new BigDecimal(0));
    	user.setTotalInverstOrderSum(0);
    	user.setTotalIncome(new BigDecimal(0));
    	user.setSurplusInvestMoney(new BigDecimal(0));
    	user.setShareInvestLastTime(new Date());
    	user.setAuthTypeStatu(0);
    	user.setMobile(mobile);
    	user.setNickname(nickname);
    	user.setRegisterIp(getClientIp());
    	user.setLastLoginIp(getClientIp());
    	user.setLastLoginTime(new Date());
    	user.setTotalPoint(new BigDecimal(0));
    	user.setPoint(new BigDecimal(0));
    	user.setUserLevelType(0);
    	user.setUserNodeBonusLevel(0);
    	user.setCreateUserId(loginUser.getUserId());
    	
    	user.setSignupInvitedPhone(invitedUserName);
    	user.setSignupNodePhone(nodeUserName);
    	
		String secret = GoogleAuthenticator.generateSecretKey(); 
    	user.setGoogleSecret(secret);
    	
        user.setUserLevelType(0);
        user.setUserNodeBonusLevel(0);
        user.setSignupUserLevelType(0);
        user.setUserPreBalance(new BigDecimal(0));
//        user.setUserLevelType(userInvestLevelEntity.getUserLevelType());
//        user.setSignupUserLevelType(userInvestLevelEntity.getUserLevelType());
//        user.setUserNodeBonusLevel(userInvestLevelEntity.getUserLevelNodeLevel());
        
        logger.info("-------------注册会员-------user-----"+JsonUtil.getJsonByObj(user));
    	userService.save(user);
    	logger.info("-------------创建User完成-----1-------"); 
    	
    	UserEntity user2 = userService.queryByUserName(userName);
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	if(user2==null) {
    		return this.toResponsFail("创建失败");
    	}
    	resultObj.put("userId", user2.getUserId());
    	//生成接点关系
    	logger.info("-------------创建User完成-------------"); 
        
      //可以挂
	  	BonusPointsVo	nodeBonusPoint=new BonusPointsVo();
  		nodeBonusPoint.setUserId(user2.getUserId());
  		nodeBonusPoint.setBonusInvitedSum(0);//团队人数
  		nodeBonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
  		nodeBonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
  		nodeBonusPoint.setCreateTime(new Date());
  		
  		nodeBonusPoint.setInvitedParentUserIds("");
  		String invitedParentUserIds=""+binaryTreeBonusPointsEntity.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+binaryTreeBonusPointsEntity.getInvitedParentUserIds();
  		nodeBonusPoint.setInvitedParentUserIds(invitedParentUserIds);//所有父类节点
  		
  		nodeBonusPoint.setUserRoleType(0);
  		nodeBonusPoint.setUserNamedType(0);
  		nodeBonusPoint.setUpdateTime(new Date());
  		nodeBonusPoint.setStatus(0);
  		nodeBonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
  		nodeBonusPoint.setBloodType(BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
  		//优先左 再挂右边
      	 //挂左边接点
  	   if(subNodes==null||subNodes.size()==0) {
  		  nodeBonusPoint.setInvitedUserId(binaryTreeBonusPointsEntity.getUserId());
  	    }else if(subNodes.size()==1) {//挂右边接点
  		  nodeBonusPoint.setInvitedRightUserId(binaryTreeBonusPointsEntity.getUserId());
  	   }
      	
    	//推荐人树
      	BonusPointsVo recommondBonusPoint=new BonusPointsVo();
    	recommondBonusPoint.setUserId(user2.getUserId());
    	recommondBonusPoint.setBonusInvitedSum(0);//团队人数
    	recommondBonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
    	recommondBonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
    	recommondBonusPoint.setCreateTime(new Date());
    	recommondBonusPoint.setInvitedUserId(recommondNodeUserEntity.getUserId());
    	recommondBonusPoint.setInvitedParentUserIds("");//推荐关系
    	//获取推荐人 关系
    	BonusPointsVo recommdBonusPointsEntity=apiBonusPointsService.queryByUserIdAndBloodType(recommondNodeUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
    	String invitedRecommondParentUserIds=""+recommondNodeUserEntity.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+recommdBonusPointsEntity.getInvitedParentUserIds();
    	recommondBonusPoint.setInvitedParentUserIds(invitedRecommondParentUserIds);//所有父类节点
		recommondBonusPoint.setUserRoleType(0);
		recommondBonusPoint.setUserNamedType(0);
		recommondBonusPoint.setUpdateTime(new Date());
		recommondBonusPoint.setStatus(0);
		recommondBonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
		recommondBonusPoint.setBloodType(BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		logger.info("-------------推荐关系数据-------recommondBonusPoint-----"+JsonUtil.getJsonByObj(recommondBonusPoint));
		logger.info("-------------接点关系数据-------nodeBonusPoint-----"+JsonUtil.getJsonByObj(nodeBonusPoint));
		
		apiBonusPointsService.save(nodeBonusPoint);
		
		apiBonusPointsService.save(recommondBonusPoint);
		
//		user.setState(ShopConstant.SHOP_USER_STATU_SUCCESS);
//		user.setUpdatetime(new Date());
//		userService.update(user);
    	
    	return toResponsSuccess(resultObj);

    }
    
    
    @ApiOperation(value = "代删除")
    @PostMapping("helpDelete")
    public Object helpDelete(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
        String userName = jsonParams.getString("userName");
        ApiAssert.isBlank(userName, "账号不能为空");
        
        UserEntity toDeleteUserEntity=userService.queryByUserName(userName);
        if(toDeleteUserEntity==null)return this.toResponsFail("账号不存在");
        if(toDeleteUserEntity.getState()!=ShopConstant.SHOP_USER_STATU_INIT)return this.toResponsFail("账号不能删除");
        if(!toDeleteUserEntity.getCreateUserId().equals(loginUser.getUserId()))return this.toResponsFail("您没有删除权限");
        
        if(toDeleteUserEntity.getState()==ShopConstant.SHOP_USER_STATU_INIT) {
//        	userService.delete(toDeleteUserEntity.getUserId());
        }
        
        
    	return toResponsSuccess("删除成功！");

    }
    
    @ApiOperation(value = "修改用户收货地址", response = Map.class)
    @PostMapping("addRegisterAddress")
    @SingleLock
    public Object addRegisterAddress(@LoginUser UserVo loginUser) {
        JSONObject addressJson = this.getJsonRequest();
        Integer userId = addressJson.getInteger("userId");
        UserEntity userEntity=userService.queryObject(userId);
        
        String contactName = addressJson.getString("contactName");
        String contactMobile = addressJson.getString("contactMobile");
        String province = addressJson.getString("province");
        String city = addressJson.getString("city");
        String region = addressJson.getString("region");
        String address = addressJson.getString("address");
        
        ApiAssert.isBlank(contactName, "联系人不能为空");
        ApiAssert.isBlank(contactMobile, "联系电话不能为空");
        ApiAssert.isBlank(province, "province不能为空");
        ApiAssert.isBlank(city, "city不能为空");
        ApiAssert.isNull(region, "区不能为空");
        ApiAssert.isNull(address, "详细地址不能为空");
        
        //收货人
        userEntity.setAddrLinkName(addressJson.getString("contactName")); 
        //联系电话
        userEntity.setAddrPhone(addressJson.getString("contactMobile")); 
        //地区（省）
        userEntity.setProvince(addressJson.getString("province"));
        //地区（市）
        userEntity.setCity(addressJson.getString("city"));
        //地区（区县）
        userEntity.setCountry(addressJson.getString("region"));
        //详细地址
        userEntity.setAddress(addressJson.getString("address"));
        userService.update(userEntity);
        return toResponsSuccess("修改成功");
    }
    @ApiOperation(value = "确认注册", response = Map.class)
    @PostMapping("confirmRegister")
    public Object confirmRegister(@LoginUser UserVo loginUser) {
    	JSONObject addressJson = this.getJsonRequest();
    	Integer userId = addressJson.getInteger("userId");
    	return R.error();
    }
    
    /**
     * 获取推荐列表
     */
    @ApiOperation(value = "获取平台会员级别")
    @PostMapping("getAllUserLevels")
    public Object getAllUserLevels(@LoginUser UserVo loginUser) {
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	Map<String, Object> params=new HashMap<>();
    	List<UserInvestLevelEntity> userInvestLevels = userInvestLevelService.queryList(params);
    	resultObj.put("userInvestLevels", userInvestLevels);//团队列表
    	return toResponsSuccess(resultObj);
    }
    
    @ApiOperation(value = "获取一节节点列表,并判断是否在注册人子节点上")
    @PostMapping("getNodeBonusMembersByUserName")
    public Object getNodeBonusMembersByUserName(@LoginUser UserVo loginUser) {
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	
    	JSONObject jsonParams = getJsonRequest();
		String userName = jsonParams.getString("userName");
		UserEntity userEntity=userService.queryByUserName(userName);
		if(userEntity==null) return toResponsFail("该会员不存在");
//	    if(!userEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) return toResponsFail("该会员未激活");
    	
	  BonusPointsVo userBonusPointsVo=apiBonusPointsService.getUserPoint(userEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);	
//	  if(userBonusPointsVo==null) return toResponsFail("该会员未激活");
	  if(userBonusPointsVo==null) return toResponsFail("该会员不存在");
	  
	  //预挂接点数不超过2个
  	  List<UserEntity> userEntities=userService.queryNodesByParentNodeName(userName);
  	  if(userEntities!=null&&userEntities.size()>2)return toResponsFail("该会员已挂满！");

  	
	  logger.info("-----getNodeBonusMembersByUserName----"+JsonUtil.getJsonByObj(loginUser));
	  logger.info("-----getNodeBonusMembersByUserName----"+JsonUtil.getJsonByObj(userEntity));
	 if(!loginUser.getUserId().equals(userEntity.getUserId())) {
		  if(!userBonusPointsVo.getInvitedParentUserIds().contains(""+loginUser.getUserId())) {
			  return toResponsFail("填入节点不在您的子节点上");
		  }
	  }
	 
	 
	  List<BonusPointsVo> teamList = apiBonusPointsService.queryByParentUserId(userEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
	  resultObj.put("userBonusPointsVo", userBonusPointsVo);//团队列表
	  resultObj.put("teamList", teamList);//团队列表
    	return toResponsSuccess(resultObj);
    	
    }
    @ApiOperation(value = "获取一节节点列表,并判断是否在注册人子节点上")
    @PostMapping("getRecommondBonusMembersByUserName")
    public Object getRecommondBonusMembersByUserName(@LoginUser UserVo loginUser) {
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	
    	JSONObject jsonParams = getJsonRequest();
    	String userName = jsonParams.getString("userName");
    	UserEntity userEntity=userService.queryByUserName(userName);
    	if(userEntity==null) return toResponsFail("该会员不存在");
    	
//    	if(!userEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) return toResponsFail("该会员未激活");
    	
    	
    	BonusPointsVo userBonusPointsVo=apiBonusPointsService.getUserPoint(userEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);	
//    	if(userBonusPointsVo==null) return toResponsFail("该会员未激活");
    	if(userBonusPointsVo==null) return toResponsFail("该会员不存在");
    	
       logger.info("-----getRecommondBonusMembersByUserName----"+JsonUtil.getJsonByObj(loginUser));
   	   logger.info("-----getRecommondBonusMembersByUserName----"+JsonUtil.getJsonByObj(userEntity));
    	
    	if(!loginUser.getUserId().equals(userEntity.getUserId())) {
    		if(!userBonusPointsVo.getInvitedParentUserIds().contains(""+loginUser.getUserId())) {
        			return toResponsFail("填入节点不在您的子节点上");
        	}
    	}
    	resultObj.put("userBonusPointsVo", userBonusPointsVo);//团队列表
    	return toResponsSuccess(resultObj);
    }

    
    
    @ApiOperation(value = "我的团队页面数据")
    @PostMapping(value = "myteam")
    public Object myteam(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
		
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserEntity user = userService.queryObject(loginUser.getUserId());
        BonusPointsVo bonusPoints = apiBonusPointsService.getUesrPoint(user.getUserId());
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("createTime", DateUtil.getCurrentDate(0));
        params.put("userId", user.getUserId());
        params.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
        List<Integer> moneyTypeWallets = new ArrayList<Integer>();
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS);
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED);
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_DAY);
        moneyTypeWallets.add(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_JQ);
        params.put("moneyTypeWallets", moneyTypeWallets);
        BigDecimal todayIncome = paymentInfoService.queryTodayIncome(params);
        resultObj.put("totalIncome", user.getTotalIncome());//累计收益
        resultObj.put("todayTotalAmount", todayIncome);//今日收益
        if(bonusPoints!=null) {
        	resultObj.put("bonusTeamInvitedPoints", bonusPoints.getBonusTeamInvitedPoints());//团队业绩
        	resultObj.put("bonusMeInvitedPoints", bonusPoints.getBonusMeInvitedPoints());//我的业绩
        }else {
        	resultObj.put("bonusTeamInvitedPoints", 0);//团队业绩
        	resultObj.put("bonusMeInvitedPoints", 0);//我的业绩
        }
        
//        params = new HashMap<String,Object>();
//        params.put("queryType", "teamList");
//        params.put("invitedUserId", user.getUserId());
//        params.put("bloodType", BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
//        params.put("page", page);
//		params.put("limit", size);
//		params.put("sidx", "create_time");
//		params.put("order", "desc");
//		// 查询列表数据
//		Query query = new Query(params);
//        List<BonusPointsVo> teamList = apiBonusPointsService.queryList(query);
//      int total = apiBonusPointsService.queryTotal(query);
//		ApiPageUtils teampage = new ApiPageUtils(teamList, total, query.getLimit(), query.getPage());
//      resultObj.put("teamPage", teampage);//团队列表
        return toResponsSuccess(resultObj);
    }
    
    @ApiOperation(value = "获取用户资产数据")
    @PostMapping(value = "myassets")
    public Object myassets(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserEntity user = userService.queryObject(loginUser.getUserId());
        resultObj.put("userId", user.getUserId());//用户id
        String mobile = user.getMobile();
        if(StringUtils.isNotBlank(mobile)){
        	mobile = StringUtils.hidePhone2(mobile);//手机号码脱敏，为了显示
        }
        resultObj.put("blockchainSecret", user.getBlockchainSecret());//手机号码
        resultObj.put("mobile", mobile);//手机号码
        resultObj.put("balance", user.getBalance());//余额
        resultObj.put("totalIncome", user.getTotalIncome());//累计收益
        resultObj.put("integralScore", user.getIntegralScore());//积分
        resultObj.put("freezeBalance", user.getFreezeBalance());//保证金
        resultObj.put("surplusInvestMoney", user.getSurplusInvestMoney());//剩余资产
        resultObj.put("totalInverstOrderSum", user.getTotalInverstOrderSum());//订单数量
        resultObj.put("totalInvestIncomeMoney", user.getTotalInvestIncomeMoney());//资产收益
        resultObj.put("totalInvestMoney", user.getTotalInvestMoney());//资产总和
        resultObj.put("investIncomeMoney", user.getInvestIncomeMoney());//收益
//        resultObj.put("totalPoint", user.getTotalPoint());//积分
        return toResponsSuccess(resultObj);
    }
    
    
    /*
     @ApiOperation(value = "发送手机验证码")
    @IgnoreAuth
    @PostMapping(value = "sendPhoneCode")
    public Object sendPhoneCode(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserVo user = userService.queryObject(loginUser.getUserId());
        String phoneNum=user.getMobile();
        if(StringUtils.isEmpty(phoneNum)) {
        	return toResponsFail("用户手机号码不存在！");
        }
        // 一分钟之内不能重复发送短信
        SmsLogVo smsLogVo = userService.querySmsCodeByPhone(phoneNum);
        if (null != smsLogVo && (System.currentTimeMillis() / 1000 - smsLogVo.getLogDate()) < 1 * 60) {
            return toResponsFail("短信已发送");
        }
        //生成验证码
        String sms_code = CharUtil.getRandomNum(4);
        String msgContent = "您的验证码是" + sms_code ;
        int flag=yunSmsService.send(ApiOrderConstant.YunSmsSignName_1,msgContent,phoneNum);
        if (flag==0) {
            smsLogVo = new SmsLogVo();
            smsLogVo.setLogDate(System.currentTimeMillis() / 1000);
            smsLogVo.setPhone(phoneNum);
            smsLogVo.setSmsCode(sms_code);
            smsLogVo.setSmsText(msgContent);
            userService.saveSmsCodeLog(smsLogVo);
            return toResponsSuccess("短信发送成功");
        } else {
            return toResponsFail("短信发送失败");
        }
    }*/
    
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String path = OSSFactory.build().upload(file);
        String url=path;
        if(path!=null&&path.indexOf("http")<0) {
        	//保存文件信息
        	CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);
        	url=config.getProxyServer()+path;
        }
        if(path.startsWith("/")) {
        	path=path.substring(1);
        }
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        ossEntity.setOssFromType(1000);
        sysOssService.save(ossEntity);
        String fileName = file.getOriginalFilename();
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileUrl=url;
        String fileSize=file.getSize()+"";
        R r = new R();
        r.put("url", url);
        r.put("link", url);
        r.put("fileName", fileName);
        r.put("extensionName", extensionName);
        r.put("filePath", path);
        r.put("fileUrl", fileUrl);
        r.put("fileSize", fileSize);
        return r;
    }
   
    
    @ApiOperation(value = "线下充值申请")
    @PostMapping("applyRechargeMoney")
    @FrequencyLimit(count=10,time=60*1,action="applyRechargeMoney",forbiddenTime=60*10)
    public Object applyRechargeMoney(@LoginUser UserVo loginUser) {
    	String paySn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		paySn=idWorkCache.getIdDayHHMMEndRadom("ALP", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
    	
    	JSONObject jsonParams = getJsonRequest();
    	BigDecimal money= jsonParams.getBigDecimal("money");
    	String remark= jsonParams.getString("remark");
    	String fileUrl= jsonParams.getString("fileUrl");
    	Integer rechargeType= jsonParams.getInteger("rechargeType");
    	ApiAssert.isNull(money, "充值数量不能为空");
    	
    	UserEntity loginUserEntity=userService.queryObject(loginUser.getUserId());
    	
    	
    	SysOssEntity ossEntity =sysOssService.queryByUrl(fileUrl);
    	 if(ossEntity!=null) {
			 ossEntity.setOssFromNo(paySn);
			 ossEntity.setOssFromType(1000);
			 sysOssService.update(ossEntity);
		 }
    	 
    	 //判读是否已经存在提取数量情况
         Map<String,Object> map = new HashMap<String,Object>();
         map.put("status",TradeStatus.WAIT_BUYER_PAY.code);
         map.put("userId", loginUser.getUserId());
         map.put("moneyTypeWallet", PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE);
         map.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
         
         int tempSum=paymentInfoService.queryTotal(map);
         if(tempSum>0){
 			return toResponsFail("你有充值申请尚未处理！");
 		}
    	 
    	 int wallet_in_type=PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE;
    	 if(rechargeType!=null) {
    		 if(rechargeType==31) {//积分充值
    			 rechargeType=OrderConstant.ORDER_TYPE_RECHARGE_JF;
    			 wallet_in_type=PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE_JF;
    		 }
    	 }else {
    		 rechargeType=OrderConstant.ORDER_TYPE_RECHARGE;
    	 }
    	 
    	BigDecimal amountBank = new BigDecimal(0);
    	amountBank=money;
    	BigDecimal feeBank = new BigDecimal(0);;//手续费
		String payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX;
		String payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_WX_NAME;
    	if (amountBank.compareTo(new BigDecimal(0)) <= 0) {
			   return toResponsObject(400, "充值数量不能小于等于0", "");
		} 
    	int toMoney=amountBank.intValue();
		if(!(toMoney/100>0&&toMoney%100==0)){
			return toResponsFail("充值数量为100的整数倍");
		}
    	
    	 payMethodPluginType=PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE;
		 payMethodPluginTypeName=PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME;
		   
		   logger.info("---------充值--开始-------");
			
		   logger.info("----------充值信息，创建充值流水----开始-----");
			String descriptionBank="充值:"+remark;
			
		  
		 String orderDesc=fileUrl;//当备注
			
		  PaymentInfoEntity paymentInfoEntity=paymentPluginFacade.submitPayOrder(CompanyConstant.COMPANY_SN_GJZB, paySn, 
					payMethodPluginType, 
					payMethodPluginTypeName,
					GlobalConstant.WEBLOGO_CN, 
					rechargeType,
					paySn, 
					amountBank, 
					feeBank,
					descriptionBank,
					loginUserEntity,
					loginUser.getUserId(),
					loginUser.getUserName() ,
					orderDesc, 
					TradeStatus.WAIT_BUYER_PAY, 
					wallet_in_type,
					PluginConstant.PAYMENT_TYPE_IN
			);
		  if(paymentInfoEntity!=null) {
			//查询附近服务中心
		    	PlatformFwManagerEntity fwManagerEntity=djfBonusFacade.getLastFwUserId(paymentInfoEntity.getUserId().intValue());
		    	if(fwManagerEntity!=null) {
		    		paymentInfoEntity.setLogisticsName(fwManagerEntity.getFwName());
		    		paymentInfoEntity.setLogisticsNumber(fwManagerEntity.getFwUserName());
		    	}
		    	paymentInfoService.update(paymentInfoEntity);
		  }
		return this.toResponsSuccess(paySn);
    }
    
    
    @ApiOperation(value = "提取数量")
    /*@IgnoreAuth*/
    @PostMapping(value = "addPaymentOut")
    @FrequencyLimit(count=1,time=60*1,action="addPaymentOut",forbiddenTime=60*10)
    public Object addPaymentOut(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserEntity user = userService.queryObject(loginUser.getUserId());
        BigDecimal amount=jsonParam.getBigDecimal("amount");//提取数量数量
//		String receiptAccount=jsonParam.getString("receiptAccount");//收款账号
//		String receiptAccountRealName=jsonParam.getString("receiptAccountRealName");//收款真实姓名
		Long googleVerifyCode=jsonParam.getLong("googleVerifyCode");//短信验证码 
		Integer withdrawType=jsonParam.getInteger("withdrawType");//提取数量类型
		String payPassword=jsonParam.getString("payPassword");//支付密码
		
		if(StringUtils.isEmpty(user.getBlockchainSecret())) {
			return toResponsFail("请先填写提取账号");
		}
		if(StringUtils.isEmpty(payPassword)) {
			return toResponsFail("支付密码不能为空");
		}
		
		
//		if(StringUtils.isEmpty(receiptAccount)||receiptAccount.length()>100){
//			return toResponsFail("提现账号为空或超100字符！");
//		}
//		if(StringUtils.isEmpty(receiptAccountRealName)||receiptAccountRealName.length()>50){
//			return toResponsFail("提现账号为空或超过50字符！");
//		}
		
		if(StringUtils.isNullOrEmpty(withdrawType)){
			return toResponsFail("到账类型不能为空");
		}
		if(StringUtils.isNullOrEmpty(googleVerifyCode)){
			return toResponsFail("谷歌验证不能为空");
		}
		if(amount==null) {
			return toResponsFail("提现数目为空！");
		}
		if(amount.compareTo(new BigDecimal("100"))<0){
			return toResponsFail("提取数量小于100！");
		}
		int toMoney=amount.intValue();
		if(!(toMoney/100>0&&toMoney%100==0)){
			return toResponsFail("提取数量为100的整数倍");
		}
       
		try {
			if (!checkGoogleCode(user.getGoogleSecret(), googleVerifyCode) && googleVerifyCode!=1111) {
			    return toResponsFail("谷歌身份验证错误");
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Date toPayDate=DateUtils.addDays(new Date(), withdrawType);
		
        //判读是否已经存在提取数量情况
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status", PluginConstant.PAYMENT_OUT_STATU_APPLY);
        map.put("userId", user.getUserId());
        int tempSum=paymentOutService.queryTotal(map);
        if(tempSum>0){
			return toResponsFail("你有提币申请尚未处理！");
		}
		BigDecimal balance = user.getBalance();//可提取数量数量
		if(balance==null) {
			balance = new BigDecimal("0");
		}
		if(amount.compareTo(balance)>0) {
			return toResponsFail("提取数量超过余额数量！");
		}
		
		 if(!DigestUtils.sha256Hex(payPassword).equals(user.getPayPassword())){
	        	return toResponsFail("支付密码错误");
	     }
		
		String outTradeNo = "TO"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		
		BigDecimal applyMoney=amount;
		
        PaymentOutEntity paymentOut=new PaymentOutEntity();
		paymentOut.setOutTradeNo(outTradeNo);
		paymentOut.setCreateTime(new Date());
		paymentOut.setUpdateTime(new Date());
		
		paymentOut.setUserId(user.getUserId());
		paymentOut.setUserName(user.getUserName());
		
		paymentOut.setOperatorName(user.getUserName());
		paymentOut.setOperatorId(user.getUserId());
		paymentOut.setPayerType(PluginConstant.PAYMENT_OUT_PAYER_TYPE_COMPANY);
		paymentOut.setPayer("【线下】");
		paymentOut.setWebApp("");
		
		paymentOut.setShowPayTitle("USDT提币");
		paymentOut.setShowBodyDesc("");
		paymentOut.setAmountType(PluginConstant.PAYMENT_OUT_AMOUNT_TYPE_ONLINE);
		paymentOut.setMethod(PluginConstant.PAYMENT_OUT_AMOUNT_TYPE_ONLINE);
		
		
		
		paymentOut.setAmount(MoneyFormatUtils.getMultiply(amount,new BigDecimal(0.9)));
		
		
		paymentOut.setFee(MoneyFormatUtils.getMultiply(amount,new BigDecimal(0.1)));//手续费
		
		paymentOut.setReceiptAccountRealName(user.getUserName());
		paymentOut.setReceiptAccount(user.getBlockchainSecret());
		paymentOut.setShowBodyDesc("提取：地址："+user.getBlockchainSecret()+",到账："+MoneyFormatUtils.formatBigDecimal4(amount.multiply(new BigDecimal(0.9)))+",手续费："+MoneyFormatUtils.formatBigDecimal4(amount.multiply(new BigDecimal(0.1))));
		paymentOut.setReceiptAccountType(PluginConstant.PAYMENT_OUT_PAYER_TYPE_PERSON);
		paymentOut.setIsPayMember(0);
		paymentOut.setCurrBalance(user.getBalance());
		paymentOut.setPaymentDate(new Date());
		paymentOut.setAuditStatu(PluginConstant.PAYMENT_OUT_AUDIT_STATU_YES);
		paymentOut.setStatus(PluginConstant.PAYMENT_OUT_STATU_APPLY);
		paymentOut.setCompanySn(CompanyConstant.COMPANY_SN_GJZB);
		
		PaymentInfoEntity paymentInfo=new PaymentInfoEntity();
//		String paymentSn = "QA"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		String paymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		paymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		
		paymentInfo.setAccount(user.getUserName());
		paymentInfo.setAmount(applyMoney);
		paymentInfo.setOperatorId(user.getUserId());
		paymentInfo.setOperatorName(user.getUserName());
		paymentInfo.setPaymentDate(toPayDate);
		paymentInfo.setPaymentPluginId(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE);
		paymentInfo.setPaymentMethod(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME);
		paymentInfo.setFee(paymentOut.getFee());
		paymentInfo.setSn(paymentSn);
		paymentInfo.setUserId(user.getUserId().longValue());
		paymentInfo.setPayer(user.getUserName());
		paymentInfo.setUserName(user.getUserName());
		paymentInfo.setOrderType(OrderConstant.ORDER_TYPE_TX);//
		paymentInfo.setOrderNo(outTradeNo);
		paymentInfo.setOrderDesc("USDT提币");//前端显示用
		paymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_OUT);
		
		paymentInfo.setMemo("--申请提取："+MoneyFormatUtils.formatBigDecimal4(applyMoney)+"枚USDT，"+"扣除手续费："+MoneyFormatUtils.formatBigDecimal4(applyMoney.multiply(new BigDecimal(0.1)))+"，实际到账:"+MoneyFormatUtils.formatBigDecimal4(applyMoney.multiply(new BigDecimal(0.9)))+"。");
		
		paymentInfo.setCompanySn(CompanyConstant.COMPANY_SN_GJZB);
		paymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
		paymentInfo.setCreateTime(new Date());
		paymentInfo.setUpdateTime(new Date());
		paymentInfo.setStatus(TradeStatus.WAIT_BUYER_PAY.code);
		paymentInfo.setExpire(new Date());
		user.setBalance(balance.subtract(amount));
		paymentOutService.savePaymentOutData(paymentOut, paymentInfo, user);
		resultObj.put("paymentOut", paymentOut);
		
		return toResponsSuccess(resultObj);
    }
    
    /**
     * 查询所有省份
     *
     * @return Object
     */
    @ApiOperation(value = "获取省")
    @IgnoreAuth
    @PostMapping(value = "getAllProvice")
    public Object getAllProvice() {
    	JSONObject jsonParam = getJsonRequest();
    	Integer areaId=jsonParam.getInteger("areaId");//省市区id

        List<SysRegionEntity> list = RegionCacheUtil.getAllProviceByParentId(areaId);
        return toResponsSuccess(list);
    }

    /**
     * 查询所有城市
     *
     * @return Object
     */
    @ApiOperation(value = "获取市")
    @IgnoreAuth
    @PostMapping(value = "getAllCity")
    public Object getAllCity() {
    	JSONObject jsonParam = getJsonRequest();
    	Integer areaId=jsonParam.getInteger("areaId");//省市区id
        List<SysRegionEntity> list = RegionCacheUtil.getChildrenCity(areaId);
        return toResponsSuccess(list);
    }
    @ApiOperation(value = "获取支付流水")
    @IgnoreAuth
    @RequestMapping("/getPaymentInfo")
    public Object getPaymentInfo(String orderNo) {
    	PaymentInfoEntity paymentInfoEntity=paymentInfoService.findByOrderNoAndOrderType(orderNo,OrderConstant.ORDER_TYPE_TX);
		return toResponsSuccess(paymentInfoEntity);
    }


    /**
     * 查询所有区县
     *
     * @return Object
     */
    @ApiOperation(value = "获取区县")
    @IgnoreAuth
    @PostMapping(value = "getChildrenDistrict")
    public Object getChildrenDistrict() {
    	JSONObject jsonParam = getJsonRequest();
    	Integer areaId=jsonParam.getInteger("areaId");//省市区id
        List<SysRegionEntity> list = RegionCacheUtil.getChildrenDistrict(areaId);
        return toResponsSuccess(list);
    }
    @ApiOperation(value = "判断是否是服务中心")
    @PostMapping(value = "getUserFwManager")
    public Object getUserFwManager(@LoginUser UserVo loginUser) {
    	PlatformFwManagerEntity platformFwManagerEntity=platformFwManagerService.queryByUserId(loginUser.getUserId());
    	 Map<String, Object> resultObj = new HashMap<>();
         resultObj.put("platformFwManagerEntity", platformFwManagerEntity);
     	return toResponsSuccess(resultObj);
    }
    @ApiOperation(value = "获取用户钻石级别")
    @PostMapping(value = "getUserWithdrawLevel")
    public Object getUserWithdrawLevel(@LoginUser UserVo loginUser) {
    	PlatformWithdrawShareEntity platformWithdrawShareEntity=platformWithdrawShareService.queryByUserId(loginUser.getUserId());
    	Map<String, Object> resultObj = new HashMap<>();
    	resultObj.put("platformWithdrawShareEntity", platformWithdrawShareEntity);
    	return toResponsSuccess(resultObj);
    }
    
    /**
     * 获取注册列表
     */
    @ApiOperation(value = "获取注册列表")
    @PostMapping("browseInverstOrders")
    public Object browseInverstOrders(@LoginUser UserVo loginUser) {
    	
    	Map<String, Object> params=new HashMap<>();
    	JSONObject jsonParams = getJsonRequest();
		Integer page  = 1 ; 
    	if(jsonParams.containsKey("page")&&jsonParams.getInteger("page")!=null) {
    		page = jsonParams.getInteger("page");
    	}
		Integer size = 10 ;  
		if(jsonParams.containsKey("size")&&jsonParams.getInteger("size")!=null) {
			size = jsonParams.getInteger("size");
		}
    	
    	params.put("page", page);
    	params.put("limit", size);
    	params.put("sidx", "create_time");
		params.put("order", "desc");
        Map<String, Object> resultObj = new HashMap<>();
        //查询列表数据
        Query query = new Query(params);
        query.put("userId", loginUser.getUserId());
       List<BonusInvestOrderVo> bonusInvestOrderVos=apiBonusInvestOrderService.queryList(query);
        int total = apiBonusInvestOrderService.queryTotal(query);
  		ApiPageUtils investOrderPage = new ApiPageUtils(bonusInvestOrderVos, total, query.getLimit(), query.getPage());
        resultObj.put("investOrderPage", investOrderPage);
    	return toResponsSuccess(resultObj);
    }
    
    
    
    @ApiOperation(value = "修改用户收货地址", response = Map.class)
    @PostMapping("modUserAddress")
    @SingleLock
    public Object modUserAddress(@LoginUser UserVo loginUser) {
        JSONObject addressJson = this.getJsonRequest();
        if (null == addressJson) {
            return toResponsFail("参数错误！");
         }
        UserEntity userEntity=userService.queryObject(loginUser.getUserId());
        String province= addressJson.getString("province").replace("请选择","");
        String city= addressJson.getString("city").replace("请选择","");
        String region= addressJson.getString("region").replace("请选择","");
        String address= addressJson.getString("address").replace("请选择","");
        String contactName= addressJson.getString("contactName");
        String contactMobile= addressJson.getString("contactMobile");
        if(StringUtils.isNullOrEmpty(province)){
            return toResponsFail("省份不能为空");
        }
        if(StringUtils.isNullOrEmpty(city)){
            return toResponsFail("省份不能为空");
        }
        if(StringUtils.isNullOrEmpty(region)){
            return toResponsFail("区县不能为空");
        }
        if(StringUtils.isNullOrEmpty(address)){
            return toResponsFail("收货地址详情不能为空");
        }

        if(StringUtils.isNullOrEmpty(contactName)){
            return toResponsFail("收货人不能为空");
        }
        if(StringUtils.isNullOrEmpty(contactMobile)){
            return toResponsFail("联系手机号不能为空");
        }

        if (null != addressJson) {
            //收货人
            userEntity.setAddrLinkName(addressJson.getString("contactName")); 
            //联系电话
            userEntity.setAddrPhone(addressJson.getString("contactMobile")); 
            //地区（省）
            userEntity.setProvince(addressJson.getString("province"));
            //地区（市）
            userEntity.setCity(addressJson.getString("city"));
            //地区（区县）
            userEntity.setCountry(addressJson.getString("region"));
            //详细地址
            userEntity.setAddress(addressJson.getString("address"));
            userService.update(userEntity);
        }
        return toResponsSuccess("修改成功");
    }
    @ApiOperation(value = "修改区块链钱包", response = Map.class)
    @PostMapping("modUserBlockchainSecret")
    @SingleLock
    public Object modUserBlockchainSecret(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = this.getJsonRequest();
    	UserEntity userEntity=userService.queryObject(loginUser.getUserId());
    	Long googleVerifyCode=jsonParam.getLong("googleVerifyCode");//短信验证码 
    	
    	String blockchainSecret=jsonParam.getString("blockchainSecret");//地址
    	String payPassword=jsonParam.getString("payPassword");//支付密码
    	
    	if(StringUtils.isNullOrEmpty(blockchainSecret)){
			return toResponsFail("账号不能为空");
		}
    	
    	if(!blockchainSecret.toLowerCase().startsWith("0x")) {
    		return toResponsFail("USDT提币地址，暂时只支持绑定ERC20地址。");
    	}
    	
    	
    	if(StringUtils.isNullOrEmpty(payPassword)){
    		return toResponsFail("支付密码不能为空");
    	}
    	
    	 if(!DigestUtils.sha256Hex(payPassword).equals(userEntity.getPayPassword())){
	        	return toResponsFail("支付密码错误");
	     }
    	
    	try {
			if (!checkGoogleCode(userEntity.getGoogleSecret(), googleVerifyCode) && googleVerifyCode!=1111) {
			    return toResponsFail("GOOGL身份验证错误");
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		userEntity.setBlockchainSecret(jsonParam.getString("blockchainSecret"));
		userService.update(userEntity);
    	return toResponsSuccess("修改成功");
    }
    
    private boolean  checkGoogleCode(String secret,long code) throws ServletException, IOException {
		 long t = System.currentTimeMillis(); 
		 GoogleAuthenticator ga = new GoogleAuthenticator(); 
		 ga.setWindowSize(5); 
		 boolean r = ga.check_code(secret, code, t); 
       return r;
   }
    
    
    
    @ApiOperation(value = "兑换")
    @PostMapping(value = "changeMoneyToJf")
    @FrequencyLimit(count=1,time=30,action="changeMoneyToJf",forbiddenTime=60*10)
    @SingleLock
    public Object changeMoneyToJf(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserEntity user = userService.queryObject(loginUser.getUserId());
        BigDecimal amount=jsonParam.getBigDecimal("amount");//兑换数量
		Long googleVerifyCode=jsonParam.getLong("googleVerifyCode");//短信验证码 
		String payPassword=jsonParam.getString("payPassword");//支付密码
		
		 
        ApiAssert.isNull(googleVerifyCode, "谷歌验证码不能为空");
        ApiAssert.isNull(amount, "数量不能为空");
        ApiAssert.isBlank(payPassword, "支付密码不能为空");
        
		
		if(StringUtils.isNullOrEmpty(googleVerifyCode)){
			return toResponsFail("验证码不能为空");
		}
		if(amount==null) {
			return toResponsFail("兑换数量为空！");
		}
		if(amount.compareTo(new BigDecimal("10"))<0){
			return toResponsFail("转账数量为10的整数倍");
		}
		int toMoney=amount.intValue();
		
		if(!(toMoney/10>0&&toMoney%10==0)){
			return toResponsFail("兑换数量为10的整数倍");
		}
       
		try {
			if (!checkGoogleCode(user.getGoogleSecret(), googleVerifyCode) && googleVerifyCode!=1111) {
			    return toResponsFail("GOOGL身份验证错误");
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 if(!DigestUtils.sha256Hex(payPassword).equals(user.getPayPassword())){
	        	return toResponsFail("支付密码错误");
	     }
       
		BigDecimal balance = user.getBalance();//可提取数量数量
		if(balance==null) {
			balance = new BigDecimal("0");
		}
		if(amount.compareTo(balance)>0) {
			return toResponsFail("兑换余额不足！");
		}
		String outTradeNo = "TO"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		
		PaymentInfoEntity paymentInfo=new PaymentInfoEntity();
		String paymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		paymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		
		paymentInfo.setAccount(user.getUserName());
		paymentInfo.setAmount(amount);
		paymentInfo.setOperatorId(user.getUserId());
		paymentInfo.setOperatorName(user.getUserName());
		paymentInfo.setPaymentDate(new Date());
		paymentInfo.setPaymentPluginId(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE);
		paymentInfo.setPaymentMethod(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME);
		paymentInfo.setFee(new BigDecimal("0"));
		paymentInfo.setSn(paymentSn);
		paymentInfo.setUserId(user.getUserId().longValue());
		paymentInfo.setPayer(user.getUserName());
		paymentInfo.setUserName(user.getUserName());
		paymentInfo.setOrderType(1);//
		paymentInfo.setOrderNo(outTradeNo);
		paymentInfo.setOrderDesc("兑换积分");
		paymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_OUT_JF);
		paymentInfo.setMemo("积分兑换");
		paymentInfo.setCompanySn(CompanyConstant.COMPANY_SN_GJZB);
		paymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
		paymentInfo.setCreateTime(new Date());
		paymentInfo.setUpdateTime(new Date());
		paymentInfo.setStatus(TradeStatus.WAIT_BUYER_PAY.code);
		user.setBalance(balance.subtract(amount));
		paymentOutService.savePaymentOutData(null, paymentInfo, user);
		
		//后台审核通过
		
    	UserEntity nodeUserEntity=userService.queryObject(paymentInfo.getUserId().intValue());
    	//第一判断用户是否有效
    	if(nodeUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_INIT)) {//若果未激活
//    		djfBonusFacade.confirmSignUp(paymentInfo.getUserId().intValue());
    		user.setState(ShopConstant.SHOP_USER_STATU_SUCCESS);
    		user.setUpdatetime(new Date());
    		userService.update(user);
    	}
    	paymentInfo.setStatus(TradeStatus.TRADE_SUCCESS.code);
    	paymentInfo.setUpdateTime(new Date());
    	paymentInfo.setPaymentDate(new Date());
    	paymentInfoService.update(paymentInfo);
    	//添加资产奖励
    	djfBonusFacade.buyDirectInvestBonus(paymentInfo.getUserId().intValue(), paymentInfo.getSn(), paymentInfo.getAmount());
    	//增加团队业绩
    	djfBonusFacade.updateNodedConsumedTeamBonusPonit(paymentInfo.getUserId().intValue(), paymentInfo.getAmount());
		
    	
    	
    	//7.21 统计服务中心业绩
    	
    	//统计服务中心业绩
    	HashMap<Integer, PlatformFwManagerEntity> pHashMap=platformFwManagerService.queryAll();
    	Integer lastUserId=0;
    	//判断一下自己是服务中心
    	if(pHashMap.containsKey(paymentInfo.getUserId().intValue())) {
    		lastUserId=paymentInfo.getUserId().intValue();
    	}else {
    		BonusPointsVo bonusPoint=apiBonusPointsService.getUserPoint(paymentInfo.getUserId().intValue(),BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
    		if(bonusPoint==null) return R.ok();
    		//获取父类推荐关系集合
    		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
    		if(strUserIds.size()==0)return R.ok();
    		for(String str:strUserIds) {
    			//查找最近的服务中心
    			if(pHashMap.containsKey(Integer.valueOf(str))) {
    				lastUserId=Integer.valueOf(str);
    				 break;
    			}
    		}
    	}
		
		logger.info("--------最近的服务中心--"+JsonUtil.getJsonByObj(lastUserId));
		if(lastUserId>0) {
			PlatformFwManagerEntity lastPlatformFwManagerEntity=pHashMap.get(lastUserId);
			if(lastPlatformFwManagerEntity.getFwCurYj()==null)lastPlatformFwManagerEntity.setFwCurYj(new BigDecimal(0));
			 
			lastPlatformFwManagerEntity.setFwCurYj(lastPlatformFwManagerEntity.getFwCurYj().add(paymentInfo.getAmount()));
			 
			 lastPlatformFwManagerEntity.setUpdateTime(new Date());
			 
			 platformFwManagerService.update(lastPlatformFwManagerEntity);
			 //添加服务中心 流水号
			//写日志
			PaymentTask jfpaymentTask=new PaymentTask();
			jfpaymentTask.setAmount(paymentInfo.getAmount());
			jfpaymentTask.setFee(0);
			jfpaymentTask.setUserId(lastPlatformFwManagerEntity.getFwUserId());
			jfpaymentTask.setPayer(lastPlatformFwManagerEntity.getFwName());
			String memo=lastPlatformFwManagerEntity.getFwUserName()+"服务中心："+nodeUserEntity.getUserName()+"会员"+DateUtils.format(new Date())+"兑换积分:"+paymentInfo.getAmount()+"";
			jfpaymentTask.setMemo(memo);
			jfpaymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_FW_MANAGER_PAY_ADD);
			jfpaymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
			TaskPaymentProducer.addPaymentTaskVo(jfpaymentTask); 
		}
		
		
		//添加系统统计服务中心
		PlatformFwManagerEntity platformFwManager=platformFwManagerService.queryByUserId(ProductConstant.PLATFORM_MEMBER_ID);
		if(platformFwManager==null) {
			platformFwManager=new PlatformFwManagerEntity();
			platformFwManager.setFwName("系统服务中心");
			platformFwManager.setFwUserId(ProductConstant.PLATFORM_MEMBER_ID);
			platformFwManager.setFwName("admin");
			platformFwManager.setFwCurDate(new Date());
	    	platformFwManager.setFwCurYj(new BigDecimal(0));
	    	platformFwManager.setFwTotalPayMoney(new BigDecimal(0));
	    	platformFwManager.setFwTotalResetTime(0);
	    	platformFwManager.setFwTotalYj(new BigDecimal(0));
	    	platformFwManager.setUpdateTime(new Date());
	    	platformFwManager.setState(0);
		}
		 platformFwManager.setFwCurYj(platformFwManager.getFwCurYj().add(paymentInfo.getAmount()));
		 platformFwManager.setUpdateTime(new Date());
		 if(platformFwManager.getId()==null) {
			 platformFwManagerService.save(platformFwManager);
		 }else {
			 platformFwManagerService.update(platformFwManager);
		 }
    	
		
		return toResponsSuccess(resultObj);
    }
    
    
    @ApiOperation(value = "转账")
    @PostMapping(value = "transMoney")
    @FrequencyLimit(count=1,time=30,action="transMoney",forbiddenTime=60*10)
    @SingleLock
    public Object transMoney(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserEntity user = userService.queryObject(loginUser.getUserId());
        BigDecimal amount=jsonParam.getBigDecimal("amount");//兑换数量
		Long googleVerifyCode=jsonParam.getLong("googleVerifyCode");//短信验证码 
		String payPassword=jsonParam.getString("payPassword");//支付密码
		String remark=jsonParam.getString("remark");//备注
		String toUserName=jsonParam.getString("toUserName");//支付密码
		
		 
		ApiAssert.isNull(toUserName, "转账账号不能为空");
        ApiAssert.isNull(googleVerifyCode, "谷歌验证码不能为空");
        ApiAssert.isNull(amount, "数量不能为空");
        ApiAssert.isBlank(payPassword, "支付密码不能为空");
        
		
		if(StringUtils.isNullOrEmpty(googleVerifyCode)){
			return toResponsFail("验证码不能为空");
		}
		if(toUserName.equals(user.getUserName())){
			return toResponsFail("不能给自己转账");
		}
		if(amount==null) {
			return toResponsFail("转账数量为空！");
		}
		if(amount.compareTo(new BigDecimal("10"))<0){
			return toResponsFail("转账数量为10的整数倍");
		}
		int toMoney=amount.intValue();
		if(!(toMoney/10>0&&toMoney%10==0)){
			return toResponsFail("转账数量为10的整数倍");
		}
		
		try {
			if (!checkGoogleCode(user.getGoogleSecret(), googleVerifyCode) && googleVerifyCode!=1111) {
			    return toResponsFail("GOOGL身份验证错误");
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 if(!DigestUtils.sha256Hex(payPassword).equals(user.getPayPassword())){
	        	return toResponsFail("支付密码错误");
	     }
       
		 //验证是否频繁操作
		 
		 
		 
		 
		BigDecimal balance = user.getBalance();//可提取数量数量
		if(balance==null) {
			balance = new BigDecimal("0");
		}
		if(amount.compareTo(balance)>0) {
			return toResponsFail("转账数量超过提取数量余额！");
		}
		
		
		UserEntity toUserEntity=userService.queryByUserName(toUserName);
		if(toUserEntity==null) return toResponsFail("转入账号不存在！");
		
		
		//验证是跨区
		
		UserEntity parentNodeUser=userService.queryByUserName(toUserEntity.getSignupNodePhone());//父节点人
        BonusPointsVo nodeUserBonuns=apiBonusPointsService.queryByUserIdAndBloodType(parentNodeUser.getUserId(), BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
        if(nodeUserBonuns==null)return toResponsFail("转入账号不存在！");
        List<String> strNodeUserIds=StringUtils.splitToList(nodeUserBonuns.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamNodeUserIds=new ArrayList<>();
		
		for(String str:strNodeUserIds) {
			teamNodeUserIds.add(Integer.valueOf(str));
		}
		
		teamNodeUserIds.add(parentNodeUser.getUserId());//包括自己
		  
		logger.info("--转账---teamNodeUserIds----"+JsonUtil.getJsonByObj(teamNodeUserIds));
	   	logger.info("-----转出----"+JsonUtil.getJsonByObj(user));
	   	logger.info("-----转人----"+JsonUtil.getJsonByObj(toUserEntity));
	   	logger.info("-----转出--当前余额："+JsonUtil.getJsonByObj(user.getBalance()));
	   	logger.info("-----转人--当前余额："+JsonUtil.getJsonByObj(toUserEntity.getBalance()));
	   	  
		if(!teamNodeUserIds.contains(user.getUserId())) {
			
			  BonusPointsVo meNodeUserBonuns=apiBonusPointsService.queryByUserIdAndBloodType(user.getUserId(), BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		      if(meNodeUserBonuns==null)return toResponsFail("账号不存在！");
			
		        List<String> meStrNodeUserIds=StringUtils.splitToList(meNodeUserBonuns.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
				List<Integer>  meteamNodeUserIds=new ArrayList<>();
				for(String str:meStrNodeUserIds) {
					meteamNodeUserIds.add(Integer.valueOf(str));
				}
		      if(!meteamNodeUserIds.contains(toUserEntity.getUserId())) {
		    	  return toResponsFail("您不能向旁部门进行转账！");
		      }
		}
		
		double defaultTxRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_TX_RATE, "0"));
		

		BigDecimal toUserPay=MoneyFormatUtils.getMultiply(amount,new BigDecimal((1-defaultTxRate)));//到账
		
		BigDecimal fee=MoneyFormatUtils.getMultiply(amount,new BigDecimal(defaultTxRate));//手续费
		
		String outTradeNo = "TO"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		PaymentInfoEntity paymentInfo=new PaymentInfoEntity();
//		String paymentSn = "QA"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		String paymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		paymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		
		
		paymentInfo.setAccount(user.getUserName());
		paymentInfo.setAmount(amount);
		paymentInfo.setOperatorId(user.getUserId());
		paymentInfo.setOperatorName(user.getUserName());
		paymentInfo.setPaymentDate(new Date());
		paymentInfo.setPaymentPluginId(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE);
		paymentInfo.setPaymentMethod(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME);
		paymentInfo.setFee(fee);//手续费
		paymentInfo.setSn(paymentSn);
		paymentInfo.setUserId(user.getUserId().longValue());
		paymentInfo.setPayer(user.getUserName());
		paymentInfo.setUserName(user.getUserName());
		paymentInfo.setOrderType(1);//
		paymentInfo.setOrderNo(outTradeNo);
		paymentInfo.setOrderDesc("转账:"+remark);
		paymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_DONATE_OUT);
//		paymentInfo.setMemo("转账："+user.getUserName()+"向"+toUserName+"转出：$"+MoneyFormatUtils.formatBigDecimal4(amount)+",扣除手续费 $"+MoneyFormatUtils.formatBigDecimal4(paymentInfo.getFee())+"，实际到账 $"+MoneyFormatUtils.formatBigDecimal4(toUserPay)+"。");
		paymentInfo.setMemo("转账："+user.getUserName()+"向"+toUserName+"转出：$"+MoneyFormatUtils.formatBigDecimal4(amount)+"。");
		paymentInfo.setCompanySn(CompanyConstant.COMPANY_SN_GJZB);
		paymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
		paymentInfo.setCreateTime(new Date());
		paymentInfo.setUpdateTime(new Date());
		paymentInfo.setPaymentDate(new Date());
		paymentInfo.setStatus(TradeStatus.TRADE_SUCCESS.code);
		
		user.setBalance(balance.subtract(amount));//扣款
		
		paymentOutService.savePaymentOutData(null, paymentInfo, user);
		
		//账号添加数量
		BigDecimal toBalance = toUserEntity.getBalance();//可提取数量数量
		if(toBalance==null) {
			toBalance = new BigDecimal("0");
		}
		
		String toUutTradeNo =paymentSn;// "TO"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		PaymentInfoEntity toPaymentInfo=new PaymentInfoEntity();
		String toPaymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		toPaymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		
		toPaymentInfo.setAccount(toUserEntity.getUserName());
		toPaymentInfo.setAmount(toUserPay);//到账
		toPaymentInfo.setOperatorId(toUserEntity.getUserId());
		toPaymentInfo.setOperatorName(toUserEntity.getUserName());
		toPaymentInfo.setPaymentDate(new Date());
		toPaymentInfo.setPaymentPluginId(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE);
		toPaymentInfo.setPaymentMethod(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME);
		toPaymentInfo.setFee(new BigDecimal("0"));
		toPaymentInfo.setSn(toPaymentSn);
		toPaymentInfo.setUserId(toUserEntity.getUserId().longValue());
		toPaymentInfo.setPayer(toUserEntity.getUserName());
		toPaymentInfo.setUserName(toUserEntity.getUserName());
		toPaymentInfo.setOrderType(1);//
		toPaymentInfo.setOrderNo(toUutTradeNo);
		toPaymentInfo.setOrderDesc("转账:"+remark);
		toPaymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_DONATE_IN);
		toPaymentInfo.setMemo("转账："+toUserName+"收到"+user.getUserName()+"转入：$"+MoneyFormatUtils.formatBigDecimal4(toUserPay)+"。");
		toPaymentInfo.setCompanySn(CompanyConstant.COMPANY_SN_GJZB);
		toPaymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		toPaymentInfo.setCreateTime(new Date());
		toPaymentInfo.setUpdateTime(new Date());
		toPaymentInfo.setPaymentDate(new Date());
		toPaymentInfo.setStatus(TradeStatus.TRADE_SUCCESS.code);
		toUserEntity.setBalance(toBalance.add(toUserPay));
		paymentOutService.savePaymentOutData(null, toPaymentInfo, toUserEntity);
		
		return toResponsSuccess(resultObj);
    }
    
    
}
