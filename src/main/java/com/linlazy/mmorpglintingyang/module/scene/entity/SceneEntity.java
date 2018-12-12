package com.linlazy.mmorpglintingyang.module.scene.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linlazy.mmorpglintingyang.module.scene.entity.model.SceneEntityInfo;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Data
public class SceneEntity {

    private int sceneId;

    /**
     * 场景实体信息集合
     */
    private String entities;

    private Set<SceneEntityInfo> entitySet = new HashSet<>();

    public Set<SceneEntityInfo> getEntitySet() {
        if(!StringUtils.isEmpty(entities)){
            entitySet = JSON.parseObject(entities, new TypeReference<HashSet<SceneEntityInfo>>() {});
        }
        return entitySet;
    }

}
