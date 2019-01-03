package com.linlazy.mmorpglintingyang.module.skill.dao;

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
         skillDao.getSkill(4194306, 1);
    }

    @Test
    public void addSkill() {


    }

    @Test
    public void updateSkill() {


    }
}