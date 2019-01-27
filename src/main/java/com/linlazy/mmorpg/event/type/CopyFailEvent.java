package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class CopyFailEvent {

    private Copy copy;

    public CopyFailEvent(Copy copy) {
        this.copy = copy;
    }
}
