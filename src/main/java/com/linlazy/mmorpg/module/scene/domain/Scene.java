package com.linlazy.mmorpg.module.scene.domain;

import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.constants.ProfessionType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.constants.MonsterType;
import com.linlazy.mmorpg.module.scene.push.ScenePushHelper;
import com.linlazy.mmorpg.module.scene.service.BossService;
import com.linlazy.mmorpg.module.scene.service.MonsterService;
import com.linlazy.mmorpg.server.threadpool.ScheduledThreadPool;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPool(20);

    /**
     * 道具消失调度句柄映射
     */
    private Map<Long,ScheduledFuture<?>> itemRemoveScheduleMap = new ConcurrentHashMap<>();

    /**
     * 场景实体ID标识
     */
    private AtomicLong maxId = new AtomicLong(0);

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
     * 场景掉落道具
     */
    private Map<Long,Item> itemMap = new HashMap<>();

    private byte[] itemLock = new byte[0];

    /**
     * 小怪定时刷新调度
     */
    public void startRefreshMonsterScheduled() {


    }

    public void addItem(Item item) {
        itemMap.put(item.getId(),item);
    }

    public Item removeItem(Item item) {
        return itemMap.remove(item.getId());
    }

    public void addItemRemoveSchedule(long id,ScheduledFuture<?> itemRemoveSchedule) {
        itemRemoveScheduleMap.putIfAbsent(id,itemRemoveSchedule);
    }

    public void cancelItemRemoveSchedule(long id) {
        ScheduledFuture<?> remove = itemRemoveScheduleMap.remove(id);
        if(remove != null){
            remove.cancel(true);
        }
    }

    public void startRefreshBossScheduled() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {

            logger.debug("【普通场景】，5分钟后，触发boss刷新事件");
            bossList.forEach(boss -> boss. quitSchedule());
            bossList.clear();
            BossService bossService = SpringContextUtil.getApplicationContext().getBean(BossService.class);
            List<Boss> bossList = bossService.getBOSSBySceneId(sceneId);
            bossList.forEach(boss -> EventBusHolder.register(boss));
            this.bossList = bossList;
            this.bossList.stream()
                    .filter(boss -> boss.getType() == MonsterType.ACTIVE)
                    .forEach(boss -> {
                        if (playerMap.size() > 0 ){
                            Player player = RandomUtils.randomElement(playerMap.values());
                            boss.setAttackTarget(player);
                            if (player.getProfession() == ProfessionType.summoner){
                                PlayerCall playerCall = player.getPlayerCall();
                                if(playerCall != null){
                                    boss.setAttackTarget(playerCall);
                                }
                            }
                            boss.startAutoAttack();
                        }

                    });

            playerMap.values().stream()
                    .forEach(player -> {
                        ScenePushHelper.pushEnterScene(player.getActorId(),"【普通场景】，5分钟时间后，触发boss刷新事件");
                    });
        }, 0L, 300L, TimeUnit.SECONDS);
    }

    public void refreshDeadMonsterSchedule(Monster monster) {

        Monster oldMonster = this.monsterMap.remove(monster.getId());
        playerMap.values().stream()
                .forEach(player -> {
                    ScenePushHelper.pushEnterScene(player.getActorId(),String.format("【普通场景】,场景【%d】，10秒后，小怪【%s】刷新",sceneId,oldMonster.getName()));
                });
        scheduledExecutorService.schedule(() -> {
            logger.debug("【普通场景】，10秒后，触发小怪刷新事件");
            MonsterService monsterService = SpringContextUtil.getApplicationContext().getBean(MonsterService.class);
            Monster newMonster = monsterService.getMonsterByMonsterId(sceneId, oldMonster.getMonsterId());
            this.monsterMap.put(newMonster.getId(),newMonster);

            if(newMonster.getType() == MonsterType.ACTIVE){
                if (playerMap.size() > 0 ){
                    Player player = RandomUtils.randomElement(playerMap.values());
                    newMonster.setAttackTarget(player);
                    if (player.getProfession() == ProfessionType.summoner){
                        PlayerCall playerCall = player.getPlayerCall();
                        if(playerCall != null){
                            newMonster.setAttackTarget(playerCall);
                        }
                    }
                    newMonster.startAutoAttack();
                }
            }

            playerMap.values().stream()
                    .forEach(player -> {
                        ScenePushHelper.pushEnterScene(player.getActorId(),String.format("【普通场景】,场景【%d】，小怪【%s】刷新",sceneId,newMonster.getName()));
                    });
        },10L, TimeUnit.SECONDS);
    }


}
