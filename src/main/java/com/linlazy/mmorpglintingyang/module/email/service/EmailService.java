package com.linlazy.mmorpglintingyang.module.email.service;

import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailService {




    /**
     *
     * 发送邮件
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> sendEmail(long actorId, long targetId) {

        //获取奖励

        return Result.success();
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
