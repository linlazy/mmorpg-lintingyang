package com.linlazy.mmorpglintingyang.server.db;

import com.linlazy.mmorpglintingyang.server.db.statement.SelectStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class BaseJdbc<T extends Entity>  extends JdbcTemplate{

    private static Logger logger = LoggerFactory.getLogger(BaseJdbc.class);
//
//    @Autowired
//    protected DataSource dataSource;
    /**
     * 批量大小
     */
    private int batchSize;

    /**
     * 批量记录更新
     */
    public void doBatchUpdate(String prepareSQL, List<Entity> entityList){
//        BatchSqlUpdate batchSqlUpdate = new BatchSqlUpdate(dataSource, prepareSQL);
//        batchSqlUpdate.setBatchSize(batchSize);
//
//        for (Entity entity : entityList) {
////        //获取参数
//            Object[] args = entity.getAllField().stream().map(FieldInfo::getValue).toArray();
//            batchSqlUpdate.update(args);
//        }
//        //调用flush实际执行未达到batchSize,而到达的batchSize会隐式调用flush方法
//        batchSqlUpdate.flush();
    }


    public  List<T> query(Entity entity){

//        Object[] args = entity.getIdentityField().stream().map(FieldInfo::getValue).toArray();
        return null;
    }

    public T query(EntityInfo entityInfo,Identity identity) {
        String sql = SelectStatement.buildPrepareSQL(entityInfo);

        List<T> query = super.query(sql, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    Entity entity = entityInfo.getEntityClass().newInstance();
                    //获取具有Cloum注解上的字段的类型，名称 ，将DB读取的值放进字段
                    for (Map.Entry<String, String> fieldNameType : entityInfo.getFieldNameTypeMap().entrySet()) {

                        Field field = this.getClass().getField(fieldNameType.getKey());
                        switch (fieldNameType.getValue()) {
                            case "Integer":
                                field.set(entity, rs.getInt(fieldNameType.getKey()));
                                break;
                            case "Long":
                                field.set(entity, rs.getLong(fieldNameType.getKey()));
                                break;
                            default:
                                break;
                        }
                    }

                    entity.afterReadDB();
                    return (T) entity;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }, identity.getArgs());
        return query.get(0);
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
