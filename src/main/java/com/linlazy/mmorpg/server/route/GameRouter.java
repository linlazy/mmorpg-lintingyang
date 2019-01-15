package com.linlazy.mmorpg.server.route;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.handler.CmdHandler;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SessionManager;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 游戏路由类
 * @author linlazy
 */
public class GameRouter {

    public static Result<?> handleRoute(JSONObject jsonObject){

        //获取模块处理器
        CmdHandler cmdHandler = SpringContextUtil.getApplicationContext().getBean(CmdHandler.class);

                //获取处理方法
                Method[] methods = cmdHandler.getClass().getMethods();
                for(Method method :methods){
                    Cmd cmd = method.getAnnotation(Cmd.class);
                    if(cmd != null){
                        String command = jsonObject.getString("command");
                        if(cmd.value().equals(command)){
                            //执行处理方法

                            //权限
                            if(cmd.auth()){
                                Channel channel = jsonObject.getObject("channel", Channel.class);
                                if(SessionManager.getActorId(channel) == null){
                                    return Result.valueOf("无权限执行此操作,请登录");
                                }
                                jsonObject.put("actorId",SessionManager.getActorId(channel));
                            }

                            try {
                                return  (Result<?>) method.invoke(cmdHandler,jsonObject);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        return null;
    }
}
