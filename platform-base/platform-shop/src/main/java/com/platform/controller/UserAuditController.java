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

import com.platform.entity.UserAuditEntity;
import com.platform.service.UserAuditService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-30 15:38:31
 */
@RestController
@RequestMapping("useraudit")
public class UserAuditController {
    @Autowired
    private UserAuditService userAuditService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("useraudit:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<UserAuditEntity> userAuditList = userAuditService.queryList(query);
        int total = userAuditService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userAuditList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("useraudit:info")
    public R info(@PathVariable("id") Integer id) {
        UserAuditEntity userAudit = userAuditService.queryObject(id);

        return R.ok().put("userAudit", userAudit);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("useraudit:save")
    public R save(@RequestBody UserAuditEntity userAudit) {
        userAuditService.save(userAudit);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("useraudit:update")
    public R update(@RequestBody UserAuditEntity userAudit) {
        userAuditService.update(userAudit);

        return R.ok();
    }
    /**
     * 升级审核
     */
    @RequestMapping("/saveAudit")
    @RequiresPermissions("useraudit:update")
    public R saveAudit(@RequestBody UserAuditEntity userAudit) {
    	userAuditService.saveAudit(userAudit);
    	return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("useraudit:delete")
    public R delete(@RequestBody Integer[] ids) {
        userAuditService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<UserAuditEntity> list = userAuditService.queryList(params);

        return R.ok().put("list", list);
    }
}
