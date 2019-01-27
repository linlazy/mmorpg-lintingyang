package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import lombok.Data;

/**
 * 副本定时刷新小怪
 * @author linlazy
 */
@Data
public class CopyRefreshMonsterEvent {
    private Copy copy;

    public CopyRefreshMonsterEvent(Copy copy) {
        this.copy = copy;
    }
}
