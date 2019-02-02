package com.linlazy.mmorpg.module.task.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class TriggerCondition {

    /**
     * 触发类型
     */
    private int triggerType;

    /**
     * 触发参数
     */
    private JSONObject triggerArgs;


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();




        return stringBuilder.toString();
    }
}
