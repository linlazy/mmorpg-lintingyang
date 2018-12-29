package com.linlazy.mmorpglintingyang.module.skill.template;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 间隔xx秒恢复N点HP，持续时间xx秒（作用实体，存档）
 */
public  class SkillTemplate2 extends SkillTemplate{


    @Override
    protected int skillTemplateId() {
        return 2;
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
