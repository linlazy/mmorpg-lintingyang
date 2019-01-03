package com.linlazy.mmorpglintingyang.utils;

import org.springframework.beans.BeansException;
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
        return applicationContext;
    }
}
