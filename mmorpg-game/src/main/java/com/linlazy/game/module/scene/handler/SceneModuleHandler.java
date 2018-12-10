package com.linlazy.game.module.scene.handler;

import com.linlazy.game.module.common.Result;
import com.linlazy.game.module.scene.service.SceneService;
import com.linlazy.game.server.ModuleHandler;

public class SceneModuleHandler extends ModuleHandler {
    public SceneModuleHandler() {
        try {
            //构建cmd与方法映射
            methodMap.put("move",this.getClass().getMethod("move"));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private SceneService sceneService = new SceneService();

    public Result<?> move(){
        System.out.println("moveCmd");
        //todo
        int targetSceneId = 0;
        return sceneService.moveTo(targetSceneId);
    }
}
