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

import com.platform.entity.PlatformMonitorEntity;
import com.platform.service.PlatformMonitorService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 20:50:37
 */
@RestController
@RequestMapping("platformmonitor")
public class PlatformMonitorController {
    @Autowired
    private PlatformMonitorService platformMonitorService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("platformmonitor:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PlatformMonitorEntity> platformMonitorList = platformMonitorService.queryList(query);
        int total = platformMonitorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(platformMonitorList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("platformmonitor:info")
    public R info(@PathVariable("id") Integer id) {
        PlatformMonitorEntity platformMonitor = platformMonitorService.queryObject(id);

        return R.ok().put("platformMonitor", platformMonitor);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("platformmonitor:save")
    public R save(@RequestBody PlatformMonitorEntity platformMonitor) {
        platformMonitorService.save(platformMonitor);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("platformmonitor:update")
    public R update(@RequestBody PlatformMonitorEntity platformMonitor) {
        platformMonitorService.update(platformMonitor);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("platformmonitor:delete")
    public R delete(@RequestBody Integer[] ids) {
        platformMonitorService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PlatformMonitorEntity> list = platformMonitorService.queryList(params);

        return R.ok().put("list", list);
    }
}
