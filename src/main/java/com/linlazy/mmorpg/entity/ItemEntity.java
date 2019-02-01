package com.linlazy.mmorpg.entity;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 道具实体类
 * @author linlazy
 */
@Data
@Table("item")
public class ItemEntity extends Entity {

    @Cloumn(pk = true)
    private long itemId;
    @Cloumn(pk = true)
    private long actorId;

    @Cloumn
    private int count;

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
