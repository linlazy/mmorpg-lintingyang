package com.linlazy.mmorpg.dto;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.ItemType;
import com.linlazy.mmorpg.domain.Equip;
import com.linlazy.mmorpg.domain.Item;
import com.linlazy.mmorpg.domain.Lattice;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class LatticeDTO {

    private final Long itemId;
    private final Integer baseItemId;
    private final Integer backpackIndex;
    private final Integer count;

    private final Item item;

    public LatticeDTO(Lattice lattice) {
        this.backpackIndex = lattice.getIndex();
        this.itemId = lattice.getItem().getItemId();
        this.baseItemId = ItemIdUtil.getBaseItemId(this.itemId);
        this.count = lattice.getItem().getCount();
        this.item = lattice.getItem();
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
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
        String desc = itemConfig.getString("desc");
        if(desc != null){
            stringBuilder.append(String.format("描述【%s】",desc));
        }


        if(item.getItemType() == ItemType.EQUIP){
            Equip equip = (Equip) item;
            stringBuilder.append(String.format("格子编号【%d】 道具配置ID【%d】 道具数量【%d】",backpackIndex,baseItemId,count));
            stringBuilder.append(new EquipDTO(equip).toString());
        }else {
            stringBuilder.append(String.format("格子编号【%d】 道具配置ID【%d】 道具ID【%d】 道具数量【%d】道具名称【%s】",backpackIndex,baseItemId,itemId,count,item.getName()));
        }

        return stringBuilder.toString();
    }
}
