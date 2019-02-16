package com.linlazy.mmorpg.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.module.backpack.service.GuildWarehouseService;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.chat.service.ChatService;
import com.linlazy.mmorpg.module.email.service.EmailService;
import com.linlazy.mmorpg.module.equip.service.EquipmentService;
import com.linlazy.mmorpg.module.guild.service.GuildService;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.dto.ItemConfigDTO;
import com.linlazy.mmorpg.module.item.dto.ItemInfoDTO;
import com.linlazy.mmorpg.module.item.push.ItemPushHelper;
import com.linlazy.mmorpg.module.item.service.ItemService;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.dto.PlayerDTO;
import com.linlazy.mmorpg.module.player.push.PlayerPushHelper;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.module.shop.service.ShopService;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.module.task.service.TaskService;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.module.transaction.service.TransactionService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class CmdHandler {

//-------------------------------------------------------装备--------------------------------------------------------
    /**
     * 装备
     */
    @Autowired
    private EquipmentService equipmentService;


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

    /**
     * 修复装备
     * @param jsonObject
     * @return
     */
    @Cmd("fixEquip")
    public Result<?> fixEquip(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long equipId = jsonObject.getLongValue("equipId");
        return equipmentService.fixEquipment(actorId,equipId);
    }


    /**
     *  穿戴装备
     * @param jsonObject
     * @return
     */
    @Cmd("dressEquip")
    public Result<?> dressEquip(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        long equipId = jsonObject.getLongValue("equipId");
        return equipmentService.dressEquip(actorId,equipId);
    }

    /**
     *  升级装备
     * @param jsonObject
     * @return
     */
    @Cmd("upgradeEquip")
    public Result<?> upgradeEquip(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        long equipId = jsonObject.getLongValue("equipId");
        return equipmentService.upgradeEquip(actorId,equipId);
    }

    /**
     *  查看穿戴装备信息
     * @param jsonObject
     * @return
     */
    @Cmd("dressedEquipInfo")
    public Result<?> dressedEquipInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return equipmentService.dressedEquipInfo(actorId);
    }



