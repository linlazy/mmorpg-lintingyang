package com.linlazy.mmorpglintingyang.server.db.statement;

import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.FieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 插入语句
 * @author linlazy
 */
public class InsertStatement {

    private static Logger logger = LoggerFactory.getLogger(InsertStatement.class);

    public static String buildPrepareSQL(Entity entity){
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(entity.getTableName() + " ");
        sql.append("( ");
        List<String> columnNameList = entity.getAllField().stream().map(FieldInfo::getName).collect(Collectors.toList());
        for(int i = 0; i < columnNameList.size(); i ++){
            if(i == columnNameList.size() -1){
                sql.append(columnNameList.get(i)).append(")");
                break;
            }
            sql.append(columnNameList.get(i)).append(",");
        }

        sql.append(" values( ");
        for(int i = 0; i < columnNameList.size(); i ++){
            if(i == columnNameList.size() -1){
                sql.append("?").append(")");
                break;
            }
            sql.append("?").append(",");
        }

        return sql.toString();
    }

}
