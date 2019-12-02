package com.platform.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.cache.CacheUtil;
import com.platform.entity.AdVo;
import com.platform.entity.GoodsCategoryVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.SysMacroEntity;
import com.platform.entity.UserVo;
import com.platform.service.ApiAdService;
import com.platform.service.ApiGoodsCartService;
import com.platform.service.ApiGoodsCategoryService;
import com.platform.service.ApiGoodsService;
import com.platform.service.SysConfigService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.JsonUtil;
import com.platform.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "首页接口文档")
@RestController
@RequestMapping("/api/index")
public class ApiIndexController extends ApiBaseAction {
    @Autowired
    private ApiGoodsCategoryService categoryService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiGoodsCartService cartService;
    @Autowired
    private ApiAdService adService;
    @Autowired
    private SysConfigService sysConfigService;
    /*
    @Autowired
    private ApiChannelService channelService;
    @Autowired
    private ApiBrandService brandService;
    @Autowired
    private ApiTopicService topicService;
    */
    
    
    
    /**
     * 测试
     */
    @IgnoreAuth
    @PostMapping(value = "test")
    public Object test() {
        return toResponsMsgSuccess("请求成功yyy");
    }

    /**
     * app首页
     */
    @ApiOperation(value = "首页")
    @IgnoreAuth
    @PostMapping(value = "index")
    public Object index(@LoginUser UserVo loginUser) {
    	
    	String userLevelCode = "";
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        resultObj.put("banner", banner);
        
        
        Map<String, Object> param2 = new HashMap<String, Object>();
        param2.put("ad_position_id", 2);
        List<AdVo> recommend = adService.queryList(param2);
        resultObj.put("recommend", recommend);
        
        Map<String, Object> param3 = new HashMap<String, Object>();
        param3.put("hot_sale", "1");
        param3.put("fields", "a.shop_id, a.category_id, a.category_name,a.id, a.name, a.goods_sn,a.img_list_url, a.market_price, a.retail_price, a.introduce");
        PageHelper.startPage(0, 3, false);
        List<GoodsVo> hotGoods = goodsService.queryHotGoodsList(param3);
        resultObj.put("hotGoodsList", hotGoods);
        return toResponsSuccess(resultObj);
    }


    @ApiOperation(value = "新商品信息")
    @IgnoreAuth
    @PostMapping(value = "newGoods")
    public Object newGoods() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("fields", "a.shop_id, a.category_id, a.category_name,a.id, a.name, a.goods_sn,a.img_list_url, a.market_price, a.retail_price, a.introduce");
        PageHelper.startPage(0, 4, false);
        List<GoodsVo> newGoods = goodsService.queryList(param);
        resultObj.put("newGoodsList", newGoods);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "新热门商品信息")
    @IgnoreAuth
    @PostMapping(value = "hotGoods")
    public Object hotGoods() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("is_hot", "1");
        param.put("is_delete", 0);
        param.put("fields", "a.shop_id, a.category_id, a.category_name,a.id, a.name, a.goods_sn,a.img_list_url, a.market_price, a.retail_price, a.introduce");
        PageHelper.startPage(0, 3, false);
        List<GoodsVo> hotGoods = goodsService.queryHotGoodsList(param);
        resultObj.put("hotGoodsList", hotGoods);
        //

        return toResponsSuccess(resultObj);
    }

//    @ApiOperation(value = "topic")
//    @IgnoreAuth
//    @PostMapping(value = "topic")
//    public Object topic() {
//        Map<String, Object> resultObj = new HashMap<String, Object>();
//        //
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("offset", 0);
//        param.put("limit", 3);
//        List<TopicVo> topicList = topicService.queryList(param);
//        resultObj.put("topicList", topicList);
//        //
//
//        return toResponsSuccess(resultObj);
//    }

