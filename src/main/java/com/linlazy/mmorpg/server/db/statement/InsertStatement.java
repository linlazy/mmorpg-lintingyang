package com.linlazy.mmorpg.server.db.statement;


import com.linlazy.mmorpg.server.db.entity.Entity;
import com.linlazy.mmorpg.server.db.entity.EntityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 插入语句
 * @author linlazy
 */
public class InsertStatement {

    private static Logger logger = LoggerFactory.getLogger(InsertStatement.class);


    public static String buildPrepareSQL(Entity entity){
        EntityInfo entityInfo = EntityInfo.ENTITY_INFO_MAP.get(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(entityInfo.getTableName() + " ");
        sql.append("( ");
        List<String> columnNameList = entityInfo.getColumnNameList();
        for(int i = 0; i < columnNameList.size(); i ++){
            if(i == columnNameList.size() -1){
                sql.append(columnNameList.get(i)).append(" )");
                break;
            }
            sql.append(columnNameList.get(i)).append(" , ");
        }

        sql.append(" values( ");
        for(int i = 0; i < columnNameList.size(); i ++){
            if(i == columnNameList.size() -1){
                sql.append(" ? ").append(" ) ");
                break;
            }
            sql.append(" ? ").append(" , ");
        }

        return sql.toString();
    }
}
