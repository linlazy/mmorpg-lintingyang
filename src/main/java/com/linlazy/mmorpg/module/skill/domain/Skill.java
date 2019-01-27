package com.linlazy.mmorpg.module.skill.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.module.player.domain.PlayerSkill;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("拥有技能【%s】 等级【%s】 ID【%d】",this.getName(),this.getLevel(),this.getSkillId()));


        Integer attack = skillTemplateArgs.getInteger("attack");
        if(attack != null){
            stringBuilder.append(String.format("攻击力【%d】 ",attack));
        }
        Integer attackNum = skillTemplateArgs.getInteger("attackNum");
        if(attackNum != null){
            if (attackNum != 0){
                stringBuilder.append(String.format("攻击人数【%d】 ",attackNum));
            }  else {
                stringBuilder.append(String.format("攻击人数【全体】 "));
            }
        }
        Integer cd = skillTemplateArgs.getInteger("cd");
        if(cd != null){
            stringBuilder.append(String.format("冷却时间【%d】秒 ",cd));
        }
        Integer mp = skillTemplateArgs.getInteger("mp");
        if(mp != null){
            stringBuilder.append(String.format("消耗【%d】点MP ",mp));
        }
        Integer hp = skillTemplateArgs.getInteger("resumeHP");
        if(hp != null){
            stringBuilder.append(String.format("恢复【%d】点HP ",hp));
        }
        Integer continueTime = skillTemplateArgs.getInteger("continueTime");
        if(continueTime != null){
            stringBuilder.append(String.format("技能持续【%d】秒 ",continueTime));
        }

        Integer duration = skillTemplateArgs.getInteger("duration");
        Integer decreaseHP = skillTemplateArgs.getInteger("decreaseHP");
        if(duration != null && decreaseHP != null){
            stringBuilder.append(String.format("每隔【%d】秒减少【%d】点HP ",duration,decreaseHP));
        }

        return stringBuilder.toString();
    }
}
