package com.linlazy.game.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class ModuleHandler {
    protected Map<String, Method> methodMap = new HashMap<>();

    public  Method getMethod(String cmd){
        return methodMap.get(cmd);
    }
}
