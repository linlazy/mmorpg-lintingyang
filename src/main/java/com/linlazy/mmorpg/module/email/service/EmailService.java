package com.linlazy.mmorpg.module.email.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.EmailDAO;
import com.linlazy.mmorpg.module.player.domain.PlayerEmail;
import com.linlazy.mmorpg.module.email.dto.EmailDTO;
import com.linlazy.mmorpg.entity.EmailEntity;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.email.push.EmailPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.email.template.BaseEmailTemplate;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.utils.RewardUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 邮件服务类
 * @author linlazy
 */
@Component
public class EmailService {

    @Autowired
    private EmailDAO emailDAO;
    @Autowired
    private RewardService rewardService;

    /**
     * 玩家邮件缓存
     */
    public static LoadingCache<Long, PlayerEmail> playerEmailCache = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Long,  PlayerEmail>() {
                @Override
                public  PlayerEmail load(Long actorId) {

                    EmailDAO emailDAO = SpringContextUtil.getApplicationContext().getBean(EmailDAO.class);


                    PlayerEmail playerEmail = new PlayerEmail();

                    List<EmailEntity> emailSet = emailDAO.getReceiveEmailSet(actorId);
                    for(EmailEntity emailEntity: emailSet){
                        if(emailEntity.getExpireTime() < DateUtils.getNowMillis()){
                            emailDAO.deleteQueue(emailEntity);
                        }else {
                            playerEmail.getMap().put(emailEntity.getEmailId(),emailEntity);
                        }
                    }
                    return playerEmail;
                }
            });

    /**
     * 订阅事件
     */
    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    /**
     * 关注登录事件
     * @param actorEvent
     */
    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        //登录时，将邮件推送给接受者
        if(actorEvent.getEventType().equals(EventType.LOGIN)){
            try {
                PlayerEmail playerEmail = playerEmailCache.get(actorEvent.getActorId());
                playerEmail.getMap().values().stream()
                        .forEach(emailEntity -> {
                            EmailPushHelper.pushEmail(emailEntity.getReceiverId(),new EmailDTO(emailEntity).toString());
                        });
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

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
     * @param emailId
     * @return
     */
    public Result<?> readEmail(long actorId, long emailId) {
        PlayerEmail playerEmail = getPlayerEmail(actorId);
        EmailEntity emailEntity = playerEmail.getMap().get(emailId);
        emailEntity.setReadStatus(true);
        emailDAO.updateQueue(emailEntity);
        EmailPushHelper.pushEmail(actorId,new EmailDTO(emailEntity).toString());
        return Result.success();
    }


    /**
     * 领取邮件奖励
     * @param actorId
     * @param emailId
     * @return
     */
    public Result<?> rewardEmail(long actorId, long emailId) {
        PlayerEmail playerEmail = getPlayerEmail(actorId);
        EmailEntity emailEntity = playerEmail.getMap().get(emailId);
        List<Reward> rewardList = RewardUtils.parseRewards(emailEntity.getRewards());
        rewardService.addRewardList(actorId,rewardList);

        emailEntity.setRewardStatus(true);
        emailDAO.updateQueue(emailEntity);
        EmailPushHelper.pushEmail(actorId,new EmailDTO(emailEntity).toString());
        return Result.success();
    }

    /**
     * 删除邮件
     * @param actorId
     * @param emailId
     * @return
     */
    public Result<?> deleteEmail(long actorId, long emailId) {
        PlayerEmail playerEmail = getPlayerEmail(actorId);
        EmailEntity emailEntity = playerEmail.getMap().remove(emailId);
        emailDAO.deleteQueue(emailEntity);
        EmailPushHelper.pushEmail(actorId,new EmailDTO(emailEntity).toString());
        return Result.success();
    }

    public PlayerEmail getPlayerEmail(long actorId){
        try {
            return playerEmailCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