//-------------------------------------------------------邮件--------------------------------------------------------
    /**
     * 邮件
     */
    @Autowired
    private EmailService emailService;

    /**
     * 发送邮件
     * @param jsonObject
     * @return
     */
    @Cmd("sendEmail")
    public Result<?> sendEmail(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return emailService.sendEmail(actorId,jsonObject);
    }
    /**
     * 读取邮件
     * @param jsonObject
     * @return
     */
    @Cmd("readEmail")
    public Result<?> readEmail(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return emailService.readEmail(actorId,targetId);
    }

    /**
     * 领取邮件
     * @param jsonObject
     * @return
     */
    @Cmd("rewardEmail")
    public Result<?> rewardEmail(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return emailService.rewardEmail(actorId,targetId);
    }

    /**
     * 删除邮件
     * @param jsonObject
     * @return
     */
    @Cmd("deleteEmail")
    public Result<?> deleteEmail(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return emailService.deleteEmail(actorId,targetId);
    }




    //-------------------------------------------------------组队--------------------------------------------------------


    @Autowired
    private TeamService teamService;
    /**
     * 邀请加入
     * @param jsonObject
     * @return
     */
    @Cmd("inviteJoinTeam")
    public Result<?> inviteJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.inviteJoin(actorId,targetId);
    }
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
    /**
     * 同意加入
     * @param jsonObject
     * @return
     */
    @Cmd("acceptJoinTeam")
    public Result<?> acceptJoin(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return teamService.acceptJoin(actorId,targetId);
    }


    /**
     * 队伍信息
     * @param jsonObject
     * @return
     */
    @Cmd("teamInfo")
    public Result<?> teamInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return teamService.teamInfo(actorId);
    }
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
    @Cmd(value ="register",auth =false,enterMap = false)
    public Result<?> register(JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirm_password");
        Channel channel = jsonObject.getObject("channel",Channel.class);
        return playerService.register(username,password,confirmPassword,channel);
    }

    /**
     * 登入
     * @param jsonObject
     * @return
     */
    @Cmd(value ="login",auth = false,enterMap = false)
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
    @Cmd(value ="profession",enterMap = false)
    public Result<?> selectProfession(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int professionId = jsonObject.getIntValue("profession");
        return playerService.selectProfession(actorId,professionId);
    }

    /**
     * 登出
     * @param jsonObject
     * @return
     */
    @Cmd(value = "logout",enterMap = false)
    public Result<?> logout(JSONObject jsonObject){
        Channel channel = jsonObject.getObject("channel",Channel.class);
        return playerService.logout(channel);
    }

    /**
     * 获得金币
     * @param jsonObject
     * @return
     */
    @Cmd("gainGold")
    public Result<?> gainGold(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long gold = jsonObject.getLongValue("gold");
        return playerService.gainGold(actorId,gold);
    }

    /**
     * 增加经验
     * @param jsonObject
     * @return
     */
    @Cmd("addExp")
    public Result<?> addExp(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long exp = jsonObject.getLongValue("exp");
        return playerService.addExp(actorId,exp);
    }


    /**
     *  查看玩家信息
     * @param jsonObject
     * @return
     */
    @Cmd("playerInfo")
    public Result<?> playerInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        Player player = playerService.getPlayer(actorId);
        PlayerPushHelper.pushChange(actorId,new PlayerDTO(player));
        return Result.success();
    }

    /**
     *  升级
     * @param jsonObject
     * @return
     */
    @Cmd("upgradeLevel")
    public Result<?> upgradeLevel(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return playerService.upgradeLevel(actorId);
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
     * 查看商城信息
     * @param jsonObject
     * @return
     */
    @Cmd("shopInfo")
    public Result<?> shopInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return shopService.shopInfo(actorId);
    }

    /**
     * 商城购买
     * @param jsonObject
     * @return
     */
    @Cmd("buyShop")
    public Result<?> buyShop(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long goodsId = jsonObject.getLongValue("goodsId");
        return shopService.buy(actorId,goodsId);
    }



    @Autowired
    private SceneService sceneService;


    /**
     * 进入场景
     * @param jsonObject
     * @return
     */
    @Cmd(value = "enterMap",enterMap = false)
    public Result<?> enterMap(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return sceneService.enter(actorId);
    }

    /**
     *  获取当前场景实体信息
     * @param jsonObject
     * @return
     */
    @Cmd("aoi")
    public Result<?> aoi(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return sceneService.aoi(actorId,jsonObject);
    }

    /**
     *  获取当前场景实体信息
     * @param jsonObject
     * @return
     */
    @Cmd("moveScene")
    public Result<?> moveScene(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int targetSceneId = jsonObject.getIntValue("targetSceneId");
        return sceneService.moveTo(actorId,targetSceneId);
    }




    @Autowired
    private ItemConfigService itemConfigService;
    /**
     *  获取游戏道具信息
     * @param jsonObject
     * @return
     */
    @Cmd(value="itemInfo")
    public Result<?> itemInfo(JSONObject jsonObject){

        Collection<JSONObject> allItemConfig = itemConfigService.getAllItemConfig();
        List<ItemConfigDTO> collect = allItemConfig.stream()
                .map(ItemConfigDTO::new)
                .collect(Collectors.toList());
        long actorId = jsonObject.getLongValue("actorId");
        ItemPushHelper.pushitemInfo(actorId, new ItemInfoDTO(collect).toString());
        return Result.success();
    }


    @Autowired
    private PlayerBackpackService playerBackpackService;
    /**
     *  获取背包信息
     * @param jsonObject
     * @return
     */
    @Cmd("getBackpackInfo")
    public Result<?> getBackPackInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return playerBackpackService.getPlayerBackpackDTO(actorId);
    }

    /**
     *  放进背包
     * @param jsonObject
     * @return
     */
    @Cmd("pushBackpack")
    public Result<?> pushBackpack(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");

        long itemId = jsonObject.getIntValue("itemId");
        int num = jsonObject.getIntValue("num");
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
        if(itemConfig == null){
            return Result.valueOf("道具不存在");
        }

        Item item = new Item(itemId,num);
        return playerBackpackService.push(actorId, Lists.newArrayList(item));
    }

    /**
     *  丢弃背包物品
     * @param jsonObject
     * @return
     */
    @Cmd("popBackpack")
    public Result<?> popBackpack(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int itemId = jsonObject.getIntValue("itemId");
        int num = jsonObject.getIntValue("num");

        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
        if(itemConfig == null){
            return Result.valueOf("道具不存在");
        }
        Item item = new Item(itemId,num);
        return playerBackpackService.pop(actorId, Lists.newArrayList(item));
    }

    @Autowired
    private ItemService itemService;
    /**
     *  消耗道具
     * @param jsonObject
     * @return
     */
    @Cmd("useItem")
    public Result<?> consumeItem(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        long itemId = jsonObject.getLongValue("itemId");

        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
        if(itemConfig == null){
            return Result.valueOf("道具不存在");
        }
        return itemService.useItem(actorId,itemId);
    }

    /**
     *  使用技能
     * @param jsonObject
     * @return
     */
    @Cmd("useSkill")
    public Result<?> attack(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int skillId = jsonObject.getIntValue("skillId");
        return playerService.attack(actorId,skillId);
    }

    /**
     *  获得技能
     * @param jsonObject
     * @return
     */
    @Cmd("gainSkill")
    public Result<?> gainSkill(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        int skillId = jsonObject.getIntValue("skillId");
        return skillService.gainSkill(actorId,skillId);
    }



    /**
     *  查看
     * @param jsonObject
     * @return
     */
    @Cmd("skillInfo")
    public Result<?> skillInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return Result.success(skillService.getPlayerSkill(actorId).toString());
    }






