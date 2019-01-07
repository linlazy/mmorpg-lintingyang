package com.linlazy.mmorpglintingyang.server.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author linlazy
 */
@Component
public class BaseJdbc {

    private static Logger logger = LoggerFactory.getLogger(BaseJdbc.class);

    @Autowired
    private DataSource dataSource;
    /**
     * 批量大小
     */
    private int batchSize;

    /**
     * 批量记录更新
     */
    public void batchUpdate(String prepareSQL, List<Entity> entityList){
        BatchSqlUpdate batchSqlUpdate = new BatchSqlUpdate(dataSource, prepareSQL);
        batchSqlUpdate.setBatchSize(batchSize);

        for (Entity entity : entityList) {
//        //获取参数
            Object[] args = entity.getAllField().stream().map(FieldInfo::getValue).toArray();
            batchSqlUpdate.update(args);
        }
        //调用flush实际执行未达到batchSize,而到达的batchSize会隐式调用flush方法
        batchSqlUpdate.flush();
    }


    //缓存

    // 多个主键
    //玩家背包主键
    //辅助键 定时更久
    //根据某个列获取记录列表
    // fk --> 缓存
    // 罗列业务
    // 主背包--->item --->actorId ,itemId
    // 技能 ---> skill --->skillId
    // 公会 ---> guild --->公会基本信息 ---> guildId 定时
    // 公会 ---> guild --->公会离线消息 guildId
    // 公会 ---> guildActor --->公会玩家列表 ---> guildId
}
