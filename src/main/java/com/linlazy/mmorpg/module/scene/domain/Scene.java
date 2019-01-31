package com.linlazy.mmorpg.module.scene.domain;

import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.push.ScenePushHelper;
import com.linlazy.mmorpg.module.scene.service.MonsterService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 场景领域类
 * @author linlazy
 */
@Data
public class Scene {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 场景调度线程池
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);

    /**
     *  场景ID
     */
    private int sceneId;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 相邻场景ID
     */
    private List<Integer> neighborSet = new ArrayList<>();

    /**
     * 场景玩家信息
     */
    protected Map<Long, Player> playerMap = new HashMap<>();

    /**
     * 场景怪物信息
     */
    protected Map<Long, Monster> monsterMap;

    /**
     * 场景召唤兽信息
     */
    protected Map<Long, PlayerCall> playerCallMap = new HashMap<>();

    /**
     * 场景BOSS信息
     */
    protected List<Boss> bossList;
    /**
     * 场景NPC信息
     */
    private Set<Npc> NpcSet = new HashSet<>();

    /**
     * 小怪定时刷新调度
     */
    public void startRefreshMonsterScheduled() {

        scheduledExecutorService.scheduleAtFixedRate(() -> {

            logger.debug("【普通场景】，一定时间后，触发小怪刷新事件");
            monsterMap.values().clear();
            MonsterService monsterService = SpringContextUtil.getApplicationContext().getBean(MonsterService.class);
            Map<Long, Monster> monsterMap = monsterService.getMonsterBySceneId(sceneId);
            this.monsterMap = monsterMap;

            playerMap.values().stream()
                    .forEach(player -> {
                        ScenePushHelper.pushEnterScene(player.getActorId(),"【普通场景】，一定时间后，触发小怪刷新事件");
                    });
        }, 0L, 10L, TimeUnit.SECONDS);
    }

}
