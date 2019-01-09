package com.linlazy.mmorpglintingyang.server.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linlazy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cloumn {
    /**
     * 是否为主键
     * @return
     */
    boolean pk() default false;

    /**
     * 是否为辅助键
     * @return
     */
    boolean fk() default false;
}
