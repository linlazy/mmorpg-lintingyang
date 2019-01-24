package com.linlazy.mmorpg.entity;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

/**
 * 道具实体类
 * @author linlazy
 */
@Data
@Table("item")
public class ItemEntity extends Entity {


    @Cloumn(pk = true)
    private long itemId;
    @Cloumn(pk = true)
    private long actorId;

    @Cloumn
    private int count;


    @Cloumn
    private String ext;

    /**
     * 道具类型
     */
    private int itemType;

    /**
     * 可折叠
     */
    private boolean superPosition;

    /**
     * 折叠上限
     */
    private int superPositionUp;

    /**
     * 名称
     */
    private String name;

    @Override
    public void afterReadDB() {
        ItemConfigService bean = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = bean.getItemConfig(ItemIdUtil.getBaseItemId(this.itemId));
        superPositionUp = itemConfig.getIntValue("superPositionUp");
        if(superPositionUp == 0){
            superPosition =false;
        }else {
            superPosition =true;
        }

        itemType = itemConfig.getIntValue("itemType");
        name = itemConfig.getString("name");
    }

    @Override
    public void beforeWriteDB() {
    }
}
