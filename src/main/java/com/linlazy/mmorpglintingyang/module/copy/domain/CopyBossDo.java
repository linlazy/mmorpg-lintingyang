package com.linlazy.mmorpglintingyang.module.copy.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;

/**
 * 副本BOSS
 */
@Data
public class CopyBossDo {
    /**
     * 副本ID
     */
    private int copyId;
    /**
     *  boss标识
     */
    private int bossId;

    private boolean dead;


    public CopyBossDo(int sceneId,int copyId) {
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(sceneId).getJSONObject("boss");
        bossId = copyConfig.getIntValue("bossId");
    }


}
