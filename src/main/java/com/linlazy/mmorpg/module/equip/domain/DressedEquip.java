package com.linlazy.mmorpg.module.equip.domain;

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
