package com.linlazy.mmorpglintingyang.module.item.service.additem;

import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.item.handler.dto.BackpackCellDTO;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.domain.BackpackCell;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public  class AddItemType1 extends AbstractAddItem {

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private RewardService rewardService;

    @Override
    protected int itemType() {
        return 1;
    }


    @Override
    public Result<?> addItem(long actorId, int baseItemId,int num) {
        List<BackpackCell> backpackCells = itemManager.addItem(actorId, baseItemId, num).getData();
        List<BackpackCellDTO> backpackCellDTOList = backpackCells.stream()
                .map(BackpackCellDTO::new)
                .collect(Collectors.toList());
        return Result.success(backpackCellDTOList);
    }
}
