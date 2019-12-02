package com.platform.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.SysLog;
import com.platform.codegen.qrcode.QrcodeGenerator;
import com.platform.codegen.qrcode.QreyesFormat;
import com.platform.codegen.qrcode.SimpleQrcodeGenerator;
import com.platform.constants.BonusConstant;
import com.platform.constants.PluginConstant;
import com.platform.entity.BonusPointsVo;
import com.platform.entity.SysConfigEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserInvestLevelEntity;
import com.platform.facade.DjfBonusFacade;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.SysConfigService;
import com.platform.service.UserInvestLevelService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.JsonUtil;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.StringUtils;
import com.platform.utils.auth.GoogleAuthenticator;

/**
 * Controller
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:07:38
 */
@RestController
@RequestMapping("user")
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;
    @Autowired
     private DjfBonusFacade djfBonusFacade;
	 @Autowired
	 private ApiBonusPointsService bonusPointsService;
	
	 
	 @Autowired
	 private SysConfigService sysConfigService;
	 
	 @Autowired
	 private UserInvestLevelService userInvestLevelService;
	 
	    String secret="TOCW2NH4KKPAFMHJ";

	    @RequestMapping("googleAuthCode")
	    public void googleAuthCode(HttpServletResponse response) throws ServletException, IOException {
	        
	        String qrContent=GoogleAuthenticator.getQRBarcode("多积分", secret);
	       
	        response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control",
					"no-store, no-cache, must-revalidate");
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("image/jpeg");
			
			System.out.println("qrContent="+qrContent);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			QrcodeGenerator generator = new SimpleQrcodeGenerator();
			generator.getQrcodeConfig()
			.setBorderSize(2)
			.setPadding(1)
			.setCodeEyesFormat(QreyesFormat.R2_BORDER_C_POINT)
			.setLogoBackgroundColorIsTransparent(false);
			try {
				generator.generate(qrContent).toStream(bout);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(bout.toByteArray());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    @RequestMapping("checkAuthCode")
	    public R checkAuthCode(long code) throws ServletException, IOException {
			 long t = System.currentTimeMillis(); 
			 GoogleAuthenticator ga = new GoogleAuthenticator(); 
			 ga.setWindowSize(5); 
			 boolean r = ga.check_code(secret, code, t); 
			 System.out.println("检查code是否正确？" + r); 
	        return R.ok("验证CODE 结果:" + r);
	    }
	 
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        logger.info("--------query-----"+JsonUtil.getJsonByObj(query));
        
        if(query.containsKey("state")) {
        	String state=(String) query.get("state");
        	if(!StringUtils.isEmpty(state)) {
        	}else {
        		query.remove("state");
        	}
        }
        
        List<UserEntity> userList = userService.queryList(query);
        
        logger.info("--------userList-----"+JsonUtil.getJsonByObj(userList));
        
        
        int total = userService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("user:info")
    public R info(@PathVariable("userId") Integer userId) {
        UserEntity user = userService.queryObject(userId);
       
        user.setPassword(null);
        user.setPayPassword(null);
        
        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:save")
    public R save(@RequestBody UserEntity user) {
    	if(StringUtils.isNotEmpty(user.getUserName())) {
//    		Map<String, Object> params = new HashMap<String,Object>();
//    		params.put("userName", user.getUserName());
//    		List<UserEntity> list = userService.queryList(params);
//    		if(list!=null&&list.size()>0) {
//    			return R.error("客户登录帐号已经存在，请重新设置帐号！");
//    		}
    		UserEntity userEntity= userService.queryByUserName(user.getUserName());
    		if(userEntity!=null&&user.getUserId().intValue()!=userEntity.getUserId().intValue()) {
    			return R.error("客户登录帐号已经存在，请重新设置帐号！");
    		}
    	}
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
    	user.setUserPreBalance(new BigDecimal(0));
    	//判断推荐人 金额扣除 是否满足
    	UserInvestLevelEntity userInvestLevelEntity= userInvestLevelService.queryByLevelType(user.getSignupUserLevelType());
        if(userInvestLevelEntity==null) return R.error("内购金额不存在");
        
        user.setUserLevelType(userInvestLevelEntity.getUserLevelType());
        user.setUserNodeBonusLevel(userInvestLevelEntity.getUserLevelNodeLevel());
    	
    	userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:update")
    public R update(@RequestBody UserEntity user) {
    	if(StringUtils.isNotEmpty(user.getUserName())) {
    		UserEntity userEntity= userService.queryByUserName(user.getUserName());
    		if(userEntity!=null&&user.getUserId().intValue()!=userEntity.getUserId().intValue()) {
    			return R.error("客户登录帐号已经存在，请重新设置帐号！");
    		}
    	}
    	if(StringUtils.isNotEmpty(user.getPassword())) {
    		user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
		}
    	
    	if(StringUtils.isNotEmpty(user.getPayPassword())) {
    		user.setPayPassword(DigestUtils.sha256Hex(user.getPayPassword()));
    	}
    	
    	user.setUpdatetime(new Date());
        userService.update(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("user:delete")
    public R delete(@RequestBody Integer[] userIds) {
    	for(Integer id:userIds) {
    		UserEntity nodeUserEntity=userService.queryObject(id);
    		if(nodeUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_INIT)) {
    			userService.delete(id);
    		}
    	}
        return R.ok();
    }
    @SysLog("停止用户分红")
    @RequestMapping("/stopUserState")
    @RequiresPermissions("user:update")
    public R stopUserState(@RequestBody Integer[] userIds) {
    	for(Integer id:userIds) {
    		UserEntity nodeUserEntity=userService.queryObject(id);
    		nodeUserEntity.setState(ShopConstant.SHOP_USER_STATU_OFFLINE);
    		userService.update(nodeUserEntity);
    	}
    	return R.ok();
    }
    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<UserEntity> list = userService.queryList(params);

        return R.ok().put("list", list);
    }
    
    /**
     * 转积分
     */
    @RequestMapping("/addIntegral")
    @RequiresPermissions("user:update")
    public R addIntegral(@RequestBody UserEntity user) {
        //userService.updateIntegral(user);
        return R.ok();
    }
    @RequestMapping("/getUserBinaryTreeSubNode")
    @RequiresPermissions("user:update")
    public R getUserBinaryTreeSubNode(String nodeUserName) {
    	  if(StringUtils.isEmpty(nodeUserName)) return R.error("不能为空");
    	UserEntity nodeUserEntity=userService.queryByUserName(nodeUserName);
    	if(nodeUserEntity==null) return R.error("查不到该用户");
    	//二叉树节点 优先左节点
    	List<BonusPointsVo> bonusPointsEntities=bonusPointsService.queryByParentUserId(nodeUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
    	
    	return R.ok().put("bonusPointsEntities",bonusPointsEntities);
    }
   
    @RequestMapping("/getUserNode")
    @RequiresPermissions("user:update")
    public R getUserNode(String nodeUserName) {
    	if(StringUtils.isEmpty(nodeUserName)) return R.error("不能为空");
    	UserEntity nodeUserEntity=userService.queryByUserName(nodeUserName);
    	if(nodeUserEntity==null) return R.error("查不到该用户");
    	return R.ok().put("nodeUserEntity",nodeUserEntity);
    }
    
    /**
     * 确认注册
     */
    @RequestMapping("/confirmSignUp")
    @RequiresPermissions("user:update")
    public R confirmSignUp(Integer userId) {
//    	return djfBonusFacade.confirmSignUp(userId);
    	return R.error();
    }
    
    @RequestMapping("/toRecharge")
    @RequiresPermissions("user:update")
    public R toRecharge(Integer userId,BigDecimal money) {
    	UserEntity nodeUserEntity=userService.queryObject(userId);
    	if(nodeUserEntity==null) return R.error("查不到该用户");
    	
    	if(money.compareTo(new BigDecimal(0))<=0) {
    		return R.error("充值金额不能为0");
    	}
    	//生成一个消费订单
    	//代扣金额
    	userService.addUserBalance(userId, money);//充值
    	//写日志
		 PaymentTask paymentTask=new PaymentTask();
		 paymentTask.setAmount(money);
		 paymentTask.setFee(0d);
		 paymentTask.setPayer(nodeUserEntity.getUserName());
		 paymentTask.setUserId(nodeUserEntity.getUserId());
		 paymentTask.setMemo("【消费】充值");
		 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
    	
    	return R.ok();
    }
    
    
    @SysLog("后台修改用户基本")
    @RequestMapping("/toUpdateUserLevel")
    @RequiresPermissions("user:update")
    public R toUpdateUserLevel(Integer userId,Integer userLevelType) {
    	UserEntity nodeUserEntity=userService.queryObject(userId);
    	if(nodeUserEntity==null) return R.error("查不到该用户");
		 UserInvestLevelEntity consumedLevelType=userInvestLevelService.queryByLevelType(userLevelType);
		if(consumedLevelType==null) return R.error("查询不到级别");
		 nodeUserEntity.setUserLevelType(consumedLevelType.getUserLevelType());
		 nodeUserEntity.setUserNodeBonusLevel(consumedLevelType.getUserLevelNodeLevel());
		 userService.update(nodeUserEntity);
    	return R.ok();
    }

    
    
    
    @SysLog("手动执行分红任务")
    @RequestMapping("/toRunBonusTask")
    @RequiresPermissions("user:update")
    public R toRunBonusTask() {
    	
    	djfBonusFacade.bonusEveryShare();
    	
    	return R.ok();
    }
    
    
    @SysLog("修改用户分红结算时间")
    @RequestMapping("/updateLastBonusTime")
    @RequiresPermissions("user:update")
    public R updateLastBonusTime(Integer userId,Date shareInvestLastTime) {
    	UserEntity userEntity=userService.queryObject(userId);
    	if(userEntity==null) return R.error("查不到该用户");
    	userEntity.setShareInvestLastTime(shareInvestLastTime);
        userService.update(userEntity);
        return R.ok();
    }
    
    
    
    @SysLog("获取推荐用户")
    @RequestMapping("/getUserRecommondUsers")
    public R getUserRecommondUsers(Integer userId) {
    	List<BonusPointsVo> bonusPointsVos= bonusPointsService.queryByParentUserId(userId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
        return R.ok().put("bonusPointsVos",bonusPointsVos);
    }
    
    @SysLog("获取推荐用户")
    @RequestMapping("/getUserNodeUsers")
    public R getUserNodeUsers(Integer userId) {
    	List<BonusPointsVo> bonusPointsVos= bonusPointsService.queryByParentUserId(userId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
        return R.ok().put("bonusPointsVos",bonusPointsVos);
    }
    
    @RequestMapping("/getUserParentNode")
    public R getUserParentNode(Integer userId) {
    	BonusPointsVo bonusPointsVo= bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
    	return R.ok().put("bonusPointsVo",bonusPointsVo);
    }
    @RequestMapping("/getUserParentRecommond")
    public R getUserParentRecommond(Integer userId) {
    	BonusPointsVo bonusPointsVo= bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
    	
    	return R.ok().put("bonusPointsVo",bonusPointsVo);
    }
    
    
    
    
    @SysLog("后台修改用户节点关系基本")
    @RequestMapping("/toUpdateUserNode")
    @RequiresPermissions("user:update")
    public R toUpdateUserNode(Integer userId,Integer parentNodeUserId, Integer parentInvitedUserId ) {//1528 1527  1321
    	
    	 List<BonusPointsVo> subNodes=bonusPointsService.queryByParentUserId(parentNodeUserId, BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		 if(subNodes!=null&&subNodes.size()>=2)return R.error("该会员已挂满!");
    	djfBonusFacade.brokeNodeByUserId(userId);
    	return djfBonusFacade.addNodesToNodeBySubNodeUserId(userId, parentNodeUserId, parentInvitedUserId);
    }
    
    @SysLog("断开用户节点关系基本")
    @RequestMapping("/toBrokeNode")
    @RequiresPermissions("user:update")
    public R toBrokeNode(Integer userId) {//1528 1527  1321
    	djfBonusFacade.brokeNodeByUserId(userId);
    	return R.ok();
    }
    @SysLog("系统扣除积分，余额，资产")
    @RequestMapping("/toTakeOffMoney")
    @RequiresPermissions("user:takeoffMoney")
    public R toTakeOffMoney(Integer userId,Integer payType,BigDecimal money,String remark) {//1528 1527  1321
    	PaymentTask paymentTask=new PaymentTask();
    	UserEntity toUserEntity=userService.queryObject(userId); 
    	if(toUserEntity==null)return R.error("该会员不存在!");
    	if(money==null)return R.error("数量不能为负!");
    	if(money.intValue()<=0)return R.error("数量不能为负!");
    	if(remark!=null&&remark.length()>40)return R.error("备注不能超过20字!");
    	if(remark==null)remark="";
		String unitName="";
		if(payType==null) return R.error("扣款类别参数错误!");
		 if(payType==0) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_SYS_OUT_BALANCE);
			 BigDecimal balance = toUserEntity.getBalance();
			 if(balance==null)balance=new BigDecimal(0);
			 if(balance.intValue()<money.intValue()) {
				 return R.error("余额不足！");
			 }
			 BigDecimal sub_balance = balance.subtract(money);
			 toUserEntity.setBalance(sub_balance);
			 unitName="元";
		 }else if(payType==1) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_SYS_OUT_JF);
			 Integer point = toUserEntity.getIntegralScore();
			 if(point==null)point=0;
			 if(point.intValue()<money.intValue()) {
				 return R.error("积分不足！");
			 }
			 int sub_point = point-money.intValue();
			 toUserEntity.setIntegralScore(sub_point);
			 unitName="个积分";
		 }else if(payType==2) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_SYS_OUT_ZC);
			 BigDecimal surplusInvestMoney = toUserEntity.getSurplusInvestMoney();
			 if(surplusInvestMoney==null)surplusInvestMoney=new BigDecimal(0);
			 if(surplusInvestMoney.intValue()<money.intValue()) {
				 return R.error("资产不足！");
			 }
			 BigDecimal sub_surplusInvestMoney = surplusInvestMoney.subtract(money);//剩余收益 额度 增加
			 toUserEntity.setSurplusInvestMoney(sub_surplusInvestMoney);
			 unitName="资产";
		 }else {
			 return R.error("扣款类别参数错误!"); 
		 }
		 userService.update(toUserEntity);
		 paymentTask.setAmount(money);
		 paymentTask.setFee(0);
		 paymentTask.setUserId(toUserEntity.getUserId());
		 paymentTask.setPayer(toUserEntity.getUserName());
		 paymentTask.setUserName(toUserEntity.getUserName());
		 paymentTask.setMemo("平台扣除："+MoneyFormatUtils.formatBigDecimal(money)+unitName);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
		 paymentTask.setOrderType(OrderConstant.ORDER_TYPE_SYS);
		 paymentTask.setOrderNo("");
		 paymentTask.setOrderDesc(remark);
		 
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask);
    	return R.ok();
    }
    
    
    @SysLog("系统增加积分，余额，资产")
    @RequestMapping("/toAddMoney")
    @RequiresPermissions("user:addMoney")
    public R toAddMoney(Integer userId,Integer payType,BigDecimal money,String remark,String payPassword) {//1528 1527  1321
//    	if (!isAdmin()) {
//    		return R.error("不是平台管理员");
//    	}
    	SysConfigEntity sysConfigEntity=sysConfigService.queryByKey(ShopConstant.DJF_STORAGE_PAY_KEY);
    	if (StringUtils.isNullOrEmpty(payPassword)) {
    		return R.error("支付密码不能为空！");
    	}
    	String  dbpassword = new Sha256Hash(payPassword).toHex();
    	if(!dbpassword.equals(sysConfigEntity.getValue())) {
    		return R.error("支付密码错误！");
    	}
    	PaymentTask paymentTask=new PaymentTask();
    	UserEntity toUserEntity=userService.queryObject(userId); 
    	if(toUserEntity==null)return R.error("该会员不存在!");
    	if(money==null)return R.error("数量不能为负!");
    	if(money.intValue()<=0)return R.error("数量不能为负!");
    	if(remark!=null&&remark.length()>40)return R.error("备注不能超过20字!");
    	if(remark==null)remark="";
		String unitName="";
		if(payType==null) return R.error("扣款类别参数错误!");
		 if(payType==0) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_SYS_IN_BALANCE);
			 BigDecimal balance = toUserEntity.getBalance();
			 if(balance==null)balance=new BigDecimal(0);
			 BigDecimal sub_balance = balance.add(money);
			 toUserEntity.setBalance(sub_balance);
			 unitName="元";
			 
		 }else if(payType==1) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_SYS_IN_JF);
			 Integer point = toUserEntity.getIntegralScore();
			 if(point==null)point=0;
			 int sub_point = point+money.intValue();
			 toUserEntity.setIntegralScore(sub_point);
			 unitName="个积分";
		 }else if(payType==2) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_SYS_IN_ZC);
			 BigDecimal surplusInvestMoney = toUserEntity.getSurplusInvestMoney();
			 if(surplusInvestMoney==null)surplusInvestMoney=new BigDecimal(0);
			 BigDecimal sub_surplusInvestMoney = surplusInvestMoney.add(money);//剩余收益 额度 增加
			 toUserEntity.setSurplusInvestMoney(sub_surplusInvestMoney);
			 unitName="资产";
		 }else {
			 return R.error("扣款类别参数错误!"); 
		 }
		 userService.update(toUserEntity);
		 paymentTask.setAmount(money);
		 paymentTask.setFee(0);
		 paymentTask.setUserId(toUserEntity.getUserId());
		 paymentTask.setPayer(toUserEntity.getUserName());
		 paymentTask.setUserName(toUserEntity.getUserName());
		 paymentTask.setMemo("平台充值："+MoneyFormatUtils.formatBigDecimal(money)+unitName);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		 paymentTask.setOrderType(OrderConstant.ORDER_TYPE_SYS);
		 paymentTask.setOrderNo("");
		 paymentTask.setOrderDesc(remark);
		 
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask);
    	return R.ok();
    }
    
    
    
    
