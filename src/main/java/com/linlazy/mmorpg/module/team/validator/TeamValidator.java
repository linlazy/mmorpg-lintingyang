//package com.linlazy.mmorpg.module.team.validator;
//
//import com.linlazy.mmorpg.server.common.Result;
//import org.springframework.stereotype.Component;
//
///**
// * @author linlazy
// */
//@Component
//public class TeamValidator {
//
//

//
//
//
//    /**
//     *  队伍已解散
//     * @param actorId
//     * @return
//     */
//    public Result<?> isDisband(long actorId) {
//        TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
//        if(actorTeamDo.isDisband()){
//            return Result.valueOf("队伍已解散");
//        }
//        return Result.success();
//    }
//
//    /**
//     *  是否 不是队长
//     * @param actorId
//     * @return
//     */
//    public Result<?> isNotCaption(long actorId) {
//        TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
//        if(!actorTeamDo.isCaptain()){
//            return Result.valueOf("不是队长");
//        }
//        return Result.success();
//    }
//}
