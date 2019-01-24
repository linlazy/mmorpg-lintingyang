package com.linlazy.mmorpg.template.email;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.EmailType;
import com.linlazy.mmorpg.dao.EmailDAO;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerEmail;
import com.linlazy.mmorpg.dto.EmailDTO;
import com.linlazy.mmorpg.entity.EmailEntity;
import com.linlazy.mmorpg.file.service.EmailConfigService;
import com.linlazy.mmorpg.push.EmailPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.EmailService;
import com.linlazy.mmorpg.service.PlayerService;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 赠送模板
 * @author linlazy
 */
@Component
public class EmailTemplate2 extends BaseEmailTemplate {

    @Autowired
    private EmailConfigService emailConfigService;
    @Autowired
    private EmailDAO emailDAO;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PlayerService playerService;

    @Override
    protected int emailTemplate() {
        return 2;
    }




    @Override
    public Result<?> sendEmail(long actorId, JSONObject jsonObject) {
        int emailTemplateId = jsonObject.getIntValue("emailTemplateId");
        JSONObject emailTemplateConfig = emailConfigService.getEmailTemplateConfig(emailTemplateId);
        if(emailTemplateConfig == null){
            return Result.valueOf("参数有误");
        }
        AtomicLong maxEmailId = emailDAO.getMaxEmailId();


        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmailId(maxEmailId.incrementAndGet());
        emailEntity.setSenderId(actorId);
        emailEntity.setReceiverId(jsonObject.getLongValue("targetId"));
        emailEntity.setTitle(emailTemplateConfig.getString("title"));
        Player player = playerService.getPlayer(actorId);
        emailEntity.setContent(String.format( emailTemplateConfig.getString("content"),player.getName()));
        emailEntity.setSendTime(DateUtils.getNowMillis());
        emailEntity.setExpireTime(DateUtils.getNowMillis() + 86400 * 7);
        emailEntity.setRewards(emailTemplateConfig.getString("attachment"));
        emailEntity.setReadStatus(false);
        emailEntity.setRewardStatus(false);
        emailEntity.setType(EmailType.SYSTEM);


        if(SessionManager.isOnline(emailEntity.getReceiverId())){
            EmailPushHelper.pushEmail(emailEntity.getReceiverId(),new EmailDTO(emailEntity).toString());
        }
        PlayerEmail playerEmail = emailService.getPlayerEmail(actorId);
        playerEmail.getMap().put(emailEntity.getEmailId(),emailEntity);
        emailDAO.insertQueue(emailEntity);


        return Result.success();
    }
}
