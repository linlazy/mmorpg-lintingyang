package com.linlazy.mmorpg.module.equip.dto;

import com.linlazy.mmorpg.module.equip.domain.DressedEquip;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
public class DressedEquipDTO {

    private Collection<EquipDTO> equipDTOS;


    public DressedEquipDTO(DressedEquip dressedEquip) {
        this.equipDTOS = dressedEquip.getEquipMap().values().stream()
                .map(EquipDTO::new).collect(Collectors.toSet());
    }


    @Override
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();

      for(EquipDTO equipDTO: equipDTOS){
          stringBuilder.append(equipDTO.toString()).append("\r\n");
      }
      return stringBuilder.toString();
    }
}
