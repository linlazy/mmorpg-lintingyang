package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.event.type.BossDeadEvent;
import com.linlazy.mmorpg.event.type.CopyBossDeadEvent;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.service.CopyService;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
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
    public int computeDefense() {
        return 0;
    }

    @Override
    public int computeAttack() {
        return 0;
    }


    @Override
    public void attacked(SceneEntity sceneEntity, Skill skill) {
        super.attacked(sceneEntity,skill);

        if(hp > 0){
            if(sceneEntity instanceof Player){
                Player player = (Player) sceneEntity;
                PlayerPushHelper.pushAttacked(player.getActorId(),String.format("%d BOSS【%s】受到玩家【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(), skill.getName(),hp));
            }
            if(sceneEntity instanceof PlayerCall){
                PlayerCall playerCall = (PlayerCall) sceneEntity;
                PlayerPushHelper.pushAttacked(playerCall.getSourceId(),String.format("%d BOSS【%s】受到玩家召唤兽【%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(), skill.getName(),hp));
            }
        } else {
            this.hp = 0;
            triggerDeadEvent();
        }
    }


    @Override
    protected void triggerDeadEvent() {

        EventBusHolder.post(new BossDeadEvent(this));
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        if(sceneConfigService.isCopyScene(this.sceneId)){
            CopyService copyService = SpringContextUtil.getApplicationContext().getBean(CopyService.class);
            Copy copy = copyService.getCopy(this.getCopyId());
            copy.getPlayerMap().values().stream()
                    .forEach(player ->PlayerPushHelper.pushAttacked(player.getActorId(),String.format("%d BOSS【%s】已死亡", DateUtils.getNowMillis()/1000,name)));
            EventBusHolder.post(new CopyBossDeadEvent(copy));
        }
    }
}