//    @ApiOperation(value = "brand")
//    @IgnoreAuth
//    @PostMapping(value = "brand")
//    public Object brand() {
//        Map<String, Object> resultObj = new HashMap<String, Object>();
//        //
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("is_new", 1);
//        param.put("sidx", "new_sort_order ");
//        param.put("order", "asc ");
//        param.put("offset", 0);
//        param.put("limit", 4);
//        List<BrandVo> brandList = brandService.queryList(param);
//        resultObj.put("brandList", brandList);
//        //
//
//        return toResponsSuccess(resultObj);
//    }

    @ApiOperation(value = "商品类别-商品列表查询")
    @IgnoreAuth
    @PostMapping(value = "category")
    public Object category(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
		String category_id = jsonParams.getString("category_id");
		Assert.isNull(category_id, "商品类别编码不能为空");
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param = new HashMap<String, Object>();
        param.put("parent_id", 0);
       
//        param.put("notName", "推荐");//<>
        List<GoodsCategoryVo> categoryList = categoryService.queryList(param);
        List<Map<String, Object>> newCategoryList = new ArrayList<>();

        for (GoodsCategoryVo categoryItem : categoryList) {
            param.remove("fields");
//            param.put("parent_id", categoryItem.getId());
//            List<GoodsCategoryVo> categoryEntityList = categoryService.queryList(param);
//            List<String> childCategoryCodes = null;
//            if (categoryEntityList != null && categoryEntityList.size() > 0) {
//            	childCategoryCodes = new ArrayList<>();
//                for (GoodsCategoryVo categoryEntity : categoryEntityList) {
//                	childCategoryCodes.add(categoryEntity.getCategoryCode());
//                }
//            }
            //
            param = new HashMap<String, Object>();
            param.put("category_id", category_id);
            logger.info("------category----childCategoryCodes---------:" + JsonUtil.getJsonByObj(categoryItem.getCode()));
            param.put("category_id", categoryItem.getId());
             PageHelper.startPage(0, 7, false);
            List<GoodsVo> categoryGoods = goodsService.queryList(param);
            Map<String, Object> newCategory = new HashMap<String, Object>();
            newCategory.put("id", categoryItem.getId());
            newCategory.put("categoryName", categoryItem.getName());
            newCategory.put("goodsList", categoryGoods);
            newCategoryList.add(newCategory);
        }
        resultObj.put("categoryList", newCategoryList);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "banner")
    @IgnoreAuth
    @PostMapping(value = "banner")
    public Object banner() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        resultObj.put("banner", banner);
        //

        return toResponsSuccess(resultObj);
    }

    /*
    @ApiOperation(value = "channel")
    @IgnoreAuth
    @PostMapping(value = "channel")
    public Object channel() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param = new HashMap<String, Object>();
        param.put("sidx", "sort_order ");
        param.put("order", "asc ");
        List<ChannelVo> channel = channelService.queryList(param);
        resultObj.put("channel", channel);
        //
        return toResponsSuccess(resultObj);
    }
    */
    
    
    @ApiOperation(value = "getMacroByCode")
    @IgnoreAuth
    @PostMapping(value = "getMacroByCode")
    public Object getMacroByCode() {
    	JSONObject jsonParams = getJsonRequest();
		String preCode = jsonParams.getString("preCode");
		String code = jsonParams.getString("codeName");
        Map<String, Object> resultObj = new HashMap<String, Object>();
        String codeValue=CacheUtil.getNameByCode(preCode, code);
        resultObj.put("codeValue", codeValue);
        return toResponsSuccess(resultObj);
    }
    
    @ApiOperation(value = "getMacroListByCode")
    @IgnoreAuth
    @PostMapping(value = "getMacroListByCode")
    public Object getMacroListByCode() {
    	JSONObject jsonParams = getJsonRequest();
		String preCode = jsonParams.getString("preCode");
        Map<String, Object> resultObj = new HashMap<String, Object>();
        List<SysMacroEntity>  codeValues=CacheUtil.getListByPreCode(preCode);
        resultObj.put("codeValues", codeValues);
        return toResponsSuccess(resultObj);
    }
    @ApiOperation(value = "getSysConfigByCode")
    @IgnoreAuth
    @PostMapping(value = "getSysConfigByCode")
    public Object getSysConfigByCode() {
    	JSONObject jsonParams = getJsonRequest();
    	String code = jsonParams.getString("code");
    	String codeValue=sysConfigService.getValue(code, "");
    	Map<String, Object> resultObj = new HashMap<String, Object>();
    	resultObj.put("codeValue", codeValue);
    	return toResponsSuccess(resultObj);
    }
    
   
  
   
}