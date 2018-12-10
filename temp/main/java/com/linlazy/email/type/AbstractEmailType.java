package com.linlazy.email.type;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEmailType {

    private Map<Integer,AbstractEmailType> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(emailType(),this);
    }

    /**
     * 邮件类型，由子类决定
     * @return
     */
    protected abstract int emailType();

    /**
     * 发送邮件
     */
    public abstract void sendEmail();
}
