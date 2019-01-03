package com.linlazy.mmorpglintingyang.module.backpack.dto;

import lombok.Data;

import java.util.List;

@Data
public class BackPackDTO {
    /**
     * 背包类型
     */
    private int type;
    /**
     * 背包格子
     */
    private List<BackPackLatticeDTO> backPackLatticeList;


    public BackPackDTO(int type) {
        this.type = type;
    }
}
