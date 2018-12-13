package com.linlazy.mmorpglintingyang.module.item.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ItemManager {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ItemConfigService itemConfigService;





    /**
     * 获取背包信息
     * @param actorId
     * @return
     */
    public Set<Item> getItemInfo(long actorId){
        return itemDao.getItemSet(actorId);
    }

    /**
     * 是否背包已满
     * @param actorId
     * @param itemId
     * @param num
     * @return
     */
    public boolean isFullPackage(long actorId, int itemId, int num) {
        Item item = itemDao.getItem(actorId, itemId);
        JSONObject itemConfig = itemConfigService.getItemConfig(itemId);
        //道具不存在或已到达叠加数
        if(item == null || item.getCount() >= itemConfig.getIntValue("superPosition")){
            //已使用格子数
            int latticeNum = 0;
            int maxLatticeNum = globalConfigService.getPackageMaxLatticeNum();
            if(latticeNum >= maxLatticeNum){
                return true;
            }
        }
        return false;
    }

    public void arrangeBackPack(long actorId) {

    }

    public void addItem(long actorId,long itemId, int num) {
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
        int superPosition = itemConfig.getIntValue("superPosition");
//        //不可叠加
//        if(superPosition <= 1){
//
//            int pushNum =0;
//            //遍历数组，找空位置
//            for(int index = 0 ; index < items.length && pushNum <num; index++){
//                if(items[index] == null){
//                    // todo 自增序号
//                    int order = 0;
//                    long newItemId =  ItemIdUtil.getNewItemId(order++ ,index,ItemIdUtil.getBaseItemId(itemId));
//                    items[index] = new Item(actorId,newItemId,1);
//                    pushNum ++;
//                }
//            }
//
//            //可叠加
//        }else {
//
//        }
    }
}
