package com.linlazy.game.common;

import io.netty.util.AttributeKey;

public class ChannelAttributeKey {
    public static final AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
