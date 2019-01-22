package com.linlazy.mmorpg.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.event.type.EquipDamageEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 装备领域类
 * @author linlazy
 */
@Data
public class Equip extends Item{


    private int equipType;

    /**
     * 等级
     */
    private int level;

    /**
     * 耐久度
     */
    private int durability;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 防御力
     */
    private int defense;

    /**
     * 装备
     */
    private boolean isDress;


    public Equip(ItemEntity entity) {
        super(entity);
        String ext = entity.getExt();
        if(!StringUtils.isEmpty(ext)){
            JSONObject jsonObject = JSON.parseObject(ext);
            durability = jsonObject.getIntValue("durability");
            level = jsonObject.getIntValue("level");
            isDress = jsonObject.getBooleanValue("dress");
        }

        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(getItemId()));
        equipType = itemConfig.getIntValue("equipType");
    }

    public Equip(long itemId, int count) {
        super(itemId, count);

        initConfig(ItemIdUtil.getBaseItemId(itemId));
    }

    @Override
    public ItemEntity convertItemEntity() {
        String ext = this.getExt();
        JSONObject jsonObject = null;
        if(!StringUtils.isEmpty(ext)){
            jsonObject = JSON.parseObject(ext);
        }else {
            jsonObject = new JSONObject();
        }
        jsonObject.put("durability",durability);
        jsonObject.put("level",level);
        jsonObject.put("dress",isDress);
        this.setExt(JSON.toJSONString(jsonObject));
        return super.convertItemEntity();
    }

    public void modifyDurability() {
        durability--;
        if(durability <= 0){
            durability = 0;
            EventBusHolder.post(new EquipDamageEvent(this));
        }
    }


    @Override
    protected void initConfig(int baseItemId) {
        super.initConfig(baseItemId);
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        this.durability = itemConfig.getIntValue("durability");
        this.level = itemConfig.getIntValue("level");
        this.setItemType(itemConfig.getIntValue("itemType"));
        this.equipType =itemConfig.getIntValue("equipType");
    }
}
