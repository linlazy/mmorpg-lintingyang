package com.linlazy.mmorpglintingyang.module.scene.manager.dao;

import com.linlazy.mmorpglintingyang.module.scene.manager.entity.SceneEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 玩家场景数据访问类
 */
@Mapper
public interface SceneEntityDao {

    String TABLE = "scene_entity";

    String FIELD = " sceneId,entities ";

    @Select("select sceneId,entities from scene_entity where sceneId = #{sceneId} limit 1")
    SceneEntity getSceneEntity(int sceneId);

    @Update({"update ",TABLE," set entities = #{entities} where sceneId = #{sceneId}"})
    void updateSceneEntity(SceneEntity sceneEntity);

    @Insert({"insert into ",TABLE,"( ",FIELD," )"," values( #{sceneId},#{entities} )"})
    void addSceneEntity(SceneEntity sceneEntity);
}
