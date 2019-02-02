package com.linlazy.mmorpg.module.item.dto;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.module.equip.dto.EquipDTO;
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


        if(item.isSuperPosition()){
            stringBuilder.append("叠加属性【可叠加】");
            stringBuilder.append(String.format("叠加上限【%d】",item.getSuperPositionUp()));
        }else {
            stringBuilder.append("叠加属性【不可叠加】");
        }


        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(item.getItemId()));
        String desc = itemConfig.getString("desc");
        if(desc != null){
            stringBuilder.append(String.format("描述【%s】",desc));
        }


        if(item.getItemType() == ItemType.EQUIP){
            stringBuilder.append(String.format(" 道具配置ID【%d】 道具数量【%d】",ItemIdUtil.getBaseItemId(item.getItemId()),item.getCount()));
            stringBuilder.append(new EquipDTO(item).toString());
        }else {
            stringBuilder.append(String.format(" 道具配置ID【%d】 道具ID【%d】 道具数量【%d】道具名称【%s】",ItemIdUtil.getBaseItemId(item.getItemId()),item.getItemId(),item.getCount(),item.getName()));
        }
        return stringBuilder.toString();
    }
}
