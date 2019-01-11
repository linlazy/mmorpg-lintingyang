package com.linlazy.mmorpg.module.skill.template;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.user.manager.UserManager;
import com.linlazy.mmorpg.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 瞬间恢复N点HP（作用实体，存档）
 * @author linlazy
 */
public  class SkillTemplate1 extends BaseSkillTemplate {


    @Override
    protected int skillTemplateId() {
        return 1;
    }

    @Autowired
    private UserManager userManager;

    public void handle(long actorId, JSONObject jsonObject, SkillDo skillDo){
        JSONObject skillTemplateArgs = skillDo.getSkillTemplateArgs();
        int hp = skillTemplateArgs.getIntValue("hp");


        long targetId = jsonObject.getLongValue("targetId");
        User user = userManager.getUser(targetId);
        user.modifyHP(hp);
        //存档
    }
}
