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

import com.platform.entity.UserInvestLevelEntity;
import com.platform.service.UserInvestLevelService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-15 02:21:39
 */
@RestController
@RequestMapping("userinvestlevel")
public class UserInvestLevelController extends AbstractController{
    @Autowired
    private UserInvestLevelService userInvestLevelService;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("userinvestlevel:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<UserInvestLevelEntity> userInvestLevelList = userInvestLevelService.queryList(query);
        int total = userInvestLevelService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userInvestLevelList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("userinvestlevel:info")
    public R info(@PathVariable("id") Integer id) {
        UserInvestLevelEntity userInvestLevel = userInvestLevelService.queryObject(id);

        return R.ok().put("userInvestLevel", userInvestLevel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("userinvestlevel:save")
    public R save(@RequestBody UserInvestLevelEntity userInvestLevel) {
//    	if(userInvestLevel.getGoodsSn()==null)
//    		return R.error("关联商品不能为空");
//    	if(userInvestLevel.getUserLevelMoneyValue()==null)
//    		return R.error("消费额度不能为空");
    	if(userInvestLevel.getUserLevelNodeLevel()==null)
    		return R.error("节点奖励层级不能为空");
    	if(userInvestLevel.getUserLevelType()==null)
    		return R.error("会员级别不能为空");
    	
    	
    	userInvestLevel.setCreateTime(new Date());
    	userInvestLevel.setUpdateTime(new Date());
    	userInvestLevel.setStatu(0);
        int id=userInvestLevelService.save(userInvestLevel);
        logger.info("-----save id-----------id"+id);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("userinvestlevel:update")
    public R update(@RequestBody UserInvestLevelEntity userInvestLevel) {
        userInvestLevelService.update(userInvestLevel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("userinvestlevel:delete")
    public R delete(@RequestBody Integer[] ids) {
        userInvestLevelService.deleteBatch(ids);
        return R.ok();
    }
    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<UserInvestLevelEntity> list = userInvestLevelService.queryList(params);
        return R.ok().put("list", list);
    }
}
