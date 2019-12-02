package com.platform.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.PlatformStatEntity;
import com.platform.entity.UserEntity;
import com.platform.facade.PlatformStatFacade;
import com.platform.service.PaymentInfoService;
import com.platform.service.PaymentOutService;
import com.platform.service.PlatformStatService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MapRemoveNullUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.StringUtils;
import com.platform.utils.excel.ExcelExport;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-05 01:17:12
 */
@RestController
@RequestMapping("platformstat")
public class PlatformStatController {
    @Autowired
    private PlatformStatService platformStatService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentInfoService paymentInfoService ;
    @Autowired
    private PaymentOutService paymentOutService ;
    @Autowired
    private PlatformStatFacade platformStatFacade ;
    @Resource
   	private JdbcTemplate jdbcTemplate;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("platformstat:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PlatformStatEntity> platformStatList = platformStatService.queryList(query);
        int total = platformStatService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(platformStatList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("platformstat:info")
    public R info(@PathVariable("id") Integer id) {
        PlatformStatEntity platformStat = platformStatService.queryObject(id);

        return R.ok().put("platformStat", platformStat);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("platformstat:save")
    public R save(@RequestBody PlatformStatEntity platformStat) {
        platformStatService.save(platformStat);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("platformstat:update")
    public R update(@RequestBody PlatformStatEntity platformStat) {
        platformStatService.update(platformStat);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("platformstat:delete")
    public R delete(@RequestBody Integer[] ids) {
        platformStatService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PlatformStatEntity> list = platformStatService.queryList(params);

        return R.ok().put("list", list);
    }
    
    
    @RequestMapping("/toStatUserMoney")
    @RequiresPermissions("userstat:info")
    public R toStatUserMoney(@RequestParam(value="userId") Integer userId) {
    	Map<String,Object> resultMap=new HashMap<String,Object>();
    	
    	UserEntity userEntity=userService.getById(userId);
    	resultMap.put("userEntity", userEntity);//用户总数据
    	
    	Map<String,Object> payTotalMap=paymentInfoService.queryStatByUserId(userId,ShopConstant.DJF_TJ_DATE);
    	resultMap.put("payTotalMap", payTotalMap);
    	
    	Map<String, Object> params=new HashMap<>();
    	params.put("userId", userId);
    	Map<String,Object> payoutTotalMap=paymentOutService.queryStat(params);
    	resultMap.put("payoutTotalMap", payoutTotalMap);
        return R.ok().put("result", resultMap);
    }
    
    
    /**
     * 查看列表
     */
    @RequestMapping("/exportStat")
    @RequiresPermissions("platformstat:list")
    public R exportStat(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //查询列表数据
        Query query = new Query(params);
        query.remove("offset");
 		query.remove("page");
 		query.remove("limit");
 		//取出所有条件
 		MapRemoveNullUtil.removeNullEntry(query);
        List<PlatformStatEntity> platformStatList = platformStatService.queryList(query);
        ExcelExport ee = new ExcelExport("统计列表_"+DateUtils.formatYYYYMMDD(new Date()));
        String[] header = new String[]{"编号","会员总数","未激活", "V1","V2","V3","V4","日新增","日未激活", "日新增V1","日新增V2","日新增V3","日新增V4",
        		"累计资产","余额总量","总积分","剩余资产","总基金",
        		"今日分成比例","今日资产收益","今日分享收益","今日社区收益","今日星星收益","今日基金收益",
        		"今日余额支付数量","今日余额支付总额","今日积分支付数量" ,"今日积分支付总额" ,"今日积分兑换总额"
        		,"余额转出数量" ,"余额转出总额" ,"余额转入数量" ,"余额转入总额"
        		,"充值数量" ,"充值金额" ,"提现数量" ,"提现金额"
        		};
        List<Map<String, Object>> list = new ArrayList<>();
        if (platformStatList != null && platformStatList.size() != 0) {
            for (PlatformStatEntity stat : platformStatList) {
            	LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                
            	map.put("statDateNumber",stat.getStatDateNumber());
            	map.put("statUserTotalSum",stat.getStatUserTotalSum());
                map.put("statUserV0Sum",stat.getStatUserV0Sum());
                map.put("statUserV1Sum",stat.getStatUserV1Sum());
                map.put("statUserV2Sum",stat.getStatUserV2Sum());
                map.put("statUserV3Sum",stat.getStatUserV3Sum());
                map.put("statUserV4Sum",stat.getStatUserV4Sum());
                
                
                map.put("statUserDayAddSum",stat.getStatUserDayAddSum());
                map.put("statUserDayAddV0Sum",stat.getStatUserDayAddV0Sum());
                map.put("statUserDayAddV1Sum",stat.getStatUserDayAddV1Sum());
                map.put("statUserDayAddV2Sum",stat.getStatUserDayAddV2Sum());
                map.put("statUserDayAddV3Sum",stat.getStatUserDayAddV3Sum());
                map.put("statUserDayAddV4Sum",stat.getStatUserDayAddV4Sum());
                
                
                map.put("statUserTotalCz", stat.getStatUserTotalCz());
                map.put("statUserTotalZc", stat.getStatUserTotalZc());
                map.put("statUserTotalJf", stat.getStatUserTotalJf());
                map.put("statUserTotalSyZc", stat.getStatUserTotalSyZc());
                map.put("statUserTotalFund", stat.getStatUserTotalFund());
                
                map.put("statDayRate", stat.getStatDayRate());
                map.put("statDayMoneyQy", stat.getStatDayMoneyQy());
                map.put("statDayMoneyFw", stat.getStatDayMoneyFw());
                map.put("statDayMoneySq", stat.getStatDayMoneySq());
                map.put("statDayMoneyXx", stat.getStatDayMoneyXx());
                map.put("statDayMoneyFund", stat.getStatDayMoneyFund());
                
                map.put("statDayPayBalanceSum", stat.getStatDayPayBalanceSum());
                map.put("statDayPayBalance", stat.getStatDayPayBalance());
                map.put("statDayPayJfSum", stat.getStatDayPayJfSum());
                map.put("statDayPayJf", stat.getStatDayPayJf());
                map.put("statDayJfDh", stat.getStatDayJfDh());
                
                map.put("statDayBalanceZzOutSum", stat.getStatDayBalanceZzOutSum());
                map.put("statDayBalanceZzOut", stat.getStatDayBalanceZzOut());
                map.put("statDayBalanceZzInSum", stat.getStatDayBalanceZzInSum());
                map.put("statDayBalanceZzIn", stat.getStatDayBalanceZzIn());
                
                map.put("statDayMoneyRechargeSum", stat.getStatDayMoneyRechargeSum());
                map.put("statDayMoneyRecharge", stat.getStatDayMoneyRecharge());
                map.put("statDayMoneyTxSum", stat.getStatDayMoneyTxSum());
                map.put("statDayMoneyTx", stat.getStatDayMoneyTx());
              
                list.add(map);
            }
        }
        ee.addSheetByMap("统计列表", list, header);
        ee.export(response);
        return R.ok();
    }

}
