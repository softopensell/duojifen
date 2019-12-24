package com.platform.controller;

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

import com.platform.entity.PlatformWithdrawShareEntity;
import com.platform.service.PlatformWithdrawShareService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
@RestController
@RequestMapping("platformwithdrawshare")
public class PlatformWithdrawShareController {
    @Autowired
    private PlatformWithdrawShareService platformWithdrawShareService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("platformwithdrawshare:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PlatformWithdrawShareEntity> platformWithdrawShareList = platformWithdrawShareService.queryList(query);
        int total = platformWithdrawShareService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(platformWithdrawShareList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("platformwithdrawshare:info")
    public R info(@PathVariable("id") Integer id) {
        PlatformWithdrawShareEntity platformWithdrawShare = platformWithdrawShareService.queryObject(id);

        return R.ok().put("platformWithdrawShare", platformWithdrawShare);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("platformwithdrawshare:save")
    public R save(@RequestBody PlatformWithdrawShareEntity platformWithdrawShare) {
    	PlatformWithdrawShareEntity temp=platformWithdrawShareService.queryByUserId(platformWithdrawShare.getUserId());
//    	if(temp!=null) {
//    		return R.ok();
//    	}
    	if(temp!=null) {
    		return R.error("该账号已存在列表中");
    	}
    	platformWithdrawShare.setState(0);
    	platformWithdrawShare.setUpdateTime(new Date());
    	platformWithdrawShareService.save(platformWithdrawShare);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("platformwithdrawshare:update")
    public R update(@RequestBody PlatformWithdrawShareEntity platformWithdrawShare) {
        platformWithdrawShareService.update(platformWithdrawShare);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("platformwithdrawshare:delete")
    public R delete(@RequestBody Integer[] ids) {
       
    	platformWithdrawShareService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PlatformWithdrawShareEntity> list = platformWithdrawShareService.queryList(params);

        return R.ok().put("list", list);
    }
}
