package com.platform.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.cache.CacheUtil;
import com.platform.cache.IdWorkCache;
import com.platform.constants.PluginConstant;
import com.platform.entity.GoodsOrderDetailEntity;
import com.platform.entity.GoodsOrderEntity;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PaymentLogEntity;
import com.platform.entity.PaymentOutEntity;
import com.platform.entity.UserEntity;
import com.platform.service.PaymentInfoService;
import com.platform.service.PaymentOutService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.utils.DateUtils;
import com.platform.utils.HttpUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MapRemoveNullUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.RandomUtil;
import com.platform.utils.ShiroUtils;
import com.platform.utils.StringUtils;
import com.platform.utils.excel.ExcelExport;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
@RestController
@RequestMapping("paymentout")
public class PaymentOutController extends AbstractController{
    @Autowired
    private PaymentOutService paymentOutService;
    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private IdWorkCache idWorkCache;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("paymentout:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        

        List<PaymentOutEntity> paymentOutList = paymentOutService.queryList(query);
        
        int total = paymentOutService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(paymentOutList, total, query.getLimit(), query.getPage());
        
        Map<String,Object> queryStat=paymentOutService.queryStat(query);

        return R.ok().put("page", pageUtil).put("queryStat", queryStat);
    }
    
    
   

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("paymentout:info")
    public R info(@PathVariable("id") Long id) {
        PaymentOutEntity paymentOut = paymentOutService.queryObject(id);

        return R.ok().put("paymentOut", paymentOut);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("paymentout:save")
    public R save(@RequestBody PaymentOutEntity paymentOut) {
        paymentOutService.save(paymentOut);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("paymentout:update")
    public R update(@RequestBody PaymentOutEntity paymentOut) {
    	
        paymentOutService.update(paymentOut);

        return R.ok();
    }
    /**
     * 打款
     */
    @RequestMapping("/toPaymentOut")
    @RequiresPermissions("paymentout:update")
    public R toPaymentOut(HttpServletRequest request, @RequestBody PaymentOutEntity paymentOutx) {
    	toPaymentOutSuccess(paymentOutx.getId());
    	return R.ok();
    }
    public R toPaymentOutSuccess(Long id) {
    	logger.info("--------id---------"+id);
    	PaymentOutEntity paymentOutEntity=paymentOutService.queryObject(id);
    	logger.info("--------paymentOutEntity---------"+JsonUtil.getJsonByObj(paymentOutEntity));
		if(paymentOutEntity.getStatus()==ShopConstant.PAYMENT_OUT_STATU_SUCCESS) {
			return R.error("已经提现成功！");
		}
		if(paymentOutEntity.getStatus()==ShopConstant.PAYMENT_OUT_STATU_FAILL) {
			return R.error("已经取消了！");
		}
		if(paymentOutEntity.getMethod()==ShopConstant.PAY_TYPE_XJ) {
			paymentOutEntity.setStatus(ShopConstant.PAYMENT_OUT_STATU_SUCCESS);
			paymentOutEntity.setPaymentDate(new Date());
			paymentOutEntity.setUpdateTime(new Date());
			if(paymentOutEntity.getOutTradeNo()==null)return R.error("支付流水未查到");
			logger.info("--------paymentOut---------"+JsonUtil.getJsonByObj(paymentOutEntity));
			PaymentInfoEntity paymentInfoEntity=paymentInfoService.findByOrderNoAndOrderType(paymentOutEntity.getOutTradeNo(),OrderConstant.ORDER_TYPE_TX);
			logger.info("--------paymentInfoEntity---------"+JsonUtil.getJsonByObj(paymentInfoEntity));
			
			if(paymentInfoEntity==null||!paymentInfoEntity.getOrderNo().equals(paymentOutEntity.getOutTradeNo())) {
				return R.error("支付流水未查到");
			}
			logger.info("--------paymentInfoEntity---------"+JsonUtil.getJsonByObj(paymentInfoEntity));
			paymentInfoEntity.setStatus(TradeStatus.TRADE_SUCCESS.code);
			paymentInfoEntity.setUpdateTime(new Date());
			paymentInfoEntity.setPaymentDate(new Date());
			paymentInfoService.update(paymentInfoEntity);
			paymentOutService.update(paymentOutEntity);
		}else if(paymentOutEntity.getMethod()==ShopConstant.PAY_TYPE_ZFB) {//支付宝
//			AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransferResponse=AlipayBaseApi.getAlipayFundTransToaccountTransferRequest(paySn, aliAccount, realName, amount, showPayTitle, payBodyDesc);
//			if(alipayFundTransToaccountTransferResponse!=null) {
//				if(alipayFundTransToaccountTransferResponse.isSuccess()){
//					paymentOut.setStatus(ShopConstant.PAYMENT_OUT_STATU_SUCCESS);
//					paymentOut.setPaymentDate(new Date());
//					paymentOut.setSnTradeNo(alipayFundTransToaccountTransferResponse.getOrderId());
//					paymentOut.setSnTradeNoDesc("SubMsg:"+alipayFundTransToaccountTransferResponse.getSubMsg()+"transferInfo:"+ObjToStringUtil.objToString(alipayFundTransToaccountTransferResponse)+"responseBody:"+alipayFundTransToaccountTransferResponse.getBody());
//					PaymentLogEntity paymentLog = CreatePaymentLog(paySn, ShopConstant.PAYMENT_OUT_STATU_SUCCESS, "转账成功", request,"【出账】转出"+ObjToStringUtil.objToString(paymentOut)+"【返回数据】"+ObjToStringUtil.objToString(alipayFundTransToaccountTransferResponse)+alipayFundTransToaccountTransferResponse.getBody());
//					paymentOutService.updatePayMentData(paymentOut, null, paymentLog, null);
//				}else{
//					paymentOut.setPaymentDate(new Date());
//					paymentOut.setSnTradeNo(alipayFundTransToaccountTransferResponse.getOrderId());
//					paymentOut.setSnTradeNoDesc("SubMsg:"+alipayFundTransToaccountTransferResponse.getSubMsg()+"transferInfo:"+ObjToStringUtil.objToString(alipayFundTransToaccountTransferResponse)+"responseBody:"+alipayFundTransToaccountTransferResponse.getBody());
//					PaymentLogEntity paymentLog = CreatePaymentLog(paySn, ShopConstant.PAYMENT_OUT_STATU_FAILL, "转账失败:"+alipayFundTransToaccountTransferResponse.getSubMsg(), request,"【出账】转出"+ObjToStringUtil.objToString(paymentOut)+"【返回数据】"+ObjToStringUtil.objToString(alipayFundTransToaccountTransferResponse)+alipayFundTransToaccountTransferResponse.getBody());
//					if(alipayFundTransToaccountTransferResponse.getSubCode().equals("PAYER_BALANCE_NOT_ENOUGH")){//非正常失败需要重新 审核
//						paymentOut.setStatus(ShopConstant.PAYMENT_OUT_STATU_ERROR);
//						paymentOutService.updatePayMentData(paymentOut, null, paymentLog, null);
//					}else{
//						//退还给个人账户：1、更新user 的账户余额记录，2、更新paymentout 状态，3、增加paymentInfo流水表记录
//						Integer userId=paymentOut.getUserId();
//						if(userId==null) {
//							 return R.error("申请数据异常，用户id为空");
//						}
//						paymentBack(paymentOut,paymentLog);
//					}
//				}
//			}else {
//				//test  测试
////				PaymentLogEntity paymentLog = CreatePaymentLog(paySn, ShopConstant.PAYMENT_OUT_STATU_FAILL, "转账失败:test", request,"【出账】转出"+ObjToStringUtil.objToString(paymentOut)+"【返回数据】test");
////				paymentBack(paymentOut,paymentLog);
//				return R.error("打款异常");
//			}
		}
		return R.ok();
    }
    
    
    
    @RequestMapping("/toPaymentBack")
    @RequiresPermissions("paymentout:update")
    public R toPaymentBack(Long id) {
    	logger.info("--------id---------"+id);
    	PaymentOutEntity paymentOut=paymentOutService.queryObject(id);
		if(paymentOut==null)return R.error("数据错误！");
		
		if(paymentOut.getStatus()==ShopConstant.PAYMENT_OUT_STATU_SUCCESS) {
			return R.error("已经提现成功！");
		}else  if(paymentOut.getStatus()==ShopConstant.PAYMENT_OUT_STATU_FAILL) {
			return R.error("已经退款！");
		}
		
		if(paymentOut.getMethod()==ShopConstant.PAY_TYPE_XJ) {
			paymentOut.setStatus(ShopConstant.PAYMENT_OUT_STATU_FAILL);
			paymentOut.setPaymentDate(new Date());
			paymentOut.setUpdateTime(new Date());
			PaymentInfoEntity paymentInfoEntity=paymentInfoService.findByOrderNoAndOrderType(paymentOut.getOutTradeNo(),OrderConstant.ORDER_TYPE_TX);
			
			if(paymentInfoEntity==null||!paymentInfoEntity.getOrderNo().equals(paymentOut.getOutTradeNo())) {
				return R.error("支付流水未查到");
			}
			
			logger.info("--------paymentInfoEntity---------"+JsonUtil.getJsonByObj(paymentInfoEntity));
			
			paymentInfoEntity.setUpdateTime(new Date());
			paymentInfoEntity.setStatus(TradeStatus.TRADE_FAILURE.code);
			paymentInfoService.update(paymentInfoEntity);
			paymentOutService.update(paymentOut);
			//退款
			Integer userId=paymentOut.getUserId();
			UserEntity user = userService.queryObject(userId);
			BigDecimal userBalance=user.getBalance();
			BigDecimal payAmount = paymentOut.getAmount();
			BigDecimal fee = paymentOut.getFee();
			if(userBalance==null) {
				userBalance = new BigDecimal("0");
			}
			if(payAmount==null) {
				payAmount = new BigDecimal("0");
			}
			if(fee==null) {
				fee = new BigDecimal("0");
			}
			BigDecimal toBackMoney=fee.add(payAmount);
			user.setBalance(userBalance.add(toBackMoney));
			userService.update(user);
		}
    	return R.ok();
    }
    
    
    
    
    /*
	 * 退还流程
	 * 1、更新member账户余额，和可提现余额
	 * 2、更新paymentOut 账户状态为 提款失败
	 * 3、增加payment 流水记录
	 */
	private void paymentBack(PaymentOutEntity paymentOut,PaymentLogEntity paymentLog) {
		Integer userId=paymentOut.getUserId();
		UserEntity user = userService.queryObject(userId);
		BigDecimal userBalance=user.getBalance();
		BigDecimal payAmount = paymentOut.getAmount();
		if(userBalance==null) {
			userBalance = new BigDecimal("0");
		}
		if(payAmount==null) {
			payAmount = new BigDecimal("0");
		}
		user.setBalance(userBalance.add(payAmount));
		paymentOut.setStatus(ShopConstant.PAYMENT_OUT_STATU_FAILL);
		paymentOut.setPaymentDate(new Date());
		PaymentInfoEntity paymentInfo=new PaymentInfoEntity();
		
//		String paymentSn = "QA"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
		
		String paymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		paymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		
		
		paymentInfo.setAccount(paymentOut.getReceiptAccount());
		paymentInfo.setAmount(paymentOut.getAmount());//退还金额
		paymentInfo.setOperatorId(ShiroUtils.getUserEntity().getUserId().intValue());
		paymentInfo.setOperatorName(ShiroUtils.getUserEntity().getUsername());
		paymentInfo.setPaymentDate(new Date());
		paymentInfo.setPaymentPluginId(ShopConstant.PAY_METHOD_PLUGIN_TYPE_ALI);
		paymentInfo.setPaymentMethod(ShopConstant.PAY_METHOD_PLUGIN_TYPE_ALI_NAME);
		paymentInfo.setSn(paymentSn);
		paymentInfo.setStatus(10);//交易结束
		paymentInfo.setFee(new BigDecimal("0"));
		paymentInfo.setUserId(userId.longValue());
		paymentInfo.setPayer(user.getUserName());
		paymentInfo.setUserName(user.getUserName());
		paymentInfo.setOrderType(1);//
		paymentInfo.setOrderNo(paymentOut.getOutTradeNo());
		paymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_BACK_MONEY);
		paymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		paymentInfo.setCreateTime(new Date());
		paymentInfo.setUpdateTime(new Date());
		paymentOutService.updatePayMentData(paymentOut, paymentInfo, paymentLog, user);
	}
    public PaymentLogEntity CreatePaymentLog(String paySn, Integer steps, String remark,
			HttpServletRequest request, String message) {
    	PaymentLogEntity paymentlog = new PaymentLogEntity();
		String clientIp =HttpUtils.getClientIp(request);
		String alireqParam = "";
		Timestamp sysTime = new Timestamp(System.currentTimeMillis());
		try {
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				alireqParam += paraName + "=" + request.getParameter(paraName)
						+ "&";
			}
			if (alireqParam.length() > 1) {
				alireqParam = alireqParam
						.substring(0, alireqParam.length() - 1);
			}
			if (alireqParam.length() > 2000) {
				alireqParam = alireqParam.substring(0, 2000);
			}
			paymentlog.setPaysn(paySn);
			paymentlog.setSteps(steps);
			paymentlog.setServerip(request.getRemoteAddr());
			paymentlog.setClientip(clientIp);
			paymentlog.setRemark(remark);
			if(StringUtils.isEmpty(message)){
				paymentlog.setReqparams(alireqParam);
			}else{
				paymentlog.setReqparams(message+alireqParam);
			}
			paymentlog.setCreateTime(sysTime);
			paymentlog.setUpdateTime(sysTime);
		} catch (Exception ex) {
			
		}
		return paymentlog;
	}

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("paymentout:delete")
    public R delete(@RequestBody Long[] ids) {
        paymentOutService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PaymentOutEntity> list = paymentOutService.queryList(params);

        return R.ok().put("list", list);
    }
    
