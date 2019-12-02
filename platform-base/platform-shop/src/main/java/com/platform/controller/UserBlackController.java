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

import com.platform.cache.UserBlackCacheUtil;
import com.platform.entity.UserBlackEntity;
import com.platform.entity.UserEntity;
import com.platform.service.UserBlackService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 03:14:08
 */
@RestController
@RequestMapping("userblack")
public class UserBlackController {
    @Autowired
    private UserBlackService userBlackService;
    @Autowired
    private UserService userService;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("userblack:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<UserBlackEntity> userBlackList = userBlackService.queryList(query);
        int total = userBlackService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userBlackList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("userblack:info")
    public R info(@PathVariable("id") Integer id) {
        UserBlackEntity userBlack = userBlackService.queryObject(id);

        return R.ok().put("userBlack", userBlack);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("userblack:save")
    public R save(@RequestBody UserBlackEntity userBlack) {
    	UserEntity userEntity=userService.queryObject(userBlack.getUserId());
    	if(userEntity!=null) {
    		userBlack.setUserId(userEntity.getUserId());
    		userBlack.setUserName(userEntity.getUserName());
    		userBlack.setBlackType(0);
        	userBlack.setCreateTime(new Date());
        	userBlack.setUpdateTime(new Date());
        	userBlack.setStatu(0);
            userBlackService.save(userBlack);
            
            userEntity.setState(ShopConstant.SHOP_USER_STATU_OFFLINE);
    		userService.update(userEntity);
            
            UserBlackCacheUtil.init();
    	}
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("userblack:update")
    public R update(@RequestBody UserBlackEntity userBlack) {
        UserEntity userEntity=userService.queryObject(userBlack.getUserId());
    	if(userEntity!=null) {
    		userBlack.setUserId(userEntity.getUserId());
    		userBlack.setUserName(userEntity.getUserName());
            userBlackService.update(userBlack);
            userEntity.setState(ShopConstant.SHOP_USER_STATU_OFFLINE);
    		userService.update(userEntity);
            
            UserBlackCacheUtil.init();
            
            
    	}
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("userblack:delete")
    public R delete(@RequestBody Integer[] ids) {
        userBlackService.deleteBatch(ids);
        UserBlackCacheUtil.init();
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<UserBlackEntity> list = userBlackService.queryList(params);

        return R.ok().put("list", list);
    }
}
