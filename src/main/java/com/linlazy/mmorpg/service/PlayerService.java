package com.linlazy.mmorpg.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.PlayerDAO;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.entity.PlayerEntity;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 玩家服务类
 * @author linlazy
 */
@Component
public class PlayerService {

    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    /**
     * 玩家缓存
     */
    public static LoadingCache<Long, Player> playerCache = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Long, Player>() {
                @Override
                public Player load(Long actorId) {

                    Player player = new Player(actorId);

                    PlayerDAO playerDAO = SpringContextUtil.getApplicationContext().getBean(PlayerDAO.class);
                    PlayerEntity playerEntity = playerDAO.getEntityByPK(actorId);

                    //todo
                    player.setActorId(actorId);
                    player.setMp(playerEntity.getMp());

                    return player;
                }
            });


    public Player getPlayer(long actorId){
        try {
            return playerCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<Player> getSameScenePlayerSet(long actorId){
        int sceneId = getPlayer(actorId).getSceneId();
        return playerCache.asMap().values().stream()
                .filter(player -> player.getSceneId() == sceneId)
                .collect(Collectors.toSet());
    }

}
