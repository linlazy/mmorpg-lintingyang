package com.linlazy.mmorpg.module.email.dto;

import com.linlazy.mmorpg.entity.EmailEntity;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.utils.RewardUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linlazy
 */
@Data
public class EmailDTO {

    /**
     * 邮件ID
     */
    private Long emailId;
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private Boolean readStatus;
    private Boolean rewardStatus;

    /**
     * 附件奖励
     */
    private List<Reward> attachmentRewardList;

    public EmailDTO(EmailEntity emailEntity) {
        emailId = emailEntity.getEmailId();
        title = emailEntity.getTitle();
        content = emailEntity.getContent();
        if(!StringUtils.isEmpty(emailEntity.getRewards())){
            attachmentRewardList = RewardUtils.parseRewards(emailEntity.getRewards());
        }else {
            attachmentRewardList = new ArrayList<>();
        }
        readStatus = emailEntity.isReadStatus();
        rewardStatus = emailEntity.isRewardStatus();
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        if(readStatus){
            stringBuilder.append(String.format("邮件已读")).append("\r\n");
        }else {
            stringBuilder.append(String.format("邮件未读")).append("\r\n");
        }

        if(rewardStatus){
            stringBuilder.append(String.format("邮件已领取")).append("\r\n");
        }else {
            stringBuilder.append(String.format("邮件未领取")).append("\r\n");
        }

        stringBuilder.append(String.format("ID【%d】",emailId)).append("\r\n");
        stringBuilder.append(String.format("标题【%s】",title)).append("\r\n");
        stringBuilder.append(String.format("内容【%s】",content)).append("\r\n");
        stringBuilder.append(String.format("附件奖励")).append("\r\n");
        if(attachmentRewardList != null){
            for(Reward reward: attachmentRewardList){
                stringBuilder.append(String.format("奖励【%s】",reward.toString())).append("\r\n");
            }
        }
        return stringBuilder.toString();
    }
}
