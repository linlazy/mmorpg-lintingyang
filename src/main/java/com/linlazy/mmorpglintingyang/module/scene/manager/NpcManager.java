package com.linlazy.mmorpglintingyang.module.scene.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.config.NpcConfigService;
import com.linlazy.mmorpglintingyang.module.scene.domain.NpcDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author linlazy
 */
@Component
public class NpcManager {

    @Autowired
    private NpcConfigService npcConfigService;

    /**
     * 获取场景怪物
     * @return
     * @param sceneId
     */
    public Set<NpcDo> getNPCDoBySceneId(int sceneId){
        Set<NpcDo> npcDoSet = new HashSet<>();

        List<JSONObject>  sceneNpcConfigs= npcConfigService.getNpcBySceneId(sceneId);
        for(int i = 0; i < sceneNpcConfigs.size(); i++){
            JSONObject sceneNpcConfig = sceneNpcConfigs.get(i);
            int npcId = sceneNpcConfig.getIntValue("npcId");
            String name = sceneNpcConfig.getString("name");
            NpcDo npcDo = new NpcDo();
            npcDo.setNpcId(npcId);
            npcDo.setName(name);
            npcDo.setSceneId(sceneId);

            npcDoSet.add(npcDo);
        }

        return npcDoSet;
    }


}
