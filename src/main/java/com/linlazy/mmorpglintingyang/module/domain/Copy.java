package com.linlazy.mmorpglintingyang.module.domain;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 副本
 * @author linlazy
 */
@Data
public class Copy {

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * 副本Boss信息
     */
    private CopyBossInfo copyBossInfo;

    /**
     * 副本玩家信息
     */
    private CopyPlayerInfo copyPlayerInfo;



   // ============================================================================================
    /**
     * 当前最大副本编号
     */
    private AtomicInteger maxCopyId = new AtomicInteger();
    /**
     * 创建副本
     * @return
     */
    public Copy createCopy(){
        Copy copy = new Copy();
        //初始化副本ID
        copy.setCopyId(maxCopyId.incrementAndGet());
        //初始化副本boss信息
        CopyBossInfo copyBossInfo = initCopyBossInfo();
        copy.setCopyBossInfo(copyBossInfo);
        //初始化副本玩家信息
        CopyPlayerInfo copyPlayerInfo = initCopyPlayerInfo();
        copy.setCopyPlayerInfo(copyPlayerInfo);

        return copy;
    }

    //===============================================================================
    /**
     * 初始化副本boss信息
     * @return
     */
    private CopyBossInfo initCopyBossInfo() {
        return null;
    }

    /**
     * 初始化副本玩家信息
     * @return
     */
    private CopyPlayerInfo initCopyPlayerInfo() {
        return null;
    }

}
