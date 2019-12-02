package com.platform.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.AdVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiAdService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ApiController
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-22 18:15:00
 */
@Api(tags = "Ad") 
@RestController
@RequestMapping("/api/ad")
public class ApiAdController extends ApiBaseAction {
    @Autowired
    private ApiAdService apiAdService;

    /**
     * 查看列表
     */
    @ApiOperation(value = "Ad_查看列表")
    @PostMapping("/list")
    public Object list(@LoginUser UserVo loginUser) {
                  
         JSONObject jsonParam = getJsonRequest();
        Integer page = 1;
        Integer size = 10;
         page = jsonParam.getInteger("page");
         size = jsonParam.getInteger("size");         
                  
    	 Map params = new HashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "asc");
        //查询列表数据
        Query query = new Query(params);

        List<AdVo> adList = apiAdService.queryList(query);
        int total = apiAdService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(adList, total, query.getLimit(), query.getPage());

        return toResponsSuccess(pageUtil);
    }


    @ApiOperation(value = "banner_可用")
    @IgnoreAuth
    @PostMapping(value = "banner")
    public Object banner() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = apiAdService.queryList(param);
        resultObj.put("banner", banner);
        //

        return toResponsSuccess(resultObj);
    }
    
    /**
     * 查看信息
     */
    @ApiOperation(value = "Ad_查看信息")
    @RequestMapping("/info")
    public Object info(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Integer id = jsonParam.getInteger("id");
     	Map resultObj = new HashMap();
        
        AdVo ad = apiAdService.queryObject(id);
		if (null == ad) {
            return toResponsObject(400, "数据不存在", "");
        }
        
		resultObj.put("ad", ad);
		
		return toResponsSuccess(resultObj);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存_Ad")
    @RequestMapping("/save")
    public Object save(@LoginUser UserVo loginUser) {

		 JSONObject jsonParam = getJsonRequest();
    	 Integer id = jsonParam.getInteger("id");
    	 AdVo ad= new AdVo();
    	 ad.setId(id);
        int num = apiAdService.save(ad);
    	 if (num > 0) {
             return toResponsMsgSuccess("保存成功");
         } else {
             return toResponsFail("保存失败");
         }

    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改_Ad")
    @RequestMapping("/update")
    public Object update(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
    	Integer id = jsonParam.getInteger("id");
    	AdVo ad  = apiAdService.queryObject(id);
        int num = apiAdService.update(ad);

        if (num > 0) {
            return toResponsMsgSuccess("修改成功");
        } else {
            return toResponsFail("修改失败");
        }
         
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除_Ad") 
    @RequestMapping("/delete")
    public Object delete(@RequestBody Integer[] ids) {
       int num =  apiAdService.deleteBatch(ids);
        if (num > 0) {
            return toResponsMsgSuccess("删除成功");
        } else {
            return toResponsFail("删除失败");
        }
    }

    /**
     * 查看所有列表
     */
     @ApiOperation(value = "查看所有列表_Ad")
    @RequestMapping("/queryAll")
    public Object queryAll(@RequestParam Map<String, Object> params) {
		Map resultObj = new HashMap();
        List<AdVo> list = apiAdService.queryList(params);
		resultObj.put("list", list);
        return toResponsSuccess(resultObj);
    }
}
