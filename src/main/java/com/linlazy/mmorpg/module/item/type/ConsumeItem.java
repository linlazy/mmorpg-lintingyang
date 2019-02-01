package com.linlazy.mmorpg.module.item.type;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.type.consume.ConsumeType;
import com.linlazy.mmorpg.module.item.type.consume.hp.BaseResumeHp;
import com.linlazy.mmorpg.module.item.type.consume.mp.BaseResumeMp;
import com.linlazy.mmorpg.module.item.type.consume.transport.BaseTransport;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.stereotype.Component;

/**
 *
 * 消耗道具：用于直接回复生命、体力、道具耐久或增加道具属性，以及合成其他物品用的道具。
 * @author linlazy
 */
@Component
public class ConsumeItem extends BaseItem{
    @Override
    protected Integer itemType() {
        return ItemType.CONSUME;
    }

    @Override
    public Result<?> useItem(long actorId,Item item) {
        JSONObject extConfig = item.getExtConfig();
        int consumeType = extConfig.getIntValue("consumeType");
        switch (consumeType){
            case ConsumeType
                    .resumeHP:
                BaseResumeHp resumeHpType = BaseResumeHp.getBaseResumeHp(extConfig.getIntValue("resumeHpType"));
                return resumeHpType.doResumeHp(actorId,item);
            case ConsumeType
                    .resumeMP:
                BaseResumeMp resumeMpType = BaseResumeMp.getBaseResumeMp(extConfig.getIntValue("resumeMpType"));
                return resumeMpType.doResumeMp(actorId,item);
            case ConsumeType
                    .transport:
                BaseTransport transportType = BaseTransport.getBaseTransport(extConfig.getIntValue("transportType"));
                return transportType.doTransport(actorId,item);
            default:

        }

       return Result.success();
    }
}
