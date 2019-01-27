package com.linlazy.mmorpg.event.type;

import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class CopySuccessEvent {

    private Copy copy;

    public CopySuccessEvent(Copy copy) {
        this.copy = copy;
    }
}
