package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 t_ad
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:51
 */
public class AdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //
    private Integer adPositionId;
    //
    private Integer mediaType;
    //
    private String adName;
    //
    private String link;
    //
    private String imageUrl;
    //
    private String content;
    //
    private Date endTime;
    //
    private Integer enabled;

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
     * 设置：
     */
    public void setAdPositionId(Integer adPositionId) {
        this.adPositionId = adPositionId;
    }

    /**
     * 获取：
     */
    public Integer getAdPositionId() {
        return adPositionId;
    }
    /**
     * 设置：
     */
    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * 获取：
     */
    public Integer getMediaType() {
        return mediaType;
    }
    /**
     * 设置：
     */
    public void setAdName(String adName) {
        this.adName = adName;
    }

    /**
     * 获取：
     */
    public String getAdName() {
        return adName;
    }
    /**
     * 设置：
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 获取：
     */
    public String getLink() {
        return link;
    }
    /**
     * 设置：
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取：
     */
    public String getImageUrl() {
        return imageUrl;
    }
    /**
     * 设置：
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取：
     */
    public String getContent() {
        return content;
    }
    /**
     * 设置：
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取：
     */
    public Date getEndTime() {
        return endTime;
    }
    /**
     * 设置：
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取：
     */
    public Integer getEnabled() {
        return enabled;
    }
}
