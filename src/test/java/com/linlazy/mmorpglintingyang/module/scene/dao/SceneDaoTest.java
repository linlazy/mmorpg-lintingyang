package com.linlazy.mmorpglintingyang.module.scene.dao;

import com.linlazy.mmorpglintingyang.module.scene.entity.Scene;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SceneDaoTest {

    @Autowired
    private SceneDao sceneDao;

    @Test
    public void getScene(){
        Scene scene = sceneDao.getScene(4194306);
    }

    @Test
    public void addScene(){
        Scene scene = new Scene();
        scene.setActorId(4194306);
        scene.setSceneId(1);
        sceneDao.addScene(scene);
    }

}