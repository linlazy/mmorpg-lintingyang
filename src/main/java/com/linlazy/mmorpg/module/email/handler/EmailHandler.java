package com.linlazy.mmorpg.module.email.handler;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.email.service.EmailService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Module("email")
@Component
public class EmailHandler {

    @Autowired
    private EmailService emailService;

    /**
     * 发送邮件
     * @param jsonObject
     * @return
     */
    @Cmd("send")
    public Result<?> send(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return emailService.sendEmail(actorId,jsonObject);
    }
    /**
     * 读取邮件
     * @param jsonObject
     * @return
     */
    @Cmd("read")
    public Result<?> read(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return emailService.readEmail(actorId,targetId);
    }

}