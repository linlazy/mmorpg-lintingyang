package com.linlazy.mmorpglintingyang.module.item.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.item.handler.dto.BackpackCellDTO;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.domain.BackpackCell;
import com.linlazy.mmorpglintingyang.module.item.service.additem.AbstractAddItem;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemService {

    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemManager itemManager;

    /**
     * 使用道具
     * @param actorId
     * @param itemId
     * @param jsonObject
     * @return
     */
    public Result<?> useItem(long actorId,long itemId,JSONObject jsonObject) {
        int baseItemId = ItemIdUtil.getBaseItemId(itemId);
        int consumeNum = jsonObject.getIntValue("num");

        long count = itemManager.getSuperPositionTotal(actorId, baseItemId);
        if(count <consumeNum){
            return Result.valueOf("道具不足");
        }
        //扣除消耗
        List<BackpackCell> backpackCells = itemManager.consumeBackPackItem(actorId, ItemIdUtil.getBaseItemId(itemId), consumeNum);
        List<BackpackCellDTO> backpackCellDTOList = backpackCells.stream()
                .map(BackpackCellDTO::new)
                .collect(Collectors.toList());
        //获取使用道具获得的奖励列表
        List<Reward> rewardList = itemManager.getRewardList(baseItemId);
        //发奖励
        rewardService.addRewardList(actorId,rewardList);
        return Result.success(backpackCellDTOList);
    }

    /**
     * 获取玩家背包信息
     * @param actorId
     * @return
     */
    public Result<?> getActorItemInfo(long actorId) {

        Set<BackpackCell> actorBackPack = itemManager.getActorBackPack(actorId);

        List<BackpackCellDTO> backpackCellDTOList = actorBackPack.stream()
                .sorted(Comparator.comparing(BackpackCell::getBackPackIndex))
                .map(BackpackCellDTO::new)
                .collect(Collectors.toList());

        return Result.success(backpackCellDTOList);
    }

    /**
     * 增加道具
     * @param actorId
     * @param baseItemId
     * @param num
     * @return 返回增加道具后，背包变化的信息
     */
    public Result<?> addItem(long actorId, int baseItemId, int num) {
        //是否背包已满
        if(itemManager.isFullBackPack(actorId,baseItemId,num)){
            return Result.valueOf("背包已满");
        }

        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        int itemType =  itemConfig.getIntValue("itemType");
        AbstractAddItem abstractAddItem = AbstractAddItem.getAbstractItemType(itemType);

        return Result.success(abstractAddItem.addItem(actorId,baseItemId,num));
    }

    /**
     * 整理背包
     * @param actorId
     * @return
     */
    public Result<?> arrangeBackPack(long actorId) {

        List<BackpackCell> arrangeBackPack = itemManager.arrangeBackPack(actorId);

        List<BackpackCellDTO> backpackCellDTOList = arrangeBackPack.stream()
                .map(BackpackCellDTO::new)
                .collect(Collectors.toList());
        return Result.success(backpackCellDTOList);
    }


    /**
     * 消耗背包道具
     * @param actorId
     * @param itemId
     * @param consumeNum
     */
    public Result<List<BackpackCellDTO>> consumeBackPackItem(long actorId,long itemId,int consumeNum) {
        int baseItemId = ItemIdUtil.getBaseItemId(itemId);
        return Result.success(itemManager.consumeBackPackItem(actorId,baseItemId,consumeNum).stream()
                .map(BackpackCellDTO::new)
                .collect(Collectors.toList()));
    }
}
