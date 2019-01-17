package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Copy;
import lombok.Data;

/**
 * 副本BOSS死亡事件
 * @author linlazy
 */
@Data
public class CopyBossDeadEvent {

    private Copy copy;

    public CopyBossDeadEvent(Copy copy) {
        this.copy = copy;
    }
}
