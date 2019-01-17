package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.event.type.CopyPlayerDeadEvent;
import com.linlazy.mmorpg.event.type.PlayerDeadEvent;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.equip.manager.domain.DressedEquip;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.service.SkillService;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.Objects;

/**
 * 玩家领域类
 * @author linlazy
 */
@Data
public class Player extends SceneEntity {


    /**
     * 玩家
     */
    private long actorId;

    /**
     * 职业
     */
    private int profession;

    /**
     * 等级
     */
    private int level;

    @Override
    protected int computeDefense() {
        return 0;
    }

    @Override
    protected void triggerDeadEvent() {
        EventBusHolder.post(new PlayerDeadEvent(this));
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        if(sceneConfigService.isCopyScene(this.sceneId)){
            EventBusHolder.post(new CopyPlayerDeadEvent(this));
        }


    }

    @Override
    public int computeAttack() {
        return 0;
    }


    /**
     * 蓝
     */
    private int mp;

    /**
     * 金币
     */
    private int gold;


    /**
     * 穿戴装备
     */
    private DressedEquip dressedEquip;

    /**
     *  玩家公会信息
     */
    private PlayerGuildInfo playerGuildInfo;

    /**
     *  玩家竞技场信息
     */
    private PlayerArenaInfo playerArenaInfo;

    /**
     * 玩家技能信息
     */
    public PlayerSkillInfo getPlayerSkillInfo(){
        SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        return null;
    }

    /**
     * 玩家任务信息
     */
    private PlayerTaskInfo playerTaskInfo;

    /**
     * 玩家队伍信息
     */
    private PlayerTeamInfo playerTeamInfo;

    /**
     * 玩家邮件信息
     */
    private PlayerEmailInfo playerEmailInfo;

    /**
     * 玩家聊天信息
     */
    private PlayerChatInfo playerChatInfo;

    /**
     * 玩家召唤兽
     */
    private PlayerCall playerCall;

    public Player(Long actorId) {
        this.actorId =actorId;
    }


    public boolean isTeam(){
        TeamService teamService = SpringContextUtil.getApplicationContext().getBean(TeamService.class);
        return teamService.isTeam(actorId);
    }

    /**
     * 获取玩家队伍
     * @return
     */
    public Team getTeam() {
        TeamService teamService = SpringContextUtil.getApplicationContext().getBean(TeamService.class);
        return teamService.getTeamByactorId(actorId);
    }

    public PlayerBackpack getBackPack(){
        PlayerBackpackService playerBackpackService = SpringContextUtil.getApplicationContext().getBean(PlayerBackpackService.class);
        return playerBackpackService.getPlayerBackpack(actorId);
    }



    public PlayerCopyInfo getPlayerCopyInfo(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return actorId == player.actorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId);
    }

    @Override
    public void attacked(SceneEntity sceneEntity,Skill skill){
        int attack = sceneEntity.computeAttack();
        int defense = computeDefense();
        int damage = attack > defense?attack - defense: 1;
        this.hp -= damage;


        if(hp > 0){
            PlayerPushHelper.pushAttacked(actorId,String.format("%d 玩家【%s】受到【%s,hash码：%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(),sceneEntity.hashCode(),skill.getName(),hp));
        } else {
            this.hp = 0;
            triggerDeadEvent();
        }
    }

}
