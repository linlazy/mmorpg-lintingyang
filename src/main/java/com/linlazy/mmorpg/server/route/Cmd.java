package com.linlazy.mmorpg.server.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String value();
    boolean auth() default true;
    boolean enterMap() default true;
}
