package com.linlazy.mmorpglintingyang.module.item.manager.backpack.response;

import lombok.Data;

import java.util.List;

/**
 * 背包信息变化
 */
@Data
public class BackPackInfo {
    List<BackPackLatticeDTO> backPackLatticeDTOS;
}
