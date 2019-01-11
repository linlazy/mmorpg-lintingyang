package com.linlazy.mmorpg.module.guild.validator;

import com.linlazy.mmorpg.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpg.module.guild.dao.GuildActorDao;
import com.linlazy.mmorpg.module.guild.entity.GuildActor;
import com.linlazy.mmorpg.module.guild.manager.GuildManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class GuildValidator {

    @Autowired
    private GuildManager guildManager;
    @Autowired
    private GuildActorDao guildActorDao;

    public boolean hasGuild(long actorId) {
        return guildManager.hasGuild(actorId);
    }


    public boolean hasAuth(long actorId,long targetId,int authLevel){
        long guildId =  guildManager.getGuildId(actorId);
        GuildActor actorGuildActor = guildActorDao.getGuildActor(guildId, actorId);
        GuildActor targetGuildActor = guildActorDao.getGuildActor(guildId, targetId);
        if(actorGuildActor.getAuthLevel() >= GuildAuthLevel.VICE_PRESIDENT
        && actorGuildActor.getAuthLevel() > targetGuildActor.getAuthLevel()
        && actorGuildActor.getAuthLevel() > authLevel){
            return true;
        }
        return false;
    }

    public boolean hasAuth(long actorId, long targetId) {
        long guildId =  guildManager.getGuildId(actorId);
        GuildActor actorGuildActor = guildActorDao.getGuildActor(guildId, actorId);
        GuildActor targetGuildActor = guildActorDao.getGuildActor(guildId, targetId);
        if(actorGuildActor.getAuthLevel() >= GuildAuthLevel.VICE_PRESIDENT
                && actorGuildActor.getAuthLevel() > targetGuildActor.getAuthLevel()){
            return true;
        }
        return false;
    }
}