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

import com.platform.entity.ShopEntity;
import com.platform.service.ShopService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell_javadream@163.com
 * @date 2019-06-28 23:37:18
 */
@RestController
@RequestMapping("shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("shop:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ShopEntity> shopList = shopService.queryList(query);
        int total = shopService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(shopList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("shop:info")
    public R info(@PathVariable("id") Integer id) {
        ShopEntity shop = shopService.queryObject(id);

        return R.ok().put("shop", shop);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("shop:save")
    public R save(@RequestBody ShopEntity shop) {
    	shop.setCreateTime(new Date());
        shopService.save(shop);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("shop:update")
    public R update(@RequestBody ShopEntity shop) {
    	shop.setUpdateTime(new Date());
        shopService.update(shop);

        return R.ok();
    }
    
    /**
     * 店铺审核
     */
    @RequestMapping("/saveAudit")
    @RequiresPermissions("useraudit:update")
    public R saveAudit(@RequestBody ShopEntity shop) {
    	shopService.saveAudit(shop);
    	return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("shop:delete")
    public R delete(@RequestBody Integer[] ids) {
        shopService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ShopEntity> list = shopService.queryList(params);

        return R.ok().put("list", list);
    }
}
