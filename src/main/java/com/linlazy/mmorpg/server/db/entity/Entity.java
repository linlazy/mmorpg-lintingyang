package com.linlazy.mmorpg.server.db.entity;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linlazy
 */
@Data
public abstract class Entity {

    /**
     * 操作类型
     */
    private int operatorType;


    private List<KeyValueEntry<String,Object>> pkKeyValue = new ArrayList<>();
    private List<KeyValueEntry<String,Object>> ordinaryKeyValue = new ArrayList<>();

    public Entity() {
        entityInfo = EntityInfo.ENTITY_INFO_MAP.get(this.getClass());
    }

    /**
     * 实体基本信息
     */
    private EntityInfo entityInfo;

    /**
     * 写入DB前执行
     */
    public void beforeWriteDB(){

    }

    /**
     * 读取DB后执行
     */
    public void afterReadDB(){

    }

    public void init(){
        init(ordinaryKeyValue,entityInfo.getOrdinaryColumnNameList());
        init(pkKeyValue,entityInfo.getPkColumnNameList());
    }

    private void init(List<KeyValueEntry<String, Object>> ordinaryKeyValue, List<String> ordinaryColumnNameList) {
        for(String ordinaryColumnName: ordinaryColumnNameList){
            Class<? extends Entity> aClass = this.getClass();
            Field field = null;
            try {
                field = aClass.getDeclaredField(ordinaryColumnName);
                field.setAccessible(true);
                KeyValueEntry<String,Object> keyValue = new KeyValueEntry<>() ;
                keyValue.setKey(ordinaryColumnName);
                keyValue.setValue(field.get(this));
                ordinaryKeyValue.add(keyValue);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }



}
