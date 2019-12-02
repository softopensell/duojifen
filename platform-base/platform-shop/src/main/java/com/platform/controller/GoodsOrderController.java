package com.platform.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.platform.cache.CacheUtil;
import com.platform.constants.PluginConstant;
import com.platform.entity.GoodsEntity;
import com.platform.entity.GoodsOrderDetailEntity;
import com.platform.entity.GoodsOrderEntity;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.GoodsOrderDetailService;
import com.platform.service.GoodsOrderService;
import com.platform.service.GoodsService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.RRException;
import com.platform.utils.excel.ExcelExport;
import com.platform.utils.excel.ExcelImport;
import com.yunpian.sdk.util.StringUtil;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 15:10:15
 */
@RestController
@RequestMapping("goodsorder")
public class GoodsOrderController extends AbstractController{
    @Autowired
    private GoodsOrderService goodsOrderService;
    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
	private UserService userService;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodsorder:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        logger.info("------query-------"+JsonUtil.getJsonByObj(query));
        List<GoodsOrderEntity> goodsOrderList = goodsOrderService.queryList(query);
        int total = goodsOrderService.queryTotal(query);
        Map<String,Object> totalMap=goodsOrderService.queryTotalPrice(query);
        PageUtils pageUtil = new PageUtils(goodsOrderList, total, query.getLimit(), query.getPage());
        Map<String,Object> userDataMap=new HashMap<String,Object>();
        userDataMap.put("orderCount", total);
        userDataMap.put("orderTotal", totalMap.get("totalPrice"));
        userDataMap.put("orderIntegralTotal", totalMap.get("totalIntegralPrice"));
        userDataMap.put("orderPayTotal", totalMap.get("totalPayPrice"));
        return R.ok().put("page", pageUtil).put("userData", userDataMap);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodsorder:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsOrderEntity goodsOrder = goodsOrderService.queryObject(id);

