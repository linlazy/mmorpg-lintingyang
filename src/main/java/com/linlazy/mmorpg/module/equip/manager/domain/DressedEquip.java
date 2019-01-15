package com.linlazy.mmorpg.module.equip.manager.domain;

import com.linlazy.mmorpg.domain.Equip;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 已穿戴装备
 * @author linlazy
 */
@Data
public class DressedEquip {

    private long actorId;

    private Map<Long, Equip> equipMap = new HashMap<>();

}
