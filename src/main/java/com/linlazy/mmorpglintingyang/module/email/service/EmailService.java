package com.linlazy.mmorpglintingyang.module.email.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.email.service.template.BaseEmailTemplate;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class EmailService {




    /**
     *
     * 发送邮件
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> sendEmail(long actorId,JSONObject jsonObject) {
        int emailTemplateId = jsonObject.getIntValue("emailTemplateId");
        BaseEmailTemplate emailTemplate = BaseEmailTemplate.getEmailTemplate(emailTemplateId);
        return emailTemplate.sendEmail(actorId,jsonObject);
    }

    /**
     * 读取邮件
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> readEmail(long actorId, long targetId) {
        return Result.success();
    }
}
