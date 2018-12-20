package com.linlazy.mmorpglintingyang.module.common.event;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpglintingyang.module.equipment.manager.domain.DressedEquip;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EquipDurabilityEventHandler {

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    /**
     * 如果受到攻击，则穿戴装备减少耐久度
     * @param actorEvent
     */
    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        //如果受到攻击，则穿戴装备减少耐久度
        if(actorEvent.getEventType().equals(EventType.ATTACKED)){
            int attacked = (int)actorEvent.getData();

            new DressedEquip(actorEvent.getActorId()).consumeDurabilityWithAttacked(attacked);
        }

        //如果攻击生效，则穿戴装备减少耐久度
        if(actorEvent.getEventType().equals(EventType.ATTACK)){
            int attack = (int)actorEvent.getData();
            new DressedEquip(actorEvent.getActorId()).consumeDurabilityWithAttacked(attack);
        }
    }
}
