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

import com.platform.entity.ActivitySponsorEntity;
import com.platform.service.ActivitySponsorService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:45
 */
@RestController
@RequestMapping("activitysponsor")
public class ActivitySponsorController {
    @Autowired
    private ActivitySponsorService activitySponsorService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activitysponsor:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ActivitySponsorEntity> activitySponsorList = activitySponsorService.queryList(query);
        int total = activitySponsorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activitySponsorList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activitysponsor:info")
    public R info(@PathVariable("id") Integer id) {
        ActivitySponsorEntity activitySponsor = activitySponsorService.queryObject(id);

        return R.ok().put("activitySponsor", activitySponsor);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activitysponsor:save")
    public R save(@RequestBody ActivitySponsorEntity activitySponsor) {
        activitySponsorService.save(activitySponsor);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activitysponsor:update")
    public R update(@RequestBody ActivitySponsorEntity activitySponsor) {
        activitySponsorService.update(activitySponsor);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activitysponsor:delete")
    public R delete(@RequestBody Integer[] ids) {
        activitySponsorService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ActivitySponsorEntity> list = activitySponsorService.queryList(params);

        return R.ok().put("list", list);
    }
}