//------------------------------------------------------任务--------------------------------------------------------------------------------
    /**
     * 任务
     */
    @Autowired
    private TaskService taskService;

    /**
     * 接受任务
     * @param jsonObject
     * @return
     */
    @Cmd("acceptTask")
    public Result<?> acceptTask(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        long taskId = jsonObject.getLongValue("taskId");
        return taskService.acceptTask(actorId,taskId);
    }


    /**
     * 提交任务
     * @param jsonObject
     * @return
     */
    @Cmd("commitTask")
    public Result<?> rewardTask(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        long taskId = jsonObject.getLongValue("taskId");
        return taskService.commitTask(actorId,taskId);
    }

    /**
     * 查看任务信息
     * @param jsonObject
     * @return
     */
    @Cmd("taskInfo")
    public Result<?> taskInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return taskService.taskInfo(actorId);
    }
//------------------------------------------------------公会--------------------------------------------------------------------------------
    /**
     * 公会
     */
    @Autowired
    private GuildService guildService;

    /**
     * 创建公会
     * @param jsonObject
     * @return
     */
    @Cmd("createGuild")
    public Result<?> createGuild(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return guildService.createGuild(actorId);
    }

    /**
     * 申请加入公会
     * @param jsonObject
     * @return
     */
    @Cmd("applyJoinGuild")
    public Result<?> applyJoinGuild(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long guildId = jsonObject.getLongValue("guildId");
        return guildService.applyJoin(actorId,guildId);
    }

    /**
     * 同意加入公会
     * @param jsonObject
     * @return
     */
    @Cmd("acceptJoinGuild")
    public Result<?> acceptJoinGuild(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long guildId = jsonObject.getLongValue("guildId");
        return guildService.acceptJoin(actorId,guildId);
    }

    /**
     * 任命公会成员权限
     * @param jsonObject
     * @return
     */
    @Cmd("guildAppoint")
    public Result<?> guildAppoint(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        int authLevel = jsonObject.getIntValue("authLevel");
        return guildService.appoint(actorId,targetId,authLevel);
    }

    /**
     * 踢出公会
     * @param jsonObject
     * @return
     */
    @Cmd("shotOffGuild")
    public Result<?> shotOffGuild(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long targetId = jsonObject.getLongValue("targetId");
        return guildService.shotOffGuild(actorId,targetId);
    }

    /**
     * 捐献金币
     * @param jsonObject
     * @return
     */
    @Cmd("donateGold")
    public Result<?> donateGold(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long gold = jsonObject.getLongValue("gold");
        return guildService.donateGold(actorId,gold);
    }


    @Autowired
    private GuildWarehouseService guildWarehouseService;

    /**
     * 公会仓库信息
     * @param jsonObject
     * @return
     */
    @Cmd("guildWareHouseInfo")
    public Result<?> guildWareHouseInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return guildService.guildWareHouseInfo(actorId);
    }

    /**
     * 放进公会仓库
     * @param jsonObject
     * @return
     */
    @Cmd("pushGuildWareHouse")
    public Result<?> pushGuildWareHouse(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");

        int itemId = jsonObject.getIntValue("itemId");
        int num = jsonObject.getIntValue("num");
        Item item = new Item(itemId,num);
        item.setCount(num);

        return guildService.push(actorId, Lists.newArrayList(item));
    }

    /**
     * 从公会仓库取出物品
     * @param jsonObject
     * @return
     */
    @Cmd("popGuildWareHouse")
    public Result<?> popGuildWareHouse(JSONObject jsonObject){


        long actorId = jsonObject.getLongValue("actorId");

        int itemId = jsonObject.getIntValue("itemId");
        int num = jsonObject.getIntValue("num");
        Item item = new Item(itemId,num);
        return guildService.pop(actorId,Lists.newArrayList(item));
    }

    @Cmd(value = "cmd",auth = false)
    public Result<?> cmd(JSONObject jsonObject){
       return Result.valueOf(
               "upgradeLevel 提升玩家等级\n" +
               "aoi 查看当前场景信息\n"+
               "moveScene 2 移动到场景2\n"+
               "gainSkill 2 得到技能2\n"
       );
    }

}
