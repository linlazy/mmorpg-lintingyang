package com.linlazy.mmorpg.utils;

import com.linlazy.mmorpg.module.item.type.ItemType;
import com.linlazy.mmorpg.module.player.constants.ProfessionType;
import com.linlazy.mmorpg.module.task.condition.start.StartConditionType;
import com.linlazy.mmorpg.module.task.constants.TaskStatus;
import com.linlazy.mmorpg.module.task.domain.TriggerCondition;

/**
 * @author linlazy
 */
public class OutputUitls {

    public static String profession(int professionId){
        StringBuilder stringBuilder = new StringBuilder();

        switch (professionId){
            case ProfessionType.warrior:
                stringBuilder.append(String.format("职业【战士】 \n"));
                break;
            case ProfessionType.minister:
                stringBuilder.append(String.format("职业【牧师】\n "));
                break;
            case ProfessionType.master:
                stringBuilder.append(String.format("职业【法师】\n "));
                break;
            case ProfessionType.summoner:
                stringBuilder.append(String.format("职业【召唤师】\n "));
                break;
            default:
        }
        return stringBuilder.toString();
    }

    public static String itemType(int itemType){
        StringBuilder stringBuilder = new StringBuilder();

        switch (itemType){
            case ItemType.CONSUME:
                stringBuilder.append(String.format("道具类型【消耗道具】 \n"));
                break;
            case ItemType.ORDINARY:
                stringBuilder.append(String.format("道具类型【普通道具】 \n"));
                break;
            case ItemType.EQUIP:
                stringBuilder.append(String.format("道具类型【装备道具】 \n"));
                break;
            case ItemType.SKILL:
                stringBuilder.append(String.format("道具类型【技能道具】 \n"));
                break;
            default:
        }
        return stringBuilder.toString();
    }

    public static String startCondition(TriggerCondition triggerCondition){
        StringBuilder stringBuilder = new StringBuilder();

        switch (triggerCondition.getTriggerType()){
            case StartConditionType
                    .TIME:
                stringBuilder.append(String.format("开启条件【】 \n"));
                break;
            case StartConditionType.COMPLETE_TASK:
                stringBuilder.append(String.format("道具类型【普通道具】 \n"));
                break;
            default:
        }
        return stringBuilder.toString();
    }

    public static String taskStatus(int status){
        StringBuilder stringBuilder = new StringBuilder();

        switch (status){
            case TaskStatus.UN_START:
                stringBuilder.append(String.format("任务状态【未开启】"));
                break;
            case TaskStatus.START_UN_ACCEPT:
                stringBuilder.append(String.format("任务状态【已开启未接受】"));
                break;
            case TaskStatus.ACCEPT_UN_COMPLETE:
                stringBuilder.append(String.format("任务状态【已接受未完成】"));
                break;
            case TaskStatus.ACCEPT_ABLE_COMPLETE:
                stringBuilder.append(String.format("任务状态【已接受可完成】"));
                break;
            case TaskStatus.COMPLETED:
                stringBuilder.append(String.format("任务状态【已完成】"));
                break;

            default:
        }
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }
}
