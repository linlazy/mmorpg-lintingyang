package com.linlazy.mmorpglintingyang.module.email.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardConfigService;
import com.linlazy.mmorpglintingyang.server.common.ConfigFile;
import com.linlazy.mmorpglintingyang.server.common.ConfigFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏场景配置服务类
 */
@Component
public class EmailConfigService {

    @Autowired
    private RewardConfigService rewardConfigService;

    private static ConfigFile emailConfigFile;

    static {
        emailConfigFile =  ConfigFileManager.use("config_file/email_config.json");
    }


    //构建邮件模板映射
    private static Map<Integer, JSONObject> emailTemplateMap = new HashMap<>();

    @PostConstruct
    public void init(){
        JSONArray jsonArray = emailConfigFile.getJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            emailTemplateMap.put(jsonObject.getIntValue("emailTemplateId"),jsonObject);
        }
    }

    /**
     * 获取邮件模板配置
     * @param emailTemplateId
     * @return
     */
    public JSONObject getEmailTemplateConfig(int emailTemplateId){
        return emailTemplateMap.get(emailTemplateId);
    }


    public List<Reward> getRewardList(String rewards){
        return rewardConfigService.parseRewards(rewards);
    }
}
