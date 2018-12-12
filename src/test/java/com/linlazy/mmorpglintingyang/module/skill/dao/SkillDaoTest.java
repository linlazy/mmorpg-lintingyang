package com.linlazy.mmorpglintingyang.module.skill.dao;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.skill.entity.model.SkillInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillDaoTest {

    @Autowired
    private SkillDao skillDao;

    @Test
    public void getSkill() {
        Skill skill = skillDao.getSkill(3);
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        skillInfoSet.add(new SkillInfo(2,2));
        System.out.println(JSONObject.toJSONString(skillInfoSet));
    }

    @Test
    public void addSkill() {

        Skill skill = new Skill();
        skill.setActorId(4194306);
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        skillInfoSet.add(new SkillInfo(1,3));
        skillInfoSet.add(new SkillInfo(2,2));
        skill.setSkills(JSONObject.toJSONString(skillInfoSet));
        skillDao.addSkill(skill);
    }

    @Test
    public void updateSkill() {

        Skill skill = new Skill();
        skill.setActorId(4194306);
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        skillInfoSet.add(new SkillInfo(1,3));
        skillInfoSet.add(new SkillInfo(2,2));
        skill.setSkills(JSONObject.toJSONString(skillInfoSet));
        skillDao.updateSkill(skill);
    }
}