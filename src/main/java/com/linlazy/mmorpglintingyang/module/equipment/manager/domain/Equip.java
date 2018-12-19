package com.linlazy.mmorpglintingyang.module.equipment.manager.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class Equip{

    /**
     * 所属玩家
     */
    private long actorId;
    /**
     * 装备ID
     */
    private long equipId;
    /**
     * 装备状态
     */
    private int status;

    /**
     * 最终加成（物理攻击，魔法攻击,物理防御，魔法防御）
     */
    List<Addition> additionList = new ArrayList<>();

    /**
     * 耐久度
     */
    private int durability;

    public Equip() {
    }

    public Equip(Item item) {
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
        jsonObject.put("status",this.status);
        String ext = JSONObject.toJSONString(jsonObject);
        item.setExt(ext);
        return item;
    }

    public int modifyDurability(int durability) {
        this.durability +=durability;
        if(this.durability <0){
            this.durability = 0;
        }
        return this.durability;
    }
}
