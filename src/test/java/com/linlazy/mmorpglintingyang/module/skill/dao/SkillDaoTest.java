package com.linlazy.mmorpglintingyang.module.skill.dao;

import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillDaoTest {

    @Autowired
    private SkillDao skillDao;

    @Test
    public void getSkill() {
        Skill skill = skillDao.getSkill(4194306, 1);
    }

    @Test
    public void addSkill() {


    }

    @Test
    public void updateSkill() {


    }
}