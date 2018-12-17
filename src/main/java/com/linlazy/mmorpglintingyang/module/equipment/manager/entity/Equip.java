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
public class Equip{

    private Item item;

    private long equipId;

    public Equip(Item item) {
        this.item = item;
        this.equipId = item.getItemId();
    }

    /**
     * 装备状态
     */
    private int status;

    /**
     * 加成
     */
    List<Addition> additionList = new ArrayList<>();
    public int getStatus() {
        JSONObject jsonObject = JSON.parseObject(this.item.getExt());
        if(jsonObject != null){
            this.status = jsonObject.getIntValue("status");
        }
        return this.status;
    }

    public void setStatus(int status) {

        JSONObject jsonObject = JSON.parseObject(item.getExt());
        if(jsonObject == null){
            jsonObject = new JSONObject();
        }
        jsonObject.put("status",status);
        item.setExt(JSONObject.toJSONString(jsonObject));

        this.status = status;
    }

    public  List<Addition> getAdditionList() {
        JSONObject jsonObject = JSON.parseObject(item.getExt());
        if(jsonObject != null){
            JSONObject additionsJsonObject = jsonObject.getObject("additions", JSONObject.class);
            if(additionsJsonObject != null){
                String additions = JSONObject.toJSONString(additionsJsonObject);
                if(!StringUtils.isEmpty(additions)){
                    additionList = JSONObject.parseObject(additions,new TypeReference<ArrayList<Addition>>(){});
                }
            }
        }
        return additionList;
    }

    public long getEquipId() {
        return equipId;
    }

    public void setEquipId(long equipId) {
        this.equipId = equipId;
    }
}
