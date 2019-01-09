package com.linlazy.mmorpglintingyang.module.user.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpglintingyang.module.dao.PlayerDAO;
import com.linlazy.mmorpglintingyang.module.domain.Player;
import com.linlazy.mmorpglintingyang.module.entity.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 玩家服务类，供外部调用
 * @author linlazy
 */
@Component
public class PlayerService {

    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerDAO playerDAO;

    /**
     * 玩家缓存
     */
    public  LoadingCache<Long, Player> playerCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<Long, Player>() {
                @Override
                public Player load(Long actorId) {
                Player player = new Player(actorId);

                PlayerEntity playerEntity = playerDAO.getPlayerEntity(actorId);
                player.setProfession(player.getProfession());
                player.setHp(playerEntity.getHp());
                player.setMp(playerEntity.getMp());
                player.setGold(playerEntity.getGold());
                return player;
                }
    });

    public  Player getPlayer(long actorId) {
        try {
            return playerCache.get(actorId);
        } catch (ExecutionException e) {
            logger.error("{}",e);
        }
        return null;
    }
}
