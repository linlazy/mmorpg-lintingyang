package com.linlazy.mmorpglintingyang.server.db.test;

import com.linlazy.mmorpglintingyang.server.db.Cloumn;
import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.Table;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Table("user")
@Data
@Component
public class UserEntity extends Entity {

    @Cloumn(pk = true)
    private long actorId;
    @Cloumn
    private String username;


}
