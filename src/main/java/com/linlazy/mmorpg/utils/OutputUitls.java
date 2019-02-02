package com.linlazy.mmorpg.utils;

import com.linlazy.mmorpg.module.item.type.ItemType;
import com.linlazy.mmorpg.module.player.constants.ProfessionType;

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
}
