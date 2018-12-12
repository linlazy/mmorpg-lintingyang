package com.linlazy.mmorpglintingyang.module.skill.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linlazy.mmorpglintingyang.module.skill.entity.model.SkillInfo;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Data
public class Skill {

    private long actorId;

    /**
     * 玩家技能信息
     */
    private String skills;

    private Set<SkillInfo> skillInfoSet = new HashSet<>();


    public Set<SkillInfo> getSkillInfoSet() {
        if(!StringUtils.isEmpty(skills)){
            skillInfoSet = JSON.parseObject(skills, new TypeReference<HashSet<SkillInfo>>() {});
        }
        return skillInfoSet;
    }
}
