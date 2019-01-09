package com.linlazy.mmorpglintingyang.server.db;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public class EntityInfo {

    /**
     * 表名
     */
    private  String tableName;

    private Class<? extends Entity> entityClass;

    /**
     * 字段名类型映射
     */
    private Map<String,String> fieldNameTypeMap = new HashMap<>();

    /**
     * 标识字段名字列表
     */
    private List<String> identityColumnNameList = new ArrayList<>();

    public static Map<Class<? extends Entity>, EntityInfo> ENTITY_INFO_MAP = new HashMap<>();

    public static EntityInfo getEntityInfo(Class<? extends Entity> entityClass){
        return ENTITY_INFO_MAP.get(entityClass);
    }
}