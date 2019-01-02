package com.linlazy.mmorpglintingyang.module.guild.dao;

import com.linlazy.mmorpglintingyang.module.guild.constants.GuildAuthLevel;
import com.linlazy.mmorpglintingyang.module.guild.entity.Guild;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuildDaoTest {

    @Autowired
    GuildDao guildDao;

    @Test
    public void addGuild() {

        Guild guild = new Guild();
        guild.setGuildId(1);
        guild.setAuthLevel(GuildAuthLevel.President);
        guild.setActorId(4194306);
        guildDao.addGuild(guild);
    }


    @Test
    public void getGuild() {

        Guild guild = guildDao.getGuild(1, 4194306);
        assert guild != null;
    }

}