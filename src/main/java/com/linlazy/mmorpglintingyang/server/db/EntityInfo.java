package com.linlazy.mmorpglintingyang.server.db;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public class EntityInfo {

    public static Map<Class<? extends Entity>, EntityInfo> ENTITY_INFO_MAP = new HashMap<>();




    private String prepareSQL;



    public static EntityInfo getEntityInfo(Class<? extends Entity> entityClass){
        return ENTITY_INFO_MAP.get(entityClass);
    }
}