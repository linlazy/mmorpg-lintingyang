package com.linlazy.mmorpg.utils;

import com.linlazy.mmorpg.MmoRpgLinTingYangApplication;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author linlazy
 */
public class SpringContextUtil {

    private static ConfigurableApplicationContext applicationContext;

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
         if(applicationContext == null){
             String[] args =new String[0];
             ConfigurableApplicationContext app = SpringApplication.run(MmoRpgLinTingYangApplication.class, args);
             applicationContext = app;
         }
         return applicationContext;
    }
}
