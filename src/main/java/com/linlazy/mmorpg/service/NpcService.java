package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.service.NpcConfigService;
import com.linlazy.mmorpg.domain.Npc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author linlazy
 */
@Component
public class NpcService {

    @Autowired
    private NpcConfigService npcConfigService;

    /**
     * 获取场景怪物
     * @return
     * @param sceneId
     */
    public Set<Npc> getNPCDoBySceneId(int sceneId){
        Set<Npc> npcSet = new HashSet<>();

        List<JSONObject>  sceneNpcConfigs= npcConfigService.getNpcBySceneId(sceneId);
        for(int i = 0; i < sceneNpcConfigs.size(); i++){
            JSONObject sceneNpcConfig = sceneNpcConfigs.get(i);
            int npcId = sceneNpcConfig.getIntValue("npcId");
            String name = sceneNpcConfig.getString("name");
            Npc npc = new Npc();
            npc.setNpcId(npcId);
            npc.setName(name);
            npc.setSceneId(sceneId);

            npcSet.add(npc);
        }

        return npcSet;
    }


    public boolean hasNPC(int sceneId, int npcId) {
        return npcConfigService.hasNPC(sceneId,npcId);
    }
}
