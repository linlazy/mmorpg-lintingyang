package com.linlazy.mmorpglintingyang.server.db;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public abstract class Entity {


    /**
     * 唯一标识
     */
    private Identity identity;

    /**
     * 操作类型
     */
    private int operatorType;
    /**
     * 表名
     */
    private String tableName;

    private EntityFieldInfo columnEntityFieldInfo = new EntityFieldInfo();

    private Map<String,String> columnNameTypeMap = new HashMap<>();
    private Map<String,String> columnNameValueMap = new HashMap<>();

    private EntityFieldInfo entityFieldInfo;
    /**
     * 写入DB前执行
     */
    protected void beforeWriteDB(){

    }

    /**
     * 读取DB后执行
     */
    protected void afterReadDB(){

    }


    public String getType(String columnName){
        return columnNameTypeMap.get(columnName);
    }

    public  String getColumnValue(String columnName){
        return columnNameValueMap.get(columnName);
    }
    // 根据某个列条件捞取数据，做缓存

    public List<FieldInfo> getIdentityField(){
        return entityFieldInfo.getIdentity();
    }

    public List<FieldInfo> getOrdinaryField(){
        return entityFieldInfo.getOrdinary();
    }

    public List<FieldInfo> getAllField(){
         return entityFieldInfo.getAll();
    }


}
