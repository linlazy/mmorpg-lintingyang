package com.linlazy.mmorpg.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.equip.service.EquipmentService;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.service.CopyService;
import com.linlazy.mmorpg.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linlazy
 */
public class CmdHandler {


    @Autowired
    private CopyService copyService;
    @Autowired
    private EquipmentService equipmentService;

    /**
     * 进入副本
     * @param jsonObject
     * @return
     */
    @Cmd("enterCopy")
    public Result<?> enterCopy(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return copyService.enterCopy(actorId);
    }

    /**
     * 卸载装备
     * @param jsonObject
     * @return
     */
    @Cmd("unDressEquip")
    public Result<?> unDressEquip(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long equipId = jsonObject.getLongValue("equipId");
        return equipmentService.unDressEquip(actorId,equipId);
    }
//==========================================
    @Autowired
    private EmailService emailService;

    /**
     * 发送邮件
     * @param jsonObject
     * @return
     */
    @Cmd("sendEmail")
    public Result<?> send(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return emailService.sendEmail(actorId,jsonObject);
    }
    /**
     * 读取邮件
     * @param jsonObject
     * @return
     */
    @Cmd("readEmail")
    public Result<?> read(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return emailService.readEmail(actorId,targetId);
    }

    @Autowired
    private TeamService teamService;

//    /**
//     * 邀请加入
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("inviteJoinTeam")
//    public Result<?> inviteJoin(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        long targetId = jsonObject.getLongValue("targetId");
//        return teamService.inviteJoin(actorId,targetId);
//    }
//
//    /**
//     * 拒绝邀请
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("rejectJoinTeam")
//    public Result<?> rejectJoin(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        long targetId = jsonObject.getLongValue("targetId");
//        return teamService.rejectJoin(actorId,targetId);
//    }
//
//    /**
//     * 同意加入
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("acceptJoinTeam")
//    public Result<?> acceptJoin(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        long targetId = jsonObject.getLongValue("targetId");
//        return teamService.acceptJoin(actorId,targetId);
//    }
//
//    /**
//     *  任命队长
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("appointCaptainTeam")
//    public Result<?> appointCaptain(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        long targetId = jsonObject.getLongValue("targetId");
//        return teamService.appointCaptain(actorId,targetId);
//    }
//
//
//    /**
//     *  离开队伍
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("leaveTeam")
//    public Result<?> leave(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        return teamService.leave(actorId);
//    }
//
//    /**
//     *  踢出队伍
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("shotOffTeam")
//    public Result<?> shotOff(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        long targetId = jsonObject.getLongValue("targetId");
//        return teamService.shotOff(actorId,targetId);
//    }
}
