package com.platform.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.SysLog;
import com.platform.cache.IdWorkCache;
import com.platform.constants.PluginConstant;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PlatformFwManagerEntity;
import com.platform.entity.UserEntity;
import com.platform.service.PaymentInfoService;
import com.platform.service.PlatformFwManagerService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.RandomUtil;
import com.platform.utils.ShiroUtils;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
@RestController
@RequestMapping("platformfwmanager")
public class PlatformFwManagerController {
    @Autowired
    private PlatformFwManagerService platformFwManagerService;
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
    @RequiresPermissions("platformfwmanager:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PlatformFwManagerEntity> platformFwManagerList = platformFwManagerService.queryList(query);
        int total = platformFwManagerService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(platformFwManagerList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("platformfwmanager:info")
    public R info(@PathVariable("id") Integer id) {
        PlatformFwManagerEntity platformFwManager = platformFwManagerService.queryObject(id);

        return R.ok().put("platformFwManager", platformFwManager);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("platformfwmanager:save")
    public R save(@RequestBody PlatformFwManagerEntity platformFwManager) {
    	PlatformFwManagerEntity temp=platformFwManagerService.queryByUserId(platformFwManager.getFwUserId());
    	if(temp!=null) {
    		return R.ok();
    	}
    	platformFwManager.setFwCurDate(new Date());
    	platformFwManager.setFwCurYj(new BigDecimal(0));
    	platformFwManager.setFwTotalPayMoney(new BigDecimal(0));
    	platformFwManager.setFwTotalResetTime(0);
    	platformFwManager.setFwTotalYj(new BigDecimal(0));
    	
    	platformFwManager.setUpdateTime(new Date());
    	
        platformFwManagerService.save(platformFwManager);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("platformfwmanager:update")
    public R update(@RequestBody PlatformFwManagerEntity platformFwManager) {
    	platformFwManager.setUpdateTime(new Date());
    	
        platformFwManagerService.update(platformFwManager);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("platformfwmanager:delete")
    public R delete(@RequestBody Integer[] ids) {
    	platformFwManagerService.deleteBatch(ids);
        return R.ok();
    }
    
    
    

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PlatformFwManagerEntity> list = platformFwManagerService.queryList(params);

        return R.ok().put("list", list);
    }
    
    
    
    
    
    /*
	 * 服务中心分红奖励
	 * 1、更新member账户余额，和可提现余额
	 * 2、更新paymentOut 账户状态为 提款失败
	 * 3、增加payment 流水记录
	 */
    @SysLog("重置服务中心分红")
    @RequestMapping("/toPayFwManagerShare")
    @RequiresPermissions("platformfwmanager:update")
    public R toPayFwManagerShare(Integer id,BigDecimal toPay) {
    	PlatformFwManagerEntity platformFwManager = platformFwManagerService.queryObject(id);
    	if(platformFwManager==null) {
    		return R.error();
    	}
    	BigDecimal curYj=platformFwManager.getFwCurYj();
    	
    	platformFwManager.setFwCurDate(new Date());
    	platformFwManager.setFwCurYj(new BigDecimal(0));
    	platformFwManager.setUpdateTime(new Date());
    	platformFwManager.setFwTotalResetTime(platformFwManager.getFwTotalResetTime()+1);
    	platformFwManager.setFwTotalPayMoney(platformFwManager.getFwTotalPayMoney().add(toPay));
    	
    	platformFwManager.setFwTotalYj(platformFwManager.getFwTotalYj().add(curYj));
    	
    	//用户不加钱 关了
    	
		UserEntity user = userService.queryObject(platformFwManager.getFwUserId());
//		BigDecimal userBalance=user.getBalance();
//		if(userBalance==null) {
//			userBalance = new BigDecimal("0");
//		}
//		
//		user.setBalance(userBalance.add(toPay));
		
		PaymentInfoEntity paymentInfo=new PaymentInfoEntity();
		String paySn = "QA"+DateUtils.format2(new Date())+RandomUtil.getRandom(1000l,9999l);
    	paySn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
    	paymentInfo.setSn(paySn);
		paymentInfo.setAccount(user.getUserName());
		paymentInfo.setAmount(toPay);
		paymentInfo.setOperatorId(ShiroUtils.getUserEntity().getUserId().intValue());
		paymentInfo.setOperatorName(ShiroUtils.getUserEntity().getUsername());
		paymentInfo.setPaymentDate(new Date());
		paymentInfo.setPaymentPluginId(ShopConstant.PAY_METHOD_PLUGIN_TYPE_ALI);
		paymentInfo.setPaymentMethod(ShopConstant.PAY_METHOD_PLUGIN_TYPE_ALI_NAME);
		paymentInfo.setStatus(8);
		paymentInfo.setFee(new BigDecimal("0"));
		paymentInfo.setUserId(user.getUserId().longValue());
		paymentInfo.setPayer(user.getUserName());
		paymentInfo.setUserName(user.getUserName());
		paymentInfo.setOrderType(0);
		paymentInfo.setOrderNo("");
		paymentInfo.setMemo("当前业绩："+curYj+"奖金数量："+toPay);
		paymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_FW_MANAGER_PAY);
		paymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		paymentInfo.setCreateTime(new Date());
		paymentInfo.setUpdateTime(new Date());
		
		paymentInfoService.save(paymentInfo);
		
//		userService.update(user);
		
		platformFwManagerService.update(platformFwManager);
		
        return R.ok();
    }

}
