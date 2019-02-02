package com.linlazy.mmorpg.module.player.domain;

import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.dto.TaskDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家任务领域类
 * @author linlazy
 */
@Data
public class PlayerTask {


    private Map<Long, Task> map = new HashMap<>();

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        map.values().forEach(task -> {

            stringBuilder.append( new TaskDTO(task).toString()).append("\r\n");
        });
        return stringBuilder.toString();
    }
}
