package com.platform.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.GoodsCartVo;
import com.platform.entity.GoodsCategoryVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiGoodsCartService;
import com.platform.service.ApiGoodsCategoryService;
import com.platform.service.ApiGoodsService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.Query;
import com.platform.utils.StringUtils;
import com.platform.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;



/**
 * 作者: @author softopensell <br>
 * 时间: 2019-03-25 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/goods")
public class ApiGoodsController extends ApiBaseAction {
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiGoodsCategoryService categoryService;
    @Autowired
    private ApiGoodsCartService cartService; 

    /**
     */
    @ApiOperation(value = "商品首页")
    @IgnoreAuth
    @PostMapping(value = "index")
    public Object index() {
        Map param = new HashMap();
        param.put("sell_status", 1);
        List<GoodsVo> goodsList = goodsService.queryList(param);
        return toResponsSuccess(goodsList);
    }

    /**
     * 商品详情页数据
     */
    @IgnoreAuth
    @ApiOperation(value = " 商品详情页数据")
    @PostMapping(value = "detail")
    public Object detail(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap();
        JSONObject jsonParams = getJsonRequest();

        Integer goods_id = jsonParams.getInteger("goods_id");
        Assert.isNull(goods_id, "商品ID不能为空");
        
        GoodsVo info = goodsService.queryObject(goods_id);

        resultObj.put("info", info);

        return toResponsSuccess(resultObj);
    }

    /**
     * 　获取分类下的商品
     */
   @ApiOperation(value = " 获取分类下的商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类id", paramType = "path", required = true)})
    @IgnoreAuth
    @PostMapping(value = "category")
    public Object category(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        GoodsCategoryVo currentCategory = categoryService.queryObject(id);
        GoodsCategoryVo parentCategory = categoryService.queryObject(currentCategory.getParentId());
        Map params = new HashMap();
        params.put("parent_id", currentCategory.getParentId());
        List<GoodsCategoryVo> brotherCategory = categoryService.queryList(params);
        resultObj.put("currentCategory", currentCategory);
        resultObj.put("parentCategory", parentCategory);
        resultObj.put("brotherCategory", brotherCategory);
        return toResponsSuccess(resultObj);
    } 

    /**
     * 　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "path", required = true),
//            @ApiImplicitParam(name = "brandId", value = "品牌Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "isNew", value = "新商品", paramType = "path", required = true),
            @ApiImplicitParam(name = "isHot", value = "热卖商品", paramType = "path", required = true)})
    @IgnoreAuth
    @PostMapping(value = "list")
    public Object list(@LoginUser UserVo loginUser) {
    	
    	JSONObject jsonParams = getJsonRequest();
    	
    	Map params = new HashMap();
    	
    	Integer size= jsonParams.getInteger("size");
    	if(size!=null) {
    		 params.put("limit", size);
    	}else {
    		params.put("limit", 10);
    	}
    	
    	Integer page= jsonParams.getInteger("page");
    	if(page!=null) {
	   		 params.put("page", page);
	   	}else {
	   		params.put("page", 1);
	   	}
    	params.put("sell_status", 1);
    	
    	Integer categoryId= jsonParams.getInteger("categoryId");
    	if(categoryId!=null&&categoryId>0) {
    		params.put("category_id", categoryId);
    	}
    	
    	Integer isHot= jsonParams.getInteger("isHot");
    	if(isHot!=null) {
    		params.put("hot_sale", isHot);
    	}
    	Integer is_new= jsonParams.getInteger("is_new");
    	if(is_new!=null) {
    		params.put("is_new", is_new);
    	}
    	String search_key= jsonParams.getString("search_key");
    	if(StringUtils.isNotEmpty(search_key)) {
    		params.put("search_key", search_key);
    	}
    	String sort= jsonParams.getString("sort");
    	String order= jsonParams.getString("order");
       
        params.put("order", sort);
        params.put("sidx", order);
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "retail_price");
            params.put("order", order);
        } else {
            params.put("sidx", "id");
            params.put("order", "desc");
        }
        //筛选的分类
        List<GoodsCategoryVo> filterCategory = new ArrayList();
        GoodsCategoryVo rootCategory = new GoodsCategoryVo();
        rootCategory.setId(0);
        rootCategory.setName("全部");
        filterCategory.add(rootCategory);
        params.put("fields", "category_id");
        
        List<GoodsVo> categoryEntityList = goodsService.queryList(params);
        params.remove("fields");
        if (null != categoryEntityList && categoryEntityList.size() > 0) {
            List<Integer> categoryIds = new ArrayList();
            for (GoodsVo goodsVo : categoryEntityList) {
                categoryIds.add(goodsVo.getCategoryId());
            }
            //查找二级分类的parent_id
            Map categoryParam = new HashMap();
            categoryParam.put("ids", categoryIds);
            categoryParam.put("fields", "parent_id");
            List<GoodsCategoryVo> parentCategoryList = categoryService.queryList(categoryParam);
            //
            List<Integer> parentIds = new ArrayList();
            for (GoodsCategoryVo categoryEntity : parentCategoryList) {
                parentIds.add(categoryEntity.getParentId());
            }
            //一级分类
            categoryParam = new HashMap();
            categoryParam.put("fields", "id,name");
            categoryParam.put("order", "asc");
            categoryParam.put("sidx", "sort_order");
            categoryParam.put("ids", parentIds);
            List<GoodsCategoryVo> parentCategory = categoryService.queryList(categoryParam);
            if (null != parentCategory) {
                filterCategory.addAll(parentCategory);
            }
        }
        //加入分类条件
        if (null != categoryId && categoryId > 0) {
            List<Integer> categoryIds = new ArrayList();
            Map categoryParam = new HashMap();
            categoryParam.put("parent_id", categoryId);
            categoryParam.put("fields", "id");
            List<GoodsCategoryVo> childIds = categoryService.queryList(categoryParam);
            for (GoodsCategoryVo categoryEntity : childIds) {
                categoryIds.add(categoryEntity.getId());
            }
            categoryIds.add(categoryId);
            params.put("categoryIds", categoryIds);
        }
        //查询列表数据
        params.put("fields", "id, name, img_list_url, market_price, retail_price, introduce");
        Query query = new Query(params);
        logger.info("-------query-----"+JsonUtil.getJsonByObj(query));
        
        List<GoodsVo> goodsList = goodsService.queryList(query);
        int total = goodsService.queryTotal(query);
        ApiPageUtils goodsData = new ApiPageUtils(goodsList, total, query.getLimit(), query.getPage());
        goodsData.setFilterCategory(filterCategory);
        goodsData.setGoodsList(goodsList);
        return toResponsSuccess(goodsData);
    } 
    /**
     * 
     * 　　在售的商品总数
     */
    @ApiOperation(value = "在售的商品总数")
    @IgnoreAuth
    @PostMapping(value = "count")
    public Object count() {
        Map<String, Object> resultObj = new HashMap<>();
        Map param = new HashMap<>();
        param.put("sell_status", 1);
        Integer goodsCount = goodsService.queryTotal(param);
        resultObj.put("goodsCount", goodsCount);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @IgnoreAuth
    @PostMapping(value = "productlist")
    public Object productlist(Integer categoryId,
                              Integer isNew, Integer discount,
                              @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String sort, String order) {
        Map params = new HashMap<>();
        params.put("is_new", isNew);
        params.put("page", page);
        params.put("limit", size);
        params.put("order", sort);
        params.put("sidx", order);
        //
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "retail_price");
            params.put("order", order);
        } else if (null != sort && sort.equals("sell")) {
            params.put("sidx", "orderNum");
            params.put("order", order);
        } else {
            params.put("sidx", "id");
            params.put("order", "desc");
        }
        // 0不限 1特价 2团购
        if (null != discount && discount == 1) {
            params.put("hot_sale", 1);
        } else if (null != discount && discount == 2) {
            params.put("is_group", true);
        }
        //加入分类条件
        if (null != categoryId && categoryId > 0) {
            List<Integer> categoryIds = new ArrayList();
            Map categoryParam = new HashMap();
            categoryParam.put("parent_id", categoryId);
            categoryParam.put("fields", "id");
            List<GoodsCategoryVo> childIds = categoryService.queryList(categoryParam);
            for (GoodsCategoryVo categoryEntity : childIds) {
                categoryIds.add(categoryEntity.getId());
            }
            categoryIds.add(categoryId);
            params.put("categoryIds", categoryIds);
        }
        //查询列表数据
        Query query = new Query(params);
        List<GoodsVo> goodsList = goodsService.queryCatalogProductList(query);
        int total = goodsService.queryTotal(query);

        // 当前购物车中
        List<GoodsCartVo> cartList = new ArrayList();
        if (null != getUserId()) {
            //查询列表数据
            Map cartParam = new HashMap();
            cartParam.put("user_id", getUserId());
            cartList = cartService.queryList(cartParam);
        }
        ApiPageUtils goodsData = new ApiPageUtils(goodsList, total, query.getLimit(), query.getPage());
        goodsData.setGoodsList(goodsData.getData());
        return toResponsSuccess(goodsData);
    }
}