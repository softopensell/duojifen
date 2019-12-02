package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推荐奖励规则实体
 * 表名 t_bonus_level_award_rule
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:46:58
 */
public class BonusLevelAwardRuleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Long id;
    //奖励层级
    private Integer bonusLevel;
    //消费总额要求
    private BigDecimal bonusLevelLeast;
    //百分比
    private Integer bonusLevelPercent;
    //状态
    private Integer status;

    /**
     * 设置：主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键ID
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：奖励层级
     */
    public void setBonusLevel(Integer bonusLevel) {
        this.bonusLevel = bonusLevel;
    }

    /**
     * 获取：奖励层级
     */
    public Integer getBonusLevel() {
        return bonusLevel;
    }
    /**
     * 设置：消费总额要求
     */
    public void setBonusLevelLeast(BigDecimal bonusLevelLeast) {
        this.bonusLevelLeast = bonusLevelLeast;
    }

    /**
     * 获取：消费总额要求
     */
    public BigDecimal getBonusLevelLeast() {
        return bonusLevelLeast;
    }
    /**
     * 设置：百分比
     */
    public void setBonusLevelPercent(Integer bonusLevelPercent) {
        this.bonusLevelPercent = bonusLevelPercent;
    }

    /**
     * 获取：百分比
     */
    public Integer getBonusLevelPercent() {
        return bonusLevelPercent;
    }
    /**
     * 设置：状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态
     */
    public Integer getStatus() {
        return status;
    }
}
