package com.linlazy.mmorpg.server.db.statement;


import com.linlazy.mmorpg.server.db.entity.Entity;
import com.linlazy.mmorpg.server.db.entity.EntityInfo;

import java.util.List;

/**
 * 更新语句
 * @author linlazy
 */
public class UpdateStatement {


    public static String buildPrepareSQL(Entity entity) {
        EntityInfo entityInfo = EntityInfo.ENTITY_INFO_MAP.get(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(entityInfo.getTableName() + " ");

        sql.append(" set ");
        List<String> ordinaryColumnNameList =entityInfo.getOrdinaryColumnNameList();
        for(int i = 0; i < ordinaryColumnNameList.size(); i ++){
            if(i == ordinaryColumnNameList.size() -1){
                sql.append(ordinaryColumnNameList.get(i)).append("=").append(" ? ");
                break;
            }
            sql.append(ordinaryColumnNameList.get(i)).append("=").append(" ? ").append(",");
        }
        sql.append(BaseStatement.whereSQL(entityInfo));

        return sql.toString();
    }

}
