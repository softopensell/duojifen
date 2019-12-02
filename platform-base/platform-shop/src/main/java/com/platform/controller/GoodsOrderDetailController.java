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

import com.platform.entity.GoodsOrderDetailEntity;
import com.platform.service.GoodsOrderDetailService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 15:10:15
 */
@RestController
@RequestMapping("goodsorderdetail")
public class GoodsOrderDetailController {
    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodsorderdetail:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<GoodsOrderDetailEntity> goodsOrderDetailList = goodsOrderDetailService.queryList(query);
        int total = goodsOrderDetailService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsOrderDetailList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodsorderdetail:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsOrderDetailEntity goodsOrderDetail = goodsOrderDetailService.queryObject(id);

        return R.ok().put("goodsOrderDetail", goodsOrderDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodsorderdetail:save")
    public R save(@RequestBody GoodsOrderDetailEntity goodsOrderDetail) {
        goodsOrderDetailService.save(goodsOrderDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodsorderdetail:update")
    public R update(@RequestBody GoodsOrderDetailEntity goodsOrderDetail) {
        goodsOrderDetailService.update(goodsOrderDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodsorderdetail:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsOrderDetailService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsOrderDetailEntity> list = goodsOrderDetailService.queryList(params);

        return R.ok().put("list", list);
    }
}
