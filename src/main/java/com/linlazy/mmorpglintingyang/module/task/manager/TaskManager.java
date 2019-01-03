package com.linlazy.mmorpglintingyang.module.task.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.task.config.TaskConfigService;
import com.linlazy.mmorpglintingyang.module.task.dao.TaskDao;
import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import com.linlazy.mmorpglintingyang.module.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class TaskManager {

    @Autowired
    private TaskConfigService taskConfigService;
    @Autowired
    TaskDao taskDao;

    public Set<TaskDo> getActorAllTaskDo(long actorId){
        Set<JSONObject> allTaskConfig = taskConfigService.getAllTaskConfig();
        return allTaskConfig.stream()
                .map(taskConfig-> {
                    Task task = taskDao.getTask(actorId,taskConfig.getIntValue("taskId"));
                    if(task == null){
                        task = new Task();
                        task.setTaskId(taskConfig.getIntValue("taskId"));
                        task.setActorId(actorId);
                    }
                    return new TaskDo(taskConfig,task);
                }).collect(Collectors.toSet());
    }


    public void update(Task convertTask) {
        taskDao.update(convertTask);
    }
}
