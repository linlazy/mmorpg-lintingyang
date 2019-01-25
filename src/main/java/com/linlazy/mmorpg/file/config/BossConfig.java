package com.linlazy.mmorpg.file.config;

import com.linlazy.mmorpg.module.common.reward.Reward;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linlazy
 */
@Data
public class BossConfig {

    /**
     * bossId
     */
    private long bossId;

    /**
     * 名称
     */
    private String name;

    /**
     * 血量
     */
    private int hp;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 所处场景
     */
    private List<Integer> sceneIdSet = new ArrayList<>();

    /**
     * 奖励
     */
    private List<Reward> rewardList = new ArrayList<>();

}
