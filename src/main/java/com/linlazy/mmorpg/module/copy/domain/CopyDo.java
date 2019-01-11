package com.linlazy.mmorpg.module.copy.domain;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.copy.manager.CopyManager;
import com.linlazy.mmorpg.module.scene.config.SceneConfigService;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.domain.SceneDo;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.scene.manager.SceneManager;
import com.linlazy.mmorpg.module.user.manager.UserManager;
import com.linlazy.mmorpg.utils.RewardUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * @author linlazy
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class CopyDo extends SceneDo {

    /**
     * 副本ID标识
     */
    private int copyId;

    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 副本玩家
     */
    private Set<Long> copyPlayerIdSet = new HashSet<>();

    /**
     * 副本怪物集合(小怪+BOSS)
     */
    private Set<SceneEntity> sceneEntitySet = new HashSet<>();


    /**
     * 获取通过副本奖励
     * @return
     */
    public List<Reward> getRewardList(){
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(this.sceneId);
        String rewards = copyConfig.getString("rewards");
        return RewardUtils.parseRewards(rewards);
    }

    /**
     * 玩家全部死亡
     * @return
     */
    public boolean isAllActorDead(){
        UserManager userManager = SpringContextUtil.getApplicationContext().getBean(UserManager.class);
        return copyPlayerIdSet.stream()
                .allMatch(actorId -> userManager.getUser(actorId).getHp() == 0);
    }

    /**
     * BOSS全部死亡
     * @return
     */
    public boolean isAllBOSSDead(){
        return sceneEntitySet.stream()
                .filter(sceneEntityDo -> sceneEntityDo.getSceneEntityType() == SceneEntityType.BOSS)
                .allMatch(bossDo -> bossDo.getHp() == 0);
    }

    /**
     * 退出副本
     */
    public void quitCopy(){


    }

    public SceneDo convertSceneDo() {
        SceneDo sceneDo = new SceneDo();
        sceneDo.setSceneId(this.sceneId);
        sceneDo.setActorIdSet(this.copyPlayerIdSet);
        sceneDo.setCopyId(this.copyId);
        sceneDo.setSceneEntitySet(this.sceneEntitySet);
        return sceneDo;
    }
}
