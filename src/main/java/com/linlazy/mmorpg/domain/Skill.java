package com.linlazy.mmorpg.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.entity.SkillEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 技能领域类
 * @author linlazy
 */
@Data
public class Skill {

    /**
     * 技能ID
     */
    private int skillId;


    /**
     * 技能名称
     */
    private String name;

    /**
     * 技能类型
     */
    private int type;

    /**
     * 等级
     */
    private int level;

    /**
     * 技能模板ID
     */
    private int skillTemplateId;

    /**
     * 技能模板参数
     */
    private JSONObject skillTemplateArgs = new JSONObject();


    /**
     * 是否已穿戴
     */
    private boolean dress;

    /**
     * 下一次CDH恢复时间
     */
    private long nextCDResumeTimes;

    /**
     * 玩家技能映射
     */
    private Map<Long, PlayerSkill> playerSkillInfoMap = new HashMap<>();


    /**
     * 获取玩家技能信息
     * @param actorId
     * @return
     */
    public PlayerSkill getPlayerSkillInfo(long actorId) {
        return playerSkillInfoMap.get(actorId);
    }


    public SkillEntity convertSkillEntity(){
        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setSkillId(skillId);
        skillEntity.setDressed(dress);
        skillEntity.setLevel(level);
        skillEntity.setNextCDResumeTimes(nextCDResumeTimes);
        return skillEntity;
    }

}
