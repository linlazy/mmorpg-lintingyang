package com.linlazy.mmorpg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author linlazy
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert() {
        jdbcTemplate.update("insert into player ( actorId , sceneId , profession , token , username , password , gold , level , hp , mp ) values(  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ) ",
                4194306,0,0,0,0,0,0,0,0,0);
    }

    @Test
    public void update() {
        jdbcTemplate.update("update player  set sceneId= ? ,profession= ? ,token= ? ,username= ? ,password= ? ,gold= ? ,level= ? ,hp= ? ,mp= ?  where actorId =  ? ",
                0,1,null,"username","password",0,0,0,0,4194308);
    }
}
