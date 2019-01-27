package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家任务领域类
 * @author linlazy
 */
@Data
public class PlayerTask {


    private Map<Long,Task> map = new HashMap<>();

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        map.values().forEach(task -> {
            stringBuilder.append(task.toString()).append("\r\n");
        });
        return stringBuilder.toString();
    }
}
