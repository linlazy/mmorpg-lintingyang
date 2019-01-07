package com.linlazy.mmorpglintingyang.server.db.statement;

import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.FieldInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
public class DeleteStatement {
    public static String buildPrepareSQL(Entity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(entity.getTableName() + " ");

        sql.append("where ");
        List<String> identityColumnNameList = entity.getIdentityField().stream().map(FieldInfo::getName).collect(Collectors.toList());
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
