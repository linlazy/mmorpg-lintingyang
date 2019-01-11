package com.linlazy.mmorpg.module.backpack.dto;

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
}
