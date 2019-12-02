package com.platform.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.constants.ActivityConstant;
import com.platform.constants.PluginConstant;
import com.platform.entity.ActivityItemEntity;
import com.platform.entity.ActivityTickeorderEntity;
import com.platform.facade.PaymentPluginFacade;
import com.platform.service.ActivityItemService;
import com.platform.service.ActivityTickeorderService;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.excel.ExcelExport;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@RestController
@RequestMapping("activitytickeorder")
public class ActivityTickeorderController {
	private static final Log logger = LogFactory.getLog(ActivityTickeorderController.class);
    @Autowired
    private ActivityTickeorderService activityTickeorderService;
    @Autowired
    private ActivityItemService activityItemService;
    @Autowired
    private PaymentPluginFacade paymentPluginFacade;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activitytickeorder:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ActivityTickeorderEntity> activityTickeorderList = activityTickeorderService.queryList(query);
        int total = activityTickeorderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityTickeorderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activitytickeorder:info")
    public R info(@PathVariable("id") Integer id) {
        ActivityTickeorderEntity activityTickeorder = activityTickeorderService.queryObject(id);

        return R.ok().put("activityTickeorder", activityTickeorder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activitytickeorder:save")
    public R save(@RequestBody ActivityTickeorderEntity activityTickeorder) {
        activityTickeorderService.save(activityTickeorder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activitytickeorder:update")
    public R update(@RequestBody ActivityTickeorderEntity activityTickeorder) {
        activityTickeorderService.update(activityTickeorder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activitytickeorder:delete")
    public R delete(@RequestBody Integer[] ids) {
    	for(Integer id:ids) {
   		 ActivityTickeorderEntity activityTickeorder = activityTickeorderService.queryObject(id);
   		 if(activityTickeorder.getOrderStatu().equals(ActivityConstant.TicketOrderStatu.DEFAULT.nCode)) {//支付成功 可以确认
       		 activityTickeorderService.deleteBatch(ids);
       	 }
   	    }
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ActivityTickeorderEntity> list = activityTickeorderService.queryList(params);

        return R.ok().put("list", list);
    }
    @RequestMapping("/toConfrimTickOrder")
    @RequiresPermissions("activitytickeorder:update")
    public R toConfrimTickOrder(@RequestBody Integer[] ids) {
        for(Integer id:ids) {
        	 ActivityTickeorderEntity activityTickeorder = activityTickeorderService.queryObject(id);
        	 if(activityTickeorder.getOrderStatu().equals(ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode)) {//支付成功 可以确认
        		 activityTickeorder.setOrderStatu(ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCode);
        		 activityTickeorder.setUpdateTime(new Date());
        		 activityTickeorderService.update(activityTickeorder);
        	 }
        }
        return R.ok();
    }
    @RequestMapping("/toRefundTickOrder")
    @RequiresPermissions("activitytickeorder:update")
    public R toRefundTickOrder(@RequestBody Integer[] ids) {
    	for(Integer id:ids) {
    		 ActivityTickeorderEntity activityTickeorder = activityTickeorderService.queryObject(id);
    		 if(activityTickeorder.getOrderStatu().equals(ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode)||activityTickeorder.getOrderStatu().equals(ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCode)) {//支付成功 可以确认
        		 activityTickeorder.setOrderStatu(ActivityConstant.TicketOrderStatu.AUDITFAIL.nCode);//退款
        		 activityTickeorder.setUpdateTime(new Date());
        		 activityTickeorderService.update(activityTickeorder);
        		 ActivityItemEntity activityItemEntity=activityItemService.queryObjectByItemNo(activityTickeorder.getItemNo());
        		 paymentPluginFacade.refundMoney(activityTickeorder.getMemberId(), activityItemEntity.getItemPayType(),new BigDecimal(activityTickeorder.getTotalPrice()), OrderConstant.ORDER_TYPE_ACTIVTIY, activityTickeorder.getOrderNo(), "活动报名取消，退款！");
        	 }
    	}
    	return R.ok();
    }
    
    @RequestMapping("/toCheckInOrder")
    @RequiresPermissions("activitytickeorder:update")
    public R toCheckInOrder(@RequestBody Integer[] ids) {
    	for(Integer id:ids) {
    		 ActivityTickeorderEntity activityTickeorder = activityTickeorderService.queryObject(id);
    		 if(activityTickeorder.getOrderStatu().equals(ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode)||activityTickeorder.getOrderStatu().equals(ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCode)) {//支付成功 可以确认
        		 activityTickeorder.setOrderStatu(ActivityConstant.TicketOrderStatu.CHECKIN.nCode);//签到
        		 activityTickeorder.setUpdateTime(new Date());
        		 activityTickeorderService.update(activityTickeorder);
        	 }
    	}
    	return R.ok();
    }
    @RequestMapping("/toDownload")
    @RequiresPermissions("activitytickeorder:update")
    public R toDownload(@RequestParam Map<String, Object> params, HttpServletResponse response) {
    	Query query = new Query(params);
 		List<ActivityTickeorderEntity> activityTickeorderList = activityTickeorderService.queryList(query);
 		List<String> tempHeader=new ArrayList<>();
 		tempHeader.add("订单号");
 		tempHeader.add("活动名称");
 		tempHeader.add("会员");
 		tempHeader.add("参会人员");
 		tempHeader.add("手机号");
 		tempHeader.add("地址");
 		tempHeader.add("参与人数");
 		tempHeader.add("金额");
 		tempHeader.add("支付方式");
 		tempHeader.add("状态");
 		logger.info("------query--------"+JsonUtil.getJsonByObj(query));
 		logger.info("------tempHeader--------"+JsonUtil.getJsonByObj(tempHeader));
        ExcelExport ee = new ExcelExport("报名列表_"+DateUtils.format5(new Date()));
        String[] header=tempHeader.toArray(new String[tempHeader.size()]);
        List<Map<String, Object>> list = new ArrayList<>();
        if (activityTickeorderList != null && activityTickeorderList.size() != 0) {
            for (ActivityTickeorderEntity activityTickeorderEntity : activityTickeorderList) {
            	if(activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.CHECKIN.nCode
            			||activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode
            			||activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCode
            			||activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.AUDITFAIL.nCode
            			) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("OrderNo", activityTickeorderEntity.getOrderNo());
                map.put("ItemTitle", activityTickeorderEntity.getItemTitle());
                map.put("ticketName", activityTickeorderEntity.getTicketName());
                map.put("memberName", activityTickeorderEntity.getMemberName());
                map.put("memberPhone", activityTickeorderEntity.getMemberPhone());
                map.put("memberAddress", activityTickeorderEntity.getMemberAddress());
                map.put("ticketSum", activityTickeorderEntity.getTicketSum());
                map.put("totalPrice", activityTickeorderEntity.getTotalPrice());
                
                 if(activityTickeorderEntity.getMemberStatu()==0) {
                	map.put("getMemberStatu", "余额");
          		 }else if(activityTickeorderEntity.getMemberStatu()==1) {
          			 map.put("getMemberStatu", "积分");
          		 }else if(activityTickeorderEntity.getMemberStatu()==2) {
          			 map.put("getMemberStatu", "资产");
          		 }else {
          			 map.put("getMemberStatu", "");
          		 }
                
                if(activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.CHECKIN.nCode) {
                	map.put("orderStatu", ActivityConstant.TicketOrderStatu.CHECKIN.nCodeName);
                }else if(activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode) {
                	map.put("orderStatu", ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCodeName);
                }else if(activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCode) {
                	map.put("orderStatu", ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCodeName);
                }else if(activityTickeorderEntity.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.AUDITFAIL.nCode) {
                	map.put("orderStatu", ActivityConstant.TicketOrderStatu.AUDITFAIL.nCodeName);
                }
                
            
                
                list.add(map);
            }
           }
        }
        ee.addSheetByMap("报名列表", list, header);
        ee.export(response);
        return R.ok();
   }
    
}
