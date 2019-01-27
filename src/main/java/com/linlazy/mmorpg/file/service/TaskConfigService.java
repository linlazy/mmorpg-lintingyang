package com.linlazy.mmorpg.file.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.file.config.TaskConfig;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.server.common.ConfigFile;
import com.linlazy.mmorpg.server.common.ConfigFileManager;
import com.linlazy.mmorpg.utils.RewardUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            int taskTemplateId = jsonObject.getIntValue("taskTemplateId");
            JSONObject taskTemplateArgs = jsonObject.getJSONObject("taskTemplateArgs");
            String beginTime = jsonObject.getString("beginTime");
            String endTime = jsonObject.getString("endTime");
            String rewards = jsonObject.getString("rewards");


            TaskConfig taskConfig = new TaskConfig();
            if(StringUtils.isEmpty(rewards)){
                taskConfig.setRewardList(Lists.newArrayList());
            }else {
                List<Reward> rewardList = RewardUtils.parseRewards(rewards);
                taskConfig.setRewardList(rewardList);
            }

            DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            taskConfig.setBeginTime( LocalDateTime.parse(beginTime,dateTimeFormatter));
            taskConfig.setEndTime(LocalDateTime.parse(endTime,dateTimeFormatter));

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
