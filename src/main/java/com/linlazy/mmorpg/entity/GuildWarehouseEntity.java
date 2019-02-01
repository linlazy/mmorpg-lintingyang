package com.linlazy.mmorpg.entity;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 公会仓库实体
 * @author linlazy
 */
@Data
@Table("guild_warehouse")
public class GuildWarehouseEntity extends Entity {

    /**
     * 公会ID
     */
    @Cloumn(pk = true)
    private long guildId;

    /**
     * 道具ID
     */
    @Cloumn(pk = true)
    private long itemId;

    /**
     * 数量
     */
    @Cloumn
    private int count;

    /**
     * 扩展属性
     */
    @Cloumn
    private String ext;

    private JSONObject extJsonObject = new JSONObject();


    @Override
    public void afterReadDB() {

        if(!StringUtils.isEmpty(ext)){
            extJsonObject = JSON.parseObject(ext);
        }
    }

    @Override
    public void beforeWriteDB() {
        ext = extJsonObject.toJSONString();
    }
}
