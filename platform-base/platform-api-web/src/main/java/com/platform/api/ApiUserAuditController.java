package com.platform.api;

import java.util.Date;
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
import com.platform.annotation.LoginUser;
import com.platform.entity.UserAuditVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserAuditService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.validator.ApiAssert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ApiController
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-31 12:02:47
 */
@Api(tags = "UserAudit") 
@RestController
@RequestMapping("/api/useraudit")
public class ApiUserAuditController extends ApiBaseAction {
    @Autowired
    private ApiUserAuditService apiUserAuditService;

    /**
     * 我的申请记录
     */
    @ApiOperation(value = "UserAudit_查看列表")
    @PostMapping("/applylist")
    public Object applylist(@LoginUser UserVo loginUser) {
                  
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

        List<UserAuditVo> userAuditList = apiUserAuditService.queryList(query);
        int total = apiUserAuditService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userAuditList, total, query.getLimit(), query.getPage());

        return toResponsSuccess(pageUtil);
    }

    /**
     * 查看信息
     */
    @ApiOperation(value = "UserAudit_查看信息")
    @RequestMapping("/info")
    public Object info(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Integer id = jsonParam.getInteger("id");
     	Map resultObj = new HashMap();
        
        UserAuditVo userAudit = apiUserAuditService.queryObject(id);
		if (null == userAudit) {
            return toResponsObject(400, "数据不存在", "");
        }
        
		resultObj.put("userAudit", userAudit);
		
		return toResponsSuccess(resultObj);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存_UserAudit")
    @RequestMapping("/submit")
    public Object submit(@LoginUser UserVo loginUser) {

		 JSONObject jsonParam = getJsonRequest();
    	 Integer nowLevel = jsonParam.getInteger("nowLevel");
    	 Integer applyLevel = jsonParam.getInteger("applyLevel");
    	 ApiAssert.isNull(applyLevel, "申请等级不能为空");
    	 
    	 
    	 if(applyLevel<=nowLevel){
    		 return toResponsFail("申请等级【"+applyLevel+"】不能小于当前等级【"+nowLevel+"】");
    	 }
    	 
    	 UserAuditVo userAudit= new UserAuditVo();
    	 if(nowLevel!=null){
    		 userAudit.setNowLevel(nowLevel); //1分销商2一级分公司3二级分公司
    	 }else{
    		 userAudit.setNowLevel(applyLevel); 
    	 }
    	 userAudit.setApplyLevel(applyLevel);//1分销商2一级分公司3二级分公司
    	 userAudit.setApplyUserId(loginUser.getUserId());//申请人ID
    	 userAudit.setApplyType(1);//申请类型：默认1申请代理2、
    	 userAudit.setApplyTime(new Date());  //申请时间
    	 userAudit.setAuditStatus(1); //1待审核2已通过3已拒绝
    	 
        int num = apiUserAuditService.save(userAudit);
    	 if (num > 0) {
             return toResponsSuccess(userAudit);
         } else {
             return toResponsFail("申请失败");
         }

    }

    /**
     * 取消审核
     */
    @ApiOperation(value = "修改_UserAudit")
    @RequestMapping("/cancle")
    public Object cancle(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
    	Integer id = jsonParam.getInteger("id");
   	 	ApiAssert.isNull(id, "id不能为空");

    	UserAuditVo userAudit  = apiUserAuditService.queryObject(id);
    	if(userAudit==null){
    		return toResponsFail("该条申请记录不存在");
    	}
    	userAudit.setAuditStatus(4);//取消
        int num = apiUserAuditService.update(userAudit);

        if (num > 0) {
            return toResponsMsgSuccess("修改成功");
        } else {
            return toResponsFail("修改失败");
        }
         
    }
    
    
    

    /**
     * 删除
     */
    @ApiOperation(value = "删除_UserAudit") 
    @RequestMapping("/delete")
    public Object delete(@RequestBody Integer[] ids) {
       int num =  apiUserAuditService.deleteBatch(ids);

        if (num > 0) {
            return toResponsMsgSuccess("删除成功");
        } else {
            return toResponsFail("删除失败");
        }
    }

    /**
     * 查看所有列表
     */
     @ApiOperation(value = "查看所有列表_UserAudit")
    @RequestMapping("/queryAll")
    public Object queryAll(@RequestParam Map<String, Object> params) {
		Map resultObj = new HashMap();
        List<UserAuditVo> list = apiUserAuditService.queryList(params);
		resultObj.put("list", list);
        return toResponsSuccess(resultObj);
    }
}
