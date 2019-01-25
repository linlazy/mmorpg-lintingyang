package com.linlazy.mmorpg.module.guild.dto;

import com.linlazy.mmorpg.module.backpack.dto.LatticeDTO;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(LatticeDTO latticeDTO: backPackLatticeList){
            stringBuilder.append(latticeDTO.toString()).append("\r\n");
        }

        return stringBuilder.toString();
    }
}
