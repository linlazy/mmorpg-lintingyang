package com.linlazy.mmorpglintingyang.module.user.manager;

import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.common.addition.AdditionType;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardID;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void addOrConsumeReward(long actorId, Reward reward) {
        User user = userDao.getUser(actorId);
        switch (reward.getRewardId()){
            case RewardID.HP:
                user.modifyHP(reward.getCount());
                break;
            case RewardID.MP:
                user.modifyMP(reward.getCount());
                break;
            default:
                System.out.println("error not implement");
        }
    }

    public void addOrRemoveAddition(long actorId, Addition addition) {
        User user = userDao.getUser(actorId);
        switch (addition.getAdditionType()){
            case AdditionType.PhysicalAttack:
                user.modifyPhysicalAttack(addition.getCount());
                break;
            case AdditionType.MagicAttack:
                user.modifyMagicAttack(addition.getCount());
                break;
            case AdditionType.PhysicalDefense:
                user.modifyPhysicalDefense(addition.getCount());
                break;
            case AdditionType.MagicDefense:
                user.modifyMagicDefense(addition.getCount());
                break;
            default:
                System.out.println("error not implement");
        }
    }

}
