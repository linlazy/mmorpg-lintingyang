package com.linlazy.mmorpg.module.item.type;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.effect.ConsumeType;
import com.linlazy.mmorpg.module.item.effect.hp.BaseResumeHp;
import com.linlazy.mmorpg.module.item.effect.mp.BaseResumeMp;
import com.linlazy.mmorpg.module.item.effect.transport.BaseTransport;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.stereotype.Component;

/**
 *
 * 消耗道具：用于直接回复生命、体力、道具耐久或增加道具属性，以及合成其他物品用的道具。
 * @author linlazy
 */
@Component
public class ConsumeItemType extends BaseItem{
    @Override
    protected Integer itemType() {
        return ItemType.CONSUME;
    }

    @Override
    public Result<?> useItem(long actorId,Item item) {
        JSONObject extJsonObject = item.getExtJsonObject();
        int consumeType = item.getConsumeType();
        switch (consumeType){
            case ConsumeType
                    .resumeHP:
                BaseResumeHp resumeHpType = BaseResumeHp.getBaseResumeHp(extJsonObject.getIntValue("resumeHpType"));
                return resumeHpType.doResumeHp(actorId,item);
            case ConsumeType
                    .resumeMP:
                BaseResumeMp resumeMpType = BaseResumeMp.getBaseResumeMp(extJsonObject.getIntValue("resumeMpType"));
                return resumeMpType.doResumeMp(actorId,item);
            case ConsumeType
                    .transport:
                BaseTransport transportType = BaseTransport.getBaseTransport(extJsonObject.getIntValue("transportType"));
                return transportType.doTransport(actorId,item);
            default:

        }

       return Result.success();
    }
}