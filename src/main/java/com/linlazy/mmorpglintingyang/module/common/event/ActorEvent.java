package com.linlazy.mmorpglintingyang.module.common.event;

import lombok.Data;

@Data
public class ActorEvent<T> {
    private EventType eventType;

    private long actorId;

    private T data;
}
