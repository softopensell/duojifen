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
    @RequestMapping("/exportUsers")
    @RequiresPermissions("user:list")
    public R exportUsers(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        //查询列表数据
        Query query = new Query(params);
        query.remove("offset");
 		query.remove("page");
 		query.remove("limit");
 		//取出所有条件
 		MapRemoveNullUtil.removeNullEntry(query);
        List<PlatformStatEntity> platformStatList = platformStatService.queryList(query);
        ExcelExport ee = new ExcelExport("会员列表_"+DateUtils.formatYYYYMMDD(new Date()));
        String[] header = new String[]{"USERID","会员账号", "昵称","级别","电话","推荐会员","父接点","团队资产","USDT余额","积分",
        		"总兑换金额","总资产","已收益","剩余资产","基金","上次奖励时间","状态","注册时间","服务中心"};
        List<Map<String, Object>> list = new ArrayList<>();
        if (platformStatList != null && platformStatList.size() != 0) {
            for (PlatformStatEntity user : platformStatList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("userId", user.getUserId());
                map.put("userName", user.getUserName());
                map.put("nickname", user.getNickname());
                map.put("userLevelTypeName", user.getUserLevelTypeName());
                map.put("mobile", user.getMobile());
                map.put("signupInvitedPhone", user.getSignupInvitedPhone());
                map.put("signupNodePhone", user.getSignupNodePhone());
                map.put("bonusTeamInvitedPoints", user.getBonusMeInvitedPoints());
                map.put("balance", ""+user.getBalance());
                map.put("integralScore", user.getIntegralScore());
                map.put("totalInvestMoney", user.getTotalInvestMoney());
                map.put("totalInvestIncomeMoney", user.getTotalInvestIncomeMoney());
                map.put("investIncomeMoney", user.getInvestIncomeMoney());
                map.put("surplusInvestMoney", user.getSurplusInvestMoney());
                map.put("fund", user.getFund());
                map.put("shareInvestLastTime", DateUtils.format(user.getShareInvestLastTime(), "yyyy-MM-dd HH:mm"));
               
                if(user.getState()==0){
                	map.put("state", "注册中");
                }else if (user.getState()==1){
                	map.put("state", "有效");
                }else if (user.getState()==2){
                	map.put("state", "失败");
                }else if (user.getState()==3){
                	map.put("state", "停止分红（失效）");
                }else {
                	map.put("state", "-");
                }
                map.put("registerTime", DateUtils.format(user.getRegisterTime(), "yyyy-MM-dd HH:mm"));
            	map.put("userCenter", "");
              
                list.add(map);
            }
        }
        ee.addSheetByMap("会员列表", list, header);
        ee.export(response);
        return R.ok();
    }

}
