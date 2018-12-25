package com.linlazy.mmorpglintingyang.module.user.manager;

import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardID;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserManager {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GlobalConfigService globalConfigService;

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public Long getMaxActorId() {
        Long maxActorId = userDao.getMaxActorId();
        if(maxActorId == null){
            maxActorId = Long.valueOf(0);
        }
        return maxActorId;
    }

    public void createUser(User user) {
        Long maxActorId = this.getMaxActorId();
        user.setActorId(maxActorId + 1);
        user.setToken(UUID.randomUUID().toString().substring(0,20));
        int initScene = globalConfigService.getInitScene();
        user.setSceneId(initScene);
        userDao.createUser(user);
    }

    public User getUser(long actorId) {
        User user = userDao.getUser(actorId);

        boolean isResume = user.resumeMP(globalConfigService.getMPResumeIntervalMills());
        if(isResume){
            userDao.updateUser(user);
        }
        return userDao.getUser(actorId);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public Map<Long,Integer> addOrConsumeReward(long actorId, Reward reward) {
        Map<Long,Integer> map = new HashMap<>();

        User user = userDao.getUser(actorId);
        if (reward.getRewardId() == RewardID.HP) {
            int hp = user.modifyHP(reward.getCount());
            map.put(RewardID.HP,hp);
        } else if(reward.getRewardId() == RewardID.MP){
            int mp = user.modifyMP(reward.getCount());
            map.put(RewardID.MP,mp);
        } else if(reward.getRewardId() == RewardID.GOLD){
            int gold = user.modifyGold(reward.getCount());
            map.put(RewardID.GOLD,gold);
        }else {
            System.out.println("error not implement");
        }

        userDao.updateUser(user);
        return map;
    }

    public void addOrRemoveAddition(long actorId, Addition addition) {
//        User user = userDao.getUser(actorId);
//        switch (addition.getAdditionType()){
//            case AdditionType.PhysicalAttack:
//                user.modifyPhysicalAttack(addition.getCount());
//                break;
//            case AdditionType.MagicAttack:
//                user.modifyMagicAttack(addition.getCount());
//                break;
//            case AdditionType.PhysicalDefense:
//                user.modifyPhysicalDefense(addition.getCount());
//                break;
//            case AdditionType.MagicDefense:
//                user.modifyMagicDefense(addition.getCount());
//                break;
//            default:
//                System.out.println("error not implement");
//        }
    }

}