    @RequestMapping("/toConfirmPaymentOut")
    @RequiresPermissions("paymentout:update")
    public R toConfirmPaymentOut(@RequestBody Long[] ids) {
         for(Long id:ids) {
        	 toPaymentOutSuccess(id);
         }
        return R.ok();
    }
    /**
     * 导出会员
     */
    @RequestMapping("/export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

    	logger.info("------params--------"+JsonUtil.getJsonByObj(params));
    	Query query = new Query(params);
    	 MapRemoveNullUtil.removeNullEntry(query);
    	logger.info("------query--------"+JsonUtil.getJsonByObj(query));
 		query.remove("offset");
 		query.remove("page");
 		query.remove("limit");
 		//取出所有条件
 		 MapRemoveNullUtil.removeNullEntry(query);
 		List<PaymentOutEntity> paymentOutList = paymentOutService.queryList(query);
        ExcelExport ee = new ExcelExport("提现列表_"+DateUtils.formatYYYYMMDD(new Date()));
        String[] header = new String[]{"交易单号","会员账号", "申请日期","提币地址","提币数量","手续费",
        		"实际到账","提币状态"};
        List<Map<String, Object>> list = new ArrayList<>();
        if (paymentOutList != null && paymentOutList.size() != 0) {
            for (PaymentOutEntity paymentOutEntity : paymentOutList) {
            	
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("orderNo", paymentOutEntity.getOutTradeNo());
                map.put("userName", paymentOutEntity.getUserName());
                map.put("createTime", DateUtils.format(paymentOutEntity.getCreateTime(), "yyyy-MM-dd HH:mm"));
                map.put("receiptAccount", paymentOutEntity.getReceiptAccount());
                map.put("allamount", paymentOutEntity.getAmount().add(paymentOutEntity.getFee()));
                map.put("free", paymentOutEntity.getFee());
                map.put("amount", paymentOutEntity.getAmount());
                if(paymentOutEntity.getStatus()==0){
                	map.put("statusName", "-");
                }else if (paymentOutEntity.getStatus()==1){
                	map.put("statusName", "待处理");
                }else if (paymentOutEntity.getStatus()==2){
                	map.put("statusName", "已处理");
                }else if (paymentOutEntity.getStatus()==3){
                	map.put("statusName", "已取消");
                }else {
                	map.put("statusName", "-");
                }
                list.add(map);
            }
        }
        ee.addSheetByMap("提列表", list, header);
        ee.export(response);
        return R.ok();
    }
    /**
     * 导出会员
     */
    @RequestMapping("/exportDealOrder")
    public R exportDealOrder(@RequestParam Map<String, Object> params, HttpServletResponse response) {

    	logger.info("------params--------"+JsonUtil.getJsonByObj(params));
    	Query query = new Query(params);
    	 MapRemoveNullUtil.removeNullEntry(query);
    	logger.info("------query--------"+JsonUtil.getJsonByObj(query));
 		query.remove("offset");
 		query.remove("page");
 		query.remove("limit");
 		//取出所有条件
 		 MapRemoveNullUtil.removeNullEntry(query);
 		List<PaymentOutEntity> paymentOutList = paymentOutService.queryList(query);
        ExcelExport ee = new ExcelExport("合并提现列表_"+DateUtils.formatYYYYMMDD(new Date()));
        String[] header = new String[]{"交易单号","会员账号", "申请日期","提币地址","提币数量","手续费",
        		"实际到账","提币状态"};
        List<Map<String, Object>> list = new ArrayList<>();
        HashMap<String,LinkedHashMap<String, Object>> dealMap=new HashMap<String, LinkedHashMap<String,Object>>();
        if (paymentOutList != null && paymentOutList.size() != 0) {
            for (PaymentOutEntity paymentOutEntity : paymentOutList) {
            	String accountKey=paymentOutEntity.getReceiptAccount()+"_"+paymentOutEntity.getStatus();
            	if(dealMap.containsKey(accountKey)) {
            		LinkedHashMap<String, Object> map =dealMap.get(accountKey);
            		String orderNostr=map.get("orderNo")+"_"+paymentOutEntity.getOutTradeNo();
            		String createTimeStr=map.get("createTime")+"_"+DateUtils.format(paymentOutEntity.getCreateTime(), "yyyy-MM-dd HH:mm");
            		BigDecimal allamount=((BigDecimal) map.get("allamount")).add(paymentOutEntity.getAmount()).add(paymentOutEntity.getFee());
            		BigDecimal free=((BigDecimal) map.get("free")).add(paymentOutEntity.getFee());
            		BigDecimal amount=((BigDecimal) map.get("amount")).add(paymentOutEntity.getFee());
	        		 map.put("orderNo", orderNostr);
	                 map.put("createTime", createTimeStr);
	                 map.put("allamount", allamount);
	                 map.put("free",free);
	                 map.put("amount",amount);
            	}else {
            		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                    map.put("orderNo", paymentOutEntity.getOutTradeNo());
                    map.put("userName", paymentOutEntity.getUserName());
                    map.put("createTime", DateUtils.format(paymentOutEntity.getCreateTime(), "yyyy-MM-dd HH:mm"));
                    map.put("receiptAccount", paymentOutEntity.getReceiptAccount());
                    map.put("allamount", paymentOutEntity.getAmount().add(paymentOutEntity.getFee()));
                    map.put("free", paymentOutEntity.getFee());
                    map.put("amount", paymentOutEntity.getAmount());
                    if(paymentOutEntity.getStatus()==0){
                    	map.put("statusName", "-");
                    }else if (paymentOutEntity.getStatus()==1){
                    	map.put("statusName", "待处理");
                    }else if (paymentOutEntity.getStatus()==2){
                    	map.put("statusName", "已处理");
                    }else if (paymentOutEntity.getStatus()==3){
                    	map.put("statusName", "已取消");
                    }else {
                    	map.put("statusName", "-");
                    }
                    dealMap.put(accountKey, map);
            	}
            }
        }
        Iterator<Entry<String, LinkedHashMap<String, Object>>> its = dealMap.entrySet().iterator();
        while(its.hasNext()){
        	Entry<String, LinkedHashMap<String, Object>> entry = its.next();
        	LinkedHashMap<String, Object> map =entry.getValue();
        	list.add(map);
        }
        ee.addSheetByMap("提列表", list, header);
        ee.export(response);
        return R.ok();
    }
    
}
