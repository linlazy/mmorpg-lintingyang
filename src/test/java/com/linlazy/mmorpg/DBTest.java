package com.linlazy.mmorpg;

import com.linlazy.mmorpg.module.dao.PlayerDAO;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * @author linlazy
 */
public class DBTest {


    @Test
    public void contextLoads() {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        PlayerDAO playerDAO = applicationContext.getBean(PlayerDAO.class);
        PlayerEntity playerEntity = playerDAO.getPlayerEntityByUsername("linlazy");
        assert playerEntity != null;
    }
}
