package com.linlazy.mmorpg.server.db.entity;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author linlazy
 */
@Data
public abstract class Entity {

    /**
     * 操作类型
     */
    private int operatorType;


    private Map<String,Object> pkKeyValue = new LinkedHashMap<>();
    private Map<String,Object> ordinaryKeyValue = new LinkedHashMap<>();

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

    private void init(Map<String, Object> ordinaryKeyValue, List<String> ordinaryColumnNameList) {
        for(String ordinaryColumnName: ordinaryColumnNameList){
            Class<? extends Entity> aClass = this.getClass();
            Field field = null;
            try {
                field = aClass.getDeclaredField(ordinaryColumnName);
                field.setAccessible(true);
                ordinaryKeyValue.put(ordinaryColumnName,field.get(this));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }



}
