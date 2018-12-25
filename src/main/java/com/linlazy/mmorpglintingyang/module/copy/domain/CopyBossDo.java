package com.linlazy.mmorpglintingyang.module.copy.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.utils.RandomUtils;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 副本BOSS
 */
@Data
public class CopyBossDo {

    /**
     *  boss标识
     */
    private int bossId;

    /**
     * boss名字
     */
    private String name;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 当前血量
     */
    private int hp;

    /**
     * 副本ID
     */
    private int copyId;

    @Autowired
    private CopyManager copyManager;

    public CopyBossDo(int sceneId) {
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(sceneId).getJSONObject("boss");
        bossId = copyConfig.getIntValue("bossId");
        name = copyConfig.getString("name");
        attack = copyConfig.getIntValue("attackPlayerDo");
        hp = copyConfig.getIntValue("hp");
    }


    public void attackPlayerDo(){
        //从副本中活着的玩家挑选一个，攻击他
        CopyDo copyDo = copyManager.getCopyDo(copyId);
        List<CopyPlayerDo> copyPlayerDoList = copyDo.getCopyPlayerDoSet().stream()
                .filter(copyPlayerDo -> copyPlayerDo.getHp() > 0)
                .collect(Collectors.toList());

        CopyPlayerDo copyPlayerDo = RandomUtils.randomElement(copyPlayerDoList);
        copyPlayerDo.attackedFromBoss(attack);
    }

    public void attacked(int attack){
       if(this.hp == 0){
           EventBusHolder.post(new ActorEvent(0, EventType.COPY_BOSS_DEAD));
       }
    }
}
