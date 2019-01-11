package com.linlazy.mmorpg.server.db.statement;


import com.linlazy.mmorpg.server.db.entity.Entity;
import com.linlazy.mmorpg.server.db.entity.EntityInfo;
import com.linlazy.mmorpg.server.db.entity.KeyValueEntry;

import java.util.List;
import java.util.stream.Collectors;

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
        List<String> notNullOrdinaryColumnNameList = entity.getNotNullOrdinaryKeyValue().stream()
                .map(KeyValueEntry::getKey).collect(Collectors.toList());

        for(int i = 0; i < notNullOrdinaryColumnNameList.size(); i ++){
            if(i == notNullOrdinaryColumnNameList.size() -1){
                sql.append(notNullOrdinaryColumnNameList.get(i)).append("=").append(" ? ");
                break;
            }
            sql.append(notNullOrdinaryColumnNameList.get(i)).append("=").append(" ? ").append(",");
        }
        sql.append(BaseStatement.whereSQL(entityInfo));

        return sql.toString();
    }

}
