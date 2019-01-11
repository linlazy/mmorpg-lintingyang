package com.linlazy.mmorpg.module.email.service.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * @author linlazy
 */
public abstract class BaseEmailTemplate {

    private static Map<Integer, BaseEmailTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(emailTemplate(),this);
    }

    /**
     * 邮件模板
     * @return 邮件模板
     */
    protected abstract int emailTemplate();

    public static BaseEmailTemplate getEmailTemplate(int emailTemplate){
        return map.get(emailTemplate);
    }

    /**
     * 发送邮件
     * @param actorId 发送目标ID
     * @param jsonObject 可变参数
     * @return 放回发送邮件结果
     */
    public abstract Result<?> sendEmail(long actorId, JSONObject jsonObject);
}
