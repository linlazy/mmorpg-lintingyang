package com.linlazy.mmorpglintingyang.module.email.service.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


public abstract class EmailTemplate {

    private static Map<Integer, EmailTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(emailTemplate(),this);
    }

    protected abstract int emailTemplate();

    public static EmailTemplate getEmailTemplate(int emailTemplate){
        return map.get(emailTemplate);
    }

    public abstract Result<?> sendEmail(long actorId, JSONObject jsonObject);
}
