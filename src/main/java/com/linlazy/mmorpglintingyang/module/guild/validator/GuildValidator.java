package com.linlazy.mmorpglintingyang.module.guild.validator;

import com.linlazy.mmorpglintingyang.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildActorDao;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildActor;
import com.linlazy.mmorpglintingyang.module.guild.manager.GuildManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        GuildActor actorGuildActor = guildActorDao.getGuild(guildId, actorId);
        GuildActor targetGuildActor = guildActorDao.getGuild(guildId, targetId);
        if(actorGuildActor.getAuthLevel() >= GuildAuthLevel.VicePresident
        && actorGuildActor.getAuthLevel() > targetGuildActor.getAuthLevel()
        && actorGuildActor.getAuthLevel() > authLevel){
            return true;
        }
        return false;
    }

    public boolean hasAuth(long actorId, long targetId) {
        long guildId =  guildManager.getGuildId(actorId);
        GuildActor actorGuildActor = guildActorDao.getGuild(guildId, actorId);
        GuildActor targetGuildActor = guildActorDao.getGuild(guildId, targetId);
        if(actorGuildActor.getAuthLevel() >= GuildAuthLevel.VicePresident
                && actorGuildActor.getAuthLevel() > targetGuildActor.getAuthLevel()){
            return true;
        }
        return false;
    }
}
