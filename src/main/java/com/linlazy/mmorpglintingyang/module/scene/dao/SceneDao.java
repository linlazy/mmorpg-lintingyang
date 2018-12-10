package com.linlazy.mmorpglintingyang.module.scene.dao;

import com.linlazy.mmorpglintingyang.module.scene.entity.Scene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 玩家场景数据访问类
 */
@Mapper
public interface SceneDao {

    String TABLE = "scene";


    @Select({"select * from ",TABLE," where actorId = #{actorId}"})
    Scene getScene(long actorId);


    @Update({"update ",TABLE," set sceneId = #{sceneId} where actorId = #{actorId}"})
    void updateScene(Scene scene);
}
