package com.linlazy.mmorpglintingyang.server.db.statement;

import com.linlazy.mmorpglintingyang.server.db.EntityInfo;

import java.util.List;

/**
 * 查询语句
 * @author linlazy
 */
public class SelectStatement {

    public static String buildPrepareSQL(EntityInfo entityInfo) {
        StringBuilder sql = new StringBuilder();

        sql.append("select * from ").append(entityInfo.getTableName());

        sql.append(" where ");
        List<String> identityColumnNameList = entityInfo.getIdentityColumnNameList();
        for(int i = 0; i < identityColumnNameList.size(); i ++){
            if(i == identityColumnNameList.size() -1){
                sql.append(identityColumnNameList.get(i)).append("=").append("?");
                break;
            }
            sql.append(identityColumnNameList.get(i)).append("=").append("?").append(" and ");
        }
        return sql.toString();
    }
}
