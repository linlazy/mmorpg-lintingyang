package com.linlazy.mmorpglintingyang.module.copy.logout;

import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.server.common.LogoutListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 副本登出监听器
 * @author linlazy
 */
@Component
public class CopyLogoutListener implements LogoutListener {

    @Autowired
    private CopyManager copyManager;

    @Override
    public void logout(long actorId) {
        copyManager.quitCopy(actorId);
    }
}
