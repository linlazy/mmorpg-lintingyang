package com.linlazy.mmorpg.server.db.statement;


import com.linlazy.mmorpg.server.db.entity.EntityInfo;

import java.util.List;

/**
 * @author linlazy
 */
public class BaseStatement {


    static String whereSQL(EntityInfo entityInfo){
        StringBuilder whereSQL = new StringBuilder();
        whereSQL.append(" where ");
        List<String> pkColumnNameList = entityInfo.getPkColumnNameList();
        for(int i = 0; i < pkColumnNameList.size(); i ++){
            if(i == pkColumnNameList.size() -1){
                whereSQL.append(pkColumnNameList.get(i)).append(" = ").append(" ? ");
                break;
            }
            whereSQL.append(pkColumnNameList.get(i)).append(" = ").append(" ? ").append(" and ");
        }
        return whereSQL.toString();
    }
}
