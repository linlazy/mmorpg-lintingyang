package com.linlazy.mmorpg.module.item.manager.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemEntityDAOTest {

    @Autowired
    private ItemDao itemDao;

    @Test
    public void getItemSet() {
        itemDao.getItemSet(3);
    }

}