package com.linlazy.mmorpg.module.item.dto;

import java.util.List;

/**
 * @author linlazy
 */
public class ItemInfoDTO {

    private List<ItemConfigDTO> itemConfigDTOS;

    public ItemInfoDTO(List<ItemConfigDTO> itemConfigDTOS) {
        this.itemConfigDTOS = itemConfigDTOS;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(ItemConfigDTO itemConfigDTO: itemConfigDTOS){
            stringBuilder.append(itemConfigDTO.toString()).append("\r\n");
        }
        return stringBuilder.toString();
    }
}
