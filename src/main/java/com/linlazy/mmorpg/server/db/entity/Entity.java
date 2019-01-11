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


    private List<KeyValueEntry<String,String>> pkKeyValue = new ArrayList<>();
    private List<KeyValueEntry<String,String>> notNullOrdinaryKeyValue = new ArrayList<>();

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
        init(notNullOrdinaryKeyValue,entityInfo.getOrdinaryColumnNameList());
        init(pkKeyValue,entityInfo.getPkColumnNameList());
    }

    private void init(List<KeyValueEntry<String, String>> notNullOrdinaryKeyValue, List<String> ordinaryColumnNameList) {
        for(String ordinaryColumnName: ordinaryColumnNameList){
            Class<? extends Entity> aClass = this.getClass();
            Field field = null;
            try {
                field = aClass.getDeclaredField(ordinaryColumnName);
                field.setAccessible(true);
                if(field.get(this) != null){
                    KeyValueEntry<String,String> keyValue = new KeyValueEntry<>() ;
                    keyValue.setKey(ordinaryColumnName);
                    keyValue.setValue((String) field.get(this));
                    notNullOrdinaryKeyValue.add(keyValue);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }



}
