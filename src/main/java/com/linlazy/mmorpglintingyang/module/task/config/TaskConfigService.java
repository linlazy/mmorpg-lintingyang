package com.linlazy.mmorpglintingyang.module.task.config;

import com.linlazy.mmorpglintingyang.module.task.domain.TaskDo;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskConfigService {

    private static ConfigFile taskConfigFile;

    private static ConfigFile taskTemplateConfigFile;

    static {
        taskConfigFile =  ConfigFileManager.use("config_file/task_config.json");
        taskTemplateConfigFile =  ConfigFileManager.use("config_file/task_template_config.json");
    }

    public Set<TaskDo> getAllTaskDo() {
        return null;
    }
}
