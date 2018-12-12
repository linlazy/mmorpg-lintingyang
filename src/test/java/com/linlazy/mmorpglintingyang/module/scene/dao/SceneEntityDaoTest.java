package com.linlazy.mmorpglintingyang.module.scene.dao;

import com.linlazy.mmorpglintingyang.module.scene.entity.SceneEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SceneEntityDaoTest {
    @Autowired
    private SceneEntityDao sceneEntityDao;

    @Test
    public void getScene(){
        SceneEntity sceneEntity = sceneEntityDao.getSceneEntity(1);
    }

}