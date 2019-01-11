package com.linlazy.mmorpg.module.equip.manager.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author linlazy
 */
@Data
public class EquipDo {

    /**
     * 所属玩家
     */
    private long actorId;
    /**
     * 装备ID
     */
    private long equipId;
    /**
     *
     */
    private int baseItemId;
    /**
     * 是否装备
     */
    private boolean dressed;

    /**
     * 耐久度
     */
    private int durability;

    /**
     * 等级
     */
    private int level;

    /**
     * 装备类型
     */
    private int type;


    /**
     * 耐久度上限
     */
    private int durabilityUp;

    /**
     * 等级上限
     */
    private int levelUp;

    /**
     * 物理攻击
     */
    private int physicalAttack;
    /**
     * 魔法攻击
     */
    private int magicAttack;

    /**
     * 物理防御
     */
    private int physicalDefense;
    /**
     * 魔法防御
     */
    private int magicDefense;



    public EquipDo(int baseItemId) {
        this.baseItemId =baseItemId;
        initConfig(baseItemId);
    }
    public EquipDo(long equipId) {
        this.equipId =equipId;
        this.baseItemId = ItemIdUtil.getBaseItemId(equipId);
        initConfig(baseItemId);
    }

    public EquipDo(ItemDo item) {
        this.actorId = item.getActorId();
        this.equipId = item.getItemId();
        this.baseItemId = item.getBaseItemId();
        String ext = item.getExt();
        if(!StringUtils.isEmpty(ext)){
            JSONObject jsonObject = JSONObject.parseObject(ext);
            this.durability = jsonObject.getIntValue("durability");
            this.level = jsonObject.getIntValue("level");
            this.dressed =jsonObject.getBooleanValue("dressed");
        }
        this.baseItemId = ItemIdUtil.getBaseItemId(equipId);
        initConfig(baseItemId);
    }

    /**
     * 初始化配置数据
     */
    public void initConfig(int baseItemId){
        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        this.durabilityUp = itemConfig.getIntValue("durabilityUp");
        this.levelUp = itemConfig.getIntValue("levelUp");
        this.physicalAttack = itemConfig.getIntValue("physicalAttack");
        this.magicAttack = itemConfig.getIntValue("magicAttack");
        this.magicDefense = itemConfig.getIntValue("magicDefense");
        this.physicalDefense = itemConfig.getIntValue("physicalDefense");
    }

    public void setEquipId(long equipId) {
        this.equipId = equipId;
        int baseItemId = ItemIdUtil.getBaseItemId(equipId);
        initConfig(baseItemId);
    }

    public ItemDo convertItemDo(){
        ItemDo itemDo = new ItemDo(this.equipId);
        itemDo.setActorId(this.actorId);
        itemDo.setCount(1);

        JSONObject jsonObject = new JSONObject();
        //耐久度
        jsonObject.put("durability",this.durability);
        //是否已穿戴
        jsonObject.put("dressed",this.dressed);
        //等级
        jsonObject.put("level",this.level);

        String ext = JSONObject.toJSONString(jsonObject);
        itemDo.setExt(ext);
        return itemDo;
    }

}
