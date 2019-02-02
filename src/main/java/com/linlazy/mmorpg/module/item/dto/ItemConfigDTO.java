package com.linlazy.mmorpg.module.item.dto;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.equip.constants.EquipType;
import com.linlazy.mmorpg.module.item.type.ItemType;
import com.linlazy.mmorpg.utils.OutputUitls;
import lombok.Data;

import java.util.List;

/**
 * @author linlazy
 */
@Data
public class ItemConfigDTO {

    private JSONObject itemConfig;

    public ItemConfigDTO(JSONObject itemConfig) {
        this.itemConfig = itemConfig;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("道具ID【%d】\n",itemConfig.getIntValue("itemId")));
        String itemType = OutputUitls.itemType(itemConfig.getIntValue("itemType"));
        stringBuilder.append(itemType);
        stringBuilder.append(String.format("道具名称【%s】\n",itemConfig.getString("name")));
        stringBuilder.append(String.format("描述【%s】\n",itemConfig.getString("desc")));

        Integer superPosition = itemConfig.getInteger("superPosition");
        if(superPosition == null){
            stringBuilder.append(String.format("叠加上限【%d】\n",1));
        }else {
            stringBuilder.append(String.format("叠加上限【%d】\n",superPosition));
        }

        JSONObject ext = itemConfig.getJSONObject("ext");

        if(itemConfig.getIntValue("itemType") == ItemType.EQUIP){
            int equipType = ext.getIntValue("equipType");
            switch (equipType){
                case EquipType
                        .ARMS:
                    stringBuilder.append(String.format("装备类型【武器】\n "));

                    stringBuilder.append(String.format("攻击力【%d】 \n",ext.getIntValue("attack")));
                    break;
                case EquipType
                        .HELMET:
                    stringBuilder.append(String.format("装备类型【头盔】\n "));
                    stringBuilder.append(String.format("攻击防御力力【%d】 \n",ext.getIntValue("defense")));
                    break;
                case EquipType
                        .UP_CLOTHES:
                    stringBuilder.append(String.format("装备类型【上衣】 \n"));
                    stringBuilder.append(String.format("攻击防御力力【%d】\n ",ext.getIntValue("defense")));
                    break;
                case EquipType
                        .HAND_GUARD:
                    stringBuilder.append(String.format("装备类型【护手】\n "));
                    stringBuilder.append(String.format("攻击防御力力【%d】\n ",ext.getIntValue("defense")));
                    break;
                case EquipType
                        .DOWN_CLOTHES:
                    stringBuilder.append(String.format("装备类型【下衣】\n "));
                    stringBuilder.append(String.format("攻击防御力力【%d】 \n",ext.getIntValue("defense")));
                    break;
                case EquipType
                        .SHOES:
                    stringBuilder.append(String.format("装备类型【鞋子】\n "));
                    stringBuilder.append(String.format("攻击防御力力【%d】\n ",ext.getIntValue("defense")));
                    break;
                default:
            }

            List<Integer> professionIds = ext.getJSONArray("professionIds").toJavaList(Integer.class);
            for(Integer professionId: professionIds){
                String profession = OutputUitls.profession(professionId);
                stringBuilder.append(profession);
            }

            int level = ext.getIntValue("level");
            stringBuilder.append(String.format("最低等级【%d】 \n",level));

            int durability = ext.getIntValue("durability");
            stringBuilder.append(String.format("最大耐久度【%d】\n ",durability));

        }

        if(itemConfig.getIntValue("itemType") ==ItemType.SKILL ){
            int professionId = ext.getIntValue("professionId");
            String profession = OutputUitls.profession(professionId);
            stringBuilder.append(profession);

            int level = ext.getIntValue("level");
            stringBuilder.append(String.format("最低等级【%d】\n",level));

        }

        return stringBuilder.toString();
    }
}
