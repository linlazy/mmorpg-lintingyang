package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.domain.Boss;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class BossEvent {
    private Boss boss;

    public BossEvent(Boss boss) {
        this.boss = boss;
    }
}
