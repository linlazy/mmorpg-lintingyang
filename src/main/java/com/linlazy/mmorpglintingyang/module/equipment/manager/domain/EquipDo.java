package com.linlazy.mmorpglintingyang.module.equipment.manager.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * 是否装备
     */
    private boolean dressed;

    /**
     * 装备类型
     */
    private int type;

    /**
     * 是否损坏
     */
    private boolean bad;

    /**
     * 耐久度
     */
    private int durability;
    /**
     * 耐久度上限
     */
    private int durabilityUp;


    /**
     * 最终加成（物理攻击，魔法攻击,物理防御，魔法防御）
     */
    List<Addition> additionList = new ArrayList<>();

    public EquipDo() {
    }

    public EquipDo(Item item) {
        this.actorId = item.getActorId();
        this.equipId = item.getItemId();
        String ext = item.getExt();
        if(!StringUtils.isEmpty(ext)){
            JSONObject jsonObject = JSONObject.parseObject(ext);
            //耐久度
            this.durability = jsonObject.getIntValue("durability");
            //加成
            String additions = jsonObject.getString("additions");
            if(!StringUtils.isEmpty(additions)){
                this.additionList = JSONObject.parseObject(additions,new TypeReference<ArrayList<Addition>>(){});
            }
        }
    }


    @Autowired
    private ItemConfigService itemConfigService;
    /**
     * 初始化配置数据
     */
    @PostConstruct
    public void initConfig(){
        int baseItemId = ItemIdUtil.getBaseItemId(this.equipId);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
    }



    public Item convertItem(){
        Item item = new Item();
        item.setItemId(this.equipId);
        item.setActorId(this.actorId);
        item.setCount(1);

        JSONObject jsonObject = new JSONObject();
        //耐久度
        jsonObject.put("durability",this.durability);
        //加成
        String additions = JSONObject.toJSONString(this.additionList);
        jsonObject.put("additions",additions);
        //状态
        jsonObject.put("dressed",this.dressed);
        String ext = JSONObject.toJSONString(jsonObject);
        item.setExt(ext);
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipDo equipDo = (EquipDo) o;
        return equipId == equipDo.equipId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipId);
    }
}
