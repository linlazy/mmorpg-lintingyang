package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.entity.EmailEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public class PlayerEmail {

    private Map<Long, EmailEntity> map = new HashMap<>();
}
