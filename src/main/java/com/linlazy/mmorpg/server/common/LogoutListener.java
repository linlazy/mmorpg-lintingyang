package com.linlazy.mmorpg.server.common;

/**
 * @author linlazy
 */
public interface LogoutListener {

     /**
      * 登出
      * @param actorId 玩家ID
      */
     void logout(long actorId);
}
