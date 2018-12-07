package com.linlazy.game.protocol.constants;

public interface RequestCommand {
    /**
     * 登录指令
     */
    byte LOGIN_REQUEST = 1;

    /**
     * 登出指令
     */
    byte LOGOUT_REQUEST = 2;

    /**
     * 移动场景指令
     */
    byte MOVE_SCENE_REQUEST = 3;
    /**
     * 注册指令
     */
    byte REGISTER_REQUEST = 4;
}
