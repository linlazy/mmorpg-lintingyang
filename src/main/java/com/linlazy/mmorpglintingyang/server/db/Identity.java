package com.linlazy.mmorpglintingyang.server.db;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Identity {

    private Object[] args =null;

    public static Identity build(Object ... args){
        Identity identity = new Identity();
        identity.args =args;
        return identity;
    }

    public Object[] getArgs() {
        return args;
    }
}
