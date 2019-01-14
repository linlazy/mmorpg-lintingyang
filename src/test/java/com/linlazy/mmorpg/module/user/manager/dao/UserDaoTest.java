package com.linlazy.mmorpg.module.user.manager.dao;

import com.linlazy.mmorpg.dao.PlayerDAO;
import com.linlazy.mmorpg.entity.PlayerEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {


    @Autowired
    private PlayerDAO userDao;

    @Test
    public void getUserByUsername() {
        String username = "linlazy";
        PlayerEntity user = userDao.getUserByUsername(username);
        System.out.println(user);
    }

    @Test
    public void createUser(){
        String username = "register";
        String password = "123456";
        String uuid = UUID.randomUUID().toString().substring(0,20);
        PlayerEntity user = new PlayerEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setToken(uuid);
        user.setActorId(4194306);
        userDao.insertQueue(user);
    }

    @Test
    public void getMaxActorId(){
    }
}