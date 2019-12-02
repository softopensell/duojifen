package com.platform.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.ActivityItemEntity;
import com.platform.entity.UserVo;
import com.platform.service.ActivityItemService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ApiController
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@Api(tags = "ActivityItem") 
@RestController
@RequestMapping("/api/activityitem")
public class ApiActivityItemController extends ApiBaseAction {
    @Autowired
    private ActivityItemService activityItemService;

    /**
     * 查看列表
     */
    @ApiOperation(value = "ActivityItem_查看列表")
    @PostMapping("/list")
    public Object list(@LoginUser UserVo loginUser) {
                  
         JSONObject jsonParam = getJsonRequest();
        Integer page = 1;
        Integer size = 10;
         page = jsonParam.getInteger("page");
         size = jsonParam.getInteger("size");         
                  
    	Map params = new HashMap();
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "asc");
        //查询列表数据
        Query query = new Query(params);

        List<ActivityItemEntity> activityItemList = activityItemService.queryList(query);
        int total = activityItemService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityItemList, total, query.getLimit(), query.getPage());

        return toResponsSuccess(pageUtil);
    }

    /**
     * 查看信息
     */
    @ApiOperation(value = "ActivityItem_查看信息")
    @RequestMapping("/info")
    public Object info(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Integer id = jsonParam.getInteger("id");
     	Map resultObj = new HashMap();
        
        ActivityItemEntity activityItem = activityItemService.queryObject(id);
		if (null == activityItem) {
            return toResponsObject(400, "数据不存在", "");
        }
        
		resultObj.put("activityItem", activityItem);
		
		return toResponsSuccess(resultObj);
    }
    @IgnoreAuth
    @ApiOperation(value = "活动详情页数据")
    @PostMapping(value = "detail")
    public Object detail(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<>();
        JSONObject jsonParams = getJsonRequest();
        String itemNo = jsonParams.getString("itemNo");
        Assert.isNull(itemNo, "活动编号itemNo不能为空");
        ActivityItemEntity activityItemEntity=activityItemService.queryObjectByItemNo(itemNo);
        resultObj.put("activityItemEntity", activityItemEntity);
        return toResponsSuccess(resultObj);
    }
   
}
