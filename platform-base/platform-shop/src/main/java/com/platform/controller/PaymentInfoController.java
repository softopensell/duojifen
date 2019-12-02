package com.platform.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.constants.PluginConstant;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PlatformFwManagerEntity;
import com.platform.entity.UserEntity;
import com.platform.facade.DjfBonusFacade;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.PaymentInfoService;
import com.platform.service.PlatformFwManagerService;
import com.platform.service.UserService;
import com.platform.util.constants.OrderConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MapRemoveNullUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.excel.ExcelExport;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
@RestController
@RequestMapping("paymentinfo")
public class PaymentInfoController extends AbstractController{
    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private UserService userService;
    
    
    @Autowired
    private DjfBonusFacade djfBonusFacade;
    @Autowired
	private ApiBonusPointsService apiBonusPointsService;
	@Autowired
	private PlatformFwManagerService platformFwManagerService ;
	
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("paymentinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
    	
    	if(params.containsKey("queryType")&&"integralcount".equals(params.get("queryType")+"")) {
    		params.put("moneyTypeWallet", PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_ADD);
        	params.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
    	}
    	
        //查询列表数据
        Query query = new Query(params);
        logger.info("------query-------"+JsonUtil.getJsonByObj(query));
        MapRemoveNullUtil.removeNullEntry(query);
        List<PaymentInfoEntity> paymentInfoList = paymentInfoService.queryList(query);
        int total = paymentInfoService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(paymentInfoList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("paymentinfo:info")
    public R info(@PathVariable("id") Long id) {
        PaymentInfoEntity paymentInfo = paymentInfoService.queryObject(id);

        return R.ok().put("paymentInfo", paymentInfo);
    }
    
    /**
     * 统计总数据
     */
    @RequestMapping("/queryIntegralCountInitData")
    public R queryIntegralCountInitData(@RequestParam Map<String, Object> params) {
    	params.put("moneyTypeWallet", PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_ADD);
    	params.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
    	Map<String,Object> resultMap=new HashMap<String,Object>();
    	Map<String,Object> allTotalMap=paymentInfoService.queryTotalByDate(params);
    	if(allTotalMap!=null) {
    		resultMap.put("allTotal", allTotalMap.get("totalIntegral"));
    	}else {
    		resultMap.put("allTotal", 0);
    	}
    	params.put("confirmTime", DateUtils.format(new Date()));
    	Map<String,Object> nowDateTotalMap=paymentInfoService.queryTotalByDate(params);
    	if(nowDateTotalMap!=null) {
    		resultMap.put("nowTotal", nowDateTotalMap.get("totalIntegral"));
    	}else {
    		resultMap.put("nowTotal", 0);
    	}
    	resultMap.put("nowDate", DateUtils.format(new Date()));
        return R.ok().put("result", resultMap);
    }
    
    /**
     * 充值列表
     */
    @RequestMapping("/rechargelist")
    @RequiresPermissions("paymentinfo:list")
    public R rechargelist(@RequestParam Map<String, Object> params) {
    	
    	if(params.get("moneyTypeWallet")!=null) {
    		params.put("moneyTypeWallet", params.get("moneyTypeWallet"));
    	}else {
    		params.put("moneyTypeWallet", PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE);
    	}
    	params.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
        //查询列表数据
        Query query = new Query(params);
        logger.info("------query-1------"+JsonUtil.getJsonByObj(query));
         MapRemoveNullUtil.removeNullEntry(query);
        logger.info("------query-2------"+JsonUtil.getJsonByObj(query));
        
        List<PaymentInfoEntity> paymentInfoList = paymentInfoService.queryList(query);
        int total = paymentInfoService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(paymentInfoList, total, query.getLimit(), query.getPage());
        
        
        Map<String,Object> queryStat=paymentInfoService.queryTotalByDate(query);
        
        return R.ok().put("page", pageUtil).put("queryStat", queryStat);
    }
    
    
    /**
     * 统计充值
     */
    @RequestMapping("/queryRechargeInitData")
    public R queryRechargeInitData(@RequestParam Map<String, Object> params) {
    	if(params.get("moneyTypeWallet")!=null) {
    		params.put("moneyTypeWallet", params.get("moneyTypeWallet"));
    	}else {
    		params.put("moneyTypeWallet", PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE);
    	}
    	params.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
    	params.put("status", TradeStatus.TRADE_SUCCESS.code);
    	Map<String,Object> resultMap=new HashMap<String,Object>();
    	Map<String,Object> allTotalMap=paymentInfoService.queryTotalByDate(params);
    	if(allTotalMap!=null) {
    		resultMap.put("allTotal", allTotalMap.get("totalIntegral"));
    	}else {
    		resultMap.put("allTotal", 0);
    	}
    	params.put("confirmTime", DateUtils.format(new Date()));
    	Map<String,Object> nowDateTotalMap=paymentInfoService.queryTotalByDate(params);
    	if(nowDateTotalMap!=null) {
    		resultMap.put("nowTotal", nowDateTotalMap.get("totalIntegral"));
    	}else {
    		resultMap.put("nowTotal", 0);
    	}
    	resultMap.put("nowDate", DateUtils.format(new Date()));
    	return R.ok().put("result", resultMap);
    }
    
    
    
    @RequestMapping("/modAmount")
    @RequiresPermissions("paymentinfo:save")
    public R modAmount(Long id,BigDecimal amount) {
    	if(id==null)return R.error("数据失败");
    	if(amount==null)return R.error("数据失败");
    	PaymentInfoEntity paymentInfo = paymentInfoService.queryObject(id);
    	if(paymentInfo==null)return R.error("数据失败");
    	paymentInfo.setAmount(amount);
    	paymentInfo.setUpdateTime(new Date());
    	paymentInfoService.update(paymentInfo);
    	logger.info("--------修改充值金额--"+JsonUtil.getJsonByObj(this.getUser())+"---"+JsonUtil.getJsonByObj(paymentInfo));
    	return R.ok();
    }
    

    /**
     * 保存
     */
    @RequestMapping("/agreeRecharge/{id}")
    @RequiresPermissions("paymentinfo:save")
    public R agreeRecharge(@PathVariable("id") Long id) {
    	
    	
    	PaymentInfoEntity paymentInfo = paymentInfoService.queryObject(id);
    	if(paymentInfo==null)return R.error("数据失败");
    	UserEntity nodeUserEntity=userService.queryObject(paymentInfo.getUserId().intValue());
    	if(nodeUserEntity==null) return R.error("查不到该用户");
    	
    	
    	if(paymentInfo.getAmount().compareTo(new BigDecimal(0))<=0) {
    		return R.error("充值金额不能为0");
    	}
    	if(paymentInfo.getStatus().equals(TradeStatus.TRADE_SUCCESS.code)) {
    		return R.error("已经充值了");
    	}
    	
    	
    	//第一判断用户是否有效
//    	if(nodeUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_INIT)) {//若果未激活
//    		
//    		djfBonusFacade.confirmSignUp(paymentInfo.getUserId().intValue());
//    		
//    	}
    	//查询附近服务中心
    	PlatformFwManagerEntity fwManagerEntity=djfBonusFacade.getLastFwUserId(paymentInfo.getUserId().intValue());
    	if(fwManagerEntity!=null) {
    		paymentInfo.setLogisticsName(fwManagerEntity.getFwName());
    		paymentInfo.setLogisticsNumber(fwManagerEntity.getFwUserName());
    	}
    	paymentInfo.setStatus(TradeStatus.TRADE_SUCCESS.code);
    	paymentInfo.setUpdateTime(new Date());
    	paymentInfo.setPaymentDate(new Date());
    	paymentInfoService.update(paymentInfo);
    	if(paymentInfo.getOrderType().equals(OrderConstant.ORDER_TYPE_RECHARGE)) {
    		//添加充值
        	userService.addUserBalance(paymentInfo.getUserId().intValue(), paymentInfo.getAmount());//充值
        	
        	
        	
    	}else if(paymentInfo.getOrderType().equals(OrderConstant.ORDER_TYPE_RECHARGE_JF)) {
    		//添加资产奖励
        	djfBonusFacade.buyDirectInvestBonus(paymentInfo.getUserId().intValue(), paymentInfo.getSn(), paymentInfo.getAmount());
        	//增加团队业绩
        	djfBonusFacade.updateNodedConsumedTeamBonusPonit(paymentInfo.getUserId().intValue(), paymentInfo.getAmount());
    	}
    	
    	
    	
    	return R.ok();
    }
    @RequestMapping("/refuseRecharge/{id}")
    @RequiresPermissions("paymentinfo:save")
    public R refuseRecharge(@PathVariable("id") Long id) {
    	PaymentInfoEntity paymentInfo = paymentInfoService.queryObject(id);
    	if(paymentInfo==null)return R.error("数据失败");
    	paymentInfo.setStatus(TradeStatus.TRADE_FAILURE.code);
    	paymentInfoService.update(paymentInfo);
    	return R.ok();
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("paymentinfo:save")
    public R save(@RequestBody PaymentInfoEntity paymentInfo) {
        paymentInfoService.save(paymentInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("paymentinfo:update")
    public R update(@RequestBody PaymentInfoEntity paymentInfo) {
        paymentInfoService.update(paymentInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("paymentinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        paymentInfoService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PaymentInfoEntity> list = paymentInfoService.queryList(params);

        return R.ok().put("list", list);
    }
    @RequestMapping("/exportRecharge")
    public R exportRecharge(@RequestParam Map<String, Object> params, HttpServletResponse response) {
 		
 		if(params.get("moneyTypeWallet")!=null) {
    		params.put("moneyTypeWallet", params.get("moneyTypeWallet"));
    	}else {
    		params.put("moneyTypeWallet", PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_RECHARGE);
    	}
    	params.put("paymentType", PluginConstant.PAYMENT_TYPE_IN);
    	Query query = new Query(params);
    	query.remove("offset");
 		query.remove("page");
 		query.remove("limit");
 		//取出所有条件
 		MapRemoveNullUtil.removeNullEntry(query);
 		
 		List<PaymentInfoEntity> paymentInfoList = paymentInfoService.queryList(query);
 		Collection<Integer> userIds=new ArrayList<>();
 		if (paymentInfoList != null && paymentInfoList.size() != 0) {
 			 for (PaymentInfoEntity paymentInfoEntity : paymentInfoList) {
 				 userIds.add(paymentInfoEntity.getUserId().intValue());
 			 }
 		 }
 		Map<Integer,UserEntity> userMap=new HashMap<>();
 		if(userIds.size()>0) {
 			userMap=userService.getByUserIds(userIds);
 		}
        ExcelExport ee = new ExcelExport("充值列表_"+DateUtils.formatYYYYMMDD(new Date()));
        String[] header = new String[]{"交易单号","会员账号", "申请日期","充值金额","充值状态","昵称","会员级别","服务中心"};
        List<Map<String, Object>> list = new ArrayList<>();
        if (paymentInfoList != null && paymentInfoList.size() != 0) {
            for (PaymentInfoEntity paymentInfoEntity : paymentInfoList) {
            	
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("orderNo", paymentInfoEntity.getOrderNo());
                map.put("userName", paymentInfoEntity.getUserName());
                map.put("userName", paymentInfoEntity.getUserName());
                map.put("createTime", DateUtils.format(paymentInfoEntity.getCreateTime(), "yyyy-MM-dd HH:mm"));
                map.put("amount", paymentInfoEntity.getAmount());
                if(paymentInfoEntity.getStatus()==0){
                	map.put("statusName", "申请中");
                }else if (paymentInfoEntity.getStatus()==1){
                	map.put("statusName", "处理中");
                }else if (paymentInfoEntity.getStatus()==8){
                	map.put("statusName", "已完成");
                }else if (paymentInfoEntity.getStatus()==10){
                	map.put("statusName", "已完成");
                }else if (paymentInfoEntity.getStatus()==11){
                	map.put("statusName", "已取消");
                }else {
                	map.put("statusName", "-");
                }
                map.put("username", "");
            	map.put("userLevel", "");
                UserEntity tempUser=userMap.get(paymentInfoEntity.getUserId().intValue());
                if(tempUser!=null) {
                	map.put("username", tempUser.getNickname());
                	map.put("userLevel", tempUser.getUserLevelTypeName());
                }
                map.put("logisticsName", paymentInfoEntity.getLogisticsName());
                list.add(map);
            }
        }
        ee.addSheetByMap("充值列表", list, header);
        ee.export(response);
        return R.ok();
    }
    
}
