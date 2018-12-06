package com.linlazy.db.entity;

public class Scene {
    /**
     * 角色ID
     */
    private long actorId;
    /**
     * 当前场景ID;
     */
    private long currentSceneId;


    public Scene(long actorId, long currentSceneId) {
        this.actorId = actorId;
        this.currentSceneId = currentSceneId;
    }

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public long getCurrentSceneId() {
        return currentSceneId;
    }

    public void setCurrentSceneId(long currentSceneId) {
        this.currentSceneId = currentSceneId;
    }
}
