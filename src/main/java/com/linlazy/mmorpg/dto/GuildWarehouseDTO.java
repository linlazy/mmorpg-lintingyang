package com.linlazy.mmorpg.dto;

import lombok.Data;

import java.util.List;

/**
 * 公会仓库DTO
 * @author linlazy
 */
@Data
public class GuildWarehouseDTO {

    /**
     * 公会
     */
    private final Long guildId;

    /**
     * 背包格子
     */
    private  List<LatticeDTO> backPackLatticeList;

    public GuildWarehouseDTO(Long guildId) {
        this.guildId = guildId;
    }
}
