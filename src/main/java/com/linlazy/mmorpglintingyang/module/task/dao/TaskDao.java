package com.linlazy.mmorpglintingyang.module.task.dao;

import com.linlazy.mmorpglintingyang.module.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author linlazy
 */
@Mapper
public interface TaskDao {

    @Select("select * from task where actorId = #{actorId} and taskId = #{taskId}")
    Task getTask(long actorId, int taskId);

    @Update("update task set status = #{status},data = #{data} where taskId = #{taskId} and actorId = #{actorId}")
    void update(Task convertTask);
}
