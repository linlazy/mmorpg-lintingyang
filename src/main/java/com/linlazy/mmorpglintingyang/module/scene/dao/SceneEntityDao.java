package com.linlazy.mmorpglintingyang.module.scene.dao;

import com.linlazy.mmorpglintingyang.module.scene.entity.SceneEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 玩家场景数据访问类
 */
@Mapper
public interface SceneEntityDao {

    String TABLE = "scene_entity";

    @Select({"select * from ",TABLE," where sceneId = #{sceneId} limit 1"})
    SceneEntity getSceneEntity(int sceneId);

    @Update({"update ",TABLE," set entities = #{entities} where sceneId = #{sceneId}"})
    void updateSceneEntity(SceneEntity sceneEntity);
}
