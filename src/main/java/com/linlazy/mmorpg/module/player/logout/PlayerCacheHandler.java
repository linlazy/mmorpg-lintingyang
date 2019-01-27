package com.linlazy.mmorpg.module.player.logout;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.event.type.LoginEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.email.service.EmailService;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.shop.service.ShopService;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.module.task.service.TaskService;
import com.linlazy.mmorpg.server.common.LogoutListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 玩家缓存处理器
 * @author linlazy
 */
@Component
public class PlayerCacheHandler implements LogoutListener {


    private static  ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    private static Map<Long,ScheduledFuture<?>> map = new ConcurrentHashMap<>();

    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(LoginEvent loginEvent){
        Player player = loginEvent.getPlayer();
        ScheduledFuture<?> scheduledFuture = map.get(player.getActorId());
        if(scheduledFuture != null){
            scheduledFuture.cancel(true);
            map.remove(player.getActorId());
        }
    }

    @Override
    public void logout(long actorId) {
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                PlayerService.playerCache.invalidate(actorId);
                SkillService.playerSkillCache.invalidate(actorId);
                TaskService.playerTaskCache.invalidate(actorId);
                PlayerBackpackService.playerBackpackCache.invalidate(actorId);
                EmailService.playerEmailCache.invalidate(actorId);
                ShopService.playerShopCache.invalidate(actorId);
            }
        }, 60L, TimeUnit.SECONDS);

        map.put(actorId,schedule);

    }
}
