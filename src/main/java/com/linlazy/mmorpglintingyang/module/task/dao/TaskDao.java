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

    /**
     * 获取玩家任务
     * @param actorId  玩家Id
     * @param taskId 任务ID
     * @return 返回玩家任务
     */
    @Select("select * from task where actorId = #{actorId} and taskId = #{taskId}")
    Task getTask(long actorId, int taskId);

    /**
     * 更新玩家任务
     * @param convertTask 玩家任务信息
     */
    @Update("update task set status = #{status},data = #{data} where taskId = #{taskId} and actorId = #{actorId}")
    void update(Task convertTask);
}
