package com.linlazy.mmorpglintingyang.module.equipment.manager.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class Equip extends Item {

    /**
     * 装备状态
     */
    private int status;

    /**
     * 加成
     */
    List<Addition> additionList = new ArrayList<>();

    public Equip(long actorId, long itemId, int count) {
        super(actorId, itemId, count);
    }

    public Equip(long actorId, long itemId, int count, String ext) {
        super(actorId, itemId, count, ext);
    }

    public int getStatus() {
        JSONObject jsonObject = JSON.parseObject(this.ext);
        this.status = jsonObject.getIntValue("status");
        return this.status;
    }

    public void setStatus(int status) {

        JSONObject jsonObject = JSON.parseObject(this.ext);
        jsonObject.put("status",status);
        this.ext = JSONObject.toJSONString(jsonObject);

        this.status = status;
    }

    public  List<Addition> getAdditionList() {
        JSONObject jsonObject = JSON.parseObject(this.ext);
        JSONObject additionsJsonObject = jsonObject.getObject("additions", JSONObject.class);
        String additions = JSONObject.toJSONString(additionsJsonObject);
        if(!StringUtils.isEmpty(additions)){
            additionList = JSONObject.parseObject(additions,new TypeReference<ArrayList<Addition>>(){});
        }
        return additionList;
    }

}
