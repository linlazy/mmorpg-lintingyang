//package com.linlazy.mmorpg.module.guild.dao;
//
//import com.linlazy.mmorpg.module.guild.constants.GuildAuthLevel;
//import com.linlazy.mmorpg.module.guild.entity.GuildActor;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class GuildEntityActorActorDaoTest {
//
//    @Autowired
//    GuildActorDao guildActorDao;
//
//    @Test
//    public void addGuild() {
//
//        GuildActor guildActor = new GuildActor();
//        guildActor.setGuildId(1);
//        guildActor.setAuthLevel(GuildAuthLevel.PRESIDENT);
//        guildActor.setActorId(4194306);
//        guildActorDao.addGuildActor(guildActor);
//    }
//
//
//    @Test
//    public void getGuild() {
//
//        GuildActor guildActor = guildActorDao.getGuildActor(1, 4194306);
//        assert guildActor != null;
//    }
//
//}