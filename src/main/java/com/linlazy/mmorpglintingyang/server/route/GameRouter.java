package com.linlazy.mmorpglintingyang.server.route;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 游戏路由类
 */
public class GameRouter {

    public static Result<?> handleRoute(JSONObject jsonObject){

        //获取模块处理器
        String moduleKey = jsonObject.getString("module");
        Map<String, Object> moduleHandlers = SpringContextUtil.getApplicationContext().getBeansWithAnnotation(Module.class);
        for(Object moduleHandler : moduleHandlers.values()){
            Module module = moduleHandler.getClass().getAnnotation(Module.class);

            if(module.value().equals(moduleKey)){
                //获取处理方法
                Method[] methods = moduleHandler.getClass().getMethods();
                for(Method method :methods){
                    Cmd cmd = method.getAnnotation(Cmd.class);
                    String command = jsonObject.getString("command");
                    if(cmd.value().equals(command)){
                        //执行处理方法

                        //权限
                        if(cmd.auth()){
                            long actorId = jsonObject.getLong("actorId");
                            if(!SessionManager.isOnline(actorId)){
                                return Result.valueOf("未登录，无权限执行此操作");
                            }
                        }

                        try {
                            return  (Result<?>) method.invoke(moduleHandler,jsonObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return null;
    }
}
