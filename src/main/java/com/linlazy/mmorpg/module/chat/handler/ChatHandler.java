package com.linlazy.mmorpg.module.chat.handler;

import com.alibaba.fastjson.JSON;
import com.linlazy.mmorpg.module.chat.channel.BaseChatChannel;
import com.linlazy.mmorpg.pb.PBModule;
import com.linlazy.mmorpg.pb.PBModule1Chat;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Module;
import com.linlazy.mmorpg.server.route.PBCmd;

/**
 * @author linlazy
 */
@Module(PBModule.Module.CHAT_VALUE)
public class ChatHandler {

    @PBCmd(value = PBModule1Chat.ClientCmd.ChatClientCmd_SendChat_VALUE,requestClass = PBModule1Chat.ReqSendChat.class)
    public Result<?> sendChat(long actorId,PBModule1Chat.ReqSendChat reqSendChat){
        BaseChatChannel chatChannel = BaseChatChannel.getChatChannel(reqSendChat.getChatType());
        if(chatChannel == null){
            return Result.valueOf("参数错误");
        }
        Result<?> result = chatChannel.sendChat(actorId, JSON.parseObject(reqSendChat.getExt()));
        if(result.isFail()){
            return Result.valueOf(result.getCode());
        }
        return Result.success();
    }
}
