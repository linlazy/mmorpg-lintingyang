package com.linlazy.mmorpglintingyang.server.db;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 字体字段信息
 * @author linlazy
 */
@Data
public class EntityFieldInfo {

    private List<FieldInfo> identity = new ArrayList<>();

    private List<FieldInfo> ordinary = new ArrayList<>();

    private List<FieldInfo> all = new ArrayList<>();
}
