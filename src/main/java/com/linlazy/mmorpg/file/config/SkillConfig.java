package com.linlazy.mmorpg.file.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * 技能配置
 * @author linlazy
 */
@Data
public class SkillConfig {
    /**
     * 技能ID
     */
    private int skillId;

    /**
     * 技能名称
     */
    private String name;

    /**
     * 具有该技能的boss集合
     */
    private List<Long> bossIds;

    /**
     * 技能模板ID
     */
    private int skillTemplateId;

    /**
     * 技能模板参数
     */
    private JSONObject skillTemplateArgs;


}
