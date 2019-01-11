package com.linlazy.mmorpg.server.db.annotation;

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
}