        return R.ok().put("goodsOrder", goodsOrder);
    }
    
    /**
     * 查看订单详情
     */
    @RequestMapping("/detailinfo/{id}")
    @RequiresPermissions("goodsorder:info")
    public R detailinfo(@PathVariable("id") Integer id) {
    	GoodsOrderEntity goodsOrder = goodsOrderService.queryObject(id);
        if(goodsOrder==null) {
        	return R.error("订单信息不存在！");
        }
        if(goodsOrder.getOrderStatus()!=null) {
        	goodsOrder.setOrderStatusName(CacheUtil.getNameByCode("orderStatus", goodsOrder.getOrderStatus()+""));
        }
        if(goodsOrder.getPayStatus()!=null) {
        	goodsOrder.setPayStatusName(CacheUtil.getNameByCode("payStatus", goodsOrder.getPayStatus()+""));
        }
        if(goodsOrder.getPayType()!=null) {
        	goodsOrder.setPayTypeName(CacheUtil.getNameByCode("payType", goodsOrder.getPayType()+""));
        }
        Map<String,Object> queryMap = new HashMap<String,Object>();
    	queryMap.put("orderNo", goodsOrder.getOrderNo());
        List<GoodsOrderDetailEntity> goodsOrderDetailList = goodsOrderDetailService.queryList(queryMap);
        if(goodsOrderDetailList==null) {
        	goodsOrderDetailList = new ArrayList<GoodsOrderDetailEntity>();
        }
        for(GoodsOrderDetailEntity goodsOrderDetailEntity:goodsOrderDetailList) {
        	GoodsEntity goodsEntity=goodsService.queryObject(goodsOrderDetailEntity.getGoodsId());
        	if(goodsEntity!=null)goodsOrderDetailEntity.setAuthor(goodsEntity.getAuthor());
        }
        
        GoodsOrderDetailEntity  totalPriceRow = new GoodsOrderDetailEntity();
        totalPriceRow.setIndex("totalPriceTitle");
        totalPriceRow.setTotalPrice(goodsOrder.getTotalPrice());
        totalPriceRow.setGoodsCount(goodsOrder.getGoodsCount());
        GoodsOrderDetailEntity  totalIntegralPriceRow = new GoodsOrderDetailEntity();
        totalIntegralPriceRow.setIndex("totalIntegralPriceTitle");
        totalIntegralPriceRow.setTotalPrice(goodsOrder.getTotalIntegralPrice());
        GoodsOrderDetailEntity  totalPayPriceRow = new GoodsOrderDetailEntity();
        totalPayPriceRow.setIndex("totalPayPriceTitle");
        totalPayPriceRow.setTotalPrice(goodsOrder.getTotalPayPrice());
        goodsOrderDetailList.add(totalPriceRow);
        goodsOrderDetailList.add(totalIntegralPriceRow);
        goodsOrderDetailList.add(totalPayPriceRow);
        goodsOrder.setGoodsOrderDetailEntityList(goodsOrderDetailList);
        return R.ok().put("goodsOrder", goodsOrder);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodsorder:save")
    public R save(@RequestBody GoodsOrderEntity goodsOrder) {
        goodsOrderService.save(goodsOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodsorder:update")
    public R update(@RequestBody GoodsOrderEntity goodsOrder) {
        goodsOrderService.update(goodsOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodsorder:delete")
    public R delete(@RequestBody Integer[] ids) {
//        goodsOrderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsOrderEntity> list = goodsOrderService.queryList(params);

        return R.ok().put("list", list);
    }
    
    
    /**
     * 取消
     */
    @RequestMapping("/cancelOrder")
    @RequiresPermissions("goodsorder:update")
    public R cancelOrder(Integer id) {
    	GoodsOrderEntity orderInfo = goodsOrderService.queryObject(id);
	    if(orderInfo==null||orderInfo.getId()==null) {
			return R.error("订单不存在！");
		}
    	if(orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_CONFRIM) {//未支付订单
			orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_CANCEL);
			goodsOrderService.update(orderInfo);
		}else if(orderInfo.getOrderStatus() == ShopConstant.ORDER_STATUS_NOSHIPMENT) {//已支付，退款，取消
			  BigDecimal amount=orderInfo.getTotalPayPrice();
			if(orderInfo.getPayType().equals(ShopConstant.PAY_TYPE_JF)) {
					//扣除用户金额
					userService.addUserIntegralScore(orderInfo.getUserId(), amount.intValue());
					//写日志
					PaymentTask paymentTask=new PaymentTask();
					paymentTask.setAmount(amount);
					paymentTask.setFee(0);
					paymentTask.setUserId(orderInfo.getUserId());
					paymentTask.setPayer(orderInfo.getContactName());
					paymentTask.setMemo("商城积分消费退款");
					paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_BACK_JF);
					paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
					paymentTask.setOrderNo(orderInfo.getOrderNo());
					paymentTask.setOrderType(orderInfo.getOrderType());
					TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
				 
			}else if(orderInfo.getPayType().equals(ShopConstant.PAY_TYPE_YE)) {
				//扣除用户金额
				 userService.addUserBalance(orderInfo.getUserId(), amount);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(amount);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(orderInfo.getUserId());
				 paymentTask.setPayer(orderInfo.getContactName());
				 paymentTask.setUserName(orderInfo.getUserName());
				 paymentTask.setMemo("商城购买消费退款");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_BACK_MONEY);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 paymentTask.setOrderNo(orderInfo.getOrderNo());
				 paymentTask.setOrderType(orderInfo.getOrderType());
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			}else {
				return R.error("退款失败，暂不支持其他方式");	
			}
			orderInfo.setOrderStatus(ShopConstant.ORDER_STATUS_REFUND);
			orderInfo.setUpdateTime(new Date());
			orderInfo.setPayStatus(ShopConstant.PAY_STATUS_REFUNDOK);
			goodsOrderService.update(orderInfo);
			
		}else {
			return R.error("已发货，不能取消");	
		}
        return R.ok();
    }
    
    /**
     * 发货
     */
    @RequestMapping("/confirmShipment")
    @RequiresPermissions("goodsorder:update")
    public R confirmShipment(@RequestBody GoodsOrderEntity goodsOrder) {
    	if(goodsOrder==null||goodsOrder.getId()==null) {
    		return R.error("订单不存在！");
    	}
    	if(StringUtil.isNullOrEmpty(goodsOrder.getLogisticsName())||StringUtil.isNullOrEmpty(goodsOrder.getLogisticsNumber())) {
    		return R.error("快递公司和快递单号不能为空！");
    	}
    	goodsOrder.setSendStatu(ShopConstant.SEND_STATUS_Y+"");
    	goodsOrder.setSendOrderTime(new Date());
    	goodsOrder.setOrderStatus(ShopConstant.ORDER_STATUS_SHIPMENT);
    	goodsOrderService.update(goodsOrder);
    	
    	return R.ok();
    }
    
    
    
    /**
     * 首页统计数据
     */
    @RequestMapping("/queryMainInitData")
    public R queryMainInitData(@RequestParam Map<String, Object> params) {
    	Map<String,Object> resultMap=new HashMap<String,Object>();
    	params.put("confirmTime", DateUtils.format(new Date()));
    	params.put("payStatus", ShopConstant.PAY_STATUS_PAYOK);//查支付成功的
    	Map<String,Object> nowDateTotalMap=goodsOrderService.queryTotalByDate(params);
    	if(nowDateTotalMap!=null) {
    		resultMap.putAll(nowDateTotalMap);
    	}
    	String orderStatusStr=params.get("orderStatuss")+"";
    	String[] orderStatuss= orderStatusStr.split(",");
    	for(String status:orderStatuss) {
    		int count=goodsOrderService.queryTotalByStatus(Integer.parseInt(status));
    		resultMap.put("status_"+status, count);
    	}
        return R.ok().put("result", resultMap);
    }
    
    /**
     * 订单统计
     */
    @RequestMapping("/queryOrderCountInitData")
    public R queryOrderCountInitData(@RequestParam Map<String, Object> params) {
    	Map<String,Object> resultMap=new HashMap<String,Object>();
    	params.put("payStatus", ShopConstant.PAY_STATUS_PAYOK);//查支付成功的
    	Map<String,Object> allTotalMap=goodsOrderService.queryTotalByDate(params);
    	if(allTotalMap!=null) {
    		resultMap.put("allTotal", allTotalMap.get("totalPrice"));
    	}else {
    		resultMap.put("allTotal", 0);
    	}
    	params.put("confirmTime", DateUtils.format(new Date()));
    	Map<String,Object> nowDateTotalMap=goodsOrderService.queryTotalByDate(params);
    	if(nowDateTotalMap!=null) {
    		resultMap.put("nowTotal", nowDateTotalMap.get("totalPrice"));
    	}else {
    		resultMap.put("nowTotal", 0);
    	}
    	resultMap.put("nowDate", DateUtils.format(new Date()));
        return R.ok().put("result", resultMap);
    }
    
    
    
    
    /**
     * 导出会员
     */
    @RequestMapping("/export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

    	Query query = new Query(params);
 		logger.info("------query--------"+JsonUtil.getJsonByObj(query));
 		query.remove("offset");
 		query.remove("page");
 		query.remove("limit");
 		//取出所有条件
 		List<GoodsOrderEntity> goodsOrderList = goodsOrderService.queryList(query);
        ExcelExport ee = new ExcelExport("订单列表");
        String[] header = new String[]{"订单号","创建时间", "客户账号", "联系方式","实付金额","付款方式","订单状态",
        		"支付状态","商品名称","规格","商品合计","收货人","收货电话","收货地址","快递公司","快递单号","商品零售价","商品市场价","市场总价","生产厂家"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (goodsOrderList != null && goodsOrderList.size() != 0) {
            for (GoodsOrderEntity goodsOrderEntity : goodsOrderList) {
            	
            	 Map<String,Object> queryMap = new HashMap<String,Object>();
             	 queryMap.put("orderNo", goodsOrderEntity.getOrderNo());
                 List<GoodsOrderDetailEntity> goodsOrderDetailList = goodsOrderDetailService.queryList(queryMap);
                 StringBuffer goodOrderDetailStrs=new StringBuffer();
                 String geStr="";
                 //单间商品价格
                 GoodsEntity goodsEntity=null;
                 if(goodsOrderDetailList!=null&&goodsOrderDetailList.size()>0) {
                	 for(GoodsOrderDetailEntity goodsOrderDetailEntity:goodsOrderDetailList) {
                		 goodOrderDetailStrs.append(goodsOrderDetailEntity.getGoodsName()+"");
                		 if(goodsOrderDetailEntity.getGoodTags()!=null) {
//                			 goodOrderDetailStrs.append("，规格："+goodsOrderDetailEntity.getGoodTags());
                			 geStr=goodsOrderDetailEntity.getGoodTags();
                		 }
//                		 goodOrderDetailStrs.append("，数量："+goodsOrderDetailEntity.getGoodsCount());
//                		 goodOrderDetailStrs.append("，单价："+goodsOrderDetailEntity.getPrice());
//                		 goodOrderDetailStrs.append("，合计："+goodsOrderDetailEntity.getTotalPrice());
                	 }
                	 if(goodsOrderDetailList.size()==1) {
                		 GoodsOrderDetailEntity onDetailEntity=goodsOrderDetailList.get(0);
                		 goodsEntity=goodsService.queryObject(onDetailEntity.getGoodsId());
                	 }
                	 
                 }
                 
                 if(goodsOrderEntity.getOrderStatus()!=null) {
                	 goodsOrderEntity.setOrderStatusName(CacheUtil.getNameByCode("orderStatus", goodsOrderEntity.getOrderStatus()+""));
                 }
                 if(goodsOrderEntity.getPayStatus()!=null) {
                	 goodsOrderEntity.setPayStatusName(CacheUtil.getNameByCode("payStatus", goodsOrderEntity.getPayStatus()+""));
                 }
                 if(goodsOrderEntity.getPayType()!=null) {
                	 goodsOrderEntity.setPayTypeName(CacheUtil.getNameByCode("payType", goodsOrderEntity.getPayType()+""));
                 }
            	
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("orderNo", goodsOrderEntity.getOrderNo());
                map.put("createTime", DateUtils.format(goodsOrderEntity.getCreateTime(), "yyyy-MM-dd HH:mm"));
                map.put("userName", goodsOrderEntity.getUserName());
                map.put("userMobile", goodsOrderEntity.getUserMobile());
                
                map.put("totalPayPrice", goodsOrderEntity.getTotalPayPrice());
                
                map.put("payType", goodsOrderEntity.getPayTypeName());
                
                map.put("orderStatusName", goodsOrderEntity.getOrderStatusName());
                map.put("payStatus", goodsOrderEntity.getPayStatusName());
                
                map.put("goodOrderDetailStrs", goodOrderDetailStrs);
                map.put("geStr", geStr);
              
                map.put("count", goodsOrderEntity.getGoodsCount());
                
                map.put("contactName", goodsOrderEntity.getContactName());
                map.put("telephone", goodsOrderEntity.getTelephone());
                map.put("pcrdetail", goodsOrderEntity.getPcrdetail());
                
                
                map.put("logisticsName", goodsOrderEntity.getLogisticsName());
                map.put("logisticsNumber", goodsOrderEntity.getLogisticsNumber());
                
                if(goodsEntity!=null) {
                	map.put("retailPrice", goodsEntity.getRetailPrice());
                	map.put("marketPrice", goodsEntity.getMarketPrice());
                	map.put("totalMarketPrice", MoneyFormatUtils.getMultiply(goodsEntity.getMarketPrice(), new BigDecimal(goodsOrderEntity.getGoodsCount())));
//                	map.put("totalMarketPrice", goodsEntity.getMarketPrice().multiply(new BigDecimal(goodsOrderEntity.getGoodsCount())));
                	if(!StringUtil.isNullOrEmpty(goodsEntity.getShopName())) {
                		map.put("author", goodsEntity.getShopName());
                	}else {
                		map.put("author", goodsEntity.getAuthor());
                	}
                }
                list.add(map);
            }
        }
        
        ee.addSheetByMap("商城订单列表", list, header);
        ee.export(response);
        return R.ok();
    }
    
    /**
     * 导入会员
     */
    @RequestMapping("/importExcel")
    public R importExcel(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
       List<String[]> excelStrs=ExcelImport.getExcelData(file);
       int successNum=0;
       int failNum=0;
       for(int i=0;excelStrs!=null&&i<excelStrs.size();i++) {
    	   String[] tempStrs=excelStrs.get(i);
    	   String logisticsName=null;
    	   String logisticsNumber=null;
    	   String orderNo=null;
    	   
    	   if(tempStrs!=null&&tempStrs.length>0) {
    		   if(tempStrs.length>=16) {
    			   orderNo=tempStrs[0];
    			   logisticsName=tempStrs[14];
    			   logisticsNumber=tempStrs[15];
    			   if(orderNo!=null&&!StringUtil.isNullOrEmpty(logisticsName)&&!StringUtil.isNullOrEmpty(logisticsNumber)) {
    				   GoodsOrderEntity  goodsOrder=goodsOrderService.queryObjectByNo(orderNo);
    				   if(goodsOrder!=null) {
    					    goodsOrder.setLogisticsName(logisticsName);
    					    goodsOrder.setLogisticsNumber(logisticsNumber);
    					    goodsOrder.setSendStatu(ShopConstant.SEND_STATUS_Y+"");
    				       	goodsOrder.setSendOrderTime(new Date());
    				       	goodsOrder.setOrderStatus(ShopConstant.ORDER_STATUS_SHIPMENT);
    				        goodsOrderService.update(goodsOrder);
    				        successNum++;
    				   }else {
    					   failNum++;
    				   }
    			   }else {
    				   failNum++;
    			   }
    		   }
    	   }
       }
       
       R r=R.ok();
       r.put("successNum", successNum);
       r.put("failNum", failNum);
       r.put("excelStrs", excelStrs);
       return r;
    }
    
    
}
