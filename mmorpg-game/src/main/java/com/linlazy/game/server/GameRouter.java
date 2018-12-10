package com.linlazy.game.server;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.game.module.common.Result;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 游戏路由类
 */
public class GameRouter {

    public static Result<?> handleRoute(JSONObject jsonObject){

        //获取模块处理器
        String module = jsonObject.getString("module");
        ModuleHandler moduleHandler = ModuleManager.INSTANCE.getModuleHandler(module);

        //获取处理方法
        String cmd = jsonObject.getString("command");
        Method method = moduleHandler.getMethod(cmd);

        //执行处理方法

        try {
            return  (Result<?>) method.invoke(moduleHandler);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
