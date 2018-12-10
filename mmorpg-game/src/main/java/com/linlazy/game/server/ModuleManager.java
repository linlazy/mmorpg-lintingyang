package com.linlazy.game.server;

import com.linlazy.game.module.scene.handler.SceneModuleHandler;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {

    public static final ModuleManager INSTANCE = new ModuleManager();

    private Map<String,ModuleHandler> moduleHandlerMap = new HashMap<>();

    public ModuleManager() {
        //构建module与handler映射
        moduleHandlerMap.put("scene",new SceneModuleHandler());
    }

    public ModuleHandler getModuleHandler(String module){
        return moduleHandlerMap.get(module);
    }
}
