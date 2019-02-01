package com.linlazy.mmorpg.module.item.type;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.domain.PlayerSkill;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * 技能书籍：在游戏中能让玩家学习到技能的道具，招式技能书
 * @author linlazy
 */
public class SkillBook extends BaseItem{

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerBackpackService playerBackpackService;
    @Autowired
    private SkillDAO skillDAO;

    @Override
    protected Integer itemType() {
        return ItemType.SKILL;
    }

    @Override
    public Result<?> useItem(long actorId, Item item) {
        Player player = playerService.getPlayer(actorId);
        JSONObject extJsonObject = item.getExt();
        int professionId = extJsonObject.getIntValue("professionId");
        if(professionId != player.getProfession()){
            return Result.valueOf(String.format("职业不匹配，无法学习该技能"));
        }

        PlayerSkill playerSkill = player.getPlayerSkill();
        if(playerSkill.hasSkill(item.getItemId())){
            return Result.valueOf(String.format("玩家已学习该技能，无法重复学习"));
        }

        //todo
        Result<?> pop = playerBackpackService.pop(actorId, Lists.newArrayList());
        if(pop.isFail()){
            return Result.valueOf(pop.getCode());
        }
        Skill skill = new Skill();
        playerSkill.addSkill(skill);
        skillDAO.insertQueue(skill.convertSkillEntity());
        return Result.success(String.format("学习技能【%s】成功",item.getName()));
    }
}
