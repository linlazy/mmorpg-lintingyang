package com.linlazy.mmorpg.server.db.entity;

import lombok.Data;

@Data
    public class KeyValueEntry<T,S> {
        private T key;
        private S value;
    }