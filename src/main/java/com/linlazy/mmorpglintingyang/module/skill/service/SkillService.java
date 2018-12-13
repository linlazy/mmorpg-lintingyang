package com.linlazy.mmorpglintingyang.module.skill.service;

import com.alibaba.fastjson.JSONArray;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.Scene;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.model.SceneEntityInfo;
import com.linlazy.mmorpglintingyang.module.scene.push.ScenePushHelper;
import com.linlazy.mmorpglintingyang.module.scene.service.SceneService;
import com.linlazy.mmorpglintingyang.module.skill.manager.SkillManager;
import com.linlazy.mmorpglintingyang.module.skill.manager.entity.model.SkillInfo;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.module.user.service.UserService;
import com.linlazy.mmorpglintingyang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SkillService {

    @Autowired
    private SceneService sceneService;
    @Autowired
    private UserService userService;
    @Autowired
    private SkillManager skillManager;

    /**
     * 获取技能配置信息
     * @return
     */
    public Result<?> getAllSkillConfigInfo() {
        JSONArray skillConfigInfo = skillManager.getSkillConfigInfo();
        return Result.success(skillConfigInfo);
    }

    /**
     * 使用技能攻击怪物
     * @param actorId
     * @param skillId
     * @param monsterId
     * @return
     */
    public Result<?> attackMonster(long actorId, int skillId, int monsterId) {

        //玩家是否具备该技能
        SkillInfo skillInfo = skillManager.getActorSkillInfo(actorId,skillId);
        System.out.println(skillInfo);
        if( skillInfo == null){
            return Result.valueOf("玩家不具备该技能");
        }

        //技能冷却
        System.out.println("当前时间"+ DateUtils.getNowMillis());
        if(DateUtils.getNowMillis() < skillInfo.getNextCDResumeTime()){
            return Result.valueOf("技能CD中...");
        }

        //是否蓝足够
        int consumeMP = skillManager.getConsumeMP(skillId);
        User user = userService.getUser(actorId);
        if(user.getMp() < consumeMP){
            return Result.valueOf("mp不足");
        }
        user.modifyMP(-consumeMP);

        SceneEntityInfo monsterInfo = sceneService.getMonsterInfo(actorId,monsterId);
        if(monsterInfo == null){
            return Result.valueOf("当前场景无此怪物");
        }

        //存档怪物
        int attack = skillManager.getSkillAttack(actorId,skillId);
        monsterInfo.attacked(attack);
        Scene scene = sceneService.getScene(actorId);
        sceneService.updateSceneEntityInfo(scene.getSceneId(),monsterInfo);
        //存档技能CD
        long nextCDResumeTime = DateUtils.getNowMillis() +skillManager.getSkillCDMills(skillId);
        System.out.println("old[nextCDResumeTime]："+ skillInfo.getNextCDResumeTime() + " new[nextCDResumeTime]:"+ nextCDResumeTime);
        skillInfo.setNextCDResumeTime(nextCDResumeTime);
        skillManager.updateSkillInfo(actorId,skillInfo);
        //存档蓝
        userService.updateUser(user);

        //通知其它玩家
        if(monsterInfo.getHp() == 0){
            Set<Long> actorIds = sceneService.getCurrentSceneOnlineActorIds(scene.getSceneId());
            for(Long pushActorId: actorIds){
                if(pushActorId != actorId){
                    ScenePushHelper.pushMonster(pushActorId,monsterInfo);
                }
            }
        }
        return Result.success(monsterInfo);
    }



    /**
     * 获取玩家技能信息集
     * @param actorId
     * @return
     */
    public  Result<?> getActorSkillInfoSet(long actorId) {
        return Result.success(skillManager.getActorSkillInfoSet(actorId));
    }

    /**
     * 升级技能
     * @param actorId
     * @param skillId
     * @return
     */
    public Result<?> upgradeSkill(long actorId, int skillId) {
        return Result.success(skillManager.upgradeSkill(actorId,skillId));
    }
}
