package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<SkillEntity> getPlayerSkillList(long actorId){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from skill where actorId = ?", actorId);
        return maps.stream()
                .map(map->{
                    SkillEntity skillEntity = new SkillEntity();
                    skillEntity.setActorId((Long) map.get("actorId"));
                    skillEntity.setNextCDResumeTimes((Long) map.get("nextCDResumeTimes"));
                    skillEntity.setLevel((Integer) map.get("level"));
                    skillEntity.setDressed((Boolean) map.get("dressed"));
                    skillEntity.setSkillId((Integer) map.get("skillId"));
                    return skillEntity;
                }).collect(Collectors.toList());
    }

    @Override
    protected Class<SkillEntity> forClass() {
        return SkillEntity.class;
    }
}
