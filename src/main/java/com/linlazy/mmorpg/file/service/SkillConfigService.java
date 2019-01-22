package com.linlazy.mmorpg.file.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.SkillType;
import com.linlazy.mmorpg.file.config.SkillConfig;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 技能配置服务类
 * @author linlazy
 */
@Component
public class SkillConfigService {


    private static ConfigFile skillConfigFile;
    static {
        skillConfigFile =  ConfigFileManager.use("config_file/skill_config.json");
    }

    /**
     * 构建技能ID与配置映射
     */
    private static final Map<Integer,SkillConfig> SKILL_ID_MAP = new HashMap<>();

    /**
     * bossID 与技能映射
     */
    private static final Map<Long, List<SkillConfig>> bossIdSkillMap = new HashMap<>();

    /**
     * monsterID 与技能映射
     */
    private static final Map<Long, List<SkillConfig>> monsterIdSkillMap = new HashMap<>();


    /**
     * professionId 与技能映射
     */
    private static final Map<Long, List<SkillConfig>> professionIdSkillMap = new HashMap<>();

    /**
     *  玩家召唤兽技能
     */
    private static final List<SkillConfig> playerCallskillList = new ArrayList<>();


    @PostConstruct
    public void init(){
        JSONArray jsonArray = skillConfigFile.getJsonArray();
        for(int i =0; i < jsonArray.size(); i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            SkillConfig skillConfig = new SkillConfig();
            int skillId = jsonObject.getIntValue("skillId");
            String name = jsonObject.getString("name");
            int skillTemplateId = jsonObject.getIntValue("skillTemplateId");
            JSONObject skillTemplateArgs = jsonObject.getJSONObject("skillTemplateArgs");
            List<Long> bossIds = jsonObject.getJSONArray("bossIds").toJavaList(Long.class);
            List<Long> monsterIds = jsonObject.getJSONArray("monsterIds").toJavaList(Long.class);

            skillConfig.setSkillId(skillId);
            skillConfig.setName(name);
            skillConfig.setSkillTemplateId(skillTemplateId);
            skillConfig.setSkillTemplateArgs(skillTemplateArgs);
            skillConfig.setBossIds(bossIds);
            skillConfig.setMonsterIds(monsterIds);
            skillConfig.setProfessionId(jsonObject.getLongValue("professionId"));
            skillConfig.setType(jsonObject.getIntValue("type"));


            SKILL_ID_MAP.putIfAbsent(skillId, skillConfig);

            // 构建bossID 与技能映射
            for(Long bossId: bossIds){
                bossIdSkillMap.computeIfAbsent(bossId, k -> new ArrayList<>());
                List<SkillConfig> skillConfigList = bossIdSkillMap.get(bossId);
                skillConfigList.add(skillConfig);
            }

            // 构建monsterID 与技能映射
            for(Long monsterId: monsterIds){
                monsterIdSkillMap.computeIfAbsent(monsterId, k -> new ArrayList<>());
                List<SkillConfig> skillConfigList = monsterIdSkillMap.get(monsterId);
                skillConfigList.add(skillConfig);
            }

            // 构建professionId 与技能映射
            professionIdSkillMap.computeIfAbsent(skillConfig.getProfessionId(), k -> new ArrayList<>());
            List<SkillConfig> skillConfigList = professionIdSkillMap.get(skillConfig.getProfessionId());
            skillConfigList.add(skillConfig);

            //构建玩家召唤兽技能
            if(skillConfig.getType() == SkillType.PLAYER_CALL){
                playerCallskillList.add(skillConfig);
            }
        }
    }

    /**
     * 获取技能配置信息
     * @return
     */
    public JSONArray getSkillConfigInfo(){
        return skillConfigFile.getJsonArray();
    }

    public SkillConfig getSkillConfig(int skillId) {
        return SKILL_ID_MAP.get(skillId);
    }

    /**
     * boss技能
     * @param bossId boss标识
     * @return
     */
    public List<SkillConfig> getBossSkillConfigList(long bossId){
        return bossIdSkillMap.get(bossId);
    }

    /**
     * monster技能
     * @param monsterId monster标识
     * @return
     */
    public List<SkillConfig> getMonsterSkillConfigList(long monsterId){
        return monsterIdSkillMap.get(monsterId);
    }


    /**
     * 职业技能
     * @param profession
     * @return
     */
    public List<SkillConfig> getProfessionSkillConfig(long profession) {
        return professionIdSkillMap.get(profession);
    }

    public List<SkillConfig> getPlayerCallSkillConfig() {
        return playerCallskillList;
    }
}
