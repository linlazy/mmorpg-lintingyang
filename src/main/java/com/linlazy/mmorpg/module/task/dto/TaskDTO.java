package com.linlazy.mmorpg.module.task.dto;

import com.linlazy.mmorpg.module.common.reward.RewardDTO;
import com.linlazy.mmorpg.module.task.domain.Task;
import com.linlazy.mmorpg.module.task.template.BaseTaskTemplate;
import com.linlazy.mmorpg.utils.OutputUitls;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Data
public class TaskDTO {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 任务进度
     */
    private String taskProcess;

    /**
     * 任务描述
     */
    private String desc;

    /**
     * 奖励
     */
    private List<RewardDTO> rewardDTOS;

    public TaskDTO(Task task) {

        taskId = task.getTaskId();
        taskName = task.getTaskName();
        desc = task.getDesc();
        status = task.getStatus();
        rewardDTOS = task.getRewardList().stream()
                .map(RewardDTO::new)
                .collect(Collectors.toList());

        BaseTaskTemplate taskTemplate = BaseTaskTemplate.getTaskTemplate(task.getTaskTemplateId());
        taskProcess = taskTemplate.getTaskProcess(task);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("任务ID【%d】\r\n",taskId));
        stringBuilder.append(String.format("任务名称【%s】\r\n",taskName));
        stringBuilder.append(String.format("任务描述【%s】\r\n",desc));

        stringBuilder.append(OutputUitls.taskStatus(status));

        stringBuilder.append(String.format("任务进度【%s】\r\n",taskProcess));

        stringBuilder.append("奖励内容\r\n");
        for(RewardDTO rewardDTO: rewardDTOS){
            stringBuilder.append(rewardDTO.toString()).append("\r\n");
        }

        return stringBuilder.toString();
    }
}
