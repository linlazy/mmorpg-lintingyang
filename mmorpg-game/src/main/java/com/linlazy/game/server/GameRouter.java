package com.linlazy.game.server;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 游戏路由类
 */
public class GameRouter {

    public static void handleRoute(JSONObject jsonObject){
        //获取模块处理器
        String module = jsonObject.getString("module");
        ModuleHandler moduleHandler = ModuleManager.INSTANCE.getModuleHandler(module);

        //获取处理方法
        String cmd = jsonObject.getString("command");
        Method method = moduleHandler.getMethod(cmd);

        //执行处理方法
        try {
            method.invoke(moduleHandler);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
