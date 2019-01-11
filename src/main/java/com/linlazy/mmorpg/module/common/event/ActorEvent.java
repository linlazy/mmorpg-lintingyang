package com.linlazy.mmorpg.module.common.event;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class ActorEvent<T> {
    private long actorId;
    private EventType eventType;
    private T data;

    public ActorEvent(long actorId, EventType eventType) {
        this.actorId = actorId;
        this.eventType = eventType;
    }

    public ActorEvent(long actorId, EventType eventType,T data) {
        this.actorId = actorId;
        this.eventType = eventType;
        this.data = data;
    }
}
