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

import com.platform.cache.IdWorkCache;
import com.platform.constants.ActivityConstant;
import com.platform.entity.ActivityItemEntity;
import com.platform.entity.ActivityTickeorderEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.ActivityItemService;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.RandomUtil;
/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@RestController
@RequestMapping("activityitem")
public class ActivityItemController extends AbstractController{
    @Autowired
    private ActivityItemService activityItemService;
    @Autowired
    private IdWorkCache idWorkCache;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activityitem:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ActivityItemEntity> activityItemList = activityItemService.queryList(query);
        int total = activityItemService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityItemList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activityitem:info")
    public R info(@PathVariable("id") Integer id) {
        ActivityItemEntity activityItem = activityItemService.queryObject(id);

        return R.ok().put("activityItem", activityItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activityitem:save")
    public R save(@RequestBody ActivityItemEntity activityItem) {
    	SysUserEntity sysUserEntity=this.getUser();
    	activityItem.setItemNo(idWorkCache.getIdDayHHMMEndRadom("AI", IdWorkCache.ID_WORK_TYPE_ACTIVITY_ITEM_SN, 2));
		activityItem.setVirtualProductSum(0);
		activityItem.setVirtualApplyPeopleSum(0);
		activityItem.setUpdateTime(new Date());
		activityItem.setStatu(0);
		activityItem.setMemberId(sysUserEntity.getUserId().intValue());
		activityItem.setCompanySn("");
		activityItem.setCreateTime(new Date());
		activityItem.setClickSum(0);
		activityItem.setApplyPeopleSum(0);
		activityItem.setApplyStatu(0);
		activityItem.setPraiseSum(0);
		activityItem.setItemAuditStatu(0);
    	activityItemService.save(activityItem);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activityitem:update")
    public R update(@RequestBody ActivityItemEntity activityItem) {
    	activityItem.setUpdateTime(new Date());
    	activityItemService.update(activityItem);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activityitem:delete")
    public R delete(@RequestBody Integer[] ids) {
        activityItemService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ActivityItemEntity> list = activityItemService.queryList(params);

        return R.ok().put("list", list);
    }
}
