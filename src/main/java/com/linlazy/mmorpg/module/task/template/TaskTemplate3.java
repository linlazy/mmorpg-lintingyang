package com.linlazy.mmorpg.module.task.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  收集A个B任务道具
 * @author linlazy
 */
@Component
public class TaskTemplate3 extends BaseTaskTemplate {
    /**
     * 关心事件道具资源变化
     * @return
     */
    @Override
    public Set<EventType> likeEvent() {
        return Sets.newHashSet(EventType.ACTOR_ITEM_CHANGE);
    }

    @Override
    public void init() {
        super.init();
    }


    @Override
    protected int templateId() {
        return 3;
    }


    @Autowired
    private PlayerBackpackService playerBackpackService;

    @Override
    public Task updateTaskData(long actorId, JSONObject jsonObject, Task task) {
        JSONObject data = task.getData();

        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        int itemId = taskTemplateArgs.getIntValue("itemId");
        Item item = new Item(itemId);
        int itemCount = playerBackpackService.getPlayerBackpack(actorId).getItemCount(item);
        data.put("itemNum",itemCount);

        return task;
    }

    /**
     * 是否达成任务条件
     * @param actorId
     * @param task
     * @return
     */
    @Override
    public boolean isReachCondition(long actorId, Task task) {
        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        int needNum = taskTemplateArgs.getIntValue("needNum");

        JSONObject data = task.getData();
        int itemNum = data.getIntValue("itemNum");

        return itemNum >= needNum;
    }

    @Override
    public String getTaskProcess(Task task) {

        JSONObject data = task.getData();
        int itemNum = data.getIntValue("itemNum");
        JSONObject taskTemplateArgs = task.getTaskTemplateArgs();
        int itemId = taskTemplateArgs.getIntValue("itemId");
        int needNum = taskTemplateArgs.getIntValue("needNum");


        ItemConfigService itemConfigService = SpringContextUtil.getApplicationContext().getBean(ItemConfigService.class);
        JSONObject itemConfig = itemConfigService.getItemConfig(itemId);

        return String.format("收集道具【%s】 %d/%d个",itemConfig.getString("name"),itemNum,needNum);
    }

}
