package com.linlazy.mmorpglintingyang.server.db.statement;

import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.FieldInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 更新语句
 * @author linlazy
 */
public class UpdateStatement {
    public static String buildPrepareSQL(Entity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(entity.getTableName() + " ");
        sql.append(" set ");
        List<String> ordinaryColumnNameList = entity.getOrdinaryField().stream().map(FieldInfo::getName).collect(Collectors.toList());
        for(int i = 0; i < ordinaryColumnNameList.size(); i ++){
            if(i == ordinaryColumnNameList.size() -1){
                sql.append(ordinaryColumnNameList.get(i)).append("=").append("?");
                break;
            }
            sql.append(ordinaryColumnNameList.get(i)).append("=").append("?").append(",");
        }

        sql.append(" where ");
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
