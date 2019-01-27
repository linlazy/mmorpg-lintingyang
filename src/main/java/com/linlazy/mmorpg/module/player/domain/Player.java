package com.linlazy.mmorpg.module.player.domain;

import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.module.equip.domain.DressedEquip;
import com.linlazy.mmorpg.module.player.attack.PlayerAttack;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.player.defense.PlayerDefense;
import com.linlazy.mmorpg.entity.PlayerEntity;
import com.linlazy.mmorpg.event.type.CopyPlayerDeadEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackedEvent;
import com.linlazy.mmorpg.event.type.PlayerDeadEvent;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.equip.service.EquipmentService;
import com.linlazy.mmorpg.module.player.push.PlayerPushHelper;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.shop.service.ShopService;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.module.team.domain.Team;
import com.linlazy.mmorpg.module.team.service.TeamService;
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


    private PlayerEntity playerEntity;

    public Player(PlayerEntity playerEntity) {
        this.playerEntity =playerEntity;
        this.setActorId(playerEntity.getActorId());
        this.setMp(playerEntity.getMp());
        this.setName(playerEntity.getUsername());
        this.setHp(playerEntity.getHp());
        this.setGold(playerEntity.getGold());
        this.setSceneEntityType(SceneEntityType.PLAYER);
        this.setProfession(playerEntity.getProfession());
        this.setLevel(playerEntity.getLevel());
        this.setSceneId(playerEntity.getSceneId());
    }

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

    /***
     * 是否锁定背包
     */
    private volatile boolean lockBackpack;

    @Override
    public int computeDefense() {
        return PlayerDefense.computeFinalDefense(this);
    }

    @Override
    protected void triggerDeadEvent() {
        super.triggerDeadEvent();
        EventBusHolder.post(new PlayerDeadEvent(this));
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        if(sceneConfigService.isCopyScene(this.sceneId)){
            EventBusHolder.post(new CopyPlayerDeadEvent(this));
        }


    }

    @Override
    public int computeAttack() {
        return PlayerAttack.computeFinalAttack(this);
    }



    /**
     * 金币
     */
    private long gold;

    public long consumeGold(long consumeGold){
        synchronized (this){
            gold -=consumeGold;
            if(gold < 0){
                gold = 0;
            }
        }
        return gold;
    }


    /**
     *  玩家竞技场信息
     */
    private PlayerArenaInfo playerArenaInfo;

    /**
     * 玩家技能信息
     */
    public PlayerSkill getPlayerSkillInfo(){
        SkillService skillService = SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        return skillService.getPlayerSkill(actorId);
    }


    /**
     * 玩家队伍信息
     */
    private PlayerTeamInfo playerTeamInfo;


    /**
     * 玩家召唤兽
     */
    private PlayerCall playerCall;


    public boolean isTeam(){
        TeamService teamService = SpringContextUtil.getApplicationContext().getBean(TeamService.class);
        return teamService.isTeam(actorId);
    }

    public  long resumeGold(int resumeGold){
        synchronized (this){
            this.gold += resumeGold;

            return this.gold;
        }
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
    public void attacked(SceneEntity sceneEntity, Skill skill){
        super.attacked(sceneEntity,skill);

        if(hp > 0){
            PlayerPushHelper.pushAttacked(actorId,String.format("%d 玩家【%s】受到【%s,hash码：%s】技能【%s】攻击：血量:%d", DateUtils.getNowMillis()/1000,name,sceneEntity.getName(),sceneEntity.hashCode(), skill.getName(),hp));
            EventBusHolder.post(new PlayerAttackedEvent(this));
        } else {
            this.hp = 0;
            triggerDeadEvent();
        }
    }

    public boolean hasSkill(int skillId){
        PlayerSkill playerSkillInfo = getPlayerSkillInfo();
        return playerSkillInfo.getSkillMap().containsKey(skillId);
    }

    public PlayerEntity convertPlayerEntity(){
        playerEntity.setActorId(actorId);
        playerEntity.setHp(hp);
        playerEntity.setLevel(level);
        playerEntity.setProfession(profession);
        playerEntity.setMp(mp);
        playerEntity.setGold(gold);
        playerEntity.setSceneId(sceneId);
        return playerEntity;
    }

    public void lockBackpack(boolean lockBackpack) {
        this.lockBackpack = lockBackpack;
    }

    public DressedEquip getDressedEquip(){
        EquipmentService equipmentService = SpringContextUtil.getApplicationContext().getBean(EquipmentService.class);
        return equipmentService.getDressedEquip(actorId);
    }

    public PlayerShop getPlayerShop(){
        ShopService shopService = SpringContextUtil.getApplicationContext().getBean(ShopService.class);
        return shopService.getPlayerShop(actorId);
    }
}
