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

import com.platform.entity.ActivityJoinEntity;
import com.platform.service.ActivityJoinService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@RestController
@RequestMapping("activityjoin")
public class ActivityJoinController {
    @Autowired
    private ActivityJoinService activityJoinService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activityjoin:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ActivityJoinEntity> activityJoinList = activityJoinService.queryList(query);
        int total = activityJoinService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityJoinList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activityjoin:info")
    public R info(@PathVariable("id") Integer id) {
        ActivityJoinEntity activityJoin = activityJoinService.queryObject(id);

        return R.ok().put("activityJoin", activityJoin);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activityjoin:save")
    public R save(@RequestBody ActivityJoinEntity activityJoin) {
        activityJoinService.save(activityJoin);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activityjoin:update")
    public R update(@RequestBody ActivityJoinEntity activityJoin) {
        activityJoinService.update(activityJoin);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activityjoin:delete")
    public R delete(@RequestBody Integer[] ids) {
        activityJoinService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ActivityJoinEntity> list = activityJoinService.queryList(params);

        return R.ok().put("list", list);
    }
}
