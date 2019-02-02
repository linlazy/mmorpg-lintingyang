package com.linlazy.mmorpg.module.item.dto;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.type.ItemType;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;

/**
 * @author linlazy
 */
public class ItemDTO {

    private Item item;

    public ItemDTO(Item item) {
        this.item = item;
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        if(item.getId() != 0){
            stringBuilder.append(String.format("道具标识【%d】",item.getId()));
        }
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(item.getItemId()));
        ItemConfigDTO itemConfigDTO = new ItemConfigDTO(itemConfig);
        stringBuilder.append(String.format(itemConfigDTO.toString()));

        JSONObject ext = item.getExt();

        if(item.getItemType() == ItemType.EQUIP){
            int durability = ext.getIntValue("durability");
            stringBuilder.append(String.format("耐久度【%d】\n",durability));

            int level = ext.getIntValue("level");
            stringBuilder.append(String.format("装备等级【%d】\n",level));
        }



        return stringBuilder.toString();
    }
}
