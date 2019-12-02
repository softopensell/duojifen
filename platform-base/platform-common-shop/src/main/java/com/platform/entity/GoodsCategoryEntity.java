package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_goods_category
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 17:47:08
 */
public class GoodsCategoryEntity extends Tree<GoodsCategoryEntity>  implements Serializable {
    private static final long serialVersionUID = 1L;

    //ID
    private Integer id;
    //名称
    private String name;
    //编码
    private String code;
    //描述
    private String frontDesc;
    //父级ID
    private Integer parentId;
    //搜索key
    private String searchKey;
    //排序
    private Integer sortOrder;
    //级别
    private String level;
    //状态:0启用1禁用
    private Integer statu;
    //店铺ID
    private Integer shopId;
    //是否显示
    private Integer isShow;
    //导航图片
    private String bannerImg;
    //图标图片
    private String iconImg;
    //跳转链接
    private String imgLinkUrl;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    
    private String parentName;

    /**
     * 设置：ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：ID
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：名称
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取：编码
     */
    public String getCode() {
        return code;
    }
    /**
     * 设置：描述
     */
    public void setFrontDesc(String frontDesc) {
        this.frontDesc = frontDesc;
    }

    /**
     * 获取：描述
     */
    public String getFrontDesc() {
        return frontDesc;
    }
    /**
     * 设置：父级ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取：父级ID
     */
    public Integer getParentId() {
        return parentId;
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
     * 设置：排序
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 获取：排序
     */
    public Integer getSortOrder() {
        return sortOrder;
    }
    /**
     * 设置：级别
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取：级别
     */
    public String getLevel() {
        return level;
    }
    /**
     * 设置：状态:0启用1禁用
     */
    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    /**
     * 获取：状态:0启用1禁用
     */
    public Integer getStatu() {
        return statu;
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
     * 设置：是否显示
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    /**
     * 获取：是否显示
     */
    public Integer getIsShow() {
        return isShow;
    }
    /**
     * 设置：导航图片
     */
    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    /**
     * 获取：导航图片
     */
    public String getBannerImg() {
        return bannerImg;
    }
    /**
     * 设置：图标图片
     */
    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    /**
     * 获取：图标图片
     */
    public String getIconImg() {
        return iconImg;
    }
    /**
     * 设置：跳转链接
     */
    public void setImgLinkUrl(String imgLinkUrl) {
        this.imgLinkUrl = imgLinkUrl;
    }

    /**
     * 获取：跳转链接
     */
    public String getImgLinkUrl() {
        return imgLinkUrl;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
    
    
}
