package com.linlazy.mmorpg.server.db.statement;

import com.linlazy.mmorpg.server.db.entity.EntityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询语句
 * @author linlazy
 */
public class SelectStatement {

    private static Logger logger = LoggerFactory.getLogger(SelectStatement.class);

    public static String buildPrepareSQLByPK(EntityInfo entityInfo) {
        StringBuilder sql = new StringBuilder();

        sql.append("select * from ").append(entityInfo.getTableName());

        sql.append(BaseStatement.whereSQL(entityInfo));

        logger.debug("{}",sql);
        return sql.toString();
    }
}
