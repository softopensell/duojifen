package com.platform.entity;

import java.io.Serializable;

/**
 * 实体
 * 表名 t_ad_position
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:50
 */
public class AdPositionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //ID
    private Integer id;
    //名称
    private String name;
    //宽度
    private Integer width;
    //高度
    private Integer height;
    //描述
    private String desc;

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
     * 设置：宽度
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 获取：宽度
     */
    public Integer getWidth() {
        return width;
    }
    /**
     * 设置：高度
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 获取：高度
     */
    public Integer getHeight() {
        return height;
    }
    /**
     * 设置：描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取：描述
     */
    public String getDesc() {
        return desc;
    }
}
