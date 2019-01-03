package com.linlazy.mmorpglintingyang.module.skill.template;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 攻击力增加xxx点（作用实体，存档）
 * @author linlazy
 */
public  class SkillTemplate4 extends BaseSkillTemplate {


    @Override
    protected int skillTemplateId() {
        return 4;
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
