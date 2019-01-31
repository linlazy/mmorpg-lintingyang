package com.linlazy.mmorpg.dao;

import com.linlazy.mmorpg.entity.EmailEntity;
import com.linlazy.mmorpg.server.db.dao.EntityDAO;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
@Data
public class EmailDAO extends EntityDAO<EmailEntity> {

    private AtomicLong maxEmailId;


    @PostConstruct
    public void init(){
        if(selectMaxEmailId() == null){
            maxEmailId = new AtomicLong(0);
        }else {
            maxEmailId = new AtomicLong(selectMaxEmailId());
        }
    }

    @Override
    protected Class<EmailEntity> forClass() {
        return EmailEntity.class;
    }

    /**
     * 获取最大邮件ID
     * @return  返回最大邮件ID
     */
    public Long selectMaxEmailId(){
        return jdbcTemplate.queryForObject("select max(emailId) from email",Long.class);
    }

    /**
     * 获取接受邮件信息者的信息集合
     *
     * @param receiver  邮件信息接受者
     * @return 返回接受者的信息邮件
     */
    public List<EmailEntity> getReceiveEmailSet(long receiver){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from email where receiverId = ?", receiver);
        return maps.stream()
                .map(map ->{
                    EmailEntity emailEntity = new EmailEntity();

                    emailEntity.setEmailId((Long) map.get("emailId"));
                    emailEntity.setReceiverId((Long) map.get("receiverId"));
                    emailEntity.setSenderId((Long) map.get("senderId"));
                    emailEntity.setTitle((String) map.get("title"));
                    emailEntity.setContent((String) map.get("content"));
                    emailEntity.setRewards((String) map.get("rewards"));
                    emailEntity.setReadStatus((Boolean) map.get("readStatus"));
                    emailEntity.setRewardStatus((Boolean) map.get("rewardStatus"));
                    emailEntity.setSendTime((Long) map.get("sendTime"));
                    emailEntity.setExpireTime((Long) map.get("expireTime"));
                    emailEntity.setType((Integer) map.get("effect"));

                    return emailEntity;
                })
                .collect(Collectors.toList());
    }

}
