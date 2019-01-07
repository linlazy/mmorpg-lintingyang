package com.linlazy.mmorpglintingyang.server.db;

import com.linlazy.mmorpglintingyang.server.db.statement.DeleteStatement;
import com.linlazy.mmorpglintingyang.server.db.statement.InsertStatement;
import com.linlazy.mmorpglintingyang.server.db.statement.UpdateStatement;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author linlazy
 */
public class EntityManager {

    private Logger logger = LoggerFactory.getLogger(EntityManager.class);

    @Autowired
    private BaseJdbc baseJdbc;

    public void init(){
        //获取所有@table注解的示例
        //构建 每个table构建 entityInfo
        Map<String, Object> tables = SpringContextUtil.getApplicationContext().getBeansWithAnnotation(Table.class);
        for(Object tableEntity: tables.values()){
            EntityInfo entityInfo = new EntityInfo();
            EntityInfo.ENTITY_INFO_MAP.put((Class<? extends Entity>) tableEntity.getClass(),entityInfo);
        }

    }

    /**
     * 合并相同sql操作
     */
    public void merge(Collection<Entity> entities){
        Map<Integer, List<Entity>> operatorTypeEntityList = entities.stream().collect(groupingBy(Entity::getOperatorType));
        for(Map.Entry<Integer,List<Entity>> entry: operatorTypeEntityList.entrySet()){
            String prepareSQL = null;
            switch (entry.getKey()){
                case EntityOperatorType.INSERT:
                    prepareSQL = InsertStatement.buildPrepareSQL(entry.getValue().get(0));
                    break;
                case EntityOperatorType.UPDATE:
                    prepareSQL = UpdateStatement.buildPrepareSQL(entry.getValue().get(0));
                    break;
                case EntityOperatorType.DELETE:
                    prepareSQL = DeleteStatement.buildPrepareSQL(entry.getValue().get(0));
                    break;
                default:
            }
            baseJdbc.batchUpdate(prepareSQL,entry.getValue());
        }

    }

}
