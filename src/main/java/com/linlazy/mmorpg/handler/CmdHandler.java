package com.linlazy.mmorpg.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.equip.service.EquipmentService;
import com.linlazy.mmorpg.service.ShopService;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.service.*;
import io.netty.channel.Channel;
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

    @Autowired
    private ChatService chatService;

    /**
     * 发送聊天信息
     * @param jsonObject
     * @return
     */
    @Cmd("sendChat")
    public Result<?> sendChat(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return chatService.sendChat(actorId,jsonObject);
    }


    @Autowired
    private PlayerService playerService;

    /**
     * 注册
     * @param jsonObject
     * @return
     */
    @Cmd(value ="register",auth =false)
    public Result<?> register(JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirm_password");
        return playerService.register(username,password,confirmPassword);
    }

    /**
     * 登入
     * @param jsonObject
     * @return
     */
    @Cmd(value ="login",auth = false)
    public Result<?> login(JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        Channel channel = jsonObject.getObject("channel",Channel.class);
        return playerService.login(username,password,channel);
    }

    /**
     * 选择职业
     * @param jsonObject
     * @return
     */
    @Cmd(value ="selectProfession")
    public Result<?> selectProfession(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int professionId = jsonObject.getIntValue("actorId");
        return playerService.selectProfession(actorId,professionId);
    }

    /**
     * 登出
     * @param jsonObject
     * @return
     */
    @Cmd(value = "logout")
    public Result<?> logout(JSONObject jsonObject){
        Channel channel = jsonObject.getObject("channel",Channel.class);
        return playerService.logout(channel);
    }

    /**
     * 获取玩家信息
     * @param jsonObject
     * @return
     */
    @Cmd(value = "getPlayer")
    public Result<?> getPlayer(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return Result.success(playerService.getPlayer(actorId));
    }


    @Autowired
    private SkillService skillService;
    /**
     * 穿戴技能
     * @param jsonObject
     * @return
     */
    @Cmd("dressSkill")
    public Result<?> dress(JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        long actorId = jsonObject.getLongValue("actorId");
        return skillService.dressSkill(actorId,skillId,jsonObject);
    }

    /**
     * 技能升级
     * @param jsonObject
     * @return
     */
    @Cmd("skillLevelUp")
    public Result<?> levelUp(JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        long actorId = jsonObject.getLongValue("actorId");
        return skillService.levelUp(actorId,skillId,jsonObject);
    }

    @Autowired
    private TransactionService transactionService;

    /**
     * 邀请交易
     * @param jsonObject
     * @return
     */
    @Cmd("inviteTrade")
    public Result<?> inviteTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return transactionService.inviteTrade(actorId,targetId);
    }

    /**
     * 拒绝交易
     * @param jsonObject
     * @return
     */
    @Cmd("rejectTrade")
    public Result<?> rejectTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return transactionService.rejectTrade(actorId,targetId);
    }

    /**
     * 同意交易
     * @param jsonObject
     * @return
     */
    @Cmd("acceptTrade")
    public Result<?> acceptTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return transactionService.acceptTrade(actorId,targetId);
    }

    /**
     * 锁定交易
     * @param jsonObject
     * @return
     */
    @Cmd("lockTrade")
    public Result<?> lockTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return transactionService.lockTrade(actorId,jsonObject);
    }

    /**
     * 确认交易
     * @param jsonObject
     * @return
     */
    @Cmd("enterTrade")
    public Result<?> enterTrade(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return transactionService.enterTrade(actorId,jsonObject);
    }

    @Autowired
    private ShopService shopService;


    /**
     * 商城购买
     * @param jsonObject
     * @return
     */
    @Cmd("buy")
    public Result<?> buy(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long goodsId = jsonObject.getLongValue("goodsId");
        int num = jsonObject.getIntValue("num");
        return shopService.buy(actorId,goodsId,num);
    }

    @Autowired
    private SceneService sceneService;

    /**
     * 移动场景
     * @param jsonObject
     * @return
     */
    @Cmd("move")
    public Result<?> move(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int targetSceneId = jsonObject.getIntValue("targetSceneId");
        return sceneService.moveTo(actorId,targetSceneId);
    }

    /**
     * 进入场景
     * @param jsonObject
     * @return
     */
    @Cmd("enter")
    public Result<?> enter(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return sceneService.enter(actorId);
    }

//    /**
//     *  获取当前场景实体信息
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("aoi")
//    public Result<?> aoi(JSONObject jsonObject){
//        long actorId = jsonObject.getLong("actorId");
//        return sceneService.aoi(actorId,jsonObject);
//    }

    /**
     *  对话npc
     * @param jsonObject
     * @return
     */
    @Cmd("talk")
    public Result<?> talk(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int npcId = jsonObject.getIntValue("npcId");
        return sceneService.talk(actorId,npcId,jsonObject);
    }
}