//    @SysLog("转账")
//    @RequestMapping("/toMoveMoney")
//    @RequiresPermissions("user:update")
//    public R toMoveMoney(Integer userId,int toUserId,BigDecimal money) {//1528 1527  1321
//        UserEntity user = userService.queryObject(userId);
//        String remark="";
//		BigDecimal balance = user.getBalance();//可提取数量数量
//		if(balance==null) {
//			balance = new BigDecimal("0");
//		}
//		UserEntity toUserEntity=userService.queryObject(toUserId);
//		String toUserName=toUserEntity.getUserName();
//		double defaultTxRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_TX_RATE, "0"));
//		BigDecimal toUserPay=money.multiply(new BigDecimal((1-defaultTxRate)));//到账
//		
//		//账号添加数量
//		BigDecimal toBalance = toUserEntity.getBalance();//可提取数量数量
//		if(toBalance==null) {
//			toBalance = new BigDecimal("0");
//		}
//		String toUutTradeNo = "TO"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
//		PaymentInfoEntity toPaymentInfo=new PaymentInfoEntity();
//		String toPaymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
//		toPaymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
//		
//		toPaymentInfo.setAccount(toUserEntity.getUserName());
//		toPaymentInfo.setAmount(toUserPay);//到账
//		toPaymentInfo.setOperatorId(toUserEntity.getUserId());
//		toPaymentInfo.setOperatorName(toUserEntity.getUserName());
//		toPaymentInfo.setPaymentDate(new Date());
//		toPaymentInfo.setPaymentPluginId(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE);
//		toPaymentInfo.setPaymentMethod(PluginConstant.PAY_METHOD_PLUGIN_TYPE_OFFLINE_NAME);
//		toPaymentInfo.setFee(new BigDecimal("0"));
//		toPaymentInfo.setSn(toPaymentSn);
//		toPaymentInfo.setUserId(toUserEntity.getUserId().longValue());
//		toPaymentInfo.setPayer(toUserEntity.getUserName());
//		toPaymentInfo.setUserName(toUserEntity.getUserName());
//		toPaymentInfo.setOrderType(1);//
//		toPaymentInfo.setOrderNo(toUutTradeNo);
//		toPaymentInfo.setOrderDesc("转账:"+remark);
//		toPaymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_DONATE_IN);
//		toPaymentInfo.setMemo("转账："+toUserName+"收到"+user.getUserName()+"转入：$"+MoneyFormatUtils.formatBigDecimal4(toUserPay)+"。");
//		toPaymentInfo.setCompanySn("");
//		toPaymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
//		toPaymentInfo.setCreateTime(new Date());
//		toPaymentInfo.setUpdateTime(new Date());
//		toPaymentInfo.setPaymentDate(new Date());
//		toPaymentInfo.setStatus(TradeStatus.TRADE_SUCCESS.code);
//		toUserEntity.setBalance(toBalance.add(toUserPay));
//		paymentOutService.savePaymentOutData(null, toPaymentInfo, toUserEntity);
//    	
//    	return R.ok();
//    }
//    
    
    
    @RequestMapping("/updatePlatPayPassword")
    public R updatePlatPayPassword(@RequestParam String payPassword) {
    	if (!isAdmin()) {
			return R.error("不是平台管理员");
        }
    	SysConfigEntity sysConfigEntity=sysConfigService.queryByKey(ShopConstant.DJF_STORAGE_PAY_KEY);
    	if(sysConfigEntity==null) {
    		sysConfigEntity=new SysConfigEntity();
    		sysConfigEntity.setKey(ShopConstant.DJF_STORAGE_PAY_KEY);
    		sysConfigEntity.setRemark("平台支付密码");
    		String dbPassword= new Sha256Hash(payPassword).toHex();
    		sysConfigEntity.setValue(dbPassword);
    		sysConfigEntity.setStatus(0);
    		sysConfigService.save(sysConfigEntity);
    	}else {
    		String dbPassword= new Sha256Hash(payPassword).toHex();
    		sysConfigEntity.setValue(dbPassword);
    		sysConfigEntity.setStatus(0);
    		sysConfigService.update(sysConfigEntity);
    	}
    	return R.ok().put("sysConfigEntity", sysConfigEntity);
    }

    public static void main(String[] args) {
    	System.out.println(DigestUtils.sha256Hex("888888"));
	}
    
}
