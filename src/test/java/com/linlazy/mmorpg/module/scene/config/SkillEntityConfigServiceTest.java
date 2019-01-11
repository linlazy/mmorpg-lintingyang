package com.linlazy.mmorpg.module.scene.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillEntityConfigServiceTest {

    @Autowired
    private MonsterConfigService monsterConfigService;

    @Test
    public void getMonsterBySceneId() {
        List<JSONObject> monsterBySceneId = monsterConfigService.getMonsterBySceneId(1);
        System.out.println(JSON.toJSONString(monsterBySceneId));
    }
}