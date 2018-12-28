package com.linlazy.mmorpglintingyang.module.copy.domain;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.utils.RewardUtils;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
     * 副本怪物集合(小怪+Boss)
     */
    private Set<SceneEntityDo> sceneEntitySet = new HashSet<>();


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
                .filter(sceneEntityDo -> sceneEntityDo.getSceneEntityType() == SceneEntityType.Boss)
                .allMatch(bossDo -> bossDo.getHp() == 0);
    }

    /**
     * 退出副本
     */
    public void quitCopy(){

        //获取退出后目标场景
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(this.sceneId);
        int targetSceneId = copyConfig.getJSONArray("neighborSet").getIntValue(0);

        //副本玩家移动到目标场景
        SceneManager sceneManager = SpringContextUtil.getApplicationContext().getBean(SceneManager.class);
        CopyManager copyManager = SpringContextUtil.getApplicationContext().getBean(CopyManager.class);
        UserManager userManager = SpringContextUtil.getApplicationContext().getBean(UserManager.class);
        Set<Long> set = Sets.newHashSet(this.copyPlayerIdSet);
        //退出副本场景
        set.stream()
                .filter(actorId -> userManager.getUser(actorId).getSceneId() == targetSceneId)
                .forEach(actorId ->{
                    sceneManager.moveToScene(actorId,targetSceneId);
                });
        //退出副本
        set.stream()
                .forEach(actorId ->{
                    sceneManager.moveToScene(actorId,targetSceneId);
                    copyManager.quitCopy(actorId);
                });
    }

    public SceneDo convertSceneDo() {
        SceneDo sceneDo = new SceneDo();
        sceneDo.setSceneId(this.sceneId);
        sceneDo.setActorIdSet(this.copyPlayerIdSet);
        sceneDo.setCopyId(this.copyId);
        sceneDo.setSceneEntityDoSet(this.sceneEntitySet);
        return sceneDo;
    }
}
