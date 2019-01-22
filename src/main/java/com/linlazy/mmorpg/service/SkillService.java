package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerSkill;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.file.config.SkillConfig;
import com.linlazy.mmorpg.file.service.SkillConfigService;
import com.linlazy.mmorpg.push.PlayerPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.template.skill.strategy.SkillTypeStrategy;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/**
 * 技能服务类
 * @author linlazy
 */
@Component
public class SkillService {

    @Autowired
    private SkillDAO skillDao;


    @Autowired
    private SkillConfigService skillConfigService;

    /**
     * 玩家技能缓存
     */
    public static LoadingCache<Long, PlayerSkill> playerSkillCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<Long, PlayerSkill>() {
                @Override
                public PlayerSkill load(Long actorId) {

                    PlayerSkill playerSkill = new PlayerSkill(actorId);
                    Map<Integer,Skill>  skillMap = new HashMap<>();


                    SkillDAO skillDAO = SpringContextUtil.getApplicationContext().getBean(SkillDAO.class);
                    SkillConfigService skillConfigService = SpringContextUtil.getApplicationContext().getBean(SkillConfigService.class);
                    List<SkillEntity> playerSkillList = skillDAO.getPlayerSkillList(actorId);
                    playerSkillList.stream()
                            .forEach(skillEntity -> {
                                Skill skill = new Skill();

                                skill.setSkillId(skillEntity.getSkillId());
                                skill.setLevel(skill.getLevel());
                                skill.setNextCDResumeTimes(skillEntity.getNextCDResumeTimes());

                                SkillConfig skillConfig = skillConfigService.getSkillConfig(skillEntity.getSkillId());
                                skill.setName(skillConfig.getName());
                                skill.setSkillTemplateArgs(skillConfig.getSkillTemplateArgs());
                                skill.setSkillTemplateId(skillConfig.getSkillTemplateId());
                                skill.setType(skillConfig.getType());

                                skillMap.put(skill.getSkillId(),skill);
                            });
                    playerSkill.setSkillMap(skillMap);

                    return playerSkill;
                }
            });



    public void useSkill(SceneEntity sceneEntity, Skill skill){
        SkillTypeStrategy skillTypeStrategy = SkillTypeStrategy.getSkillTypeStrategy(skill.getType());
        skillTypeStrategy.useSkill(sceneEntity,skill);
    }


    public Result<?> dressSkill(long actorId, int skillId, JSONObject jsonObject) {

        //未拥有此技能或此技能已穿戴
        if(notHas(actorId,skillId) || isDressed(actorId,skillId)){
            return Result.valueOf("参数错误");
        }
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        skillEntity.setDressed(true);
        skillDao.updateQueue(skillEntity);
        return Result.success();
    }

    public Result<?> levelUp(long actorId, int skillId, JSONObject jsonObject) {

        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        if(skillEntity != null){
            return Result.valueOf("已拥有技能");
        }

        skillEntity = new SkillEntity();
        skillEntity.setActorId(actorId);
        skillEntity.setSkillId(skillId);
        skillEntity.setDressed(false);
        skillEntity.setLevel(1);
        skillEntity.setNextCDResumeTimes(DateUtils.getNowMillis());

        skillDao.insertQueue(skillEntity);
        return Result.success(skillEntity);
    }

    public boolean notHas(long actorId,int skillId){
        return skillDao.getEntityByPK(actorId, skillId) == null;
    }
    public boolean isDressed(long actorId,int skillId){
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        if(skillEntity != null && skillEntity.isDressed()){
            return true;
        }
        return false;
    }

    public List<Skill> getBossSkillList(long bossId){
        List<SkillConfig> bossSkillConfigList = skillConfigService.getBossSkillConfigList(bossId);
        return bossSkillConfigList.stream()
                .map(skillConfig -> {
                    Skill skill = new Skill();

                    skill.setSkillId(skillConfig.getSkillId());
                    skill.setSkillTemplateId(skillConfig.getSkillTemplateId());
                    skill.setSkillTemplateArgs(skillConfig.getSkillTemplateArgs());
                    skill.setName(skillConfig.getName());

                    return skill;
                })
                .collect(toList());
    }

    public List<Skill> getMonsterSkillList(long monsterId){
        List<SkillConfig> monsterSkillConfigList = skillConfigService.getMonsterSkillConfigList(monsterId);
        return monsterSkillConfigList.stream()
                .map(skillConfig -> {
                    Skill skill = new Skill();

                    skill.setSkillId(skillConfig.getSkillId());
                    skill.setSkillTemplateId(skillConfig.getSkillTemplateId());
                    skill.setSkillTemplateArgs(skillConfig.getSkillTemplateArgs());
                    skill.setName(skillConfig.getName());

                    return skill;
                })
                .collect(toList());
    }

    public PlayerSkill getPlayerSkill(long actorId) {
        try {
            return playerSkillCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得技能
     * @param actorId 玩家ID
     * @param skillId 技能ID
     * @return
     */
    public Result<?> gainSkill(long actorId, int skillId) {
        PlayerSkill playerSkill = getPlayerSkill(actorId);
        Skill skill = new Skill();

        skill.setSkillId(skillId);
        skill.setLevel(skill.getLevel());
        skill.setNextCDResumeTimes(DateUtils.getNowMillis());

        SkillConfig skillConfig = skillConfigService.getSkillConfig(skill.getSkillId());
        skill.setName(skillConfig.getName());
        skill.setSkillTemplateArgs(skillConfig.getSkillTemplateArgs());
        skill.setSkillTemplateId(skillConfig.getSkillTemplateId());

        playerSkill.getSkillMap().put(skill.getSkillId(),skill);
        SkillEntity skillEntity = skill.convertSkillEntity();
        skillEntity.setActorId(actorId);
        skillDao.insertQueue(skillEntity);

        return Result.success();
    }

    /**
     * 初始化玩家职业技能
     * @param player
     */
    public void initPlayerProfessionSkill(Player player) {
        PlayerSkill playerSkill = new PlayerSkill(player.getActorId());
        Map<Integer,Skill>  skillMap = new HashMap<>();

        List<SkillConfig> professionSkillConfig = skillConfigService.getProfessionSkillConfig(player.getProfession());


        professionSkillConfig.stream()
                .forEach(skillConfig -> {
                    Skill skill = new Skill();

                    skill.setSkillId(skillConfig.getSkillId());
                    skill.setNextCDResumeTimes(DateUtils.getNowMillis());
                    skill.setName(skillConfig.getName());
                    skill.setSkillTemplateArgs(skillConfig.getSkillTemplateArgs());
                    skill.setSkillTemplateId(skillConfig.getSkillTemplateId());
                    skill.setType(skillConfig.getType());

                    SkillEntity skillEntity = skill.convertSkillEntity();
                    skillEntity.setActorId(player.getActorId());
                    skillDao.insertQueue(skillEntity);

                    skillMap.put(skill.getSkillId(),skill);
                });
        playerSkill.setSkillMap(skillMap);

        playerSkillCache.put(player.getActorId(),playerSkill);
        PlayerPushHelper.pushPlayerSkillInfo(player.getActorId(),playerSkill);
    }

    public List<Skill> getPlayerCallSkillList() {
        List<SkillConfig> playerCallSkillConfig = skillConfigService.getPlayerCallSkillConfig();
        List<Skill> playerCallSkill = new ArrayList<>();
        playerCallSkillConfig.stream()
                .forEach(skillConfig -> {
                    Skill skill = new Skill();

                    skill.setSkillId(skillConfig.getSkillId());
                    skill.setNextCDResumeTimes(DateUtils.getNowMillis());
                    skill.setName(skillConfig.getName());
                    skill.setSkillTemplateArgs(skillConfig.getSkillTemplateArgs());
                    skill.setSkillTemplateId(skillConfig.getSkillTemplateId());
                    skill.setType(skillConfig.getType());

                    playerCallSkill.add(skill);
                });

        return playerCallSkill;
    }
}
