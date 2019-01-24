package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public class PlayerShop {

    private Map<Long,Shop> map = new HashMap<>();
}
