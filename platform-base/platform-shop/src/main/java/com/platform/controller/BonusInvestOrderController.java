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

import com.platform.api.entity.BonusInvestOrderVo;
import com.platform.api.service.ApiBonusInvestOrderService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-09 21:47:32
 */
@RestController
@RequestMapping("bonusinvestorder")
public class BonusInvestOrderController {
    @Autowired
    private ApiBonusInvestOrderService apiBonusInvestOrderService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("bonusinvestorder:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<BonusInvestOrderVo> bonusInvestOrderList = apiBonusInvestOrderService.queryList(query);
        int total = apiBonusInvestOrderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(bonusInvestOrderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("bonusinvestorder:info")
    public R info(@PathVariable("id") Integer id) {
        BonusInvestOrderVo bonusInvestOrder = apiBonusInvestOrderService.queryObject(id);

        return R.ok().put("bonusInvestOrder", bonusInvestOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("bonusinvestorder:save")
    public R save(@RequestBody BonusInvestOrderVo bonusInvestOrder) {
    	apiBonusInvestOrderService.save(bonusInvestOrder);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("bonusinvestorder:update")
    public R update(@RequestBody BonusInvestOrderVo bonusInvestOrder) {
    	apiBonusInvestOrderService.update(bonusInvestOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("bonusinvestorder:delete")
    public R delete(@RequestBody Integer[] ids) {
    	apiBonusInvestOrderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<BonusInvestOrderVo> list = apiBonusInvestOrderService.queryList(params);

        return R.ok().put("list", list);
    }
}
