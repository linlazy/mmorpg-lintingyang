package com.linlazy.mmorpg.server.db.entity;

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

    /**
     * 实体类类型
     */
    private Class<? extends Entity> entityClass;

    /**
     * 字段名类型映射
     */
    private Map<String,String> fieldNameTypeMap = new HashMap<>();

    /**
     * 主键字段名字列表
     */
    private List<String> pkColumnNameList = new ArrayList<>();

    /**
     * 普通字段名字列表
     */
    private List<String> ordinaryColumnNameList = new ArrayList<>();

    public static Map<Class<? extends Entity>, EntityInfo> ENTITY_INFO_MAP = new HashMap<>();

    public List<String> getColumnNameList() {
        List<String> columnNameList = new ArrayList<>();
        columnNameList.addAll(pkColumnNameList);
        columnNameList.addAll(ordinaryColumnNameList);
        return columnNameList;
    }

    public String getType(String fieldName){
        return fieldNameTypeMap.get(fieldName);
    }
}