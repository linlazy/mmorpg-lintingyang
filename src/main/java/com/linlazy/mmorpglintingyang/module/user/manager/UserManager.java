package com.linlazy.mmorpglintingyang.module.user.manager;

import com.linlazy.mmorpglintingyang.module.common.GlobalConfigService;
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
}
