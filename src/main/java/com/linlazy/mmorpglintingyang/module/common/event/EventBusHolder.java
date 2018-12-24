package com.linlazy.mmorpglintingyang.module.common.event;

import com.google.common.eventbus.EventBus;

public class EventBusHolder {

    private static EventBus eventBus = new EventBus();

    public static void register(Object o){
        eventBus.register(o);
    }

    public static void post(Object o){
        eventBus.post(o);
    }
}
