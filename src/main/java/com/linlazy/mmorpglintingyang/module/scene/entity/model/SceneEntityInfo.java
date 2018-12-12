package com.linlazy.mmorpglintingyang.module.scene.entity.model;

import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityTypeStatus;
import lombok.Data;

import java.util.Objects;

@Data
public class SceneEntityInfo {


    /**
     * 实体ID
     */
    private long entityId;
    /**
     * 实体类型
     */
    private int entityType;
    /**
     * 实体状态
     */
    private int status;

    /**
     * 实体血量
     */
    private int hp;

    public void attacked(int decreaseHP){
        synchronized (this){
            if(decreaseHP > this.hp){
                this.hp = 0 ;
                this.status = SceneEntityTypeStatus.DEATH;
            }
        }
    }


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
