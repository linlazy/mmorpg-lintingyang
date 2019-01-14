package com.linlazy.mmorpg.service;

import com.linlazy.mmorpg.domain.Boss;
import com.linlazy.mmorpg.file.config.BossConfig;
import com.linlazy.mmorpg.file.service.BossConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class BossService {

    private Map<Long,Boss> bossMap = new ConcurrentHashMap<>();


    private AtomicLong maxBossId = new AtomicLong();

    @Autowired
    private BossConfigService bossConfigService;


    public List<Boss> createCopyBoss(int sceneId){
        List<BossConfig> bossConfigList = bossConfigService.getBossConfigBySceneId(sceneId);
        return bossConfigList.stream()
                .map(bossConfig -> {
                    Boss boss = new Boss();

                    boss.setId(maxBossId.incrementAndGet());
                    boss.setSceneId(sceneId);
                    boss.setBossId(bossConfig.getBossId());
                    boss.setHp(bossConfig.getHp());
                    boss.setAttack(bossConfig.getAttack());
                    boss.setName(bossConfig.getName());

                    return boss;
                }).collect(Collectors.toList());
    }

    public Set<Boss> getBOSSBySceneId(Integer sceneId) {
        return null;
    }
}
