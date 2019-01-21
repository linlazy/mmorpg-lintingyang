package com.linlazy.mmorpg.server.route;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.handler.CmdHandler;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.threadpool.BusinessTask;
import com.linlazy.mmorpg.server.threadpool.ThreadOrderPool;
import com.linlazy.mmorpg.utils.SessionManager;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 游戏路由类
 * @author linlazy
 */
@Data
@Component
public class GameRouter {

    @Autowired
    private CmdHandler cmdHandler;

    private static ThreadOrderPool threadOrderPool = new ThreadOrderPool(16);

    private static Map<String,Method> map = new HashMap<>();


    @PostConstruct
    public void init(){
        //获取处理方法
        Method[] methods =cmdHandler.getClass().getMethods();
        for (Method method : methods) {
            Cmd cmd = method.getAnnotation(Cmd.class);
            if (cmd != null) {
                map.put(cmd.value(),method);
            }
        }
    }


    public  Result<?> handleRoute(JSONObject jsonObject) throws InterruptedException, ExecutionException, InvocationTargetException, IllegalAccessException {

        String command = jsonObject.getString("command");
        Method method = map.get(command);
        if(method == null){
            throw  new RuntimeException(String.format("none match [%s]",command));
        }

        Cmd cmd = method.getAnnotation(Cmd.class);
        //权限
        if (cmd.auth()) {
            Channel channel = jsonObject.getObject("channel", Channel.class);
            if (SessionManager.getActorId(channel) == null) {
                return Result.valueOf("无权限执行此操作,请登录");
            }
            jsonObject.put("actorId", SessionManager.getActorId(channel));
            ThreadOrderPool.threadOrderPoolMap.putIfAbsent("actor",threadOrderPool);
            Future<Result<?>> execute = threadOrderPool.execute(new BusinessTask(SessionManager.getActorId(channel)) {
                @Override
                public Result<?> call() throws Exception {
                    return (Result<?>) method.invoke(cmdHandler, jsonObject);
                }

            });
            return execute.get();
        }

        return (Result<?>) method.invoke(cmdHandler, jsonObject);
    }
}
