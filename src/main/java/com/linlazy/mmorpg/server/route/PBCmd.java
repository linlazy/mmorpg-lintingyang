package com.linlazy.mmorpg.server.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PBCmd {
    short value(); //指令
    Class requestClass(); //请求结构
}
