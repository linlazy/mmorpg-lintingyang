package com.linlazy.mmorpg.file.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import com.linlazy.mmorpg.utils.RewardUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author linlazy
 */
@Component
public class TaskConfigService {

    private static ConfigFile taskConfigFile;

    static {
        taskConfigFile =  ConfigFileManager.use("config_file/task_config.json");
    }

    private static Map<Long, TaskConfig> map = new HashMap<>();


    @PostConstruct
    public void init(){
        JSONArray jsonArray = taskConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            long taskId = jsonObject.getLongValue("taskId");
            String desc = jsonObject.getString("desc");
            int taskTemplateId = jsonObject.getIntValue("taskTemplateId");
            JSONObject taskTemplateArgs = jsonObject.getJSONObject("taskTemplateArgs");
            String rewards = jsonObject.getString("rewards");
            JSONArray startCondition = jsonObject.getJSONArray("startCondition");
            JSONArray acceptCondition = jsonObject.getJSONArray("acceptCondition");
            String taskName = jsonObject.getString("taskName");


            TaskConfig taskConfig = new TaskConfig();
            taskConfig.setTaskName(taskName);
            taskConfig.setDesc(desc);
            if(StringUtils.isEmpty(rewards)){
                taskConfig.setRewardList(Lists.newArrayList());
            }else {
                List<Reward> rewardList = RewardUtils.parseRewards(rewards);
                taskConfig.setRewardList(rewardList);
            }


            if(Objects.nonNull(startCondition)){
                Map<Integer, TriggerCondition> startConditionMap = taskConfig.getStartConditionMap();
                for(int j = 0 ; j < startCondition.size(); j ++){
                    JSONObject jsonObject1 = startCondition.getJSONObject(j);
                    int triggerType = jsonObject1.getIntValue("triggerType");
                    JSONObject triggerArgs = jsonObject1.getJSONObject("triggerArgs");

                    TriggerCondition triggerCondition = new TriggerCondition();
                    triggerCondition.setTriggerType(triggerType);
                    triggerCondition.setTriggerArgs(triggerArgs);

                    startConditionMap.put(triggerCondition.getTriggerType(),triggerCondition);
                }
            }

            if(Objects.nonNull(acceptCondition)){
                Map<Integer, TriggerCondition> acceptConditionMap = taskConfig.getAcceptConditionMap();
                for(int j = 0 ; j < acceptCondition.size(); j ++){
                    JSONObject jsonObject1 = acceptCondition.getJSONObject(j);
                    int triggerType = jsonObject1.getIntValue("triggerType");
                    JSONObject triggerArgs = jsonObject1.getJSONObject("triggerArgs");

                    TriggerCondition triggerCondition = new TriggerCondition();
                    triggerCondition.setTriggerType(triggerType);
                    triggerCondition.setTriggerArgs(triggerArgs);

                    acceptConditionMap.put(triggerCondition.getTriggerType(),triggerCondition);
                }
            }



            taskConfig.setTaskTemplateArgs(taskTemplateArgs);
            taskConfig.setTaskTemplateId(taskTemplateId);
            taskConfig.setTaskId(taskId);

            map.put(taskConfig.getTaskId(),taskConfig);

        }
    }

    public Collection<TaskConfig> getAllTaskConfig() {
        return map.values();
    }
}
