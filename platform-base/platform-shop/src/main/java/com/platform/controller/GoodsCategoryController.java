package com.platform.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.GoodsCategoryEntity;
import com.platform.service.GoodsCategoryService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.StringUtils;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:08
 */
@RestController
@RequestMapping("goodscategory")
public class GoodsCategoryController {
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodscategory:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryService.queryList(query);
        int total = goodsCategoryService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsCategoryList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 选择商品分类(添加、修改商品)
     *
     * @return R
     */
    @RequestMapping("/select")
    @RequiresPermissions("goodscategory:list")
    public R select(@RequestParam String type) {
        Map<String, Object> map = new HashMap<>();
        List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryService.queryList(map);
        if(!"goods".equals(type)) {
        	//添加一级类别
        	GoodsCategoryEntity root = new GoodsCategoryEntity();
        	root.setId(0);
        	root.setName("一级类别");
        	root.setParentId(-1);
        	root.setOpen(true);
        	goodsCategoryList.add(root);
        }

        return R.ok().put("goodsCategoryList", goodsCategoryList);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodscategory:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsCategoryEntity goodsCategory = goodsCategoryService.queryObject(id);

        return R.ok().put("goodsCategory", goodsCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodscategory:save")
    public R save(@RequestBody GoodsCategoryEntity goodsCategory) {
    	if(StringUtils.isNotEmpty(goodsCategory.getCode())) {
    		Map<String, Object> params=new HashMap<String,Object>();
    		params.put("code", goodsCategory.getCode());
    		List<GoodsCategoryEntity> list = goodsCategoryService.queryList(params);
    		if(list!=null&&list.size()>0) {
    			return R.error("类别编码已存在，请换个编码重新提交！");
    		}
    	}
    	goodsCategory.setCreateTime(new Date());
        goodsCategoryService.save(goodsCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodscategory:update")
    public R update(@RequestBody GoodsCategoryEntity goodsCategory) {
    	if(StringUtils.isNotEmpty(goodsCategory.getCode())) {
    		Map<String, Object> params=new HashMap<String,Object>();
    		params.put("code", goodsCategory.getCode());
    		List<GoodsCategoryEntity> list = goodsCategoryService.queryList(params);
    		if(list!=null&&list.size()>0&&goodsCategory.getId().intValue()!=list.get(0).getId().intValue()) {
    			return R.error("类别编码已存在，请换个编码重新提交！");
    		}
    	}
    	goodsCategory.setUpdateTime(new Date());
        goodsCategoryService.update(goodsCategory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodscategory:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsCategoryService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsCategoryEntity> list = goodsCategoryService.queryList(params);

        return R.ok().put("list", list);
    }
}
