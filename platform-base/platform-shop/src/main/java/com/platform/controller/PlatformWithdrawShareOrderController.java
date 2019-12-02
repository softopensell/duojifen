package com.platform.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.PlatformWithdrawShareOrderEntity;
import com.platform.service.PlatformWithdrawShareOrderService;
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
@RequestMapping("platformwithdrawshareorder")
public class PlatformWithdrawShareOrderController {
    @Autowired
    private PlatformWithdrawShareOrderService platformWithdrawShareOrderService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("platformwithdrawshareorder:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PlatformWithdrawShareOrderEntity> platformWithdrawShareOrderList = platformWithdrawShareOrderService.queryList(query);
        int total = platformWithdrawShareOrderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(platformWithdrawShareOrderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("platformwithdrawshareorder:info")
    public R info(@PathVariable("id") Integer id) {
        PlatformWithdrawShareOrderEntity platformWithdrawShareOrder = platformWithdrawShareOrderService.queryObject(id);

        return R.ok().put("platformWithdrawShareOrder", platformWithdrawShareOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("platformwithdrawshareorder:save")
    public R save(@RequestBody PlatformWithdrawShareOrderEntity platformWithdrawShareOrder) {
        platformWithdrawShareOrderService.save(platformWithdrawShareOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("platformwithdrawshareorder:update")
    public R update(@RequestBody PlatformWithdrawShareOrderEntity platformWithdrawShareOrder) {
        platformWithdrawShareOrderService.update(platformWithdrawShareOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("platformwithdrawshareorder:delete")
    public R delete(@RequestBody Integer[] ids) {
        platformWithdrawShareOrderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PlatformWithdrawShareOrderEntity> list = platformWithdrawShareOrderService.queryList(params);

        return R.ok().put("list", list);
    }
}
