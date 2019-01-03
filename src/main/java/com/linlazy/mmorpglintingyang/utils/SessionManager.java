package com.linlazy.mmorpglintingyang.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 会话管理类，管理用户与通道连接
 * @author linlazy
 */
public class SessionManager {

    /**
     * 用户标识，通道连接映射
     */
    private static Map<Long, Channel> actorIdChannelMap = new HashMap<>();

    /**
     * 通道连接，用户标识映射
     */
    private static Map<Channel,Long> channelActorIdMap = new HashMap<>();

    /**
     * 绑定用户与通道连接
     * @param actorId
     * @param channel
     */
    public static void bind(long actorId, Channel channel){
        actorIdChannelMap.put(actorId,channel);
        channelActorIdMap.put(channel,actorId);
    }

    /**
     * 解绑用户与通道连接
     * @param channel
     */
    public static void unBind(Channel channel){
        long actorId = channelActorIdMap.get(channel);

        actorIdChannelMap.remove(actorId);
        channelActorIdMap.remove(channel);
    }

    /**
     * 通道是否已绑定用户
     * @param
     * @return
     */
    public static boolean isBind(Channel channel){
        return channelActorIdMap.get(channel) != null;
    }



    /**
     * 用户是否在线
     * @param actorId
     * @return
     */
    public static boolean isOnline(long actorId){
        return actorIdChannelMap.get(actorId) != null;
    }

    public static Channel getChannel(long actorId) {
        return actorIdChannelMap.get(actorId);
    }

    public static long getActorId(Channel channel){
        return channelActorIdMap.get(channel);
    }

    public static Set<Long> getOnlineActorIds() {
        return actorIdChannelMap.keySet();
    }

    public static Set<Channel> getOnlineChannelSet(){
        return channelActorIdMap.keySet();
    }
}
