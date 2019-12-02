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

import com.platform.entity.PlatformMonitorStadetailEntity;
import com.platform.service.PlatformMonitorStadetailService;
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
@RequestMapping("platformmonitorstadetail")
public class PlatformMonitorStadetailController {
    @Autowired
    private PlatformMonitorStadetailService platformMonitorStadetailService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("platformmonitorstadetail:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PlatformMonitorStadetailEntity> platformMonitorStadetailList = platformMonitorStadetailService.queryList(query);
        int total = platformMonitorStadetailService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(platformMonitorStadetailList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("platformmonitorstadetail:info")
    public R info(@PathVariable("id") Integer id) {
        PlatformMonitorStadetailEntity platformMonitorStadetail = platformMonitorStadetailService.queryObject(id);

        return R.ok().put("platformMonitorStadetail", platformMonitorStadetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("platformmonitorstadetail:save")
    public R save(@RequestBody PlatformMonitorStadetailEntity platformMonitorStadetail) {
        platformMonitorStadetailService.save(platformMonitorStadetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("platformmonitorstadetail:update")
    public R update(@RequestBody PlatformMonitorStadetailEntity platformMonitorStadetail) {
        platformMonitorStadetailService.update(platformMonitorStadetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("platformmonitorstadetail:delete")
    public R delete(@RequestBody Integer[] ids) {
        platformMonitorStadetailService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PlatformMonitorStadetailEntity> list = platformMonitorStadetailService.queryList(params);

        return R.ok().put("list", list);
    }
}
