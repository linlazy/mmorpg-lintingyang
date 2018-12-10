package com.linlazy.game.module.scene.handler;

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


    public void move(){
        System.out.println("moveCmd");
    }
}
