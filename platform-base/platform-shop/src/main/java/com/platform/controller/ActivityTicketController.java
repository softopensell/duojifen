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

import com.platform.entity.ActivityTicketEntity;
import com.platform.service.ActivityTicketService;
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
@RequestMapping("activityticket")
public class ActivityTicketController {
    @Autowired
    private ActivityTicketService activityTicketService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activityticket:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ActivityTicketEntity> activityTicketList = activityTicketService.queryList(query);
        int total = activityTicketService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityTicketList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activityticket:info")
    public R info(@PathVariable("id") Integer id) {
        ActivityTicketEntity activityTicket = activityTicketService.queryObject(id);

        return R.ok().put("activityTicket", activityTicket);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activityticket:save")
    public R save(@RequestBody ActivityTicketEntity activityTicket) {
        activityTicketService.save(activityTicket);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activityticket:update")
    public R update(@RequestBody ActivityTicketEntity activityTicket) {
        activityTicketService.update(activityTicket);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activityticket:delete")
    public R delete(@RequestBody Integer[] ids) {
        activityTicketService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ActivityTicketEntity> list = activityTicketService.queryList(params);

        return R.ok().put("list", list);
    }
}
