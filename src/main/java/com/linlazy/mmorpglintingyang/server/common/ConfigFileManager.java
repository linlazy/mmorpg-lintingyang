package com.linlazy.mmorpglintingyang.server.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linlazy
 */
public class ConfigFileManager {

    private static ConfigFile prop = null;

    private static final ConcurrentHashMap<String, ConfigFile> map = new ConcurrentHashMap<>();
    private static final String DEFAULT_ENCODE = "UTF-8";

    public static ConfigFile use(String fileName) {
        ConfigFile result = map.get(fileName);
        if (result == null) {
            synchronized (ConfigFileManager.class) {
                result = map.get(fileName);
                if (result == null) {
                    result = new ConfigFile(fileName,DEFAULT_ENCODE);
                    map.put(fileName, result);
                    if (ConfigFileManager.prop == null) {
                        ConfigFileManager.prop = result;
                    }
                }
            }
        }
        return result;
    }

    public static ConfigFile getConfigFile() {
        if (prop == null) {
            throw new IllegalStateException("Load propties file by invoking PropKit.use(String fileName) method first.");
        }
        return prop;
    }

    public static ConfigFile getConfigFile(String fileName) {
        return map.get(fileName);
    }
}
