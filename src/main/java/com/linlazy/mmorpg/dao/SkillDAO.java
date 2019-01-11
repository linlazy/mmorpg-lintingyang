package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linlazy
 */
@Component
public class SkillDAO extends EntityDAO<SkillEntity> {

    /**
     * 获取玩家所有技能信息
     * @param actorId  玩家ID
     * @return 返回玩家所有技能信息
     */
    List<SkillEntity> getSkillSet(long actorId){
        return jdbcTemplate.queryForList("select * from skill where actorId = ?",new Object[]{actorId},SkillEntity.class);
    }

    @Override
    protected Class<SkillEntity> forClass() {
        return SkillEntity.class;
    }
}
