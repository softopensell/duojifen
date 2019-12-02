package com.platform.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

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
    
}
