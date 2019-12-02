package com.platform.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.entity.AddressVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiAddressService;
import com.platform.util.ApiBaseAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author softopensell <br>
 * 时间: 2019-03-28 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "收货地址")
@RestController
@RequestMapping("/api/address")
public class ApiAddressController extends ApiBaseAction {
    @Autowired
    private ApiAddressService addressService;

    /**
     * 获取用户的收货地址
     */
    @ApiOperation(value = "获取用户的收货地址接口", response = Map.class)
    @PostMapping("list")
    public Object list(@LoginUser UserVo loginUser) {
        List<AddressVo> addressEntities = getAddressList(loginUser);
        return toResponsSuccess(addressEntities);
    }

	private List<AddressVo> getAddressList(UserVo loginUser) {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id", loginUser.getUserId());
        List<AddressVo> addressEntities = addressService.queryList(param);
		return addressEntities;
	}
    
    /**
     * 获取用户的默认收货地址
     */
    @ApiOperation(value = "获取用户的默认收货地址接口", response = Map.class)
    @PostMapping("getSelectedAddress")
    public Object getSelectedAddress(@LoginUser UserVo loginUser) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id", loginUser.getUserId());
        param.put("is_default", 1);
        List<AddressVo> addressEntities = addressService.queryList(param);
        return toResponsSuccess(addressEntities);
    }

    /**
     * 获取收货地址的详情
     */
    @ApiOperation(value = "获取收货地址的详情", response = Map.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "收获地址ID", required = true, dataType = "Integer")})
    @PostMapping("detail")
    public Object detail(@LoginUser UserVo loginUser) {
    	 JSONObject jsonParam = this.getJsonRequest();
         Integer id = jsonParam.getIntValue("id");
        AddressVo entity = addressService.queryObject(id);
        //判断越权行为
        if (!entity.getUserId().equals(loginUser.getUserId())) {
            return toResponsObject(403, "您无权查看", "");
        }
        return toResponsSuccess(entity);
    }

    /**
     * 添加或更新收货地址
     */
    @ApiOperation(value = "添加或更新收货地址", response = Map.class)
    @PostMapping("save")
    public Object save(@LoginUser UserVo loginUser) {
        JSONObject addressJson = this.getJsonRequest();
        AddressVo entity = new AddressVo();
        if (null != addressJson) {
            entity.setId(addressJson.getInteger("id"));
            //收货人
            entity.setUserId(loginUser.getUserId());
            //收货人
            entity.setContactName(addressJson.getString("contactName")); 
            //联系电话
            entity.setContactMobile(addressJson.getString("contactMobile")); 
            //地区（省）
            entity.setProvinceId(addressJson.getInteger("provinceId"));
            //地区（市）
            entity.setCityId(addressJson.getInteger("cityId"));
            //地区（区县）
            entity.setRegionId(addressJson.getInteger("regionId"));
            entity.setPcrdetail(addressJson.getString("pcrdetail"));
            entity.setZipcode(addressJson.getString("zipcode"));
            //详细地址
            entity.setAddress(addressJson.getString("address"));
            //地址坐标
            entity.setLatitude(addressJson.getString("latitude"));
            entity.setLongitude(addressJson.getString("longitude"));
            //默认标识1、代表默认
            entity.setIsDefault(addressJson.getString("isDefault"));
            //创建时间
            entity.setCreateTime(new Date());
        }
        //如果当前地址为默认地址，需要将原来地址修改为非默认
        if(entity.getIsDefault().equals("1")){
        	Map<String, Object> param = new HashMap<String, Object>();
        	param.put("user_id", loginUser.getUserId());
        	param.put("is_default", 1);
        	List<AddressVo> addressEntities = addressService.queryList(param);
        	for(AddressVo addressVo :addressEntities){
        		addressVo.setIsDefault("0");
        		addressService.update(addressVo);
        	}
        }
        
        if (null == entity.getId() || entity.getId() == 0) {
            entity.setId(null);
            addressService.save(entity);
        } else {
            addressService.update(entity);
        }
        return toResponsSuccess(getAddressList(loginUser));
    }

    
    /**
     * 更新默认地址
     */
    @ApiOperation(value = "更新默认地址", response = Map.class)
    @PostMapping("selected")
    public Object selected(@LoginUser UserVo loginUser) {
    	 Map<String, Object> param = new HashMap<String, Object>();
         param.put("user_id", loginUser.getUserId());
         param.put("is_default", 1);
         List<AddressVo> addressEntities = addressService.queryList(param);
         for(AddressVo addressVo :addressEntities){
        	 addressVo.setIsDefault("0");
        	 addressService.update(addressVo);
         }
    	
        JSONObject addressJson = this.getJsonRequest();
        Integer id = addressJson.getInteger("id");
        if (null != addressJson) {
            AddressVo entity = addressService.queryObject(id);
            //默认标识1、代表默认
            entity.setIsDefault("1");
            //创建时间
            entity.setUpdateTime(new Date());
            addressService.update(entity);
        }
        return toResponsSuccess(getAddressList(loginUser));
    }
    
    /**
     * 删除指定的收货地址
     */
    @ApiOperation(value = "删除指定的收货地址", response = Map.class)
    @PostMapping("delete")
    public Object delete(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = this.getJsonRequest();
        Integer id = jsonParam.getIntValue("id");

        AddressVo entity = addressService.queryObject(id);
        //判断越权行为
        if (!entity.getUserId().equals(loginUser.getUserId())) {
            return toResponsObject(403, "您无权删除", "");
        }
        addressService.delete(id);
        return toResponsSuccess(getAddressList(loginUser));
    }
}