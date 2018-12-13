package com.linlazy.mmorpglintingyang.module.item.manager.dao;

import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemDaoTest {

    @Autowired
    private ItemDao itemDao;

    @Test
    public void getItemSet() {
        Set<Item> itemSet = itemDao.getItemSet(3);
    }

}