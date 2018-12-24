package com.linlazy.mmorpglintingyang.module.copy.manager;


import com.linlazy.mmorpglintingyang.module.copy.domain.CopyDo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CopyManager {




    private Map<Integer, CopyDo> copyIdCopyDoMap = new HashMap<>();


    /**
     * 退出副本
     */
    public void quitCopy(int copyId){
        copyIdCopyDoMap.remove(copyId);
    }


    /**
     * 初始化副本
     * @param actorId
     */
    public int initCopy(long actorId) {

        return 0;
    }
}
