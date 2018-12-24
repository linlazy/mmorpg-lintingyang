package com.linlazy.mmorpglintingyang.module.email.service.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.email.config.EmailConfigService;
import com.linlazy.mmorpglintingyang.module.email.dto.EmailDTO;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 补偿邮件模板
 */
@Component
public class CompensateEmailTemplate extends EmailTemplate {

    @Autowired
    private EmailConfigService emailConfigService;

    @Override
    protected int emailTemplate() {
        return 1;
    }

    @Override
    public Result<?> sendEmail(long actorId, JSONObject jsonObject) {
        int emailTemplateId = jsonObject.getIntValue("emailTemplateId");
        JSONObject emailTemplateConfig = emailConfigService.getEmailTemplateConfig(emailTemplateId);
        if(emailTemplateConfig == null){
            return Result.valueOf("参数有误");
        }

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTitle(emailTemplateConfig.getString("title"));
        emailDTO.setContent(emailTemplateConfig.getString("content"));

        String rewards = emailTemplateConfig.getString("attachment");
        List<Reward> rewardList = emailConfigService.getRewardList(rewards);
        emailDTO.setAttachment(rewardList);
        return Result.success(emailDTO);
    }
}
