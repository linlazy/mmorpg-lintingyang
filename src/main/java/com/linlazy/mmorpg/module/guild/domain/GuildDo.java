package com.linlazy.mmorpg.module.guild.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linlazy
 */
@Data
public class GuildDo {

    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 会长ID
     */
    private long presidentId;

    /**
     * 副会长ID集合
     */
    private Set<Long> voidPresidentIdSet = new HashSet<>();

    /**
     * 优秀会员ID集合
     */
    private Set<Long>  excellentMemberIdSet = new HashSet<>();

    /**
     * 普通会员ID集合
     */
    private Set<Long> ordinaryMemberIdSet = new HashSet<>();

    /**
     * 新进会员ID集合
     */
    private Set<Long> newMemberIdSet = new HashSet<>();


    public Set<Long> getHasAuthInviteSet(){
        Set<Long> hasAuthInviteSet = new HashSet<>();
        //会长
        hasAuthInviteSet.add(presidentId);
        //副会长
        hasAuthInviteSet.addAll(voidPresidentIdSet);
        //优秀会员
        hasAuthInviteSet.addAll(excellentMemberIdSet);
        return hasAuthInviteSet;
    }
}
