package com.linlazy.mmorpg.module.item.type.consume.mp;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.event.type.PlayerDeadEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.threadpool.ScheduledThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * @author linlazy
 */
@Component
public  class DurationResumeMp extends BaseResumeMp {

    /**
     * 恢复MP调度线程池
     */
    ScheduledExecutorService scheduledExecutorService =new ScheduledThreadPool(20);


    /**
     * 玩家恢复MP调度句柄
     */
    private Map<Long, ScheduledFuture<?> > playerScheduledFutureMap = new ConcurrentHashMap<>();

    @Override
    @PostConstruct
    public void init(){
        super.init();
        EventBusHolder.post(this);
    }

    /**
     * 死亡时，取消恢复MP效果
     * @param playerDeadEvent
     */
    @Subscribe
    public void playerDead(PlayerDeadEvent playerDeadEvent){
        Player player = playerDeadEvent.getPlayer();
        ScheduledFuture<?> scheduledFuture = playerScheduledFutureMap.remove(player.getActorId());
        if(scheduledFuture != null && !scheduledFuture.isCancelled()){
            scheduledFuture.cancel(true);
        }
    }


    @Autowired
    private PlayerService playerService;


    @Override
    protected Integer resumeMpType() {
        return ResumeMpType.durationResume;
    }


    @Override
    public Result<?> doResumeMp(long actorId, Item item) {
        Player player = playerService.getPlayer(actorId);
        JSONObject extJsonObject = item.getExt();
        int continueTime = extJsonObject.getIntValue("continueTime");
        int durationTime = extJsonObject.getIntValue("durationTime");
        int resumeMP = extJsonObject.getIntValue("resumeMP");

        //定时恢复hp效果
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            player.resumeMP(resumeMP);
        }, 0L, durationTime, TimeUnit.SECONDS);
        playerScheduledFutureMap.put(player.getActorId(),scheduledFuture);


        //取消效果
        scheduledExecutorService.schedule(()->{
            playerScheduledFutureMap.remove(player.getActorId());
            if(scheduledFuture != null && !scheduledFuture.isCancelled()){
                scheduledFuture.cancel(true);
            }
        },continueTime,TimeUnit.SECONDS);

        return Result.success();
    }

}
