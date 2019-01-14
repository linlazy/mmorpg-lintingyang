package com.linlazy.mmorpg.server.db.statement;


import com.linlazy.mmorpg.server.db.entity.EntityInfo;

/**
 * 删除语句
 * @author linlazy
 */
public class DeleteStatement {
    public static String buildPrepareSQL(EntityInfo entityInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(entityInfo.getTableName() + " ");

        sql.append(BaseStatement.whereSQL(entityInfo));

        return sql.toString();
    }
}
