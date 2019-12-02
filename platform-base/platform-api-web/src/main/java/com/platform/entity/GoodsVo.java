package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 * 表名 t_goods
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:51
 */
public class GoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //店铺ID
    private Integer shopId;
    //分类id
    private Integer categoryId;
    //分类名称
    private String categoryName;
    //商品序列号
    private String goodsSn;
    //商品名称
    private String name;
    //市场价
    private BigDecimal marketPrice;
    //零售价格
    private BigDecimal retailPrice;
    //快递费用
    private BigDecimal expressPrice;
    //商品简介
    private String introduce;
    //商品封面图片
    private String imgUrl;
    //商品列表图片
    private String imgListUrl;
    //用户id
    private Integer createUserId;
    //是否是新品
    private Integer isNew;
    //热门推荐
    private Integer hotSale;
    //点击数
    private Integer hits;
    //商品详情
    private String goodsDetail;
    //多图片展示 ‘，分割’
    private String images;
    //销售数量
    private Integer sellcount;
    //总库存
    private Integer stock;
    //搜索key
    private String searchKey;
    //购买须知
    private String notice;
    //服务保障
    private String promise;
    //评论分
    private Integer score;
    //0表示 正常商品 买卖 1表示团购商品
    private Integer sellType;
    //评论数量
    private Integer commentSum;
    //收藏数量
    private Integer collectSum;
    //商品有效期
    private Date expiryDate;
    //商品类型0;//普通商品1;//特价商品
    private Integer goodsType;
    //0待审核,1审核通过,2审核失败
    private Integer auditStatus;
    //0下架,1上架,2售罄
    private Integer sellStatus;
    //作者
    private String author;
    //材质
    private String material;
    //服务说明
    private String serviceDesc;
    //规格
    private String specification;
    //0 默认 商城商品  1 表示来源画家商城
    private Integer goodsFrom;
    //来源编号
    private String goodsFromNumber;
    //添加时间
    private Date createtime;
    //更新时间
    private Date updatetime;

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：店铺ID
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取：店铺ID
     */
    public Integer getShopId() {
        return shopId;
    }
    /**
     * 设置：分类id
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取：分类id
     */
    public Integer getCategoryId() {
        return categoryId;
    }
    /**
     * 设置：分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取：分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }
    /**
     * 设置：商品序列号
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    /**
     * 获取：商品序列号
     */
    public String getGoodsSn() {
        return goodsSn;
    }
    /**
     * 设置：商品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：商品名称
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：市场价
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * 获取：市场价
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }
    /**
     * 设置：零售价格
     */
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * 获取：零售价格
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }
    /**
     * 设置：快递费用
     */
    public void setExpressPrice(BigDecimal expressPrice) {
        this.expressPrice = expressPrice;
    }

    /**
     * 获取：快递费用
     */
    public BigDecimal getExpressPrice() {
        return expressPrice;
    }
    /**
     * 设置：商品简介
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /**
     * 获取：商品简介
     */
    public String getIntroduce() {
        return introduce;
    }
    /**
     * 设置：商品封面图片
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取：商品封面图片
     */
    public String getImgUrl() {
        return imgUrl;
    }
    /**
     * 设置：商品列表图片
     */
    public void setImgListUrl(String imgListUrl) {
        this.imgListUrl = imgListUrl;
    }

    /**
     * 获取：商品列表图片
     */
    public String getImgListUrl() {
        return imgListUrl;
    }
    /**
     * 设置：用户id
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取：用户id
     */
    public Integer getCreateUserId() {
        return createUserId;
    }
    /**
     * 设置：是否是新品
     */
    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    /**
     * 获取：是否是新品
     */
    public Integer getIsNew() {
        return isNew;
    }
    /**
     * 设置：热门推荐
     */
    public void setHotSale(Integer hotSale) {
        this.hotSale = hotSale;
    }

    /**
     * 获取：热门推荐
     */
    public Integer getHotSale() {
        return hotSale;
    }
    /**
     * 设置：点击数
     */
    public void setHits(Integer hits) {
        this.hits = hits;
    }

    /**
     * 获取：点击数
     */
    public Integer getHits() {
        return hits;
    }
    /**
     * 设置：商品详情
     */
    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    /**
     * 获取：商品详情
     */
    public String getGoodsDetail() {
        return goodsDetail;
    }
    /**
     * 设置：多图片展示 ‘，分割’
     */
    public void setImages(String images) {
        this.images = images;
    }

    /**
     * 获取：多图片展示 ‘，分割’
     */
    public String getImages() {
        return images;
    }
    /**
     * 设置：销售数量
     */
    public void setSellcount(Integer sellcount) {
        this.sellcount = sellcount;
    }

    /**
     * 获取：销售数量
     */
    public Integer getSellcount() {
        return sellcount;
    }
    /**
     * 设置：总库存
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取：总库存
     */
    public Integer getStock() {
        return stock;
    }
    /**
     * 设置：搜索key
     */
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    /**
     * 获取：搜索key
     */
    public String getSearchKey() {
        return searchKey;
    }
    /**
     * 设置：购买须知
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     * 获取：购买须知
     */
    public String getNotice() {
        return notice;
    }
    /**
     * 设置：服务保障
     */
    public void setPromise(String promise) {
        this.promise = promise;
    }

    /**
     * 获取：服务保障
     */
    public String getPromise() {
        return promise;
    }
    /**
     * 设置：评论分
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取：评论分
     */
    public Integer getScore() {
        return score;
    }
    /**
     * 设置：0表示 正常商品 买卖 1表示团购商品
     */
    public void setSellType(Integer sellType) {
        this.sellType = sellType;
    }

    /**
     * 获取：0表示 正常商品 买卖 1表示团购商品
     */
    public Integer getSellType() {
        return sellType;
    }
    /**
     * 设置：评论数量
     */
    public void setCommentSum(Integer commentSum) {
        this.commentSum = commentSum;
    }

    /**
     * 获取：评论数量
     */
    public Integer getCommentSum() {
        return commentSum;
    }
    /**
     * 设置：收藏数量
     */
    public void setCollectSum(Integer collectSum) {
        this.collectSum = collectSum;
    }

    /**
     * 获取：收藏数量
     */
    public Integer getCollectSum() {
        return collectSum;
    }
    /**
     * 设置：商品有效期
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * 获取：商品有效期
     */
    public Date getExpiryDate() {
        return expiryDate;
    }
    /**
     * 设置：商品类型0;//普通商品1;//特价商品
     */
    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * 获取：商品类型0;//普通商品1;//特价商品
     */
    public Integer getGoodsType() {
        return goodsType;
    }
    /**
     * 设置：0待审核,1审核通过,2审核失败
     */
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取：0待审核,1审核通过,2审核失败
     */
    public Integer getAuditStatus() {
        return auditStatus;
    }
    /**
     * 设置：0下架,1上架,2售罄
     */
    public void setSellStatus(Integer sellStatus) {
        this.sellStatus = sellStatus;
    }

    /**
     * 获取：0下架,1上架,2售罄
     */
    public Integer getSellStatus() {
        return sellStatus;
    }
    /**
     * 设置：作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取：作者
     */
    public String getAuthor() {
        return author;
    }
    /**
     * 设置：材质
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * 获取：材质
     */
    public String getMaterial() {
        return material;
    }
    /**
     * 设置：服务说明
     */
    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    /**
     * 获取：服务说明
     */
    public String getServiceDesc() {
        return serviceDesc;
    }
    /**
     * 设置：规格
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * 获取：规格
     */
    public String getSpecification() {
        return specification;
    }
    /**
     * 设置：0 默认 商城商品  1 表示来源画家商城
     */
    public void setGoodsFrom(Integer goodsFrom) {
        this.goodsFrom = goodsFrom;
    }

    /**
     * 获取：0 默认 商城商品  1 表示来源画家商城
     */
    public Integer getGoodsFrom() {
        return goodsFrom;
    }
    /**
     * 设置：来源编号
     */
    public void setGoodsFromNumber(String goodsFromNumber) {
        this.goodsFromNumber = goodsFromNumber;
    }

    /**
     * 获取：来源编号
     */
    public String getGoodsFromNumber() {
        return goodsFromNumber;
    }
    /**
     * 设置：添加时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取：添加时间
     */
    public Date getCreatetime() {
        return createtime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }
}
