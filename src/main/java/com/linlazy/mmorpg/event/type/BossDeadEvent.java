package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.scene.domain.Boss;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class BossDeadEvent {
    private Boss boss;

    public BossDeadEvent(Boss boss) {
        this.boss = boss;
    }
}
