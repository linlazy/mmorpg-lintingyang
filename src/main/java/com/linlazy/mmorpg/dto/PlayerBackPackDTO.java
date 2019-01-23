package com.linlazy.mmorpg.dto;

import lombok.Data;

import java.util.List;

/**
 * @author linlazy
 */
@Data
public class PlayerBackPackDTO {

    private Long actorId;

    /**
     * 背包格子
     */
    private List<LatticeDTO> backPackLatticeList;

    public PlayerBackPackDTO(Long actorId) {
        this.actorId = actorId;
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for(LatticeDTO latticeDTO: backPackLatticeList){
            stringBuilder.append(latticeDTO.toString()).append("\r\n");
        }

        return stringBuilder.toString();
    }
}
