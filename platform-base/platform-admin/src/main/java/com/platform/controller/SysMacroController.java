package com.platform.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.SysLog;
import com.platform.cache.CacheUtil;
import com.platform.entity.SysMacroEntity;
import com.platform.service.SysMacroService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * 通用字典表Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-08-22 11:48:16
 */
@RestController
@RequestMapping("sys/macro")
public class SysMacroController {
    @Autowired
    private SysMacroService sysMacroService;
    /**
     * 所有字典列表
     *
     * @param params 请求参数
     * @return R
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:macro:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<SysMacroEntity> sysMacroList = sysMacroService.queryList(query);
        int total = sysMacroService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysMacroList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键获取字典信息
     *
     * @param macroId 主键
     * @return R
     */
    @RequestMapping("/info/{macroId}")
    @RequiresPermissions("sys:macro:info")
    public R info(@PathVariable("macroId") Long macroId) {
        SysMacroEntity sysMacro = sysMacroService.queryObject(macroId);

        return R.ok().put("macro", sysMacro);
    }

    /**
     * 新增字典
     *
     * @param sysMacro 字典
     * @return R
     */
    @SysLog("新增字典")
    @RequestMapping("/save")
    @RequiresPermissions("sys:macro:save")
    public R save(@RequestBody SysMacroEntity sysMacro) {
        sysMacroService.save(sysMacro);

        return R.ok();
    }

    /**
     * 修改字典
     *
     * @param sysMacro 字典
     * @return R
     */
    @SysLog("修改字典")
    @RequestMapping("/update")
    @RequiresPermissions("sys:macro:update")
    public R update(@RequestBody SysMacroEntity sysMacro) {
        sysMacroService.update(sysMacro);

        return R.ok();
    }

    /**
     * 删除字典
     *
     * @param macroIds 主键集
     * @return R
     */
    @SysLog("删除字典")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:macro:delete")
    public R delete(@RequestBody Long[] macroIds) {
        sysMacroService.deleteBatch(macroIds);

        return R.ok();
    }

    /**
     * 查看字典列表
     *
     * @param params 请求参数
     * @return R
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<SysMacroEntity> list = sysMacroService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 根据value查询数据字典
     *
     * @param value value
     * @return R
     */
    @RequestMapping("/queryMacrosByValue")
    public R queryMacrosByValue(@RequestParam String value) {
    	 List<SysMacroEntity> list = CacheUtil.getListByPreCode(value);
    	 if(list==null||list.size()==0) {
    		 list = sysMacroService.queryMacrosByValue(value);
    	 }else {
    		 Collections.sort(list,new Comparator<SysMacroEntity>() {
    			    //升序排序
    				public int compare(SysMacroEntity o1, SysMacroEntity o2) {
    					if(o1.getOrderNum()==null) {
    						o1.setOrderNum(1);
    					}
    					if(o2.getOrderNum()==null) {
    						o2.setOrderNum(1);
    					}
    					BigDecimal b1=new BigDecimal(o1.getOrderNum());
    					BigDecimal b2=new BigDecimal(o2.getOrderNum());
    					return b1.compareTo(b2);//倒序
    				}

    			});
    	 }

        return R.ok().put("list", list);
    }
}
