package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.utils.RandomUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * BOSS
 * @author linlazy
 */
@Data
public class Boss extends SceneEntity {

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     *  bossId
     */
    private long bossId;

    /**
     * boss血量
     */
    private int hp;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 名称
     */
    private String name;

    /**
     * boss技能列表
     */
    private List<Skill> skillList = new ArrayList<>();

    public Skill randomSkill(){
        return RandomUtils.randomElement(skillList);
    }

    @Override
    protected int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return attack;
    }
}
