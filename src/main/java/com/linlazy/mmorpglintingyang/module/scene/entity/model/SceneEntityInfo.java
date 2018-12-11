package com.linlazy.mmorpglintingyang.module.scene.entity.model;

import lombok.Data;

import java.util.Objects;

@Data
public class SceneEntityInfo {

    private long entityId;

    private int entityType;

    public SceneEntityInfo(long entityId, int entityType) {
        this.entityId = entityId;
        this.entityType = entityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneEntityInfo that = (SceneEntityInfo) o;
        return entityId == that.entityId &&
                entityType == that.entityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, entityType);
    }
}
