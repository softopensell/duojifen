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

import com.platform.entity.GoodsEntity;
import com.platform.service.GoodsService;
import com.platform.util.ShopConstant;
import com.platform.utils.CommonUtil;
import com.platform.utils.DateUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import com.platform.utils.StringUtils;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:07
 */
@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goods:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        if(query.containsKey("queryType")&&"goodscount".equals(query.get("queryType"))&&query.containsKey("countDay")) {
        	if(!StringUtils.isNullOrEmpty(query.get("countDay"))) {
        		int countDay=Integer.parseInt( query.get("countDay")+"");
        		String countDateStart=DateUtil.getDateStrDay(DateUtil.getCurrentDate(), -countDay, 4);
        		query.put("countDateStart", countDateStart);
        	}
        }
        List<GoodsEntity> goodsList = goodsService.queryList(query);
        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goods:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsEntity goods = goodsService.queryObject(id);

        return R.ok().put("goods", goods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goods:save")
    public R save(@RequestBody GoodsEntity goods) {
    	goods.setShopId(ShopConstant.DEFAULT_SHOP_ID);
    	goods.setGoodsSn(CommonUtil.generateGoodsCode());
    	goods.setCreatetime(new Date());
    	goods.setCreateUserId(ShiroUtils.getUserEntity().getUserId().intValue());
        goodsService.save(goods);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goods:update")
    public R update(@RequestBody GoodsEntity goods) {
    	goods.setUpdatetime(new Date());
        goodsService.update(goods);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goods:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsEntity> list = goodsService.queryList(params);

        return R.ok().put("list", list);
    }
    
    /**
     * 上架
     */
    @RequestMapping("/enSale")
    public R enSale(@RequestBody Integer id) {
    	goodsService.enSale(id);

        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/unSale")
    public R unSale(@RequestBody Integer id) {
    	goodsService.unSale(id);

        return R.ok();
    }
}
